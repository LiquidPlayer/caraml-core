const path = require('path')
const fs = require('fs')

const jazzy = (() => {
/*
clean: true
objc: true
umbrella_header: caraml-core/caraml_core.h
framework_root: caraml-core
sdk: iphonesimulator
module: caraml-core
module_version: {{version}}
author: LiquidPlayer
author_url: https://github.com/LiquidPlayer
github_url: https://github.com/LiquidPlayer/caraml-core
copyright: © 2019-2020 LiquidPlayer
hide_documentation_coverage: true
output: ../../LiquidPlayer.github.io/caraml-core/{{version}}
root_url: https://liquidplayer.github.io/caraml-core/{{version}}

*/
}).toString().split(/\n/).slice(2, -2).join('\n')

let inp = fs.readFileSync(path.resolve('..','package.json'))
let data = JSON.parse(inp)

let version = data.version

let yaml = jazzy.replace(/{{version}}/g, version)

fs.writeFileSync('.jazzy.yaml', yaml)
