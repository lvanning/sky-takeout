const name = 'Vue Typescript Admin'
const IS_PROD = ['production', 'development'].includes(process.env.NODE_ENV)

module.exports = {
  'publicPath': process.env.NODE_ENV === 'production' ? './' : '/', // TODO: Remember to change this to fit your need
  'lintOnSave': process.env.NODE_ENV === 'development',
  'pwa': {
    'name': name
  },
  // 开启代理
  devServer: {
    host: '0.0.0.0',
    port: 8888,
    open: true,
    allowedHosts: 'all', // 对应旧版 disableHostCheck
    hot: true,
    client: {
      // 替代旧版 devServer.public
      webSocketURL: 'auto://0.0.0.0:0/ws',
      overlay: {
        warnings: false,
        errors: true
      }
    },
    proxy: {
      '/api': {
        target: process.env.VUE_APP_URL,
        ws: false,
        secure: false,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  chainWebpack: (config) => {
    config.resolve.symlinks(true) // 修复热更新失效

    // vue-loader 15 会为每个 <style> 生成未使用的默认导入，而 mini-css-extract-plugin
    // 提取后的样式模块没有默认导出。该警告无害，但 @soda/friendly-errors-webpack-plugin
    // 直接读取 compilation.warnings，不受 ignoreWarnings 影响，这里在 afterCompile 阶段过滤。
    config.plugin('filter-style-warnings').use({
      apply (compiler) {
        compiler.hooks.afterCompile.tap('FilterStyleWarnings', (compilation) => {
          compilation.warnings = compilation.warnings.filter(
            (warning) => !/export 'default' \(imported as 'style\d+'\) was not found/.test(warning.message)
          )
        })
      }
    })
  },
  configureWebpack: {
    devtool: 'source-map'
  },

  css: {
    // 是否使用css分离插件 ExtractTextPlugin
    extract: IS_PROD,
    // 开启 CSS source maps?
    sourceMap: false,
    // css预设器配置项
    loaderOptions: {
      scss: {
        // 替代 vue-cli-plugin-style-resources-loader，向所有 scss 注入全局变量与 mixin
        additionalData: (content, loaderContext) => {
          const { resourcePath } = loaderContext
          if (/[\\/](_variables|variables\.module|_mixins)\.scss$/.test(resourcePath)) {
            return content
          }
          return '@import "~@/styles/_variables.scss";\n@import "~@/styles/_mixins.scss";\n' + content
        },
        sassOptions: {
          // 不输出 node_modules 中第三方样式（Element UI 主题）的弃用警告
          quietDeps: true
        }
      }
    }
  }
}
