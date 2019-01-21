cmake_minimum_required(VERSION 3.4.1)

add_library(
        caraml-core.node
        SHARED
        src/main/cpp/caraml-core.cpp)

include_directories(
        build/liquidcore-addon/include/node
        build/liquidcore-addon/include/v8
)

find_library(
        log-lib
        log)

add_library(
        node-lib
        SHARED
        IMPORTED
)
add_library(
        liquidcore-lib
        SHARED
        IMPORTED
)
set_target_properties(
        node-lib
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/build/liquidcore-addon/jni/${ANDROID_ABI}/libnode.so
)
set_target_properties(
        liquidcore-lib
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/build/liquidcore-addon/jni/${ANDROID_ABI}/libliquidcore.so
)
target_link_libraries( # Specifies the target library.
        caraml-core.node
        node-lib
        liquidcore-lib
        ${log-lib})