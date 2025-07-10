<template>
  <div class="print-only print-doc">
    <!-- Debug dans le template -->
    <div v-if="!projet" style="color: red; font-weight: bold;">Aucun projet transmis</div>
    <div v-else>
      <!-- En-tête CollabSpace -->
      <div class="header flex items-center justify-between mb-8 border-b pb-6">
        <div class="flex items-center gap-4">
          <img src="/assets/images/logo.png" alt="CollabSpace Logo" class="logo-collabspace" />
          <span class="collabspace-title">Collab<span class="collabspace-accent">Space</span></span>
        </div>
        <div class="text-right">
          <h1 class="text-3xl font-bold mb-1 text-projet">{{ projet.name }}</h1>
          <p class="text-lg text-slate-600">Dossier projet</p>
        </div>
      </div>

      <!-- Informations principales -->
      <div class="grid grid-cols-2 gap-8 mb-8">
        <div>
          <h2 class="section-title">Informations générales</h2>
          <div class="space-y-3">
            <div><span class="font-medium">Statut:</span> <span class="ml-2">{{ formatStatus(projet.status) }}</span></div>
            <div><span class="font-medium">Date de début:</span> <span class="ml-2">{{ formatDate(projet.dateDebut) }}</span></div>
            <div><span class="font-medium">Date de fin:</span> <span class="ml-2">{{ formatDate(projet.dateFin) }}</span></div>
            <div><span class="font-medium">Budget:</span> <span class="ml-2">{{ formatMontant(projet.budget) }}</span></div>
            <div><span class="font-medium">Chef de projet:</span> <span class="ml-2">{{ projet.chefProjet?.lastName }} {{ projet.chefProjet?.firstName || '' }}</span></div>
          </div>
        </div>
        <div>
          <h2 class="section-title">Description</h2>
          <p class="whitespace-pre-wrap">{{ projet.description || 'Aucune description' }}</p>
        </div>
      </div>

      <!-- Tâches -->
      <div class="mb-8">
        <h2 class="section-title">Tâches</h2>
        <table class="styled-table">
          <thead>
            <tr>
              <th>Titre</th>
              <th>Statut</th>
              <th>Assignés</th>
              <th>Date d'échéance</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="tache in projet.taches" :key="tache.id">
              <td>{{ tache.libelle }}</td>
              <td>{{ formatStatus(tache.statutTache) }}</td>
              <td>
                <span v-if="tache.membres && tache.membres.length">
                  <span v-for="(m, idx) in tache.membres" :key="m.id">
                    {{ m.lastName }} {{ m.firstName }}<span v-if="idx < tache.membres.length - 1">, </span>
                  </span>
                </span>
                <span v-else>Non assigné</span>
              </td>
              <td>{{ formatDate(tache.dateEcheance) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Matériel -->
      <div class="mb-8">
        <h2 class="section-title">Matériel</h2>
        <table class="styled-table">
          <thead>
            <tr>
              <th>Libellé</th>
              <th>Quantité</th>
              <th>Prix unitaire</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="materiel in projet.materiels" :key="materiel.id">
              <td>{{ materiel.libelle }}</td>
              <td>{{ materiel.quantite }}</td>
              <td>{{ formatMontant(materiel.prix) }}</td>
              <td>{{ formatMontant(materiel.prix * materiel.quantite) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Dépenses -->
      <div class="mb-8">
        <h2 class="section-title">Dépenses</h2>
        <table class="styled-table">
          <thead>
            <tr>
              <th>Libellé</th>
              <th>Date</th>
              <th>Montant</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="depense in projet.depenses" :key="depense.id">
              <td>{{ depense.libelle }}</td>
              <td>{{ formatDate(depense.createdAt) }}</td>
              <td>{{ formatMontant(depense.montant) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Fichiers -->
      <div class="mb-8">
        <h2 class="section-title">Fichiers attachés</h2>
        <ul class="list-disc pl-5">
          <li v-for="fichier in projet.fichiers" :key="fichier.id" class="mb-2">
            {{ fichier.nom }}
          </li>
        </ul>
      </div>

      <!-- Pied de page -->
      <div class="footer mt-8 pt-4 border-t text-sm text-slate-600 flex justify-between items-center">
        <span>Document généré le {{ formatDate(new Date()) }}</span>
        <span class="collabspace-title">Collab<span class="collabspace-accent">Space</span></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useFormatters } from '~/composables/useFormatters'

const props = defineProps({
  projet: {
    type: Object,
    required: true
  }
})

const { formatMontant, formatDate } = useFormatters()

const formatStatus = (status) => {
  const statusMap = {
    'EN_COURS': 'En cours',
    'TERMINE': 'Terminé',
    'EN_ATTENTE': 'En attente'
  }
  return statusMap[status] || status
}
</script>

<style>
.print-only {
  display: none;
}
@media print {
  .print-only {
    display: block !important;
  }
  .print-doc {
    max-width: 900px;
    margin: 0 auto;
    padding: 0 0.5cm;
  }
  .header {
    border-bottom: 4px solid #2563eb;
    padding-bottom: 1.5rem;
    margin-bottom: 2rem;
  }
  .logo-collabspace {
    height: 48px;
    width: 48px;
    object-fit: contain;
  }
  .collabspace-title {
    font-size: 2rem;
    font-weight: bold;
    color: #2563eb;
    letter-spacing: 1px;
  }
  .collabspace-accent {
    color: #7c3aed;
  }
  .text-projet {
    color: #222;
    font-size: 2.2rem;
    font-weight: 700;
  }
  .section-title {
    font-size: 1.3rem;
    font-weight: 600;
    color: #2563eb;
    margin-bottom: 0.7rem;
    border-left: 4px solid #7c3aed;
    padding-left: 0.5rem;
  }
  .styled-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 1.5rem;
    font-size: 1rem;
  }
  .styled-table th {
    background: #2563eb;
    color: #fff;
    font-weight: 600;
    padding: 8px 6px;
    border-bottom: 2px solid #7c3aed;
  }
  .styled-table td {
    padding: 8px 6px;
    border-bottom: 1px solid #e5e7eb;
  }
  .styled-table tr:nth-child(even) td {
    background: #f3f4f6;
  }
  .footer {
    border-top: 2px solid #2563eb;
    margin-top: 2rem;
    padding-top: 1rem;
    color: #666;
    font-size: 0.95rem;
  }
}
</style> 