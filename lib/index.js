/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
const bindings = require('bindings')
const core = bindings('caramlcore')

module.exports = core.getInstance()