# Install script for directory: C:/Users/chris/Documents/GitHub/TowerDefenceEvolution/CrispinMobile/src/main/cpp/freetype-2.9

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "C:/Program Files (x86)/CrispinMobile")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "Debug")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

# Install shared libraries without execute permission?
if(NOT DEFINED CMAKE_INSTALL_SO_NO_EXE)
  set(CMAKE_INSTALL_SO_NO_EXE "0")
endif()

# Is this installation the result of a crosscompile?
if(NOT DEFINED CMAKE_CROSSCOMPILING)
  set(CMAKE_CROSSCOMPILING "TRUE")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/freetype2" TYPE DIRECTORY FILES "C:/Users/chris/Documents/GitHub/TowerDefenceEvolution/CrispinMobile/src/main/cpp/freetype-2.9/include/" REGEX "/internal$" EXCLUDE REGEX "/ftconfig\\.h$" EXCLUDE REGEX "/ftoption\\.h$" EXCLUDE)
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/freetype2/freetype/config" TYPE FILE FILES
    "C:/Users/chris/Documents/GitHub/TowerDefenceEvolution/CrispinMobile/.externalNativeBuild/cmake/debug/x86/freetype-2.9/include/freetype/config/ftconfig.h"
    "C:/Users/chris/Documents/GitHub/TowerDefenceEvolution/CrispinMobile/.externalNativeBuild/cmake/debug/x86/freetype-2.9/include/freetype/config/ftoption.h"
    )
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "C:/Users/chris/Documents/GitHub/TowerDefenceEvolution/CrispinMobile/.externalNativeBuild/cmake/debug/x86/freetype-2.9/libfreetyped.a")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  if(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/cmake/freetype/freetype-config.cmake")
    file(DIFFERENT EXPORT_FILE_CHANGED FILES
         "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/cmake/freetype/freetype-config.cmake"
         "C:/Users/chris/Documents/GitHub/TowerDefenceEvolution/CrispinMobile/.externalNativeBuild/cmake/debug/x86/freetype-2.9/CMakeFiles/Export/lib/cmake/freetype/freetype-config.cmake")
    if(EXPORT_FILE_CHANGED)
      file(GLOB OLD_CONFIG_FILES "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/cmake/freetype/freetype-config-*.cmake")
      if(OLD_CONFIG_FILES)
        message(STATUS "Old export file \"$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/cmake/freetype/freetype-config.cmake\" will be replaced.  Removing files [${OLD_CONFIG_FILES}].")
        file(REMOVE ${OLD_CONFIG_FILES})
      endif()
    endif()
  endif()
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib/cmake/freetype" TYPE FILE FILES "C:/Users/chris/Documents/GitHub/TowerDefenceEvolution/CrispinMobile/.externalNativeBuild/cmake/debug/x86/freetype-2.9/CMakeFiles/Export/lib/cmake/freetype/freetype-config.cmake")
  if("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib/cmake/freetype" TYPE FILE FILES "C:/Users/chris/Documents/GitHub/TowerDefenceEvolution/CrispinMobile/.externalNativeBuild/cmake/debug/x86/freetype-2.9/CMakeFiles/Export/lib/cmake/freetype/freetype-config-debug.cmake")
  endif()
endif()
