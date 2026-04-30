package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class WordleDictionaryLoaderTest {

    private PrintWriter testLog;

    @BeforeEach
    void create() {
        testLog = new PrintWriter(System.out, true);
    }

    @TempDir
    Path tempDir;

    @Test
    void shouldReturnCorrectWords() throws IOException {
        Path file = tempDir.resolve("test.txt");
        try (FileWriter writer = new FileWriter(file.toFile())) {
            writer.write("кошка\n");
            writer.write("азарт\n");
            writer.write("ЛОДка\n");
            writer.write(" мир \n");
            writer.write("полёт\n");
            writer.write("длинноеслово\n");
        }
        WordleDictionaryLoader loader = new WordleDictionaryLoader(testLog);
        WordleDictionary dictionary = loader.readWordsFromFile(file.toString());
        assertEquals(4, dictionary.getAllWords().size());
        assertTrue(dictionary.getAllWords().contains("кошка"));
        assertTrue(dictionary.getAllWords().contains("азарт"));
        assertTrue(dictionary.getAllWords().contains("лодка"));
        assertTrue(dictionary.getAllWords().contains("полет"));
        assertFalse(dictionary.getAllWords().contains("мир"));
        assertFalse(dictionary.getAllWords().contains("полёт"));
        assertFalse(dictionary.getAllWords().contains("длинноеслово"));
    }

    @Test
    void shouldReturnExceptionWithEmptyFile() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        try {
            WordleDictionaryLoader loader = new WordleDictionaryLoader(testLog);
            WordleDictionary dictionary = loader.readWordsFromFile(file.toString());
            fail("Expected IOException");
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void shouldReturnExceptionForFileWithoutCorrectWords() throws IOException {
        Path file = tempDir.resolve("incorrect.txt");
        try (FileWriter writer = new FileWriter(file.toFile())) {
            writer.write("абс\n");
            writer.write("длинноеслово\n");
            writer.write("луна\n");
            writer.write("     \n");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(testLog);
            WordleDictionary dictionary = loader.readWordsFromFile(file.toString());
            fail("Expected IOException");

        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }
}
