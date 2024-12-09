# Emotion Echo

## Overview
![alt text](image.png)

Emotion Echo is an Android application designed to help users who may struggle to recognize
emotions in speech. With the use of machine learning, this app analyzes audio to detect and display
emotions in speech, assisting users in understanding emotional cues in their own or others' voices.
The application additionally allows users to practice recognizing emotion in pre-existing audio clips.
This project is currently in progress.

There is an increasing need for learning how to understand emotional information from speech in conversations.
Emotion Echo provides an efficient way to detect emotion from audio recordings in real-time and present accurate
feedback for users. The app will also provide user features including
multiple microphone modes, account setup, and database they can use for practice.


## Features
- Real-Time speech to emotion recognition with visual reporting (3 second intervals)
- Emotion classification practive mode with real speech audio
- Login Screen
- Logout Option
- Signup Screen and credential management
- New user navigation tutorial
- Settings Screen
- Android Dark mode integration
- Android Wallpaper integration and custom developed theme


## Bugs
- When running on emulator in android studio, poor audio quality causes inaccurate machine learning emotion predictions

## Android Installation Instructions
- Go to device settings, Privacy and Security.
- Toggle the option "Unknown sources" to "On".
- Use the web browser to download the APK file from the release on this github page
- Locate the APK file in your file manager
- Tap on the APK file to start the installation process
- Follow the on-screen promps to complete the installation.

## Android Studio Build Instructions
- Clone the project in your Android Studio
- Confirm you have java 20+ installed:
  `java -version`
- Install java if needed:
  https://www.oracle.com/cis/java/technologies/downloads/
- Navigate to Device Manager and Add the latest device for best performance (Pixel 9 Pro API 34 is current emulator being used)
- Build & Run application with the green play button at the top of the screen (Build takes > 1min)
- **Warning:** Emulator and application require a large amount of processing power with machine learning libraries