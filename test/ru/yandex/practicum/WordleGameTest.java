package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {

    WordleGame game;
    static WordleDictionary dictionary;
    static PrintWriter testLog;

    @BeforeEach
    void create() {
        game = new WordleGame(dictionary, testLog);
    }

    @BeforeAll
    static void createStatic() {
        dictionary = new WordleDictionary(List.of("кошка", "азарт", "анфас", "булка"));
        testLog = new PrintWriter(System.out);
    }

    @Test
    void shouldCorrectlyFinishGameIfWordIsGuessed() {
        String word = game.getAnswer();
        game.play(word);
        assertTrue(dictionary.containsWord(word));
        assertTrue(game.isGameOver());
    }

    @Test
    void shouldCorrectlyCountStepsAndFinishGame() {
        assertEquals(6, game.getSteps());
        game.play("арбуз");
        assertEquals(5, game.getSteps());

        game.play("мышка");
        game.play("мышка");
        game.play("мышка");
        game.play("мышка");
        assertEquals(1, game.getSteps());

        assertFalse(game.isGameOver());
        game.play("мышка");
        assertEquals(0, game.getSteps());
        assertTrue(game.isGameOver());
    }

    @Test
    void shouldCorrectlyGivingHint() {
        WordleDictionary smallDict = new WordleDictionary(List.of("азарт"));
        WordleGame smallGame = new WordleGame(smallDict, testLog);
        String answer = smallGame.getAnswer();
        String hint1 = smallGame.getHint("арбуз");

        assertEquals("азарт", answer);
        assertEquals("+^--^", hint1);

        String hint2 = smallGame.getHint("кошка");
        assertEquals("----^", hint2);

        String hint3 = smallGame.getHint("ббббб");
        assertEquals("-----", hint3);

        String hint4 = smallGame.getHint("азарт");
        assertEquals("+++++", hint4);
    }

    @Test
    void shouldCorrectlyGetHelp() throws WordNotFound {
        assertFalse(game.getPossibleWords().isEmpty());
        String hint = game.getHelp();
        assertTrue(game.getPossibleWords().contains(hint));
    }

    @Test
    void shouldThrowWordNotFoundWhenPossibleWordsEmpty() {
        List<String> possibleWords = game.getPossibleWords();
        possibleWords.clear();
        try {
            game.getHelp();
            fail("Expected WordNotFound");
        } catch (WordNotFound e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void shouldCorrectlyFilterPossibleWordsList() {
        WordleDictionary smallDict = new WordleDictionary(List.of("азарт"));
        WordleGame smallGame = new WordleGame(smallDict, testLog);
        smallGame.filterPossibleWords("арбуз", "+^--^", smallGame.getPossibleWords());
        assertEquals(1, smallGame.getPossibleWords().size());
        assertEquals("азарт", smallGame.getPossibleWords().get(0));
    }
}
