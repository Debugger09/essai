package com.ime.api.tache.service.impl;

import com.ime.api.tache.dto.TacheDto;
import com.ime.api.tache.mapper.TacheMapper;
import com.ime.api.tache.model.Tache;
import com.ime.api.tache.repository.TacheRepository;
import com.ime.api.tache.service.TacheService;
import com.ime.api.projet.model.Projet;
import com.ime.api.projet.repository.ProjetRepository;
import com.ime.api.tache.repository.ListeMembreRepository;
import com.ime.api.user.mapper.UserMapper;
import com.ime.api.user.dto.UserDTO;
import com.ime.api.tache.model.ListeMembre;
import com.ime.api.user.repository.UserRepository;
import com.ime.api.tache.model.enums.StatutTache;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class TacheServiceImpl implements TacheService {

    private final TacheRepository tacheRepository;
    private final TacheMapper tacheMapper;
    private final ProjetRepository projetRepository;
    private final ListeMembreRepository listeMembreRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    public void enrichirMembres(Tache tache, TacheDto dto) {
        List<ListeMembre> membres = listeMembreRepository.findByTacheId(tache.getId());
        List<UserDTO> membresDto = membres.stream()
            .map(lm -> userMapper.toDto(lm.getMembre()))
            .toList();
        dto.setMembres(membresDto);
    }

    private void assignerMembres(Tache tache, List<Long> membresIds) {
        // Supprimer les anciennes associations
        listeMembreRepository.findByTacheId(tache.getId()).forEach(lm -> listeMembreRepository.deleteById(lm.getId()));
        // Créer les nouvelles associations
        if (membresIds != null) {
            for (Long membreId : membresIds) {
                userRepository.findById(membreId).ifPresent(user -> {
                    ListeMembre lm = new ListeMembre();
                    lm.setTache(tache);
                    lm.setMembre(user);
                    listeMembreRepository.save(lm);
                });
            }
        }
    }

    @Override
    public TacheDto createTache(TacheDto tacheDto) {
        Tache tache = tacheMapper.toEntity(tacheDto);

        // Récupérer le projet complet si présent
        if (tacheDto.getProjetId() != null) {
            Projet projet = projetRepository.findById(tacheDto.getProjetId())
                    .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
            tache.setProjet(projet);
        } else {
            tache.setProjet(null);
        }

        Tache savedTache = tacheRepository.save(tache);
        // Assigner les membres
        assignerMembres(savedTache, tacheDto.getMembresIds());
        TacheDto dto = tacheMapper.toDto(savedTache);
        enrichirMembres(savedTache, dto);
        return dto;
    }

    @Override
    public TacheDto getTacheById(Long id) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable avec l'id : " + id));
        TacheDto dto = tacheMapper.toDto(tache);
        enrichirMembres(tache, dto);
        return dto;
    }

    @Override
    public List<TacheDto> getAllTaches() {
        return tacheRepository.findAll()
                .stream()
                .map(tache -> {
                    TacheDto dto = tacheMapper.toDto(tache);
                    enrichirMembres(tache, dto);
                    dto.setProjetName(tache.getProjet() != null ? tache.getProjet().getName() : null);
                    return dto;
                })
                .toList();
    }

    @Override
    public TacheDto updateTache(Long id, TacheDto tacheDto) {
        Tache tache = tacheRepository.findById(id)
                .map(t -> {
                    t.setLibelle(tacheDto.getLibelle());
                    t.setDescription(tacheDto.getDescription());
                    t.setDateEcheance(tacheDto.getDateEcheance());
                    t.setPriorite(tacheDto.getPriorite());
                    t.setStatutTache(tacheDto.getStatutTache());
                    // Charger le projet complet
                    if (tacheDto.getProjetId() != null) {
                        Projet projet = projetRepository.findById(tacheDto.getProjetId())
                                .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
                        t.setProjet(projet);
                    } else {
                        t.setProjet(null);
                    }
                    return tacheRepository.save(t);
                })
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable avec l'id : " + id));
        // Assigner les membres
        assignerMembres(tache, tacheDto.getMembresIds());
        TacheDto dto = tacheMapper.toDto(tache);
        enrichirMembres(tache, dto);
        return dto;
    }

    @Override
    public void deleteTache(Long id) {
        tacheRepository.deleteById(id);
    }

    @Override
    public TacheDto changerStatutTache(Long tacheId, String nouveauStatut, Long userId) {
        Tache tache = tacheRepository.findById(tacheId)
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        // Suppression de la vérification d'appartenance au projet
        tache.setStatutTache(StatutTache.valueOf(nouveauStatut));
        tacheRepository.save(tache);
        return tacheMapper.toDto(tache);
    }
}