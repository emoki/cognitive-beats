project (beat_generation_tests)

# beat_generation_tests version number.
# TODO - finish this when you have time!
set (beat_generation_tests_VERSION_MAJOR 1)
set (beat_generation_tests_VERSION_MINOR 0)
set (beat_generation_tests_VERSION_PATCH 0)
set (beat_generation_tests_VERSION_TWEAK 0)

# Set the source files.
set (beat_generation_tests_SRCS 
	beat_generation_tests.cpp
	main_tests.cpp
	${SRC_PLATFORM_SPECIFIC}/stdafx.cpp)

#set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS ON)
add_executable (beat_generation_tests ${beat_generation_tests_SRCS}) 

TARGET_LINK_LIBRARIES(beat_generation_tests beat_generation gtest)
