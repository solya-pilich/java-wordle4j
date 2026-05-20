package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static String normalize(String word) {
        return word.trim().toLowerCase().replace("ё", "е");
    }

    public static void main(String[] args) throws IOException {

        try (PrintWriter log = new PrintWriter(new FileWriter("game.log", StandardCharsets.UTF_8, true))) {

                WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
                WordleDictionary dictionary;
                try {
                    dictionary = loader.readWordsFromFile("words_ru.txt");
                } catch (EmptyDictionaryException e) {
                    log.println("Ошибка загрузки слов" + e.getMessage());
                    System.err.println("В словаре нет подходящих слов. Игра завершена");
                    return;
                }

                WordleGame game = new WordleGame(dictionary, log);
                Scanner scanner = new Scanner(System.in);

                String input;
                while (!game.isGameOver()) {
                    System.out.println("Введите слово");
                    input = scanner.nextLine();
                    String word = normalize(input);
                    if (word.isEmpty()) {
                        try {
                            String help = game.getHelp();
                            System.out.println(help);
                            game.play(help);
                        } catch (WordNotFound | GameAlreadyFinishedException e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                    } else if (word.length() != 5) {
                        System.out.println("Слово должно состоять из пяти букв");
                    } else if (!dictionary.containsWord(word)) {
                        System.out.println("Такого слова нет в нашем словаре :(");
                    } else {
                        try {
                            game.play(word);
                        } catch (GameAlreadyFinishedException e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                    }
                }

        } catch (Exception e) {
            System.err.println("Критическая ошибка. Программа завершена" + e.getMessage());
            e.getStackTrace();
        }
    }
}