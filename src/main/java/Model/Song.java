package Model;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Song {
    /**
     * Absolute path to song
     */
    private String path;

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

            this.setPath(file.getAbsolutePath().replace("\\", "/").replace(" ", "%20"));

            if (metadata.get("xmpDM:artist") != null) {
                this.setArtist(metadata.get("xmpDM:artist"));
            }
            else{
                this.setArtist("Unknown");
            }

            if (metadata.get("xmpDM:album") != null) {
                this.setAlbum(metadata.get("xmpDM:album"));
            }
            else{
                this.setAlbum("Unknown");
            }

            if (metadata.get("title") != null) {
                this.setTitle(metadata.get("title"));
            }
            else{
                this.setTitle(file.getName());
            }

            if (metadata.get("xmpDM:releaseDate") != null) {
                this.setYear(metadata.get("xmpDM:releaseDate"));
            }
            else{
                this.setTitle("Unknown");
            }

            this.setLenghtInSecs(metadata.get("xmpDM:duration"));

            if (metadata.get("xmpDM:genre") != null) {
                this.setGenre(metadata.get("xmpDM:genre"));
            }
            else{
                this.setTitle("Unknown");
            }
        } catch (SAXException | TikaException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
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
