package com.card.cardapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpGetCall {

    public static void main(String[] args) {
        System.setProperty("http.agent", "Chrome");
        try {
            URL url = new URL("https://coderbyte.com/api/challenges/json/rest-get-simple");
            URLConnection connection = url.openConnection();

            // Get the response code to check if the request was successful (200)
            int responseCode = ((HttpURLConnection) connection).getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the connection's input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response and extract the hobbies property manually
                String jsonResponse = response.toString();
                String hobbies = extractHobbies(jsonResponse);

                // Print the hobbies property in the specified format
                System.out.println(hobbies.replace("\"",""));
            } else {
                System.out.println("GET request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static String extractHobbies(String jsonResponse) {
        // Here, you should use a JSON library like Jackson or Gson to properly parse the JSON response.
        // However, for simplicity, let's manually extract the hobbies property.

        int startIndex = jsonResponse.indexOf("\"hobbies\":[") + 11; // 10 is the length of "\"hobbies\":"
        int endIndex = jsonResponse.indexOf("]", startIndex);

        return jsonResponse.substring(startIndex, endIndex);
    }
}
