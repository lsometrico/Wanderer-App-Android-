# Wanderer
<img src="logo.png" alt="drawing" width="200"/>


## Overview

Wanderer is an Android application that will provide functionality as a travel itinerary for organizing common travel information. Highlighted features include organizing activities by type and priority, with easy editing for a traveler’s on-the-fly itinerary management needs.

## Features
- Easily add, edit, and delete trips of any length
- Keep track of activities, and easily change times/details as needed
- Arbitrary numbers of activities per day in an auto-sorted list
- Fully offline; no Internet connection required

## Application Compatibility

The application was developed on Android Studio (vers. Panda 1 | 2025.3.1) with Kotlin (compiler v2.3.0) and tested on Android Studio Emulator (26.4.9) with Android SDK 36.0 and 36.1, with physical device debugging completed on Android SDK 30. By Android Studio documentation, the application should be operable from Android SDK 26 through 36, but testing scope has not verified this.

## Setup & Usage
Download the APK from the releases tab to your phone. Installation instructions will depend on your file manager; you may need to give your file manager permission to install apps. This link has a tutorial: https://www.androidauthority.com/how-to-install-apks-31494/

Alternatively, to install from source, import the project into Android Studio. You will need to edit the path in the setting’s file local.properties to the location of your emulator’s SDK. It is highly recommended to use physical device debugging rather than to attempt to use the emulator; the emulator is often slower and may have bugs that don't exist on a physical device. To use physical device debugging, plug your phone into your computer, ensure it's the selected device (at the top, next to the run button), and hit the run button. Your phone must be in developer mode and have USB debugging enabled.

When starting the app, you will start on the trip list screen. Press the + button at the bottom to create a trip. Adjust settings accordingly. Then, press the trip card to enter the trip. Trips can also be edited from the trip list using the edit icon. To delete a trip, press the edit trip button and then the Delete button.

Entering the trip will send you to the calendar menu. The +New button can be used to add activities. Use the previous/next buttons to switch between days. Activities can be edited and deleted using their respective buttons. Pressing Exit to Trips will return you to the trip editor screen.

Adding, deleting, and editing of trips and activities is saved automatically. The app does not currently support editing of activities to different days in a trip. Instead, delete the trip, switch to the new day, and recreate the trip as desired.

NOTE: The app does not currently support editing trip start/end dates. **Do not attempt to edit trip arrival/departure dates. All trip data will be deleted.**

## Known Bugs

Editing arrival/departure dates in trip causes all associated activities to be deleted. It also does not edit the number of days in the trip.

Up/down arrows in Calendar change the location within the list but do not update the stored time.

Editing time does not change the location of the activity within the list.

An unknown error caused deletion of trip data. Could not replicate, but worth keeping in mind.

## Technologies Used
This app uses Kotlin with the following libraries: Jetpack Compose and Kotlinx Serialization, both licensed under the Apache license. 

Fonts utilized are Reglo and Roboto, and are licensed under the SIL Open Font License Version 1.1.

## Team Members

Michael Gonzalez-Vasquez (https://github.com/lsometrico): Lead Designer<br/>
Jaedon Snyder (https://github.com/jsnyde13git): Lead Programmer<br/>
Sylas Wilson (https://github.com/saesy): Documentation Lead