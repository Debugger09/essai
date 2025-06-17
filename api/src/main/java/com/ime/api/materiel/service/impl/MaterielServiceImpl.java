package com.ime.api.materiel.service.impl;

import com.ime.api.materiel.dto.MaterielDto;
import com.ime.api.materiel.mapper.MaterielMapper;
import com.ime.api.materiel.model.Materiel;
import com.ime.api.materiel.repository.MaterielRepository;
import com.ime.api.materiel.service.MaterielService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterielServiceImpl implements MaterielService {

    private final MaterielRepository materielRepository;
    private final MaterielMapper materielMapper;

    @Override
    public MaterielDto createMateriel(MaterielDto materielDto) {
        Materiel materiel = materielMapper.toEntity(materielDto);
        Materiel saved = materielRepository.save(materiel);
        return materielMapper.toDto(saved);
    }

    @Override
    public MaterielDto getMaterielById(Long id) {
        Materiel materiel = materielRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matériel introuvable avec l'id : " + id));
        return materielMapper.toDto(materiel);
    }

    @Override
    public List<MaterielDto> getAllMateriels() {
        return materielRepository.findAll()
                .stream()
                .map(materielMapper::toDto)
                .toList();
    }

    @Override
    public MaterielDto updateMateriel(Long id, MaterielDto materielDto) {
        Materiel materiel = materielRepository.findById(id)
                .map(m -> {
                    m.setLibelle(materielDto.getLibelle());
                    m.setReutilisable(materielDto.isReutilisable());
                    m.setActif(materielDto.isActif());
                    m.setQuantite(materielDto.getQuantite());
                    return materielRepository.save(m);
                })
                .orElseThrow(() -> new EntityNotFoundException("Matériel introuvable avec l'id : " + id));
        return materielMapper.toDto(materiel);
    }

    @Override
    public void deleteMateriel(Long id) {
        materielRepository.deleteById(id);
    }
}