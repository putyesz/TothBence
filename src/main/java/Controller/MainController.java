package Controller;

import Model.Song;
import View.AddPathWindow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    public ListView <String> yearList;
    public ListView <String> artistList;
    public ListView <String> albumList;
    public ListView <String> songList;
    public ListView <String> genreList;

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

    public ChoiceBox<String> yearChoiceBox;
    public TextField artistTextField;
    public TextField albumTextField;
    public TextField songTextField;
    public ChoiceBox<String> genreChoiceBox;

    private ArrayList <Song> allSongsList;

    private boolean isPaused;
    private boolean isMuted;
    private boolean isOnShuffle;
    private boolean isLooped;

    private double volumeSliderValue;

    public MainController() {
        Init();
        InitSongs();
        yearChoiceBox = new ChoiceBox <>();
        //yearChoiceBox.getItems().addAll("sdfa", "sdas");
        yearChoiceBox.setValue("asdasdasdas");
    }

    private void Init() {
        isPaused = true;
        isMuted = false;
        isOnShuffle = false;
        isLooped = false;
        allSongsList = new ArrayList <>();
        yearChoiceBox = new ChoiceBox <>();
        genreChoiceBox = new ChoiceBox <>();
        //InitChoiceBoxes();

        try {
            MakeXMLFiles();
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void InitSongs(){
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

                for(File file : filesInFolder) {
                    if (new File(file.getAbsolutePath()).exists()) {
                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document doc = documentBuilder.parse("Songs.xml");
                        doc.getDocumentElement().normalize();

                        Element root = doc.getDocumentElement();

                        Element songElement = doc.createElement("song");
                        root.appendChild(songElement);

                        Element yearElement = doc.createElement("year");
                        yearElement.appendChild(doc.createTextNode(GetMetas(file.getAbsolutePath(), "year")));
                        songElement.appendChild(yearElement);

                        Element artistElement = doc.createElement("artist");
                        artistElement.appendChild(doc.createTextNode(GetMetas(file.getAbsolutePath(), "artist")));
                        songElement.appendChild(artistElement);

                        Element albumElement = doc.createElement("album");
                        albumElement.appendChild(doc.createTextNode(GetMetas(file.getAbsolutePath(), "album")));
                        songElement.appendChild(albumElement);

                        Element songTitleElement = doc.createElement("title");
                        songTitleElement.appendChild(doc.createTextNode(GetMetas(file.getAbsolutePath(), "title")));
                        songElement.appendChild(songTitleElement);

                        Element lengthElement = doc.createElement("length");
                        lengthElement.appendChild(doc.createTextNode(GetMetas(file.getAbsolutePath(), "length")));
                        songElement.appendChild(lengthElement);

                        Element genreElement = doc.createElement("genre");
                        genreElement.appendChild(doc.createTextNode(GetMetas(file.getAbsolutePath(), "genre")));
                        songElement.appendChild(genreElement);

                        TransformerFactory tf = TransformerFactory.newInstance();
                        Transformer transformer = tf.newTransformer();
                        DOMSource source = new DOMSource(doc);

                        StreamResult result = new StreamResult(new File("Songs.xml"));

                        transformer.transform(source, result);
                    }
                    allSongsList.add(new Song(file));
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException | TikaException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        yearChoiceBox.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .map(Song::getYear)
                .distinct()
                .collect(Collectors.toList())));
        genreChoiceBox.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .map(Song::getGenre)
                .distinct()
                .collect(Collectors.toList())));
    }

    private String GetMetas(String fileLocation, String neededMeta) throws IOException, TikaException, SAXException {
        InputStream input = new FileInputStream(new File(fileLocation));
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();

        switch (neededMeta){
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

    private void MakeXMLFiles() throws ParserConfigurationException, TransformerException {
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

        if (! new File("Locations.xml").exists()) {
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
    private void StartPause() {
        //TODO
        // if no selected item in playlist
        // automatically play first song
        // else first element must be played


        isPaused = !isPaused;
        if (isPaused) {
            playImageView.setImage(new Image("./Images/playButt.png"));
            System.out.println("Start");
        } else {
            playImageView.setImage(new Image("./Images/pauseButt.png"));
            System.out.println("Pause");
        }
    }

    @FXML
    private void PrevSong() {
        System.out.println("Prev");
        //TODO
        // don't throw Exception or error, if first song int the list
        // else play the previous element in the playList

    }

    @FXML
    private void NextSong() {
        System.out.println("Next");
        //TODO
        // don't throw Exception or error, if last song in the list
        // else play the next element in the playList

    }

    @FXML
    private void StopSong() {
        playSlider.adjustValue(0);
        System.out.println("Stop");
    }

    @FXML
    private void MuteUnmute() {
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
    private void Shuffle() {
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
    private void Loop() {
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
    public void AddPathWindow() throws IOException {
        new AddPathWindow();
    }

    @FXML
    public void Import() throws ParserConfigurationException, IOException, SAXException {
        //TODO
        // Automatic import from database to a Song type List
        // then to yearList, GenreList etc...
        // if done, no need for import menu item
    }

    @FXML
    public void SearchYear() {
        yearList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getYear().equals(yearChoiceBox.getValue().toString()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    @FXML
    public void SearchArtist() {
        artistList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getArtist().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    @FXML
    public void SearchSong() {
        songList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getTitle().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    @FXML
    public void SearchGenre() {
        genreList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getGenre().equals(genreChoiceBox.getValue()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }

    @FXML
    public void SearchAlbum() {
        albumList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getAlbum().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
    }
}