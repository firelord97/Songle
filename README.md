# 🎵 Songle - Music Guessing Game

This repository contains the implementation of **Songle**, a music-based guessing game where players collect words on a map to identify a song. The game utilizes various Android features such as Google Maps and local storage to create an engaging and interactive experience.

---

## 📑 Table of Contents

1. [Introduction](#introduction)
2. [Features](#-features)
3. [Technologies Used](#-technologies-used)
4. [How to Play](#-how-to-play)
5. [Installation](#-installation)
6. [Activity Overview](#-activity-overview)
7. [Scoring System](#-scoring-system)
8. [Future Enhancements](#-future-enhancements)

---

## Introduction

**Songle** is an Android game where players collect words hidden around the map to guess the song title. Words are unlocked by visiting specific locations in a virtual map of the University of Edinburgh Central Campus. The game includes difficulty settings, hints, and a scoring system that rewards players for accurate guesses with fewer hints.

---

## Features

- **Map-Based Gameplay**: Collect words by visiting locations on a Google Maps-based interface.
- **Dynamic Difficulty**: Choose different map levels for varying challenges.
- **Hint System**: Unlock additional words to help identify the song.
- **Score Calculation**: Scores are based on difficulty level, number of words collected, and incorrect guesses.
- **YouTube Integration**: Watch the music video for the guessed song if the answer is correct.

---

## Technologies Used

- **Platform**: Android 7.1.1 (Nougat)
- **Libraries**: Google Maps, Google Play Services, XML Parser
- **Programming Languages**: Java
- **Tools**: Android Studio

---

## How to Play

1. **Start the Game**: Launch the app and select a song from the list.
2. **Select Difficulty**: Choose a map level, with higher levels offering fewer markers.
3. **Collect Words**: Visit locations on the map to unlock words related to the song.
4. **Guess the Song**: Use the collected words to guess the song title or ask for hints to reveal more words.
5. **Win or Try Again**: If the guess is correct, view your score and watch the music video. Otherwise, keep trying until you guess the song.

---

## Installation

To run this project, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in **Android Studio**.
3. Make sure you have the necessary **Google Play Services** and **Google Maps API** keys set up.
4. Build and run the app on an emulator or an Android device with at least **Android 7.1.1 (Nougat)**.

---

## Activity Overview

1. **Main Activity**: Start screen with options to play or view instructions.
2. **HowToPlay Activity**: Instructions and game mechanics.
3. **ListSelect Activity**: Displays a list of songs to choose from.
4. **LevelSelect Activity**: Select a map (difficulty level) for the chosen song.
5. **Maps Activity**: Main gameplay screen where players collect words on the map.
6. **Guess Activity**: View collected words and guess the song title.
7. **CorrectAnswer Activity**: Displays the score and song details, with a YouTube link to the music video.

---

## Scoring System

The scoring system is designed to reward players for accurate guesses:

- **Score Formula**: `100 * (Number of words not collected / Total Words) * (1 + MapLevel * 0.1) - Number of Guesses * 10`
- **Map Level Impact**: Higher levels yield higher potential scores but have fewer words available.
- **Hints**: Using hints may lower the final score by unlocking additional words.

---

## Future Enhancements

- **Multiplayer Mode**: Allow multiple players to compete in real-time.
- **Additional Maps and Songs**: Expand the song library and map locations.
- **Achievements and Leaderboards**: Integrate with Google Play Games for tracking achievements and high scores.
- **Offline Mode**: Improve local storage to allow offline gameplay.

---

