require "json"

lcpackage = JSON.parse(File.read(File.join(__dir__, "package.json")))
version = lcpackage['version']

Pod::Spec.new do |s|
  s.name         = "caraml-core"
  s.version      = version
  s.summary      = "Native mobile UI addon for LiquidCore"
  s.description  = <<-DESC
    Provides a view that can be embedded in apps that exposes a surface API
    to LiquidCore.
  DESC
  s.homepage     = "https://github.com/LiquidPlayer/caraml-core"
  s.license = {:type => "MIT", :file => "LICENSE.md"}
  s.author  = { "Eric Lange" => "eric@flicket.tv" }
  s.platform = :ios, '11.0'
  s.source = { :git => "https://github.com/LiquidPlayer/caraml-core.git", :tag => "#{s.version}" }
  s.source_files  = "src/*.{cpp,h}",
    "caraml-core/caraml-core/*.{h,m,mm}"
  s.public_header_files = [
    "caraml-core/caraml-core/LCCaramlJS.h",
    "caraml-core/caraml-core/LCCaramlSurface.h",
    "caraml-core/caraml-core/LCCaramlView.h",
    "caraml-core/caraml-core/caraml_core.h"
  ]
  s.xcconfig = {
    :CLANG_WARN_DOCUMENTATION_COMMENTS => 'NO',
    :CLANG_ALLOW_NON_MODULAR_INCLUDES_IN_FRAMEWORK_MODULES => 'YES',
    :HEADER_SEARCH_PATHS => [
      "${PODS_CONFIGURATION_BUILD_DIR}/LiquidCore-headers/LiquidCore-headers.framework/PrivateHeaders",
    ].join(' '),
  }
  s.dependency 'LiquidCore'
  s.dependency 'LiquidCore-headers'
end
