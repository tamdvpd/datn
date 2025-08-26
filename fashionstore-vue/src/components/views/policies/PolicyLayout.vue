<template>
  <div class="container py-4">
    <h1 class="h3 mb-3">{{ title }}</h1>
    <p v-if="subtitle" class="text-muted mb-4">{{ subtitle }}</p>
    <div class="card shadow-sm">
      <div class="card-body content">
        <slot />
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: { type: String, required: true },
  subtitle: { type: String, default: '' }
})
</script>

<style scoped>
.content :is(h2,h3){ margin-top: 1.25rem; font-weight: 700; }
.content p{ margin-bottom: .75rem; line-height: 1.6; }
.content ul{ margin: .25rem 0 .75rem 1.25rem; }
</style>
