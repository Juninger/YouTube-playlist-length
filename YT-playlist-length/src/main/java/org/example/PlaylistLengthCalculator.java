package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlaylistLengthCalculator {

    // Base URLs
    private String playlistUrl = "https://www.googleapis.com/youtube/v3/playlistItems";
    private String videoUrl = "https://www.googleapis.com/youtube/v3/videos";

    public PlaylistLengthCalculator(String apiKey, String playlistId) {

        // Transforms any special characters to standardized UTF-8 format that is web-safe
        String encodedID = URLEncoder.encode(playlistId, StandardCharsets.UTF_8);
        String encodedKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        // Prepares query parameters
        String partParam = "?part=contentDetails"; // Comma-separated list. Available: [contentDetails, id, snippet, status]
        String playlistIdParam = "&playlistId=" + encodedID;
        String keyParam = "&key=" + encodedKey;
        String maxResultsParam = "&maxResults=" + 50; // 0-50 accepted, default value is 5
        String fieldsParam = "&fields=items"; // Only returns array of items

        // Constructs finalized URLs for requests
        playlistUrl = playlistUrl + partParam + playlistIdParam + keyParam + maxResultsParam;
        videoUrl = videoUrl + partParam + keyParam + fieldsParam;

    }

    public Duration getTotalDuration() throws IOException, InterruptedException {

        // The YouTube API can return a maximum of 50 items per request, followed by a token that can be used to query the next 50 items and so on
        String nextPageToken = "";

        // Stores the total duration of the playlist
        Duration totalDuration = Duration.ZERO;

        // Constructs new HttpClient using java.net library
        HttpClient client = HttpClient.newHttpClient();

        // Used for Jackson data-bindings to map and traverse JSON responses
        ObjectMapper mapper = new ObjectMapper();

        do {

            // Prepares and builds playlistItem-request, also adds eventual pageToken in multi-page-queries
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(playlistUrl + "&pageToken=" + nextPageToken))
                    .GET()
                    .build();

            // Sends playlistItem-request and stores raw response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parses response to traversable JSON object
            JsonNode root = mapper.readTree(response.body());

            // Stores list of video-IDs from results. Used to query specific video details later on
            StringBuilder idList = new StringBuilder();

            // Loop results and extract ID for every video, which is then added to comma-separated idList
            for (JsonNode playlistItem : root.get("items")) {
                idList.append(playlistItem.get("contentDetails").get("videoId").asText()).append(",");
            }

            // Removes trailing comma from end of idList
            idList.deleteCharAt(idList.length()-1);

            // Checks if there are more results (videos) available in future pages
            JsonNode nextPageTokenNode = root.get("nextPageToken");
            nextPageToken = (nextPageTokenNode != null ? nextPageTokenNode.asText() : null);

            // Prepares and builds videoList-request with list of IDs from current result set (from playlistItems)
            request = HttpRequest.newBuilder()
                    .uri(URI.create(videoUrl + "&id=" + idList))
                    .GET()
                    .build();

            // Sends request and stores raw response
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parses response to traversable JSON object
            root = mapper.readTree(response.body());

            // Loop video-results and extract duration from each item to add to totalDuration
            for (JsonNode video : root.get("items")) {
                String durationString = video.get("contentDetails").get("duration").asText();
                Duration duration = Duration.parse(durationString);
                totalDuration = totalDuration.plus(duration);
            }

        } while (nextPageToken != null); // Keep sending requests while there are more results (videos) available in future pages

        return totalDuration;
    }
}
