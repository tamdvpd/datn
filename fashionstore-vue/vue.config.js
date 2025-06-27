const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 3001  // 👉 Đổi cổng tại đây (ví dụ: 3001)
  }
})

