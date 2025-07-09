<template>
  <div v-if="modelValue" class="fixed inset-0 bg-transparent flex items-center justify-center z-50 p-4" @click.self="onClose">
    <div class="bg-white dark:bg-gray-800 rounded-3xl shadow-2xl max-w-lg w-full p-6 sm:p-8 border border-white/20 dark:border-gray-700/50 transform transition-all duration-300 scale-100">
      <!-- Header -->
      <div class="flex items-center justify-between mb-6">
        <div class="flex items-center space-x-3">
          <div class="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-2xl">
            <slot name="icon">
              <svg class="h-6 w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
            </slot>
          </div>
          <h3 class="text-xl font-bold text-slate-900 dark:text-slate-100">
            {{ title }}
          </h3>
        </div>
        <button 
          @click="onClose"
          class="p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 hover:bg-slate-100 dark:hover:bg-gray-700 rounded-xl transition-all duration-200"
        >
          <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- Content -->
      <div class="space-y-6">
        <slot></slot>
      </div>

      <!-- Footer -->
      <!-- SUPPRIME LE SLOT FOOTER ICI -->
    </div>
  </div>
</template>

<script setup>
defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  },
  confirmText: {
    type: String,
    default: 'Confirmer'
  },
  cancelText: {
    type: String,
    default: 'Annuler'
  }
})

const emit = defineEmits(['update:modelValue', 'confirm'])

const onClose = () => {
  emit('update:modelValue', false)
}

const onConfirm = () => {
  emit('confirm')
}
</script> 