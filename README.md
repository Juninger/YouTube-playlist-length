# YouTube Playlist length calculator

This module allows you to check the total length of a user's ***public*** YouTube playlist using the YouTube Data API.

This can be used as a starter template to implement the backend API functionality in your own project. If you are looking for an easy way to see the length of your own playlists or 'Watch later' list, take a look at [THIS PROJECT](https://github.com/Juninger/YouTube-watch-later-length-calculator) instead! 

## Prerequisites

To use this application, you will need the following:

    🔑 An API key for the YouTube Data API.

    🆔 The playlist ID of the YouTube playlist you want to check.

## Technologies Used

This application is written in Java 17 and utilizes the following technologies:

- Maven: A build automation and dependency management tool.
- Jackson: A library for JSON serialization and deserialization.

## How to Run the Application

To run the application, follow these steps:

1. Clone the project to your local machine.
2. Ensure that you have Maven installed and configured properly.
3. Open the project in IntelliJ.
    - If prompted, trust the project to proceed with the updates.
5. Use the Maven context menu (e.g., `mvn clean install`) to update the project dependencies.
6. Open the `App.java` file and locate the following variables:
   - `API_KEY`: Replace this with your own API key for the YouTube Data API.
   - `PLAYLIST_ID`: Replace this with the playlist ID of the YouTube playlist you want to check.
7. Save the changes to the `App.java` file.
8. Start the application, and the total duration of the playlist will be printed in the console.

> ⚠️ Please note that the number of calls your API key allows may have restrictions. For more information, refer to the credentials page for the YouTube API.

## License

This project is licensed under the [MIT License](LICENSE). Feel free to modify and distribute it as needed.
