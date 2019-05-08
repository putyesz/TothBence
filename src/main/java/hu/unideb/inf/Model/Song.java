package hu.unideb.inf.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;

import java.io.File;
import java.net.MalformedURLException;
import java.time.Year;

public class Song {
    /**
     * Az eloado neve
     */
    private StringProperty artist;

    /**
     * Album cime
     */
    private StringProperty album;

    /**
     * A zeneszam cime
     */
    private StringProperty title;

    /**
     * Kiadas eve
     */
    private IntegerProperty year;

    /**
     * A szam hossza masodpercben
     */
    private StringProperty lenghtInSecs;

    /**
     *
     * @param file
     */
    public Song(File file) throws MalformedURLException {
       Media media = new Media(file.toPath().toAbsolutePath().toUri().toURL().toExternalForm());
       initProps();


    }

    public void initProps(){
        artist = new SimpleStringProperty();
        album = new SimpleStringProperty();
        title = new SimpleStringProperty();
        year = new SimpleIntegerProperty();
        lenghtInSecs = new SimpleStringProperty();
    }

    public String getArtist() {
        return artist.get();
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public String getAlbum() {
        return album.get();
    }

    public StringProperty albumProperty() {
        return album;
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public String getLenghtInSecs() {
        return lenghtInSecs.get();
    }

    public StringProperty lenghtInSecsProperty() {
        return lenghtInSecs;
    }

    public void setLenghtInSecs(String lenghtInSecs) {
        this.lenghtInSecs.set(lenghtInSecs);
    }
}
