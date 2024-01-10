package src;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        // Input Validation, if the username and two values are missing then exit early.
        if (args.length != 3) {
            System.out.println("String arguments needs to be provided! \"java executable.java [username] [string 1] [string 2]\"");
            System.exit(1);
        }

        // If the input arguments are acceptable then validation is performed.
        // The strings are checked for whitespaces and numbers with a regex pattern.
        // Invalid inputs are reported and the system exits early.
        String[] usernameAndValues = {args[0], args[1], args[2]};
        boolean earlyExit = false;

        Pattern validationPattern = Pattern.compile("\\s|\\d");
        for (String input : usernameAndValues) {
            Matcher validationMatcher = validationPattern.matcher(input);
            if (validationMatcher.find()) {
                System.out.printf("Input \"%s\" rejected. Reason: Contained whitespaces or numbers.%n", input);
                earlyExit = true;
            }
        }
        if (earlyExit) {
            System.exit(1);
        }

        // Finally, the correct inputs are checked for Anagrams.
        Anagram anagramChecker = new Anagram(usernameAndValues[1], usernameAndValues[2]);
        anagramChecker.process();
    }
}
