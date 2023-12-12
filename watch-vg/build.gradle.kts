// Copyright 2023 MicroEJ Corp. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be found with this software.

import java.io.FileInputStream
import java.util.Properties

plugins{
    id("com.microej.gradle.application") version "0.13.0"
}

group = "com.microej.demo.watch.imxrt595"
version = "4.0.0"

microej {
    applicationMainClass = "com.microej.Demo"
}

dependencies {
    implementation("ej.api:edc:1.3.3")
    implementation("ej.api:bon:1.4.0")
    implementation("ej.api:trace:1.1.0")
    implementation("ej.api:microui:3.0.3")
    implementation("ej.api:drawing:1.0.2")
    implementation("ej.api:microvg:1.1.0")

    implementation("ej.library.eclasspath:io:1.1.0")
    implementation("ej.library.eclasspath:properties:1.1.0")
    implementation("ej.library.eclasspath:stringtokenizer:1.1.0")
    implementation("ej.library.eclasspath:collections:1.4.0")
    implementation("ej.library.eclasspath:logging:1.2.1")
    implementation("ej.library.runtime:annotation:1.0.0")
    implementation("ej.library.runtime:basictool:1.5.0")
    implementation("ej.library.runtime:message:2.1.0")
    implementation("ej.library.runtime:nls:3.0.1")
    implementation("ej.library.runtime:service:1.1.1")
    implementation("ej.library.ui:mwt:3.4.0")
    implementation("ej.library.ui:widget:3.1.0")
    implementation("ej.library.ui:motion:3.1.1")
    implementation("ej.library.util:map:1.1.0")
    implementation("ej.library.util:observable:2.0.0")
    implementation("com.microej.library.runtime:nls-po:2.2.0")

    implementation("com.microej.demo.watch:watch-util:3.0.0")
    implementation("com.microej.demo.watch.watchface:watchface-sport:3.0.0")
    implementation("com.microej.demo.watch.watchface:watchface-flower:3.0.0")
    implementation("com.microej.demo.watch.watchface:watchface-flower-lp:3.0.0")

    microejVeePort("com.nxp.vee.mimxrt595:evk_platform:1.2.0")
}