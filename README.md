# Wanderer
<img src="logo.png" alt="drawing" width="200"/>


## Overview

Wanderer is an Android application that will provide functionality as a travel itinerary for organizing common travel information. Highlighted features include organizing activities by type and priority, with easy editing for a traveler’s on-the-fly itinerary management needs. 

## Features
- Easily add, edit, and delete trips
- Keep track of activities, and easily change times/details as needed
- Arbitrary numbers of activities per day in an auto-sorted list
- Fully offline; no Internet connection required

## Setup & Usage
Download the APK from the releases tab to your phone. Installation instructions will depend on your file manager; you may need to give your file manager permission to install apps. This link has a tutorial: https://www.androidauthority.com/how-to-install-apks-31494/

Alternatively, to install from source, import the project into Android Studio. It is highly recommended to use physical device debugging rather than to attempt to use the emulator; the emulator may have bugs that don't exist on a physical device. To use physical device debugging, plug your phone into your computer, ensure it's the selected device (at the top, next to the run button), and hit the run button. Your phone must be in developer mode and have USB debugging enabled

When starting the app, you will start on the trip list screen. Press the + button at the bottom to create a trip. Adjust settings accordingly. Then, press the trip card to enter the trip. Trips can also be edited from the trip list using the edit icon. To delete a trip, press the edit trip button and then the Delete button.

Entering the trip will send you to the calendar menu. The +New button can be used to add activities. Use the previous/next buttons to switch between days. Activities can be edited and deleted using their respective buttons. Pressing Exit to Trips will return you to the trip editor screen.

Adding, deleting, and editing of trips and activities is saved automatically. 

NOTE: The app does not currently support editing trip start/end dates. **Do not attempt to edit trip arrival/departure dates. All trip data will be deleted.**

## Technologies Used
This app uses Kotlin with the Jetpack Compose library, as well as the Kotlinx Serialization library. 

## Team Members

Michael Gonzalez-Vasquez: Lead Designer<br/>
Jaedon Snyder: Lead Programmer<br/>
Sylas Wilson: Documentation Lead