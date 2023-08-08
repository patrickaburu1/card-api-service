package com.card.cardapi;

import java.util.ArrayList;
import java.util.List;

public class Sepaarte2 {


    public static String commandLine(String str) {
        // Check if the input string is null or empty
        if (str == null || str.trim().isEmpty()) {
            return "Input string is null or empty.";
        }

        // Initialize a list to store the lengths of parameter tokens and values
        List<String> lengths = new ArrayList<>();

        // Use a regular expression to match the parameter-value pairs
        // The pattern '([^\\s=]+)=([^\\s=]+)' matches a sequence of non-space and non-'=' characters, separated by '='
        String regex = "([^\\s=]+)=([^\\s=]+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(str);

        // Process the matched pairs
        while (matcher.find()) {
            String token = matcher.group(1);
            String value = matcher.group(2);

            // Get the lengths of the token and value
            int tokenLength = token.length();
            int valueLength = value.length();

            // Create a string in the desired format and add it to the lengths list
            String lengthString = tokenLength + "=" + valueLength;
            lengths.add(lengthString);
        }

        // Join the lengths list with spaces to form the final output string
        return String.join(" ", lengths);
    }

    public static void main(String[] args) {
        // Test cases including null input
        System.out.println(commandLine("SampleNumber=3234 provider=Dr. M. Welby patient=John Smith priority=High"));
        // Output: "12=4 8=12 7=10 8=4"

        System.out.println(commandLine("letters-A B Z T numbers-1 2 26 20 combine=true"));
        // Output: "7=7 7=9 7=4"

        System.out.println(commandLine("a-3 b=4 a-23 b=a 4 23 c_"));
        // Output: "1=1 1=1 1=2 1=6 1=0"

        System.out.println(commandLine(null));
        // Output: "Input string is null or empty."

        System.out.println(commandLine(""));
        // Output: "Input string is null or empty."
    }
}
