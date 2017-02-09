cmake_minimum_required (VERSION 3.4)
project(cognitive_beats CXX)

#set (CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${PROJECT_SOURCE_DIR}/cmake/")
#set (CMAKE_RUNTIME_OUTPUT_DIRECTORY "${PROJECT_SOURCE_DIR}/output")
#set (CMAKE_LIBRARY_OUTPUT_DIRECTORY "${PROJECT_SOURCE_DIR}/output")
#set (CMAKE_ARCHIVE_OUTPUT_DIRECTORY "${PROJECT_SOURCE_DIR}/output")

# Set QT
#set (CMAKE_INCLUDE_CURRENT_DIR ON)
#set (qt_INSTALL_PATH "e:/bin/qt/5.8/5.8/msvc2015" CACHE STRING "Path to QT.")
#set (CMAKE_PREFIX_PATH ${qt_INSTALL_PATH} ${CMAKE_PREFIX_PATH})
## All qt packages must be found within top level dir.
#find_package (Qt5 COMPONENTS Core Multimedia)


# Download and unpack googletest at configure time
#configure_file(CMakeLists.googletest.in
#               ${CMAKE_BINARY_DIR}/googletest_download/CMakeLists.txt)
#execute_process(COMMAND ${CMAKE_COMMAND} -G "${CMAKE_GENERATOR}" .
#  WORKING_DIRECTORY ${CMAKE_BINARY_DIR}/googletest_download )
#execute_process(COMMAND ${CMAKE_COMMAND} --build .
#  WORKING_DIRECTORY ${CMAKE_BINARY_DIR}/googletest_download )
# Prevent GoogleTest from overriding our compiler/linker options
# when building with Visual Studio
#set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
# Add googletest directly to our build. This adds
# the following targets: gtest, gtest_main, gmock
# and gmock_main
#add_subdirectory(${CMAKE_BINARY_DIR}/googletest_src
#                 ${CMAKE_BINARY_DIR}/googletest_build)
				 
				 
# Download and unpack kfr at configure time
#configure_file(CMakeLists.kfr.in
#               kfr_download/CMakeLists.txt)
#execute_process(COMMAND ${CMAKE_COMMAND} -G "${CMAKE_GENERATOR}" .
#  WORKING_DIRECTORY ${CMAKE_BINARY_DIR}/kfr_download )
#execute_process(COMMAND ${CMAKE_COMMAND} --build .
#  WORKING_DIRECTORY ${CMAKE_BINARY_DIR}/kfr_download )
#set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
#add_subdirectory(${CMAKE_BINARY_DIR}/kfr_src
#                 ${CMAKE_BINARY_DIR}/kfr_build)
#include_directories ("${CMAKE_BINARY_DIR}/kfr_src/include")
			 

if (MSVC)
	message("Using msvc!")
	set (SRC_PLATFORM_SPECIFIC "${PROJECT_SOURCE_DIR}/src/cognitive_beats/arch/win")
	add_definitions (/D_CRT_SECURE_NO_WARNINGS)
	add_definitions (/D_SCL_SECURE_NO_WARNINGS)
	add_definitions (/D_VARIADIC_MAX=10)
	if(MSVC_VERSION LESS 1800)
		# # VC11 bug: http://code.google.com/p/googletest/issues/detail?id=408
		# #          add_definition(-D_VARIADIC_MAX=10)
		# #          https://github.com/anhstudios/swganh/pull/186/files
		add_definitions (/wd4996)
	endif()
	message(${CMAKE_VS_PLATFORM_TOOLSET})
#endif()
else()
# GCC configurations.
#if (CMAKE_COMPILER_IS_GNUCXX)
	message("Using gnucxx!")
	set (SRC_PLATFORM_SPECIFIC "${PROJECT_SOURCE_DIR}/src/cognitive_beats/arch/linux")
	add_definitions (-std=c++11) # Enable C++11!
    add_definitions(-Wall)
    add_definitions(-Wextra)
    add_definitions(-Wno-unused-parameter)
	add_definitions(-Wno-deprecated-declarations) # Use until we change IPP deprecated functions.
	add_definitions(-fPIC) # Use because we want the libraries to be location independent.
endif ()


include_directories ("${SRC_PLATFORM_SPECIFIC}")
include_directories ("${PROJECT_SOURCE_DIR}/src/")
include_directories ("${PROJECT_SOURCE_DIR}/third_party/googletest/include")
include_directories ("${PROJECT_SOURCE_DIR}/third_party/tonic/src")
#include_directories ("${PROJECT_SOURCE_DIR}/third_party/stk/include")
#add_subdirectory (third_party/stk)
add_subdirectory (third_party/tonic)
add_subdirectory (third_party/googletest/googletest)
add_subdirectory (src/cognitive_beats/beat_generation)
add_subdirectory (src/cognitive_beats/beat_generation_tests)

