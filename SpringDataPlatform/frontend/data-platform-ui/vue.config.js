const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    // Serve index.html for all routes that don't match a static file.
    // Required for Vue Router history mode — without this, refreshing any
    // route other than / returns webpack-dev-server's own 404 page.
    historyApiFallback: true,
  }
})
