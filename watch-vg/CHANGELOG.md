The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 4.0.0 - 2023-12-12

### Added

- SDK 6 compatibility with gradle.

### Removed

- SDK 5 compatibility.

## 3.0.0 - 2023-06-12

### Added
- Add Java 1.7 Compliance settings

### Removed
- Remove KF dependency.

## 2.0.2 - 2023-06-12

### Fixed
   - Fix number "vibration" on activity page. 

### Changed
   - Update Platform version to 2.0.2.
   - Add a gradient fill in mascot.xml (mascot body).

## 2.0.0 - 2022-10-19

### Changed
   - Low-power watchface: improve the power consumption by rendering and flushing at the same time.
   - Replace VectorFont and VGLite library by VG Pack. 
   - Change Platform version to 2.0.1.

## 1.4.1 - 2021-11-29

### Changed
   - Change Platform version to 1.4.1. 

## 1.4.0 - 2021-09-23

### Changed
   - Change Platform version to 1.4.0.
   - Remove svg Hands x4 scaling factor.
   - Update text sizes for new vectorfont lib.
   - Update vglite library dependencies.
   - Update vectorfont library dependencies.
   - Update some svg files with the version provided by MicroEJ designer without manual Inkscape modifications.
   - Move PowerManagementHelper to com.nxp.rt595.util package.
   - Update VectorFontGreeting matrix initialisation for drawChar new origin location.
   - Update START_DATE.
   
## 1.3.0 - 2021-05-20

### Added

   - Add PowerManagementHelper to control BSP perfomance profiles and services.
   - Add NetworkTimeUpdateThread class and net/wifi support to update current time with an NTP request.

### Changed

   - Use animator provided by the Desktop (since MWT 3.2.0).
   - Remove forks of observable classes and use observable lib instead.
   - Change color of circle arc cursor in application list.
   - Add start/stop methods in the ObservableService interface.
   - Manage the feature loader as a service.
   - LowPower watchface: update minute and hour hands angle only every minute and crop rendering to minimal possible area.

## 1.2.3 - 2021-03-15

### Added

   - Add multi-app support for watchfaces.
   - Add Watchface "low power" : com.microej.demo.watch.watchface.flowerlp.
   - Add Watchface "simple" : com.microej.demo.watch.watchface.simple.
   - Add circle arc cursor in application list.

### Changed

   - Change power level widget in sport watchface.
   - Low-power: FreeRTOS tickless and Deep Sleep modes are enabled by default.
   - Use fixed image/text spacing in application list.
   - Update mwt dependency.

### Removed

   - Extract watchfaces code and utility code to external modules.

### Fixed

   - Fix carousel state after new lay out.

## 1.2.1 - 2020-12-21
### Fixed
   - An issue when drawing a circle arc with an arc angle of 360 degrees.
   
### Changed
   - Handle MicroUI Command events instead of Button events.

## 1.2.0 - 2020-12-15
### Changed
   - Use latest platform made with new Platform Qualification's additional scripts (step3)
   - Resize application to fit iMXRT595-revC board
   - Use SVG converted as immutables instead of Java code
   - Reduce Java heap usage

### Fixed
   - Replace font "ZhiMangXing-Regular.ttf" with "ZCOOLKuaiLe-Regular.ttf"
   - Rename "Arc de Cercle VG" to "Cyclone VG" in french
   - Reduce visibility of methods in the type hierarchy of AbstractWatchface
   - Fix package and name of some classes
   

## 1.1.1 - 2020-10-14
### Changed
  - Split the code of class StylesheetFactory for better readability
  - Migrated to latest versions of MicroUI/MWT/Widget
  - Changed the implementation of performances optimization (Compass and Activity) 

### Fixed
  - Limit heart rate values between min and max values
  - Fixed issues reported by Sonar analysis
  - Fixed wrong copyright on some files
  - Fixed README


## 1.1.0 - 2020-07-13
### Added
  - Added NLS (National Language Support): English, Chinese, French
  - Added the "Settings" page
  - Added a full-vector chart on the "Heart Rate" page (vglite paths and gradients)
  - Added the calories progress on the "Activity" page (progress bar with vglite gradient, vector font)
  - Added full vector font support across the application
  - Added the NXP logo to the "Mascot" page
  - Added the "Vector font" page
  - Added support for a mock implementation of the current heart rate value and current battery level

### Changed
  - Changed the date and time format on the "Flower" watchface to "YYYY-MM-DD hh:mm:ss.ss"
  - Improved the performances of the "Flower" watchface, "Compass" and "Activity" pages

### Fixed
  - Fixed resources (images, fonts)

  
## 1.0.0 - 2020-06-23
### Added
  - Added full support for MicroUI/MWT/Widget version 3
  - Added vector hands on the watchfaces (vglite paths)
  - Added rotation and scaling effects on the vector hands
  - Added the "radar effect" on the second hand of the "Flower" watchface (vglite gradient)
  - Added the date and time on the Flower watchface ("YYYY-MM-DD" format, vector font)
  - Added the stopwatch on the "Activity" page (progress bar with vglite gradient, vector font with gradient)
  - Added new design for the application list (scaling on icons and vector font)
  
### Removed
  - Removed the "Freetype" demo page
  - Removed the "Message" stub page
  


  
---
_Copyright 2019-2023 MicroEJ Corp. All rights reserved._  
_Use of this source code is governed by a BSD-style license that can be found with this software._  
 
