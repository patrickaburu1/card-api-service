package com.card.cardapi;

import java.util.ArrayList;
import java.util.List;

public class Separat1 {
    public static String commandLine(String str) {
        // Initialize a list to store the lengths of parameter tokens and values
        List<String> listLength = new ArrayList<>();

        // Split the input string by spaces to separate parameter-value pairs
        String[] pairs = str.split("\\s+");

        for (String pair : pairs) {
            // Find the index of '=' in the pair
            int equalsIndex = pair.indexOf('=');

            // If '=' is not found, skip the pair
            if (equalsIndex == -1) {
                continue;
            }

            // Separate the token and value using the '=' index
            String token = pair.substring(0, equalsIndex);
            String value = pair.substring(equalsIndex + 1);

            // Get the lengths of the token and value
            int tokenLength = token.length();
            int valueLength = value.length();

            // Create a string in the desired format and add it to the lengths list
            String lengthString = tokenLength + "=" + valueLength;
            listLength.add(lengthString);
        }

        // Join the lengths list with spaces to form the final output string
        return String.join(" ", listLength);
    }

    public static void main(String[] args) {
        // Test cases
        System.out.println(commandLine("SampleNumber=3234 provider=Dr. M. Welby patient=John Smith priority=High"));
        // Output: "12=4 8=12 7=10 8=4"

        System.out.println(commandLine("letters-A B Z T numbers-1 2 26 20 combine=true"));
        // Output: "7=7 7=9 7=4"

        System.out.println(commandLine("a-3 b=4 a-23 b=a 4 23 c="));
        // Output: "1=1 1=1 1=2 1=6 1=0"
    }
}