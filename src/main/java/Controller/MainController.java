package Controller;

import Model.MediaModel;
import Model.Song;
import View.AddPathWindow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.w3c.dom.*;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    public ListView <String> yearList;
    public ListView <String> artistList;
    public ListView <String> albumList;
    public ListView <String> songList;
    public ListView <String> genreList;
    public ListView <String> playlistList;

    public ImageView playImageView;
    public ImageView shuffleImageView;
    public ImageView muteImageView;
    public ImageView loopImageView;

    public Button playButton;
    public Button prevButton;
    public Button nextButton;
    public Button stopButton;
    public Button muteButton;
    public Button shuffleButton;
    public Button loopButton;

    public Button search_year_button;
    public Button search_artist_button;
    public Button search_album_button;
    public Button search_song_button;
    public Button search_genre_button;

    public Slider volumeSlider;
    public Slider playSlider;

    public MenuItem importMenuitem;
    public MenuItem addPathMenuitem;

    public ChoiceBox <String> yearChoiceBox;
    public TextField artistTextField;
    public TextField albumTextField;
    public TextField songTextField;
    public ChoiceBox <String> genreChoiceBox;

    public TabPane tabPane;

    private HashMap <String, Song> songHashMap;

    private ArrayList <Song> allSongsList;

    private boolean isPaused;
    private boolean isMuted;
    private boolean isOnShuffle;
    private boolean isLooped;

    private double volumeSliderValue;

    public MainController() {
        init();
        initSongs();
    }

    private void init() {
        isPaused = false;
        isMuted = false;
        isOnShuffle = false;
        isLooped = false;
        allSongsList = new ArrayList <>();
        yearChoiceBox = new ChoiceBox <>();
        genreChoiceBox = new ChoiceBox <>();
        songHashMap = new HashMap <>();

        try {
            makeXMLFiles();
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void initSongs() {
        try {
            File inputFile = new File("Locations.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(inputFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("location");

            List <File> filesInFolder;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                filesInFolder = Files.walk(Paths.get(element.getAttribute("path")))
                        .filter(name -> name.toString().toLowerCase().endsWith(".mp3"))
                        .map(Path::toFile)
                        .collect(Collectors.toList());

                for (File file : filesInFolder) {
                    if (new File(file.getAbsolutePath()).exists()) {
                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document doc = documentBuilder.parse("Songs.xml");
                        doc.getDocumentElement().normalize();

                        Element root = doc.getDocumentElement();

                        Element songElement = doc.createElement("song");
                        root.appendChild(songElement);

                        Element yearElement = doc.createElement("year");
                        yearElement.appendChild(doc.createTextNode(getMetas(file.getAbsolutePath(), "year")));
                        songElement.appendChild(yearElement);

                        Element artistElement = doc.createElement("artist");
                        artistElement.appendChild(doc.createTextNode(getMetas(file.getAbsolutePath(), "artist")));
                        songElement.appendChild(artistElement);

                        Element albumElement = doc.createElement("album");
                        albumElement.appendChild(doc.createTextNode(getMetas(file.getAbsolutePath(), "album")));
                        songElement.appendChild(albumElement);

                        Element songTitleElement = doc.createElement("title");
                        songTitleElement.appendChild(doc.createTextNode(getMetas(file.getAbsolutePath(), "title")));
                        songElement.appendChild(songTitleElement);

                        Element lengthElement = doc.createElement("length");
                        lengthElement.appendChild(doc.createTextNode(getMetas(file.getAbsolutePath(), "length")));
                        songElement.appendChild(lengthElement);

                        Element genreElement = doc.createElement("genre");
                        genreElement.appendChild(doc.createTextNode(getMetas(file.getAbsolutePath(), "genre")));
                        songElement.appendChild(genreElement);

                        TransformerFactory tf = TransformerFactory.newInstance();
                        Transformer transformer = tf.newTransformer();
                        DOMSource source = new DOMSource(doc);

                        StreamResult result = new StreamResult(new File("Songs.xml"));

                        transformer.transform(source, result);
                    }
                    Song s = new Song(file);
                    allSongsList.add(s);
                    songHashMap.put(s.getArtist() + " - " + s.getTitle(), s);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException | TikaException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        yearList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        artistList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        albumList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        songList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genreList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        yearChoiceBox.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .map(Song::getYear)
                .distinct()
                .collect(Collectors.toList())));
        genreChoiceBox.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .map(Song::getGenre)
                .distinct()
                .collect(Collectors.toList())));
    }

    private String getMetas(String fileLocation, String neededMeta) throws IOException, TikaException, SAXException {
        InputStream input = new FileInputStream(new File(fileLocation));
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();

        switch (neededMeta) {
            case "year":
                return metadata.get("xmpDM:releaseDate");
            case "artist":
                return metadata.get("xmpDM:artist");
            case "album":
                return metadata.get("xmpDM:album");
            case "length":
                return metadata.get("xmpDM:duration");
            case "title":
                return metadata.get("title");
            case "genre":
                return metadata.get("xmpDM:genre");
            default:
                throw new IllegalArgumentException("Invalid metadata");
        }

    }

    private void makeXMLFiles() throws ParserConfigurationException, TransformerException {
        if (!new File("Songs.xml").exists()) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("songs");
            doc.appendChild(rootElement);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File("Songs.xml"));

            transformer.transform(source, result);
        }
        //-------------------------------------------------------------

        if (!new File("Locations.xml").exists()) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("locations");
            doc.appendChild(rootElement);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File("Locations.xml"));

            transformer.transform(source, result);
        }
    }

    @FXML
    private void startPause() {
        //TODO
        // if no selected item in playlist
        // automatically play first song
        // else first element must be played

        MediaModel.playPause();


        if (playlistList.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No song in the playlist");
            alert.showAndWait();
            return;
        }

        Media media;
        MediaPlayer mediaPlayer;

        if (playlistList.getSelectionModel().getSelectedItem().isEmpty()) {
            media = new Media(playlistList.getItems().get(0));
            mediaPlayer = new MediaPlayer(media);
        } else {
            media = new Media(playlistList.getSelectionModel().getSelectedItem());
            mediaPlayer = new MediaPlayer(media);
        }


        isPaused = !isPaused;
        if (isPaused) {
            playImageView.setImage(new Image("./Images/playButt.png"));
            mediaPlayer.play();
            System.out.println("Start");
        } else {
            playImageView.setImage(new Image("./Images/pauseButt.png"));
            mediaPlayer.pause();
            System.out.println("Pause");
        }
    }

    @FXML
    private void prevSong() {
        System.out.println("Prev");
        //TODO
        // don't throw Exception or error, if first song in the list
        // else play the previous element in the playList

    }

    @FXML
    private void nextSong() {
        System.out.println("Next");
        //TODO
        // don't throw Exception or error, if last song in the list
        // else play the next element in the playList

    }

    @FXML
    private void stopSong() {
        playSlider.adjustValue(0);
        MediaPlayer mp = new MediaPlayer(new Media(""));
        mp.stop();
        System.out.println("Stop");
    }

    @FXML
    private void muteUnmute() {
        //TODO
        // simply mute or unmute volume

        isMuted = !isMuted;
        if (isMuted) {
            muteImageView.setImage(new Image("./Images/unmuteButt.png"));
            volumeSliderValue = volumeSlider.getValue();
            volumeSlider.adjustValue(0);
            System.out.println("Muted");
        } else {
            muteImageView.setImage(new Image("./Images/muteButt.png"));
            volumeSlider.adjustValue(volumeSliderValue);
            System.out.println("Unmuted");
        }
    }

    @FXML
    private void shuffle() {
        //TODO
        // shuffle playList

        isOnShuffle = !isOnShuffle;
        if (isOnShuffle) {
            shuffleImageView.setImage(new Image("./Images/shuffleOn.png"));
            System.out.println("On shuffle");
        } else {
            shuffleImageView.setImage(new Image("./Images/shuffle.png"));
            System.out.println("Not on shuffle");
        }
    }

    @FXML
    private void loop() {
        //TODO
        // loop whole playList,
        // so last element -> first element
        // maybe second click just the actual song will be looped

        isLooped = !isLooped;
        if (isLooped) {
            loopImageView.setImage(new Image("./Images/loopButtOn.png"));
            System.out.println("Looped");
        } else {
            loopImageView.setImage(new Image("./Images/loopButt.png"));
            System.out.println("Not looped");
        }
    }

    @FXML
    public void addPathWindow() throws IOException {
        new AddPathWindow();
    }

    @FXML
    public void Import() {
        yearList.refresh();
        artistList.refresh();
        albumList.refresh();
        songList.refresh();
        genreList.refresh();
        initialize();

        initSongs();
    }

    @FXML
    public void searchYear() {
        yearList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getYear().equals(yearChoiceBox.getValue()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    @FXML
    public void searchArtist() {
        artistList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getArtist().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    @FXML
    public void searchSong() {
        songList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getTitle().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    @FXML
    public void searchGenre() {
        genreList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getGenre().equals(genreChoiceBox.getValue()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    @FXML
    public void searchAlbum() {
        albumList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getAlbum().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    public void addToPlaylist() {
        switch(tabPane.getSelectionModel().getSelectedItem().getText()){
            case "Year":
                playlistList.getItems().addAll(yearList.getSelectionModel().getSelectedItems());
            case "Artist":
                playlistList.getItems().addAll(artistList.getSelectionModel().getSelectedItems());
            case "Album":
                playlistList.getItems().addAll(albumList.getSelectionModel().getSelectedItems());
            case "Song":
                playlistList.getItems().addAll(songList.getSelectionModel().getSelectedItems());
            case "Genre":
                playlistList.getItems().addAll(genreList.getSelectionModel().getSelectedItems());
        }
        playlistList.refresh();
    }
}