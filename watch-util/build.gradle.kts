//Copyright 2023 MicroEJ Corp. All rights reserved.
//Use of this source code is governed by a BSD-style license that can be found with this software.

plugins {
    id("com.microej.gradle.addon-library") version "0.13.0"
}

group = "com.microej.demo.watch"
version = "3.0.0"

dependencies {
    implementation("ej.api:edc:1.3.3")
    implementation("ej.api:bon:1.4.0")
    implementation("ej.api:microui:3.0.3")
    implementation("ej.api:drawing:1.0.2")
    implementation("ej.api:microvg:1.1.0")

    implementation("ej.library.runtime:service:1.1.1")
    implementation("ej.library.util:observable:2.0.0")
    implementation("ej.library.ui:mwt:3.2.0")
}

