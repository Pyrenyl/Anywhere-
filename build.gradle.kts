// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    google()
    gradlePluginPortal()
    maven("https://jitpack.io")
  }
  dependencies {
    classpath("com.android.tools.build:gradle:9.3.0")
    classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.10")
    classpath("dev.rikka.tools.materialthemebuilder:gradle-plugin:1.5.1")
  }
}

allprojects {
  repositories {
    google()
    maven("https://jitpack.io")
    mavenCentral()
  }
}

tasks.register<Delete>("clean") {
  delete(layout.buildDirectory)
}
