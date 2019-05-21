package model;

import exception.PlayerNotInitializedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MediaModelTest {

    Song song;
    Song song1;
    Song song2;
    Song song3;

    @BeforeEach
    void setUp() {
        song = new Song(" ", "dnbartist", "Single", "Jump", "2019", "10", "DnB");
        song1 = new Song(new File("src/test/resources/01. Anti-social.mp3"));
        song2 = new Song("c:\\", null, null, null, null, "1000", null);
        //song3 = new Song(new File("src/test/resources/Cat Dealers - Your Body.mp3"));
        song3 = new Song(new File("src/test/resources/Cat Dealers - Your Body (Original Mix).mp3"));
    }

    @Test
    void playPause() {
        boolean result = false;
        try{
            MediaModel.playPause();
        } catch (PlayerNotInitializedException e){
            result = true;
        }
        assertEquals(true, result, "Player not initialized.");
    }

    @Test
    void mute() {
        boolean result = false;
        try{
            MediaModel.mute();
        } catch (PlayerNotInitializedException e){
            result = true;
        }
        assertEquals(true, result, "Player not initialized.");
    }

    @AfterEach
    void tearDown() {
        song = null;
        song1 = null;
        song2 = null;
        song3 = null;
    }
}
