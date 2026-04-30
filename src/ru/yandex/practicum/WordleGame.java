package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordleGame {

    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private boolean gameOver;
    private final PrintWriter log;
    private List<String> possibleWords;
    private final Random random;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        answer = dictionary.getRandomWord();
        steps = 6;
        gameOver = false;
        this.log = log;
        possibleWords = dictionary.getAllWords();
        random = new Random();
        log.println("Игра началась. Загадано слово: " + answer);
    }

    public void play(String wordOfPlayer) {
        if (steps <= 0) {
            throw new IllegalStateException("Ходов не осталось. Игра завершена");
        }
        steps--;
        if (wordOfPlayer.equals(answer)) {
            gameOver = true;
            log.println("Игра окончена, игрок угадал слово " + answer);
            System.out.println("Поздравляем, вы угадали слово!");
            System.out.println("Загаданное слово: " + answer);
            return;
        }
        if (steps == 0) {
            gameOver = true;
            log.println("Игра окончена. Игрок проиграл");
            System.out.println("Игра окончена, количество попыток закончилось :(");
            System.out.println("Загаданное слово: " + answer);
            return;
        }
        String hint = getHint(wordOfPlayer);
        log.printf("Игрок ввел слово: %s, подсказка: %s, осталось попыток: %d%n", wordOfPlayer, hint, steps);
        System.out.println(hint);
        possibleWords = filterPossibleWords(wordOfPlayer, hint, possibleWords);
        System.out.println("Осталось попыток: " + steps);

    }

    public String getHint(String wordOfPlayer) {
        char[] answerWord = answer.toCharArray();
        char[] playerWord = wordOfPlayer.toCharArray();
        char[] hint = new char[5];

        for (int i = 0; i < 5; i++) {
            if (playerWord[i] == answerWord[i]) {
                hint[i] = '+';
                answerWord[i] = '#';
            }
        }

        for (int i = 0; i < 5; i++) {
            if (hint[i] == '+') {
                continue;
            }
            int index = -1;
            for (int j = 0; j < 5; j++) {
                if (answerWord[j] == playerWord[i]) {
                    index = j;
                    break;
                }
            }
            if (index != -1) {
                hint[i] = '^';
                answerWord[index] = '#';
            } else {
                hint[i] = '-';
            }
        }
        return new String(hint);
    }

    public String getHelp() throws WordNotFound {
        if (possibleWords.isEmpty()) {
            log.println("Нет подходящих слов, список пуст");
            throw new WordNotFound("Нет подходящих слов");
        }
        String help = possibleWords.get(random.nextInt(possibleWords.size()));
        log.println("Подсказка " + help);
        return help;
    }

    public List<String> filterPossibleWords(String wordOfPlayer, String hint, List<String> possibleWords) {
        for (int i = 0; i < 5; i++) {
            if (hint.charAt(i) == '+') {
                int finalI = i;
                possibleWords.removeIf(word -> word.charAt(finalI) != wordOfPlayer.charAt(finalI));
            }
        }

        Set<Character> illegalSymbol = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            if (hint.charAt(i) == '-') {
                illegalSymbol.add(wordOfPlayer.charAt(i));
            }
        }
        possibleWords.removeIf(word -> {
            for (char symbol : illegalSymbol) {
                if (word.indexOf(symbol) != -1) {
                    return true;
                }
            }
            return false;
        });

        for (int i = 0; i < 5; i++) {
            if (hint.charAt(i) == '^') {
                char interestingSymbol = wordOfPlayer.charAt(i);
                int finalI = i;
                possibleWords.removeIf(word ->
                        word.indexOf(interestingSymbol) == -1 || word.charAt(finalI) == interestingSymbol);
            }
        }
        return possibleWords;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public List<String> getPossibleWords() {
        return possibleWords;
    }
}
