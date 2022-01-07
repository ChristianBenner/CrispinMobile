# A CMakeList test to see if multiple native libraries can be used in Android

# Set version of CMake
cmake_minimum_required(VERSION 3.10.2)

# Create project wide CMake
project(CrispinMobile)

# Add the library sub directories (each have their own CMakeLists.txt)
message("Adding freetype library")
add_subdirectory(freetype)
message("Added freetype library")

message("Adding Oboe audio library")
add_subdirectory(oboe)
include_directories(oboe/include)
message("Added Oboe audio library")

message("Adding Crispin Native Interface library")
add_subdirectory(nativeinterface)
message("Added Crispin Native Interface library")


message("Finished CMake Setup")
