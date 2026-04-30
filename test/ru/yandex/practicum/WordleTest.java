package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    @Test
    void correctConversionWordToCorrectFormat() {
        assertEquals("аванс", Wordle.normalize("  аванс  "));
        assertEquals("кошка", Wordle.normalize("КоШка"));
        assertEquals("еж", Wordle.normalize("Ёж"));
        assertEquals("", Wordle.normalize("     "));
    }

}
