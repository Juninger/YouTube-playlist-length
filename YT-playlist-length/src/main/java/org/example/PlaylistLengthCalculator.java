package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlaylistLengthCalculator {

    private final String baseUrl = "https://www.googleapis.com/youtube/v3/playlistItems";
    private String finalUrl = "";

    public PlaylistLengthCalculator(String apiKey, String playlistId) {

        // Transforms any special characters to standardized UTF-8 format that is web-safe
        String encodedID = URLEncoder.encode(playlistId, StandardCharsets.UTF_8);
        String encodedKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        // Prepares query parameters
        String partParam = "?part=contentDetails"; // Comma-separated list. Available: [contentDetails, id, snippet, status]
        String idParam = "&playlistId=" + encodedID;
        String keyParam = "&key=" + encodedKey;
        String maxResultsParam = "&maxResults=" + 50; // 0-50 accepted, default value is 5

        // Constructs finalized URL for request
        finalUrl = baseUrl + partParam + idParam + keyParam + maxResultsParam;
    }

    public String getPlaylistItems() throws IOException, InterruptedException {

        // Comma-separated list of video-id's from results. Used to query specific video details later on
        String idList = "";

        // The YouTube API can return a maximum of 50 items per request, followed by a token that can be used to query the next 50 items and so on
        String nextPageToken = "";

        // Constructs new HttpClient using java.net library
        HttpClient client = HttpClient.newHttpClient();

        do {

            // Prepares and builds request, also adds eventual pageToken in multi-page-queries
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(finalUrl + "&pageToken=" + nextPageToken))
                    .GET()
                    .build();

            // Sends request and stores raw response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Uses Jackson data-bindings to map and traverse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            // Checks if there are more results (videos) available in future pages
            JsonNode nextPageTokenNode = root.get("nextPageToken");
            nextPageToken = (nextPageTokenNode != null ? nextPageTokenNode.asText() : null);

        } while (nextPageToken != null); // Keep sending requests while there are more results (videos) available in future pages


        return idList;
    }
}
