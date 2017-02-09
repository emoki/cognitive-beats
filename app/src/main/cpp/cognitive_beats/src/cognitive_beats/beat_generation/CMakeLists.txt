project (beat_generation)

# beat_generation version number.
# TODO - finish this when you have time!
set (beat_generation_VERSION_MAJOR 1)
set (beat_generation_VERSION_MINOR 0)
set (beat_generation_VERSION_PATCH 0)
set (beat_generation_VERSION_TWEAK 0)

# Set the source files.
set (beat_generation_SRCS 
	beat_generation.cpp
	beat_generation.h
	beat_generation_impl.h
	${SRC_PLATFORM_SPECIFIC}/stdafx.cpp)

#set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS ON)
add_library (beat_generation STATIC ${beat_generation_SRCS}) 
target_link_libraries(beat_generation tonic)
