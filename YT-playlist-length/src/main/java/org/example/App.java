package org.example;

import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException, InterruptedException {
        final String API_KEY = "YOUR_API_KEY_HERE"; // Enter you API-key here, see https://developers.google.com/youtube/v3/getting-started for info on API-keys and Google Projects
        final String PLAYLIST_ID = "PLDDl-WM3TJ4jaZTGNn8YjdYLvWriRDN3A"; // Shorter list (10 videos) for testing 1-page-response
        final String PLAYLIST_ID_2 = "PLIPaB4hHh2xQ4Ns1BU6V_LfCfBynOG1y1"; // Longer list (69 videos) for testing multi-page-response

        PlaylistLengthCalculator api = new PlaylistLengthCalculator(API_KEY, PLAYLIST_ID_2);

        String idList = api.getPlaylistItems();

    }
}
