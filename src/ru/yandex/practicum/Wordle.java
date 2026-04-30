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

            try {
                WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
                WordleDictionary dictionary;
                try {
                    dictionary = loader.readWordsFromFile("words_ru.txt");
                } catch (IOException e) {
                    log.println("Ошибка загрузки словаря" + e.getMessage());
                    e.printStackTrace(log);
                    System.err.println("Не удалось загрузить словарь. Игра завершена");
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
                        } catch (WordNotFound e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                    } else if (word.length() != 5) {
                        System.out.println("Слово должно состоять из пяти букв");
                    } else if (!dictionary.containsWord(word)) {
                        System.out.println("Такого слова нет в нашем словаре :(");
                    } else {
                        game.play(word);
                    }
                }

            } catch (Exception e) {
                log.println("Необработанное исключение " + e.getMessage());
                e.printStackTrace(log);
                System.err.println("Внутренняя ошибка. Игра завершена, попробуйте начать сначала.");
            }

        } catch (IOException e) {
            System.err.println("Не удалось создать лог-файл" + e.getMessage());
            e.getStackTrace();
        }
    }
}