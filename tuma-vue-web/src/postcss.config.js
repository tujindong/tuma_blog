module.exports = {
    plugins: {
        autoprefixer: { browsers: 'last 5 version' },
        'postcss-px2rem-exclude': {
            remUnit: 72, //设计稿宽度/10
            exclude: /node_modules|folder_name/i
        }
    }
}
