<template>
  <footer class="bg-white/95 backdrop-blur-xl border-t border-gray-200/50 mt-auto">
    <div class="relative px-4 py-3">
      <!-- Gradient décoratif subtil -->
      <div class="absolute inset-0 bg-gradient-to-r from-blue-900/5 via-transparent to-cyan-700/5" />

      <div class="relative text-center">
        <p class="text-xs text-slate-500">
          <span class="font-medium bg-gradient-to-r from-blue-900 to-cyan-700 bg-clip-text text-transparent">
            Collab<span class="text-cyan-600">Space</span>
          </span>
          v1.0 - © {{ new Date().getFullYear() }}
        </p>
      </div>
    </div>
  </footer>
</template>
