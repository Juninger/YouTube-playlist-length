package org.example;

import java.io.IOException;
import java.time.Duration;

public class App {
    public static void main( String[] args ) throws IOException, InterruptedException {
        final String API_KEY = "YOUR_API_KEY_HERE"; // Enter you API-key here, see https://developers.google.com/youtube/v3/getting-started for info on API-keys and Google Projects
        final String PLAYLIST_ID = "YOUR_PLAYLIST_ID_HERE"; // e.g. from https://www.youtube.com/playlist?list={PLAYLIST_ID} only the PLAYLIST_ID is needed

        PlaylistLengthCalculator api = new PlaylistLengthCalculator(API_KEY, PLAYLIST_ID);

        Duration duration = api.getTotalDuration();

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.toSeconds() % 60;

        String time = String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);

        System.out.println(time);

    }
}
