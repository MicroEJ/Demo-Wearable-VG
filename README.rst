.. image:: https://shields.microej.com/endpoint?url=https://repository.microej.com/packages/badges/sdk_6.0.json
   :alt: sdk_6.0 badge
   :align: left

.. image:: https://shields.microej.com/endpoint?url=https://repository.microej.com/packages/badges/arch_7.20.json
   :alt: arch_7.18 badge
   :align: left

.. image:: https://shields.microej.com/endpoint?url=https://repository.microej.com/packages/badges/gui_3.json
   :alt: gui_3 badge
   :align: left

Overview
========

The version of this demo is compatible with `SDK 6 <https://docs.microej.com/en/latest/SDK6UserGuide/index.html>`_ only.
If you are using `SDK 5 <https://docs.microej.com/en/latest/SDKUserGuide/index.html>`_, please have a look at `1.0.1-sdk5 <https://github.com/MicroEJ/Demo-Wearable-VG/tree/1.0.1-sdk5>`_ version.

This repository contains a smartwatch demo application. 

It features several watch faces and side applications such as heart rate monitoring, activity tracking, compass, and more.

The application uses `MicroUI <https://docs.microej.com/en/latest/ApplicationDeveloperGuide/UI/MicroUI/index.html>`_ and `MicroVG <https://docs.microej.com/en/latest/ApplicationDeveloperGuide/UI/MicroVG/index.html>`_ to exploit hardware's vector capabilities and to make a nice-looking/efficient user interface.

Following you can have a look at some screens from the demo: 

- Flower watchface screen:
.. image:: pictures/watchface_flower.png
   :alt: Flower Watchface
   :align: center

- Application list screen: 
.. image:: pictures/watchface_application_list.png
   :alt: Application List
   :align: center

- Heart Rate application screen: 
.. image:: pictures/heartrate_application.png
   :alt: HeartRate Application
   :align: center

Contents
========

Watch-vg
--------

This project contains the application code. It depends on the watchfaces and the utils to launch the Demo Wearable VG application. 
The use of this demo will be explainded in more detail in the README of this project.

For more information, please refer to this `README.md <watch-vg/README.md>`_.

Watch-util
----------

This project contains the utility methods used by the Demo Wearable VG application.

For more information, please refer to this `README.md <watch-util/README.md>`_.

Watchface-flower
----------------

This project contains the flower watchface used by the Demo Wearable VG application.

For more information, please refer to this `README.md <watchface-flower/README.md>`_.

Watchface-flower-lp
-------------------

This project contains the flower low power watchface used by the Demo Wearable VG application.

For more information, please refer to this `README.md <watchface-flower-lp/README.md>`_.

Watchface-sport
---------------

This project contains the sport watchface used by the Demo Wearable VG application.

For more information, please refer to this `README.md <watchface-sport/README.md>`_.

Usage
=====

Please refer to this `README.md <watch-vg/README.md>`_ for more information about how to use and launch this application.

.. ReStructuredText
.. Copyright 2023 MicroEJ Corp. All rights reserved.
.. Use of this source code is governed by a BSD-style license that can be found with this software.
