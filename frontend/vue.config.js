// vue.config.js
module.exports = {
  // proxy all webpack dev-server requests starting with /api
  // to our Spring Boot backend (localhost:8088) using http-proxy-middleware
  // see https://cli.vuejs.org/config/#devserver-proxy
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8098', // this configuration needs to correspond to the Spring Boot backends' application.properties server.port
        ws: true,
        changeOrigin: true
      }
    }
  },

  // Markdown
  // Refer to: https://segmentfault.com/a/1190000019412548?tdsourcetag=s_pctim_aiomsg
  // chainWebpack: config => {
  //   config.module.rule('rimIntroduction')
  //       .test(/\.rimIntroduction/)
  //       .use('vue-loader')
  //       .loader('vue-loader')
  //       .end()
  //       .use('vue-markdown-loader')
  //       .loader('vue-markdown-loader/lib/markdown-compiler')
  //       .options({
  //         raw: true
  //       })
  // },


  // Change build paths to make them Maven compatible
  // see https://cli.vuejs.org/config/
  outputDir: 'target/dist',
  assetsDir: 'static'
};
