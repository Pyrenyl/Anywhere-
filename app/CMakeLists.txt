cmake_minimum_required(VERSION 3.22.1)
project(Anywhere LANGUAGES CXX)

add_library(izuko SHARED src/main/cpp/izuko.cpp)
target_include_directories(izuko PRIVATE src/main/cpp/include)
target_link_options(izuko PRIVATE
  "-Wl,-z,max-page-size=16384"
  "-Wl,-z,common-page-size=16384"
)
