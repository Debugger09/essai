import { ref } from 'vue'

export const useModal = () => {
  const showModal = ref(false)
  const isEditing = ref(false)
  const submitting = ref(false)
  const submitError = ref('')

  const openAddModal = () => {
    isEditing.value = false
    showModal.value = true
  }

  const openEditModal = () => {
    isEditing.value = true
    showModal.value = true
  }

  const closeModal = () => {
    showModal.value = false
    isEditing.value = false
    submitting.value = false
    submitError.value = ''
  }

  return {
    showModal,
    isEditing,
    submitting,
    submitError,
    openAddModal,
    openEditModal,
    closeModal
  }
} 