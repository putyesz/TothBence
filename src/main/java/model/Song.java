package model;

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
 * model for song objets.
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
     * Constructor for manual song initialization.
     * @param path path to file.
     * @param artist artist of the song.
     * @param album album of the song.
     * @param title title of the song.
     * @param year release year.
     * @param lenghtInSecs duration of the song.
     * @param genre genre of the song.
     */
    public Song(String path, String artist, String album, String title, String year, String lenghtInSecs, String genre) {
        setPath(path.replace("\\", "/")
                .replace(" ", "%20")
                .replace("[", "%5B")
                .replace("]", "%5D"));

        if (artist != null) {
            this.setArtist(artist);
        } else {
            this.setArtist("Unknown");
        }

        if (album != null) {
            this.setAlbum(album);
        } else {
            this.setAlbum("Unknown");
        }

        if (title != null) {
            this.setTitle(title);
        } else {
            this.setTitle(artist + " - " + title + " from " + album + " released in: " + year);
        }

        if (year != null) {
            this.setYear(year);
        } else {
            this.setYear("Unknown");
        }

        this.setLenghtInSecs(Double.toString(Double.parseDouble(lenghtInSecs) / 1000));

        if (genre != null) {
            this.setGenre(genre);
        } else {
            this.setGenre("Unknown");
        }
    }

    /**
     * Constructor to define a Song object by getting the songs metadatas.
     *
     * @param file {@link File}
     */
    public Song(final File file) {
        try {
            InputStream input = new FileInputStream(new File(file.getAbsolutePath()));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            this.setPath(file.getAbsolutePath().replace("\\", "/")
                    .replace(" ", "%20")
            .replace("[", "%5B")
            .replace("]", "%5D"));

            if (metadata.get("xmpDM:artist") != null && !metadata.get("xmpDM:artist").isBlank()) {
                this.setArtist(metadata.get("xmpDM:artist"));
            } else {
                this.setArtist("Unknown");
            }

            if (metadata.get("xmpDM:album") != null && !metadata.get("xmpDM:album").isBlank()) {
                this.setAlbum(metadata.get("xmpDM:album"));
            } else {
                this.setAlbum("Unknown");
            }

            if (metadata.get("title") != null && !metadata.get("title").isBlank()) {
                this.setTitle(metadata.get("title"));
            } else {
                this.setTitle(file.getName());
            }

            if (metadata.get("xmpDM:releaseDate") != null && !metadata.get("xmpDM:releaseDate").isBlank()) {
                this.setYear(metadata.get("xmpDM:releaseDate"));
            } else {
                this.setYear("Unknown");
            }

            this.setLenghtInSecs(Double.toString(Double.parseDouble(metadata.get("xmpDM:duration")) / 1000));

            if (metadata.get("xmpDM:genre") != null && !metadata.get("xmpDM:genre").isBlank()) {
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
     *
     * @return {@link #path}
     */
    final public String getPath() {
        return path;
    }

    /**
     * Method for setting the absolute path to a song.
     *
     * @param path {@link #path}
     */
    private void setPath(final String path) {
        this.path = path;
    }

    /**
     * Method for getting the performer of the song.
     *
     * @return {@link #artist}
     */
    final public String getArtist() {
        return artist;
    }

    /**
     * Method for setting the performer for a song.
     *
     * @param artist {@link #artist}
     */
    private void setArtist(final String artist) {
        this.artist = artist;
    }

    /**
     * Method for getting the album containing the song.
     *
     * @return {@link #album}
     */
    final public String getAlbum() {
        return album;
    }

    /**
     * Method for setting the album for a song.
     *
     * @param album {@link #album}
     */
    private void setAlbum(final String album) {
        this.album = album;
    }

    /**
     * Method for getting the title of a song.
     *
     * @return {@link #title}
     */
    final public String getTitle() {
        return title;
    }

    /**
     * Method for setting the title for a song.
     *
     * @param title {@link #title}
     */
    private void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Method for getting the publication year of the song (album).
     *
     * @return {@link #year}
     */
    final public String getYear() {
        return year;
    }

    /**
     * Method for setting the publication year for a song (album).
     *
     * @param year {@link #year}
     */
    private void setYear(final String year) {
        this.year = year;
    }

    /**
     * Method for getting the duration of a song.
     *
     * @return {@link #lenghtInSecs}
     */
    final public String getLenghtInSecs() {
        return lenghtInSecs;
    }

    /**
     * Method for setting a duration of a song.
     *
     * @param lenghtInSecs {@link #lenghtInSecs}
     */
    private void setLenghtInSecs(final String lenghtInSecs) {
        this.lenghtInSecs = lenghtInSecs;
    }

    /**
     * Method for getting the genre of a song.
     *
     * @return {@link #genre}
     */
    final public String getGenre() {
        return genre;
    }

    /**
     * Method for setting the genre of a song.
     *
     * @param genre {@link #genre}
     */
    private void setGenre(final String genre) {
        this.genre = genre;
    }
}
