<template>
  <div class="print-only">
    <!-- En-tête -->
    <div class="text-center mb-8 border-b pb-6">
      <h1 class="text-3xl font-bold mb-2">{{ projet.name }}</h1>
      <p class="text-lg text-slate-600">Détails du projet</p>
    </div>

    <!-- Informations principales -->
    <div class="grid grid-cols-2 gap-8 mb-8">
      <div>
        <h2 class="text-xl font-semibold mb-4">Informations générales</h2>
        <div class="space-y-3">
          <div>
            <span class="font-medium">Statut:</span>
            <span class="ml-2">{{ formatStatus(projet.status) }}</span>
          </div>
          <div>
            <span class="font-medium">Date de début:</span>
            <span class="ml-2">{{ formatDate(projet.dateDebut) }}</span>
          </div>
          <div>
            <span class="font-medium">Date de fin:</span>
            <span class="ml-2">{{ formatDate(projet.dateFin) }}</span>
          </div>
          <div>
            <span class="font-medium">Budget:</span>
            <span class="ml-2">{{ formatMontant(projet.budget) }}</span>
          </div>
          <div>
            <span class="font-medium">Chef de projet:</span>
            <span class="ml-2">{{ projet.chefProjet?.name || 'Non assigné' }}</span>
          </div>
        </div>
      </div>
      
      <div>
        <h2 class="text-xl font-semibold mb-4">Description</h2>
        <p class="whitespace-pre-wrap">{{ projet.description || 'Aucune description' }}</p>
      </div>
    </div>

    <!-- Tâches -->
    <div class="mb-8">
      <h2 class="text-xl font-semibold mb-4">Tâches</h2>
      <table class="w-full border-collapse">
        <thead>
          <tr class="border-b">
            <th class="text-left py-2">Titre</th>
            <th class="text-left py-2">Statut</th>
            <th class="text-left py-2">Assigné à</th>
            <th class="text-left py-2">Date d'échéance</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tache in projet.taches" :key="tache.id" class="border-b">
            <td class="py-2">{{ tache.titre }}</td>
            <td class="py-2">{{ formatStatus(tache.status) }}</td>
            <td class="py-2">{{ tache.membre?.name || 'Non assigné' }}</td>
            <td class="py-2">{{ formatDate(tache.dateEcheance) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Matériel -->
    <div class="mb-8">
      <h2 class="text-xl font-semibold mb-4">Matériel</h2>
      <table class="w-full border-collapse">
        <thead>
          <tr class="border-b">
            <th class="text-left py-2">Libellé</th>
            <th class="text-left py-2">Quantité</th>
            <th class="text-left py-2">Prix unitaire</th>
            <th class="text-left py-2">Total</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="materiel in projet.materiels" :key="materiel.id" class="border-b">
            <td class="py-2">{{ materiel.libelle }}</td>
            <td class="py-2">{{ materiel.quantite }}</td>
            <td class="py-2">{{ formatMontant(materiel.prix) }}</td>
            <td class="py-2">{{ formatMontant(materiel.prix * materiel.quantite) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Dépenses -->
    <div class="mb-8">
      <h2 class="text-xl font-semibold mb-4">Dépenses</h2>
      <table class="w-full border-collapse">
        <thead>
          <tr class="border-b">
            <th class="text-left py-2">Libellé</th>
            <th class="text-left py-2">Date</th>
            <th class="text-left py-2">Montant</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="depense in projet.depenses" :key="depense.id" class="border-b">
            <td class="py-2">{{ depense.libelle }}</td>
            <td class="py-2">{{ formatDate(depense.createdAt) }}</td>
            <td class="py-2">{{ formatMontant(depense.montant) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Fichiers -->
    <div>
      <h2 class="text-xl font-semibold mb-4">Fichiers attachés</h2>
      <ul class="list-disc pl-5">
        <li v-for="fichier in projet.fichiers" :key="fichier.id" class="mb-2">
          {{ fichier.nom }}
        </li>
      </ul>
    </div>

    <!-- Pied de page -->
    <div class="mt-8 pt-4 border-t text-sm text-slate-600">
      <p>Document généré le {{ formatDate(new Date()) }}</p>
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

<style scoped>
.print-only {
  display: none;
}

@media print {
  .print-only {
    display: block;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    page-break-inside: avoid;
  }

  th, td {
    padding: 8px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }

  th {
    font-weight: bold;
    background-color: #f8f9fa;
  }

  @page {
    margin: 2cm;
  }

  h1, h2 {
    page-break-after: avoid;
  }

  tr {
    page-break-inside: avoid;
  }
}
</style> 