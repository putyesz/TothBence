package Controller;

import Model.MediaModel;
import Model.Song;
import View.AddPathWindow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

/**
 * MainWindow Controller.
 */
public class MainController {

    /**
     * <pre>LoggerFactory a logoláshoz.</pre>
     */
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    /**
     * Listview to show when Year tab is open.
     */
    public ListView <String> yearList;
    /**
     * Listview to show when Artist tab is open.
     */
    public ListView <String> artistList;
    /**
     * Listview to show when Album tab is open.
     */
    public ListView <String> albumList;
    /**
     * Listview to show when Song tab is open.
     */
    public ListView <String> songList;
    /**
     * Listview to show when Genre tab is open.
     */
    public ListView <String> genreList;
    /**
     * Listview for the actual playlist.
     */
    public ListView <String> playlistList;

    /**
     * Imageview for the play button.
     */
    public ImageView playImageView;
    /**
     * Imageview for the shuffle button.
     */
    public ImageView shuffleImageView;
    /**
     * Imageview for the mute button.
     */
    public ImageView muteImageView;
    /**
     * Imageview for the loop button.
     */
    public ImageView loopImageView;

    /**
     * Button element for play and pause actions.
     */
    public Button playButton;
    /**
     * Button element for switching to the previous song.
     */
    public Button prevButton;
    /**
     * Button element for switching to the next song.
     */
    public Button nextButton;
    /**
     * Button element to stop action.
     */
    public Button stopButton;
    /**
     * Button element to mute the mediaplayer.
     */
    public Button muteButton;
    /**
     * Button element for shuffling the playlist.
     */
    public Button shuffleButton;
    /**
     * Button element for looping the playlist.
     */
    public Button loopButton;

    /**
     * Button for the Year tab to search for song with the year from Choicebox in allSongList.
     */
    public Button searchYearButton;
    /**
     * Button for the Artist tab to search for songs with Artist from Textfield in allSongList.
     */
    public Button searchArtistButton;
    /**
     * Button for the Album tab to search for songs from the Album in Textfield, in allSongList.
     */
    public Button searchAlbumButton;
    /**
     * Button for the Song tab to search for songs with Title in Textfield, in allSongList.
     */
    public Button searchSongButton;
    /**
     * Button for the Genre tab to search for songs with the same genre from allSongList.
     */
    public Button searchGenreButton;

    /**
     * Slider to manually adjust Volume value.
     */
    public Slider volumeSlider;
    /**
     * Slider to show and manually modify media progression.
     */
    public Slider playSlider;

    /**
     * Menuitem for manual importation.
     */
    public MenuItem importMenuitem;
    /**
     * Menuitem to open a Window to add and delete directory paths.
     */
    public MenuItem addPathMenuitem;

    /**
     * ChoiceBox to select a year from.
     */
    public ChoiceBox <String> yearChoiceBox;
    /**
     * TextField in the Artist tab to search for songs from this Artist.
     */
    public TextField artistTextField;
    /**
     * TextField in the Album tab to search for songs from this Album.
     */
    public TextField albumTextField;
    /**
     * TextField in the Song tab to search for songs with this Titles.
     */
    public TextField songTextField;
    /**
     * ChoiceBox to select a genre from.
     */
    public ChoiceBox <String> genreChoiceBox;

    /**
     * Tabpane to group tabs.
     */
    public TabPane tabPane;

    /**
     * HashMap with a string key which is the concatenation of the artist and the title of the song, and the value is the Song object itself.
     */
    private HashMap <String, Song> songHashMap;

    /**
     * An Array to collect all the available songs.
     */
    private ArrayList <Song> allSongsList;

    /**
     * An integer value to store the position, where we are in the playlist.
     */
    private int index = 0;

    /**
     * Variable to show the status of the playButton.
     */
    private boolean isPaused;
    /**
     * Variable to show the status of the muteButton.
     */
    private boolean isMuted;
    /**
     * Variable to show the status of the shuffleButton.
     */
    private boolean isOnShuffle;
    /**
     * Variable to show the status of the loopButton.
     */
    private boolean isLooped;

    /**
     * Variable to store the previously set value of the volume slider.
     */
    private double volumeSliderValue;

    /**
     * Timer for slider changes.
     */
    private Timer timer;

    /**
     * Constructor.
     */
    public MainController() {
        init();
        initSongs();
    }

    /**
     * Initializes boolean variables, ChoiceBox, HashMap and a timer for updating the slider for the media.
     * Then invoces makeXMLFiles.
     */
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
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    updateSlider();
                }
                }, 0, 2000);
        }finally {
            timer.cancel();
        }

        try {
            makeXMLFiles();
        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException");
            e.printStackTrace();
        } catch (TransformerException e) {
            logger.error("TransformerException");
            e.printStackTrace();
        }
        logger.info("initialized bools, Choiceboxes, Map, Timer");
    }

    /**
     * Reading tha Locatios.xml file tag-by-tag, looking through all the subdirectories to find all the .mp3 files and store informations from the songs in allSongList and Songs.xml.
     */
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

                        Element pathElement = doc.createElement("path");
                        pathElement.appendChild(doc.createTextNode(file.getAbsolutePath().replace("\\", "/")));
                        songElement.appendChild(pathElement);

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
                logger.info("Songs read");
            }
            logger.info("Location read" + nodeList);
        } catch (IOException e) {
            logger.error("IOException");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException");
            e.printStackTrace();
        } catch (SAXException e) {
            logger.error("SAXException");
            e.printStackTrace();
        } catch (TransformerException e) {
            logger.error("TransformerException");
            e.printStackTrace();
        } catch (TikaException e) {
            logger.error("TikaException");
            e.printStackTrace();
        }
        logger.info("Read done");
    }

    /**
     * FXML file initialization for Lists to allow multiple selection.
     * And for Choiceboxes to have values.
     */
    @FXML
    private void initialize() {
        yearList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        artistList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        albumList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        songList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genreList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playlistList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        initChoiceBoxes();
        logger.info("Init Lists");
    }

    /**
     * Method for starting and pausing the play of media.
     * First check if the playlist is empty, and if not, then check if there's any selected item in the playlist.
     * If not, the plays the first song in the list.
     * Else plays the selected song.
     */
    @FXML
    private void startPause() {
        if (playlistList.getItems().isEmpty()) {
            logger.warn("Empty playlist");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No song in the playlist");
            alert.showAndWait();
            return;
        }

        if (!MediaModel.isSet()) {
            logger.warn("Already playing song");
            if (playlistList.getSelectionModel().getSelectedItems().isEmpty()) {
                MediaModel.setMusic(songHashMap.get(playlistList.getItems().get(index)).getPath());
                logger.info("First Song selected automatically");
            } else {
                MediaModel.setMusic(songHashMap.get(playlistList.getSelectionModel().getSelectedItems().get(0)).getPath());
                logger.info("Manual selection");
            }
        } else {
            MediaModel.playPause();
            logger.info("Song started");
        }
        playImageChanger();
    }

    /**
     * Skips to the previous song.
     */
    @FXML
    private void prevSong() {
        index--;
        changeSong();
        logger.info("Previous Song pressed");
    }

    /**
     * Skips to the next song.
     */
    @FXML
    private void nextSong() {
        index++;
        changeSong();
        logger.info("Next Song pressed");
    }

    /**
     * Stops the song.
     */
    @FXML
    private void stopSong() {
        MediaModel.stopSong();
        playSlider.setValue(0);
        playImageChanger();
        logger.info("StopSong pressed");
    }

    /**
     * Mute and Unmute the player.
     */
    @FXML
    private void muteUnmute() {
        muteImageChanger();
        MediaModel.mute();
        logger.info("Mute pressed");
    }

    /**
     * Makes index random.
     */
    @FXML
    private void shuffle() {
        logger.info("Shuffle pressed");

        isOnShuffle = !isOnShuffle;
        if (isOnShuffle) {
            shuffleImageView.setImage(new Image("./Images/shuffleOn.png"));
            logger.info("Shuffle is on");
        } else {
            shuffleImageView.setImage(new Image("./Images/shuffle.png"));
            logger.info("Shuffle is off");
        }
    }

    /**
     * TBH it is always looped.
     */
    @FXML
    private void loop() {
        logger.info("Loop pressed");

        isLooped = !isLooped;
        if (isLooped) {
            loopImageView.setImage(new Image("./Images/loopButtOn.png"));
            logger.info("Looped");
        } else {
            loopImageView.setImage(new Image("./Images/loopButt.png"));
            logger.info("Not Looped");
        }
    }

    /**
     * Opens a new AddPath window.
     * @throws IOException if AddPath does not exists
     */
    @FXML
    private void addPathWindow() throws IOException {
        new AddPathWindow();
        logger.info("AddPath window opened");
    }

    /**
     * Does not do it's job.
     */
    @FXML
    private void Import() {
        initChoiceBoxes();
        logger.info("Itt update kellene");
    }

    /**
     * Sets items for yearList.
     */
    @FXML
    private void searchYear() {
        yearList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getYear().equals(yearChoiceBox.getValue()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        logger.info("YearList updated");
    }

    /**
     * Sets items for artistList.
     */
    @FXML
    private void searchArtist() {
        artistList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getArtist().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        logger.info("ArtistList updated");
    }

    /**
     * Sets items for albumList.
     */
    @FXML
    private void searchAlbum() {
        albumList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getAlbum().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        logger.info("AlbumList updated");
    }

    /**
     * Sets items for songList.
     */
    @FXML
    private void searchSong() {
        songList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getTitle().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        logger.info("SongList updated");
    }

    /**
     * Sets items for genreList.
     */
    @FXML
    private void searchGenre() {
        genreList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getGenre().equals(genreChoiceBox.getValue()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        logger.info("GenreList updated");
    }

    /**
     * Adds items to playlistList.
     */
    @FXML
    private void addToPlaylist() {
        switch (tabPane.getSelectionModel().getSelectedItem().getText()) {
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
        logger.info("Items added to playlist");
    }

    /**
     * Sets the volume of the player.
     */
    @FXML
    public void volumeSliderEvent() {
        setVolume(volumeSlider.getValue());
        logger.info("Volume changed");
    }

    /**
     * Seeking of a Slider.
     */
    @FXML
    public void playSliderEvent() {
        MediaModel.setSeek(playSlider.getValue());
        logger.info("Media slider changed");
    }

    /**
     * Initializes items in Choiceboxes.
     */
    private void initChoiceBoxes(){
        yearChoiceBox.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .map(Song::getYear)
                .distinct()
                .collect(Collectors.toList())));
        logger.info("Previous Song pressed");
        genreChoiceBox.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .map(Song::getGenre)
                .distinct()
                .collect(Collectors.toList())));
        logger.info("ChoiceBoxes items updated");
    }

    /**
     * Update method to track progression of a song.
     */
    private void updateSlider(){
        if(MediaModel.isSet()){
            playSlider.setValue(MediaModel.getSeek());
            logger.info("Media is playing, can seek");
        }
    }

    /**
     * Gets the metadatas from a song.
     * @param fileLocation path to song
     * @param neededMeta what do you want?
     * @return selected metadatas
     * @throws IOException if file does not exists
     * @throws TikaException parsing problem
     * @throws SAXException parsing problem
     */
    private String getMetas(String fileLocation, String neededMeta) throws TikaException, SAXException, IOException {
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
                logger.error("Invalid metadata");
                throw new IllegalArgumentException("Invalid metadata");
        }

    }

    /**
     * Creates xml document if does not exists.
     * @throws ParserConfigurationException cannot parse from document
     * @throws TransformerException cannot transform to document
     */
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
            logger.info("Songs.xml created");
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
            logger.info("Locations.xml created");
        }
    }

    /**
     * Changes the image if muteButton pressed.
     */
    private void muteImageChanger() {
        isMuted = !isMuted;
        if (isMuted) {
            muteImageView.setImage(new Image("./Images/muteButt.png"));
            volumeSliderValue = volumeSlider.getValue();
            volumeSlider.adjustValue(0);
            setVolume(0);
            logger.info("Muted");
        } else {
            muteImageView.setImage(new Image("./Images/unmuteButt.png"));
            volumeSlider.adjustValue(volumeSliderValue);
            setVolume(volumeSliderValue);
            logger.info("Not muted");
        }
    }

    /**
     * Changes the image if playButton pressed.
     */
    private void playImageChanger() {
        isPaused = !isPaused;
        if (!isPaused) {
            playImageView.setImage(new Image("./Images/pauseButt.png"));
            logger.info("Image changed to pauseButton");
        } else {
            playImageView.setImage(new Image("./Images/playButt.png"));
            logger.info("Image changed to playButton");
        }
    }

    /**
     * Skips to the number index song.
     */
    private void changeSong(){
        if (isOnShuffle){
            index = new Random().nextInt(playlistList.getItems().size());
        }
        if(index < 0){
            index = playlistList.getItems().size() - 1;
            logger.info("Negative index");
        }
        index %= playlistList.getItems().size();
        MediaModel.setMusic(songHashMap.get(playlistList.getItems().get(index)).getPath());
        playlistList.getSelectionModel().select(index);
        logger.info("Song Changed");
    }

    /**
     * Starts song with doubleclick.
     * @param mouseEvent clicks
     */
    public void DoubleClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            startMusic();
            logger.info("DoubleClickStart");
        }
    }

    /**
     * Actually starts playing the song.
     */
    private void startMusic() {
        String s = playlistList.getSelectionModel().getSelectedItem();
        logger.info(songHashMap.get(s).getPath());
        MediaModel.setMusic(songHashMap.get(s).getPath());
        playImageChanger();
        logger.info("Music started");
    }

    /**
     * Sets the volume to the player.
     * @param d amount value
     */
    private void setVolume(double d) {
        MediaModel.setVolume(d);
        logger.info("Volume set");
    }

}
