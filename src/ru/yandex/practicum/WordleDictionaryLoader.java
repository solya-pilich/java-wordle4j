package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary readWordsFromFile(String fileName) throws IOException, EmptyDictionaryException {
        List<String> wordList = new ArrayList<>();
        Path path = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 5) {
                    String word = line.toLowerCase().replace("ё", "е");
                    wordList.add(word);
                }
            }
        } catch (IOException e) {
            log.println("Ошибка чтения файла " + e.getMessage());
            throw new DictionaryLoadException("Не удалось загрузить словарь", e);
        }
        if (wordList.isEmpty()) {
            log.println("Не удалось создать словарь. Загружено слов: " + wordList.size());
            throw new EmptyDictionaryException("Словарь не содержит слов длинной 5 букв");
        }
        WordleDictionary wordleDictionary = new WordleDictionary(wordList);
        log.println("Словарь успешно создан. Загружено слов: " + wordList.size());
        return wordleDictionary;
    }
}
