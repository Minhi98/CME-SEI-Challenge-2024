package src;

import java.io.*;
import java.util.*;

class Anagram {

    private final String valueOne;
    private final String valueTwo;

    // The cache is defined as a List of sets containing strings that are anagrams of other strings in the set.
    // Ideally this should speed up processing by checking if already processed values are members of the same set.
    private final String cacheFilePath = "AnagramCachedSets.txt";
    private List<Set<String>> cache;
    private boolean preCheck = false;

    Anagram(String valueOne, String valueTwo) throws IOException {
        // This is a letter-based anagram, it will consider case-insensitive letters. Unwanted characters are taken out.
        String preProcessRegex = "[^a-zA-Z]";
        this.valueOne = valueOne.replaceAll(preProcessRegex, "").toLowerCase();
        this.valueTwo = valueTwo.replaceAll(preProcessRegex, "").toLowerCase();

        // The cache is read from a file to speed up processing later.
        this.cache = readCache();
    }

    public String getValueOne() {
        return valueOne;
    }

    public String getValueTwo() {
        return valueTwo;
    }

    void process() throws IOException {
        if (checkCache(this.valueOne, this.valueTwo)) {
            System.out.println("Cache Lookup: The inputted values are anagrams.");
        } else {
            if (anagramChecker(this.valueOne, this.valueTwo)) {
                System.out.println("Result: The inputted values are anagrams");
                updateInternalCache(this.valueOne, this.valueTwo);
            } else {
                System.out.println("Result: The inputted values are not anagrams");
            }
        }
    }

    boolean checkCache(String s1, String s2) {
        return this.cache.stream().anyMatch(set -> set.contains(s1) && set.contains(s2));
    }

    void updateInternalCache(String s1, String s2) throws IOException {
        // This assumes s1 is an anagram of s2.
        // This comprehensively goes through the cache and updates every matching anagram already cached.
        boolean setWasUpdated = false;

        for (int i = 0; i < this.cache.size(); i++) {
            HashSet<String> currentSet = new HashSet<>(this.cache.get(i));

            // If one string is present in the anagram set, then add the other string.
            if (currentSet.contains(s1)) {
                currentSet.add(s2);
                this.cache.set(i, currentSet);
                setWasUpdated = true;
            }
            if (currentSet.contains(s2)) {
                currentSet.add(s1);
                this.cache.set(i, currentSet);
                setWasUpdated = true;
            }
        }

        // If no sets were updated that implies that this is a new anagram, so a new set must be made and cached.
        if (!setWasUpdated) {
            this.cache.add(Set.of(new String[]{s1, s2}));
        }

        WriteCache();
    }

    List<Set<String>> readCache() throws IOException {
        List<Set<String>> cachedData = new ArrayList<>();

        // Check for a cache file in the current directory. If it doesn't exist then return an empty List.
        boolean cacheCheck = new File(this.cacheFilePath).exists();
        if (!cacheCheck) {
            return cachedData;
        }

        // The text file treats each line as a set. Each set was written to the line as comma separated.
        BufferedReader br = new BufferedReader(new FileReader(this.cacheFilePath));
        String line = "";
        while ((line = br.readLine()) != null) {
            cachedData.add(Set.of(line.split(",")));
        }

        return cachedData;
    }

    void WriteCache() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.cacheFilePath));
        for (Set<String> set : this.cache) {
            String[] newSet = set.toArray(String[]::new);

            for (int i = 0; i < newSet.length; i++) {
                writer.write(newSet[i]);
                if (i < newSet.length - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();  // Add this line to insert a newline after each set
        }

        writer.close(); // Close the BufferedWriter to ensure changes are written to the file
    }

    boolean anagramChecker(String valueOne, String valueTwo) {
        // If they are not of the same length then they are not anagrams of each other.
        if (valueOne.length() != valueTwo.length()) {
            return false;
        }
        // An anagram is found if the characters of two strings are the same, this is done by converting to a
        // char array, sorting and then comparing equality.
        char[] s1 = valueOne.toCharArray();
        char[] s2 = valueTwo.toCharArray();
        Arrays.sort(s1);
        Arrays.sort(s2);
        return Arrays.equals(s1, s2);
    }
}
