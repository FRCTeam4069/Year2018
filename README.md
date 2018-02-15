# Year2018
This repository contains FIRST Robotics Competition Team 4069's robot code for the 2018 Power Up season.

## Contributing
* All team members wishing to contribute should commit directly to the `develop` branch, where code will be tested.
* Code from `develop` that has been tested and guaranteed to be stable and as bug-free as possible is merged into `master`.
* At competitions, we should use the `develop` branch for quick changes so that we have the stable `master` to fall back on in case of serious problems.

## Commands and Subsystems
We are generally trying to base our commands and subsystems architecture on the system described in the following article: https://github.com/BadRobots1014/BadRobot2013/wiki/Command-Based-Structure

## Build System
We are using the GradleRIO build system, which allows easy building and deploying from the command line on macOS, Windows, and Linux. It also provides IDE integration for IntelliJ IDEA and Eclipse, which you can enable by running `./gradlew idea` or `./gradlew eclipse` in the project folder, respectively. You should not need to install anything else to have functional IDE integration. (Replace `./gradlew` with `gradlew` for all of these commands if you are using `cmd` on Windows.) Finally, you can build and deploy on the robot by running `./gradlew deploy` while connected to the robot. More detailed instructions can be found at https://github.com/Open-RIO/GradleRIO.

## Style Guide
Team members are asked to import `FRC4069-Style-Guide-Eclipse.xml` or `FRC4069-Style-Guide-IntelliJ.xml` (depending on whether they are using Eclipse or IntelliJ IDEA) into their IDEs and format code files appropriately.

## Kotlin
Some portions of this year's code are written in the Kotlin programming language.
To familiarize yourself with the language, take some time to run through [The Kotlin Koans](https://kotlinlang.org/docs/tutorials/koans.html)
