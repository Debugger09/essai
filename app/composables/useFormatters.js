export const useFormatters = () => {
  const formatMontant = (montant) => {
    if (!montant) return '0 FCFA'
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XAF',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(montant)
  }

  const formatDate = (date) => {
    if (!date) return ''
    return new Date(date).toLocaleDateString('fr-FR')
  }

  const formatStatus = (status) => {
    const statusMap = {
      'EN_COURS': 'En cours',
      'TERMINE': 'Terminé',
      'EN_ATTENTE': 'En attente'
    }
    return statusMap[status] || status
  }

  return {
    formatMontant,
    formatDate,
    formatStatus
  }
} 