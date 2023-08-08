package com.card.cardapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpGetCallAge {

    public static void main(String[] args) {
        System.setProperty("http.agent", "Chrome");
        try {
            URL url = new URL("https://coderbyte.com/api/challenges/json/age-counting");
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
                Integer hobbies = extractHobbies(jsonResponse);

                // Print the hobbies property in the specified format
                System.out.println(hobbies);
            } else {
                System.out.println("GET request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static Integer extractHobbies(String jsonString) {
        int count = 0;

        try {
            // Parse the JSON string into a JsonObject
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

            // Get the "data" property as a string
            String data = jsonObject.get("data").getAsString();

            // Split the data by commas to get individual entries
            String[] entries = data.split(",");

            // Loop through each entry and extract the age
            for (String entry : entries) {
                // Extract the age value from the entry
                int age = extractAge(entry);

                // Check if the age is greater than 50 and update the count
                if (age >= 50) {
                    count++;
                }
            }

            // Print the count of ages greater than 50
            System.out.println("Count of ages greater than 50: " + count);
        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        }
        return count;
    }
    private static int extractAge(String entry) {
        // Split the entry by "=", and extract the age value
        String[] keyValue = entry.trim().split("=");
        if (keyValue.length == 2 && keyValue[0].trim().equals("age")) {
            try {
                return Integer.parseInt(keyValue[1].trim());
            } catch (NumberFormatException e) {
                // Handle invalid age values (non-numeric)
                return -1;
            }
        }
        return -1;
    }
}

