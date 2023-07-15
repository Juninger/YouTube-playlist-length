package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class PlaylistLengthCalculator {

    private final String baseUrl = "https://www.googleapis.com/youtube/v3/playlistItems";

    public PlaylistLengthCalculator(String apiKey, String playlistId) throws IOException, InterruptedException {

        // Constructs new HttpClient using java.net library
        HttpClient client = HttpClient.newHttpClient();

        // Transforms any special characters to standardized UTF-8 format that is web-safe
        String encodedID = URLEncoder.encode(playlistId, StandardCharsets.UTF_8);
        String encodedKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        // Prepares query parameters
        String partParam = "?part=contentDetails"; // Comma-separated list [contentDetails, id, snippet, status]
        String idParam = "&playlistId=" + encodedID;
        String keyParam = "&key=" + encodedKey;
        String maxResultsParam = "&maxResults=" + 50; // 0-50 accepted, default value is 5

        // Constructs finalized URL for request
        String finalUrl = baseUrl + partParam + idParam + keyParam + maxResultsParam;

        // Prepares and builds request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalUrl))
                .GET()
                .build();

        // Sends request and stores response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
