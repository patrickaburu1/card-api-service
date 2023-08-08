package com.card.cardapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StringSeparator {


    public static String commandLine(String str) {
        // Split the input string by spaces to separate parameter-value pairs
        String[] pairs = str.split("\\s+");

        // Initialize a list to store the lengths of parameter tokens and values
        List<String> lengths = new ArrayList<>();

        System.out.println(Arrays.toString(pairs));

        String[] pairs1 = new String[pairs.length];
        int lastWorkingIndex = 0;
        for (int i = 0; i < Arrays.stream(pairs).count(); i++) {
            if (!pairs[i].contains("=")) {
                pairs1[lastWorkingIndex] = pairs1[lastWorkingIndex] +" "+ pairs[i];
                System.out.println("--- adding" + pairs1[lastWorkingIndex]);
            } else {
                pairs1[i] = pairs[i];
                lastWorkingIndex = i;
            }

        }

        System.out.println(Arrays.toString(pairs1));

        List<String> filteredEntries = Arrays.stream(pairs1)
                .filter(Objects::nonNull) // Filter out null values
                .collect(Collectors.toList());
        for (String pair : filteredEntries) {
            // Split each parameter-value pair by '=' to separate the token and value
            if (null==pair)
                continue;
            String[] parts = pair.split("=");

            // Get the lengths of the token and value
            int tokenLength = parts[0].length();
            String lengthString;
            System.out.println(Arrays.toString(parts));
            if (parts.length>1) {
                int valueLength = parts[1].length();
                // Create a string in the desired format and add it to the lengths list
                 lengthString = tokenLength + "=" + valueLength;
            }else
                lengthString= tokenLength+"="+"0";
            lengths.add(lengthString);
        }

        // Join the lengths list with spaces to form the final output string
        return String.join(" ", lengths);
    }

    public static void main(String[] args) {
        // Test cases
       // System.out.println(commandLine("SampleNumber=3234 provider=Dr. M. Welby patient=John Smith priority=High"));
        // Output: "12=4 8=12 7=10 8=4"

//        System.out.println(commandLine("letters-A B Z T numbers-1 2 26 20 combine=true"));
//        // Output: "7=7 7=9 7=4"
//
        System.out.println(commandLine("a=3 b=4 a-23 b=a 4 23 c="));
        // Output: "1=1 1=1 1=2 1=6 1=0"
    }


}
