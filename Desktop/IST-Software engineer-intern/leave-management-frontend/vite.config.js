import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'   // <-- DEFAULT IMPORT (no {})
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
})


