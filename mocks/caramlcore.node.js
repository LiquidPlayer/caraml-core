/*
 * Copyright (c) 2020 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
console.warn('WARN: Using mock interface for caraml-core')
module.exports = {
  getInstance : () => ({})
}