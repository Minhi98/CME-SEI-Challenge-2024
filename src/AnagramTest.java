package src;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class AnagramTest {

    @Test
    public void testBasicAnagram() throws Exception {
        String valueOne = "friend";
        String valueTwo = "finder";
        Anagram anagram = new Anagram(valueOne, valueTwo);
        assertTrue(anagram.anagramChecker(valueOne, valueTwo));
    }

    @Test
    public void testNonAnagram() throws Exception {
        String valueOne = "are";
        String valueTwo = "friend";
        Anagram anagram = new Anagram(valueOne, valueTwo);
        assertFalse(anagram.anagramChecker(valueOne, valueTwo));
    }

    @Test
    public void testCache() throws Exception {
        String valueOne = "friend";
        String valueTwo = "finder";
        Anagram anagram = new Anagram(valueOne, valueTwo);
        anagram.process();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        anagram.process();
        assertEquals("Cache Lookup: The inputted values are anagrams." + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testCacheFileIO() throws Exception {
        String valueOne = "friend";
        String valueTwo = "finder";
        Anagram anagram = new Anagram(valueOne, valueTwo);
        anagram.process();

        List<Set<String>> cachedData = anagram.readCache();
        assertTrue(cachedData.stream().anyMatch(set -> set.contains(valueOne) && set.contains(valueTwo)));
    }

    @Test
    public void testInputPreProcessing() throws Exception {
        Anagram anagram = new Anagram("FRIEND", "FINDER");
        assertEquals("friend", anagram.getValueOne());
        assertEquals("finder", anagram.getValueTwo());
    }
}