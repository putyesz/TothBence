package Model;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;

public class Song {
    /**
     * Name of the performer
     */
    private String artist;

    /**
     * Album title
     */
    private String album;

    /**
     * Song title
     */
    private String title;

    /**
     * Publication year
     */
    private String year;

    /**
     * Lenght of the song in seconds
     */
    private String lenghtInSecs;


    /**
     *
     */
    private String genre;

    public Song(File file){
        try {
            InputStream input = new FileInputStream(new File(file.getAbsolutePath()));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            this.setArtist(metadata.get("xmpDM:artist"));
            this.setAlbum(metadata.get("xmpDM:album"));
            this.setTitle(metadata.get("title"));
            this.setYear(metadata.get("xmpDM:releaseDate"));
            this.setLenghtInSecs(metadata.get("xmpDM:duration"));
            this.setGenre(metadata.get("xmpDM:genre"));
        } catch (SAXException | TikaException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getArtist() {
        return artist;
    }

    private void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    private void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    private void setYear(String year) {
        this.year = year;
    }

    public String getLenghtInSecs() {
        return lenghtInSecs;
    }

    private void setLenghtInSecs(String lenghtInSecs) {
        this.lenghtInSecs = lenghtInSecs;
    }

    public String getGenre() {
        return genre;
    }

    private void setGenre(String genre) {
        this.genre = genre;
    }
}
