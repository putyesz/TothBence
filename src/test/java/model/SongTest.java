package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SongTest {

    private Song song;
    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    void setUp() {
        song = new Song(" ", "dnbartist", "Single", "Jump", "2019", "10", "DnB");
        song1 = new Song(new File("src/test/resources/01. Anti-social.mp3"));
        song2 = new Song("c:\\", null, null, null, null, "1000", null);
        //song3 = new Song(new File("src/test/resources/Cat Dealers - Your Body.mp3"));
        song3 = new Song(new File("src/test/resources/Cat Dealers - Your Body (Original Mix).mp3"));
    }

    @Test
    void getPath() {
        assertEquals("%20", song.getPath(), "Failed to get correct path");
        assertNotEquals("", song1.getPath().toLowerCase(), "Failed to get correct path");
        assertEquals("c:/".toLowerCase(), song2.getPath().toLowerCase(), "Failed to get correct path");
        assertNotEquals("", song3.getPath().toLowerCase(), "Failed to get correct path");
    }

    @Test
    void getArtist() {
        assertEquals("dnbartist", song.getArtist(), "Failed to get correct artist");
        assertNotEquals("", song1.getArtist(), "Failed to get correct artist");
        assertEquals("Unknown", song2.getArtist(), "Failed to get correct artist");
        assertNotEquals("", song3.getArtist(), "Failed to get correct artist");
    }

    @Test
    void getAlbum() {
        assertEquals("Single", song.getAlbum(), "Failed to get correct album");
        assertNotEquals("", song1.getAlbum(), "Failed to get correct album");
        assertEquals("Unknown", song2.getAlbum(), "Failed to get correct album");
        assertNotEquals("", song3.getAlbum(), "Failed to get correct album");
    }

    @Test
    void getTitle() {
        assertEquals("Jump", song.getTitle(), "Failed to get correct title");
        assertNotEquals("", song1.getTitle(), "Failed to get correct title");
        assertNotEquals("Unknown", song2.getTitle(), "Failed to get correct title");
        assertNotEquals("", song3.getTitle(), "Failed to get correct title");
    }

    @Test
    void getYear() {
        assertEquals("2019", song.getYear(), "Failed to get correct year");
        assertNotEquals("", song1.getYear(), "Failed to get correct year");
        assertNotEquals("", song2.getYear(), "Failed to get correct year");
        assertNotEquals("", song3.getYear(), "Failed to get correct year");
    }

    @Test
    void getLenghtInSecs() {
        assertEquals(Double.toString(Double.parseDouble("10") / 1000), song.getLenghtInSecs(), "Failed to get correct duration");
        assertNotEquals("", song1.getLenghtInSecs(), "Failed to get correct duration");
        assertEquals(Double.toString(Double.parseDouble("1000") / 1000), song2.getLenghtInSecs(), "Failed to get correct duration");
        assertNotEquals("", song3.getLenghtInSecs(), "Failed to get correct duration");

    }

    @Test
    void getGenre() {
        assertEquals("dnb", song.getGenre().toLowerCase(), "Failed to get correct genre");
        assertNotEquals("", song1.getGenre().toLowerCase(), "Failed to get correct genre");
        assertEquals("unknown", song2.getGenre().toLowerCase(), "Failed to get correct genre");
        assertNotEquals("", song3.getGenre().toLowerCase(), "Failed to get correct genre");

    }

    @AfterEach
    void tearDown() {
        song = null;
        song1 = null;
        song2 = null;
        song3 = null;
    }

}