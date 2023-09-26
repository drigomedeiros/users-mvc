const fs = require('fs');
const { src, dest, series } = require('gulp');

async function delSimpleModules() { 
    if(fs.existsSync('simple_modules')){
        fs.rmSync('simple_modules', { recursive: true, force: true });
    }
}

function mountModules() {
    return src([
                    'node_modules/**/*.js', 
                    'node_modules/**/*.css',
                    'node_modules/**/*.js.map', 
                    'node_modules/**/*.css.map',
                    'node_modules/**/*.eot',
                    'node_modules/**/*.svg',
                    'node_modules/**/*.ttf',
                    'node_modules/**/*.woff',
                    'node_modules/**/*.woff2',
                    'node_modules/**/*.png',
                    'node_modules/**/*.jpg'

                ]).pipe(dest('simple_modules'));
}

async function delNodeModules() { 
    fs.rmSync('node_modules', { recursive: true, force: true });
}

exports.default = series(delSimpleModules, mountModules, delNodeModules)