plugins {
  id("com.android.application")
  id("com.google.devtools.ksp")
  id("kotlin-parcelize")
  id("dev.rikka.tools.materialthemebuilder")
}

val verName = "2.5.5"
val verCode = 2050500
val cfgReleaseStoreFile = providers.environmentVariable("RELEASE_STORE_FILE").orNull
val cfgReleaseStorePassword = providers.environmentVariable("RELEASE_STORE_PASSWORD").orNull
val cfgReleaseKeyAlias = providers.environmentVariable("RELEASE_KEY_ALIAS").orNull
val cfgReleaseKeyPassword = providers.environmentVariable("RELEASE_KEY_PASSWORD").orNull

android {
  namespace = "com.absinthe.anywhere_"
  compileSdk = 37

  defaultConfig {
    applicationId = "com.absinthe.anywhere_"
    minSdk = 24
    targetSdk = 36
    versionCode = verCode
    versionName = verName
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    manifestPlaceholders["appName"] = "Anywhere-"
    ndk {
      abiFilters += "arm64-v8a"
    }
  }

  ksp {
    arg("room.incremental", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
  }

  buildFeatures {
    aidl = true
    buildConfig = true
    viewBinding = true
  }

  androidResources {
    generateLocaleConfig = true
    localeFilters += listOf("en", "zh-rCN", "zh-rTW", "zh-rHK")
  }

  signingConfigs {
    if (cfgReleaseStoreFile != null) {
      create("release") {
        storeFile = file(cfgReleaseStoreFile)
        storePassword = cfgReleaseStorePassword
        keyAlias = cfgReleaseKeyAlias
        keyPassword = cfgReleaseKeyPassword
      }
    }
  }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      manifestPlaceholders["appName"] = "Anywhere-β"
      buildConfigField("boolean", "BETA", "true")
    }
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      signingConfigs.findByName("release")?.let { signingConfig = it }
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      buildConfigField("boolean", "BETA", "false")
    }
    all {
      buildConfigField(
        "String",
        "APP_CENTER_SECRET",
        "\"" + System.getenv("APP_CENTER_SECRET").orEmpty() + "\""
      )
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  dependenciesInfo.includeInApk = false

  packaging {
    resources {
      excludes += "META-INF/**"
      excludes += "okhttp3/**"
      excludes += "kotlin/**"
      excludes += "org/**"
      excludes += "**.properties"
      excludes += "**.bin"
    }
  }
}

androidComponents.onVariants { variant ->
  variant.outputs.forEach {
    it.outputFileName.set("Anywhere-${verName}-${verCode}-${variant.name}.apk")
  }
}

materialThemeBuilder {
  themes {
    create("anywhere") {
      primaryColor = "#8BC34A"
      lightThemeFormat = "Theme.Material3.Light.%s"
      lightThemeParent = "Theme.Material3.Light.Rikka"
      darkThemeFormat = "Theme.Material3.Dark.%s"
      darkThemeParent = "Theme.Material3.Dark.Rikka"
    }
  }
  generatePalette = true
}

repositories {
  mavenCentral()
}

configurations.all {
  exclude(group = "androidx.appcompat", module = "appcompat")
  exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
  exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

  implementation(project(":color-picker"))
  implementation(files("libs/IceBox-SDK-1.0.6.aar"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

  implementation("com.github.zhaobozhen.libraries:me:1.1.5.4")
  implementation("com.github.zhaobozhen.libraries:utils:1.1.5.4")

  val appCenterSdkVersion = "5.0.3"
  implementation("com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}")
  implementation("com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}")

  //Android X
  val roomVersion = "2.8.4"
  implementation("androidx.room:room-runtime:${roomVersion}")
  implementation("androidx.room:room-ktx:${roomVersion}")
  ksp("androidx.room:room-compiler:${roomVersion}")
  androidTestImplementation("androidx.room:room-testing:${roomVersion}")

  val lifecycleVersion = "2.9.0"
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:${lifecycleVersion}")
  implementation("androidx.lifecycle:lifecycle-common-java8:${lifecycleVersion}")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycleVersion}")

  implementation("androidx.browser:browser:1.8.0")
  implementation("androidx.constraintlayout:constraintlayout:2.2.1")
  implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")
  implementation("androidx.viewpager2:viewpager2:1.1.0")
  implementation("androidx.recyclerview:recyclerview:1.4.0")
  implementation("androidx.drawerlayout:drawerlayout:1.2.0")

  //KTX
  implementation("androidx.collection:collection-ktx:1.4.5")
  implementation("androidx.activity:activity-ktx:1.10.1")
  implementation("androidx.fragment:fragment-ktx:1.8.7")
  implementation("androidx.palette:palette-ktx:1.0.0")
  implementation("androidx.core:core-ktx:1.16.0")
  implementation("androidx.preference:preference-ktx:1.2.1")

  //Google
  implementation("com.google.android.material:material:1.12.0")

  //Function
  implementation("com.github.bumptech.glide:glide:4.16.0")
  ksp("com.github.bumptech.glide:compiler:4.16.0")

  implementation("com.google.code.gson:gson:2.13.2")
  implementation("com.google.zxing:core:3.5.4")
  implementation("com.blankj:utilcodex:1.31.1")
  implementation("com.tencent:mmkv:2.4.0")
  implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.11")
  implementation("com.github.heruoxin.Delegated-Scopes-Manager:client:master-SNAPSHOT")
  implementation("com.github.topjohnwu.libsu:core:6.0.0")
  implementation("com.github.thegrizzlylabs:sardine-android:0.9") {
    exclude(group = "xpp3", module = "xpp3")
  }
  implementation("com.jonathanfinerty.once:once:1.3.1")
  implementation("org.lsposed.hiddenapibypass:hiddenapibypass:6.1")
  implementation("com.jakewharton.timber:timber:5.0.1")

  //UX
  implementation("com.drakeet.about:about:2.5.2")
  implementation("com.drakeet.multitype:multitype:4.3.0")
  implementation("com.drakeet.drawer:drawer:1.0.3")
  implementation("com.github.sephiroth74:android-target-tooltip:2.0.4")
  implementation("com.leinardi.android:speed-dial:3.3.0")
  implementation("me.zhanghai.android.fastscroll:library:1.3.0")

  val shizukuVersion = "13.1.5"
  // required by Shizuku and Sui
  implementation("dev.rikka.shizuku:api:$shizukuVersion")
  // required by Shizuku
  implementation("dev.rikka.shizuku:provider:$shizukuVersion")

  implementation("dev.rikka.rikkax.appcompat:appcompat:1.6.1")
  implementation("dev.rikka.rikkax.core:core:1.4.1")
  implementation("dev.rikka.rikkax.material:material:2.7.2")
  implementation("dev.rikka.rikkax.recyclerview:recyclerview-ktx:1.3.2")
  implementation("dev.rikka.rikkax.widget:borderview:1.1.0")
  implementation("dev.rikka.rikkax.preference:simplemenu-preference:1.0.3")
  implementation("dev.rikka.rikkax.insets:insets:1.3.0")
  implementation("dev.rikka.rikkax.layoutinflater:layoutinflater:1.3.0")
  implementation("dev.rikka.rikkax.material:material-preference:2.0.0")

  //Network
  implementation("com.squareup.okhttp3:okhttp:4.12.0")
  implementation("com.squareup.okio:okio:3.9.1")

  //Rx
  implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
  implementation("io.reactivex.rxjava2:rxjava:2.2.21")
  implementation("org.reactivestreams:reactive-streams:1.0.4")

  //Debug
  testImplementation("junit:junit:4.13.2")
  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
  androidTestImplementation("androidx.test:runner:1.6.2")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
