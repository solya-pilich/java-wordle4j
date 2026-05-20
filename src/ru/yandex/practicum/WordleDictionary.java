package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    private final List<String> words;
    private final Random random = new Random();

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public boolean containsWord(String word) {
        return words.contains(word);
    }

    public List<String> getAllWords() {
        return new ArrayList<>(words);
    }
}
