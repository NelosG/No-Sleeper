cmake_minimum_required(VERSION 3.25)
project(no-sleeper)

set(CMAKE_CXX_STANDARD 17)

#=======================================================================================================================
# Output Directories.
#=======================================================================================================================
set(OUTPUT_DIR ${CMAKE_SOURCE_DIR}/output-${CMAKE_BUILD_TYPE})

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${OUTPUT_DIR})
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${OUTPUT_DIR})
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${OUTPUT_DIR})

add_executable(${PROJECT_NAME} main.cpp resourses/icon.rc)


if (CMAKE_BUILD_TYPE MATCHES Release)
    set_target_properties(
            ${PROJECT_NAME}
            PROPERTIES LINK_FLAGS_DEBUG "/SUBSYSTEM:CONSOLE"
            LINK_FLAGS_RELEASE "/SUBSYSTEM:WINDOWS /ENTRY:mainCRTStartup"
    )
endif ()
