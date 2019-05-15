package Model;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Model for song objets.
 */
public class Song {
    /**
     * Absolute path to song.
     */
    private String path;

    /**
     * Name of the performer.
     */
    private String artist;

    /**
     * Album title.
     */
    private String album;

    /**
     * Song title.
     */
    private String title;

    /**
     * Publication year.
     */
    private String year;

    /**
     * Lenght of the song.
     */
    private String lenghtInSecs;


    /**
     * Genre of the song.
     */
    private String genre;

    /**
     * Constructor to define a Song object by getting the songs metadatas.
     * @param file Getting informations from
     */
    public Song(File file) {
        try {
            InputStream input = new FileInputStream(new File(file.getAbsolutePath()));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            this.setPath(file.getAbsolutePath().replace("\\", "/").replace(" ", "%20"));

            if (metadata.get("xmpDM:artist") != null) {
                this.setArtist(metadata.get("xmpDM:artist"));
            } else {
                this.setArtist("Unknown");
            }

            if (metadata.get("xmpDM:album") != null) {
                this.setAlbum(metadata.get("xmpDM:album"));
            } else {
                this.setAlbum("Unknown");
            }

            if (metadata.get("title") != null) {
                this.setTitle(metadata.get("title"));
            } else {
                this.setTitle(file.getName());
            }

            if (metadata.get("xmpDM:releaseDate") != null) {
                this.setYear(metadata.get("xmpDM:releaseDate"));
            } else {
                this.setYear("Unknown");
            }

            this.setLenghtInSecs(metadata.get("xmpDM:duration"));

            if (metadata.get("xmpDM:genre") != null) {
                this.setGenre(metadata.get("xmpDM:genre"));
            } else {
                this.setGenre("Unknown");
            }
        } catch (SAXException e) {
            LoggerFactory.getLogger(Song.class).error("SAYException");
            e.printStackTrace();
        } catch (TikaException e) {
            LoggerFactory.getLogger(Song.class).error("TikaException");
            e.printStackTrace();
        } catch (IOException e) {
            LoggerFactory.getLogger(Song.class).error("IOException");
            e.printStackTrace();
        }
    }

    /**
     * Method for getting the absolute path to the song.
     * @return path to song
     */
    public String getPath() {
        return path;
    }

    /**
     * Method for setting the absolute path to a song.
     * @param path to song
     */
    private void setPath(String path) {
        this.path = path;
    }

    /**
     * Method for getting the performer of the song.
     * @return artist of a song
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Method for setting the performer for a song.
     * @param artist of song
     */
    private void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Method for getting the album containing the song.
     * @return album of a song
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Method for setting the album for a song.
     * @param album of song
     */
    private void setAlbum(String album) {
        this.album = album;
    }

    /**
     * Method for getting the title of a song.
     * @return title of a song
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method for setting the title for a song.
     * @param title of a song
     */
    private void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method for getting the publication year of the song (album).
     * @return release year
     */
    public String getYear() {
        return year;
    }

    /**
     * Method for setting the publication year for a song (album).
     * @param year of release
     */
    private void setYear(String year) {
        this.year = year;
    }

    /**
     * Method for getting the duration of a song.
     * @return lenght of a song
     */
    public String getLenghtInSecs() {
        return lenghtInSecs;
    }

    /**
     * Method for setting a duration of a song.
     * @param lenghtInSecs duration to set
     */
    private void setLenghtInSecs(String lenghtInSecs) {
        this.lenghtInSecs = lenghtInSecs;
    }

    /**
     * Method for getting the genre of a song.
     * @return genre of a song
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Method for setting the genre of a song.
     * @param genre of a song
     */
    private void setGenre(String genre) {
        this.genre = genre;
    }
}
