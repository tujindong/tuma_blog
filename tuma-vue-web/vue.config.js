const path = require('path')
const CompressionPlugin = require("compression-webpack-plugin")

function resolve(dir) {
    return path.join(__dirname, dir)
}

module.exports = {
    // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
    productionSourceMap: false,
    outputDir: 'web',
    publicPath: './',
    //打包app时放开该配置
    configureWebpack: config => {
        //生产环境取消 console.log
        if (process.env.NODE_ENV === 'production') {
            config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true
        }
    },
    chainWebpack: (config) => {

        //生产环境，开启js\css压缩
        if (process.env.NODE_ENV === 'production') {
            config.plugin('compressionPlugin').use(new CompressionPlugin({
                test: /\.(js|css|less)$/, // 匹配文件名
                threshold: 10240, // 对超过10k的数据压缩
                deleteOriginalAssets: false // 不删除源文件
            }))
        }

        // 配置 webpack 识别 markdown 为普通的文件
        config.module
            .rule('markdown')
            .test(/\.md$/)
            .use()
            .loader('file-loader')
            .end()

        // 编译vxe-table包里的es6代码，解决IE11兼容问题
        config.module
            .rule('vxe')
            .test(/\.js$/)
            .include
            .add(resolve('node_modules/vxe-table'))
            .add(resolve('node_modules/vxe-table-plugin-antd'))
            .end()
            .use()
            .loader('babel-loader')
            .end()

    },

    css: {
        loaderOptions: {
            sass: {
                modifyVars: {
                    'primary-color': '#2e9dd2',
                    'link-color': '#2e9dd2',
                },
                javascriptEnabled: true,
            }
        }
    },

    // devServer: {
    //     port: 8080,
    //     proxy: {
    //         '/tuma-boot': {
    //             target: 'http://120.76.216.29:8089', //请求本地 需要jeecg-boot后台项目
    //             ws: false,
    //             changeOrigin: true
    //         },
    //     }
    // },

    lintOnSave: undefined
}