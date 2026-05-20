package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {

    static WordleDictionary dictionary;

    @BeforeAll
    static void create() {
        dictionary = new WordleDictionary(List.of("кошка", "азарт", "анфас", "булка"));
    }

    @Test
    void shouldContainCorrectWords() {
        assertTrue(dictionary.containsWord("кошка"));
        assertTrue(dictionary.containsWord("азарт"));
        assertTrue(dictionary.containsWord("анфас"));
        assertTrue(dictionary.containsWord("булка"));
        assertFalse(dictionary.containsWord("жираф"));
    }

    @Test
    void shouldReturnCorrectList() {
        assertEquals(4, dictionary.getAllWords().size());
    }

    @Test
    void shouldReturnCorrectRandomWord() {
        String word = dictionary.getRandomWord();
        assertNotNull(word);
        assertTrue(dictionary.getAllWords().contains(word));
    }
}
