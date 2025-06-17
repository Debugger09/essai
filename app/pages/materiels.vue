<template>
  <div>
    <MaterielList />
  </div>
</template>

<script setup>
import MaterielList from '~/components/Materiels/index.vue'

definePageMeta({
  middleware: 'auth',
  layout: 'default'
})
</script> 