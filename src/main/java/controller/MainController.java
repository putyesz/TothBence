package controller;

import exception.PlayerNotInitializedException;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import model.MediaModel;
import model.Song;
import view.AddPathWindow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MainWindow controller.
 */
public class MainController {

    /**
     * <pre>LoggerFactory a logoláshoz.</pre>
     */
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    /**
     * ListView to show when Year tab is open.
     */
    public ListView <String> yearList;
    /**
     * ListView to show when Artist tab is open.
     */
    public ListView <String> artistList;
    /**
     * ListView to show when Album tab is open.
     */
    public ListView <String> albumList;
    /**
     * ListView to show when Song tab is open.
     */
    public ListView <String> songList;
    /**
     * ListView to show when Genre tab is open.
     */
    public ListView <String> genreList;
    /**
     * ListView for the actual playlist.
     */
    public ListView <String> playlistList;

    /**
     * ImageView for the play button.
     */
    public ImageView playImageView;
    /**
     * ImageView for the shuffle button.
     */
    public ImageView shuffleImageView;
    /**
     * ImageView for the mute button.
     */
    public ImageView muteImageView;
    /**
     * ImageView for the loop button.
     */
    public ImageView loopImageView;

    /**
     * Button for play and pause actions.
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
     * Button for the {@link #tabPane} Year tab to search for song with the year from {@link #yearChoiceBox}.
     */
    public Button searchYearButton;
    /**
     * Button for a {@link #tabPane} Artist tab to search for song with artist from Textfield in {@link #allSongsList}.
     */
    public Button searchArtistButton;
    /**
     * Button for a {@link #tabPane} Album tab to search for song from the album in Textfield in {@link #allSongsList}.
     */
    public Button searchAlbumButton;
    /**
     * Button for the {@link #tabPane} Song tab to search for song with title in Textfield in {@link #allSongsList}.
     */
    public Button searchSongButton;
    /**
     * Button for the {@link #tabPane} Genre tab to search for song from genre from {@link #allSongsList}.
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
    public MenuItem updateMenuItem;
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
     * TabPane to group tabs.
     */
    public TabPane tabPane;

    /**
     * Counts song on tab.
     */
    public Label yearTabSongCounter;
    /**
     * Sums playtime on tab.
     */
    public Label yearTabTime;
    /**
     * Counts song on tab.
     */
    public Label artistTabSongCounter;
    /**
     * Sums playtime on tab.
     */
    public Label artistTabTime;
    /**
     * Counts song on tab.
     */
    public Label albumTabSongCounter;
    /**
     * Sums playtime on tab.
     */
    public Label albumTabTime;
    /**
     * Counts song on tab.
     */
    public Label songTabSongCounter;
    /**
     * Sums playtime on tab.
     */
    public Label songTabTime;
    /**
     * Counts song on tab.
     */
    public Label genreTabSongCounter;
    /**
     * Sums playtime on tab.
     */
    public Label genreTabTime;
    /**
     * Counts song on tab.
     */
    public Label playlistSongCounter;
    /**
     * Sums playtime on tab.
     */
    public Label playlistTime;

    /**
     * HashMap with a string key which is the concatenation of the artist and the title of the song.
     * And the value is the Song object itself.
     */
    protected HashMap <String, Song> songHashMap;

    /**
     * ArrayList to collect all the available songs.
     */
    protected ArrayList <Song> allSongsList;

    /**
     * An integer value to store the position, where we are in the playlist.
     */
    private int index = 0;

    /**
     * Variable to show the status of the {@link #playButton}.
     */
    private boolean isPaused;
    /**
     * Variable to show the status of the {@link #muteButton}.
     */
    private boolean isMuted;
    /**
     * Variable to show the status of the {@link #shuffleButton}.
     */
    private boolean isOnShuffle;
    /**
     * Variable to show the status of the {@link #loopButton}.
     */
    private boolean isLooped;

    /**
     * Variable to store the previously set value of the {@link #volumeSlider}.
     */
    private double volumeSliderValue;

    /**
     * Timer for {@link #playSlider} changes.
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
     * Initializes {@link #isPaused}, {@link #isMuted}, {@link #isOnShuffle},
     * {@link #isLooped}, {@link #allSongsList},
     * {@link #songHashMap} and a timer for updating the
     * {@link #playSlider} for the media.
     * Then invokes {@link #makeXMLFiles()}.
     */
    private void init() {
        isPaused = false;
        isMuted = false;
        isOnShuffle = false;
        isLooped = false;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateSlider();
            }
        }, 0, 1000);

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
     * <a href="file:src/main/resources/MainWindow.fxml">MainWindow.fxml</a> initialization for {@link #yearList}.
     * {@link #artistList}, {@link #albumList}, {@link #songList}, {@link #genreList} and {@link #playlistList}.
     * To allow multiple selection.
     * And for {@link #genreChoiceBox}, {@link #yearChoiceBox}to have values.
     */
    @FXML
    private void initialize() {
        yearList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        artistList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        albumList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        songList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genreList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        initChoiceBoxes();
        logger.info("Init Lists");
    }

    /**
     * Method for starting and pausing the play of media.
     * First check if the playlist is empty.
     * And if not, then check if there's any selected item in the {@link #playlistList}.
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
            logger.warn("Not playing song");
            if (playlistList.getSelectionModel().getSelectedItems().isEmpty()) {
                MediaModel.setMusic(songHashMap.get(playlistList.getItems().get(index)).getPath());
                logger.info("First Song selected automatically");
            } else {
                MediaModel.setMusic(songHashMap.get(playlistList.getSelectionModel()
                        .getSelectedItems().get(0)).getPath());
                logger.info("Manual selection");
            }
        } else {
            try {
                MediaModel.playPause();
                logger.info("Song started");
            } catch (PlayerNotInitializedException e) {
                e.printStackTrace();
            }
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
     * Reading tha <a href="file:../../../../src/main/resources/Locations.xml">Locations.xml</a> file tag-by-tag.
     * Looking through all the subdirectories to find all the .mp3 files and store informations from the song.
     * In {@link #allSongsList} and <a href="file:../../../../src/main/resources/Songs.xml">Songs.xml</a>.
     */
    private void initSongs() {
        allSongsList = new ArrayList <>();
        songHashMap = new HashMap <>();
        try {
            File inputFile = new File("src/main/resources/Locations.xml");
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
                    Song s = new Song(file);
                    if (!songHashMap.containsKey(s.getArtist() + " - " + s.getTitle())
                            && new File(file.getAbsolutePath()).exists()) {
                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document doc = documentBuilder.parse("src/main/resources/Songs.xml");
                        doc.getDocumentElement().normalize();

                        Element root = doc.getDocumentElement();

                        Element songElement = doc.createElement("song");
                        root.appendChild(songElement);

                        Element pathElement = doc.createElement("path");
                        pathElement.appendChild(doc.createTextNode(s.getPath()));
                        songElement.appendChild(pathElement);

                        Element yearElement = doc.createElement("year");
                        yearElement.appendChild(doc.createTextNode(s.getYear()));
                        songElement.appendChild(yearElement);

                        Element artistElement = doc.createElement("artist");
                        artistElement.appendChild(doc.createTextNode(s.getArtist()));
                        songElement.appendChild(artistElement);

                        Element albumElement = doc.createElement("album");
                        albumElement.appendChild(doc.createTextNode(s.getAlbum()));
                        songElement.appendChild(albumElement);

                        Element songTitleElement = doc.createElement("title");
                        songTitleElement.appendChild(doc.createTextNode(s.getTitle()));
                        songElement.appendChild(songTitleElement);

                        Element lengthElement = doc.createElement("length");
                        lengthElement.appendChild(doc.createTextNode(s.getLenghtInSecs()));
                        songElement.appendChild(lengthElement);

                        Element genreElement = doc.createElement("genre");
                        genreElement.appendChild(doc.createTextNode(s.getGenre()));
                        songElement.appendChild(genreElement);

                        TransformerFactory tf = TransformerFactory.newInstance();
                        Transformer transformer = tf.newTransformer();
                        DOMSource source = new DOMSource(doc);

                        StreamResult result = new StreamResult(new File("src/main/resources/Songs.xml"));

                        transformer.transform(source, result);
                        logger.info("Reading " + s.getArtist() + " - " + s.getTitle());
                        songHashMap.put(s.getArtist() + " - " + s.getTitle(), s);
                        allSongsList.add(s);
                    }
                }
                logger.info("Directory done");
            }
            logger.info("Locations done");
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
        }
        logger.info("Read done");
    }

    /**
     * Stops the song.
     */
    @FXML
    private void stopSong() {
        MediaModel.stopSong();
        playSlider.setValue(0);
        logger.info("StopSong pressed");
    }

    /**
     * Mute and Unmute the player.
     */
    @FXML
    private void muteUnmute() {
        try {
            MediaModel.mute();
            muteImageChanger();
            logger.info("Mute pressed");
        } catch (PlayerNotInitializedException e) {
            logger.error("Player not initialized");
            e.printStackTrace();
        }
    }

    /**
     * Makes {@link #index} random.
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
     *
     * @throws IOException if AddPath does not exists
     */
    @FXML
    private void addPathWindow() throws IOException {
        new AddPathWindow();
        logger.info("AddPath window opened");
    }

    /**
     * Newly added places will be read.
     */
    @FXML
    protected void updateSongs() {
        initSongs();
        initChoiceBoxes();
        logger.info("SongList updated");
    }

    /**
     * Sets items for {@link #yearList}.
     */
    @FXML
    private void searchYear() {
        yearList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getYear().equals(yearChoiceBox.getValue()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        yearTabSongCounter.setText(getSongCount(yearList.getItems()));
        yearTabTime.setText(getTime(yearList.getItems()));
        tabPane.getSelectionModel().getSelectedItem();
        logger.info("YearList updated");
    }

    /**
     * Sets items for {@link #artistList}.
     */
    @FXML
    private void searchArtist() {
        artistList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getArtist().toLowerCase().contains(artistTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        artistTabSongCounter.setText(getSongCount(artistList.getItems()));
        artistTabTime.setText(getTime(artistList.getItems()));
        logger.info("ArtistList updated");
    }

    /**
     * Sets items for {@link #albumList}.
     */
    @FXML
    private void searchAlbum() {
        albumList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getAlbum().toLowerCase().contains(albumTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        albumTabSongCounter.setText(getSongCount(albumList.getItems()));
        albumTabTime.setText(getTime(albumList.getItems()));
        logger.info("AlbumList updated");
    }

    /**
     * Sets items for {@link #songList}.
     */
    @FXML
    private void searchSong() {
        songList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getTitle().toLowerCase().contains(songTextField.getText().toLowerCase()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        songTabSongCounter.setText(getSongCount(songList.getItems()));
        songTabTime.setText(getTime(songList.getItems()));
        logger.info("SongList updated");
    }

    /**
     * Sets items for {@link #genreList}.
     */
    @FXML
    private void searchGenre() {
        genreList.setItems(FXCollections.observableArrayList(allSongsList.stream()
                .filter(song -> song.getGenre().equals(genreChoiceBox.getValue()))
                .map(song -> song.getArtist() + " - " + song.getTitle())
                .collect(Collectors.toList())));
        genreTabSongCounter.setText(getSongCount(genreList.getItems()));
        genreTabTime.setText(getTime(genreList.getItems()));
        logger.info("GenreList updated");
    }

    /**
     * Adds items to {@link #playlistList}.
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
            default:
                break;
        }
        playlistSongCounter.setText(getSongCount(playlistList.getItems()));
        playlistTime.setText(getTime(playlistList.getItems()));
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
     * Seeking of a {@link #playSlider}.
     */
    @FXML
    public void playSliderEvent() {
        MediaModel.setSeek(playSlider.getValue());
        logger.info("Media slider changed");
    }

    /**
     * Initializes items in {@link #yearChoiceBox} and {@link #genreChoiceBox}.
     */
    private void initChoiceBoxes() {
        yearChoiceBox.getItems().setAll(FXCollections.observableArrayList(allSongsList.stream()
                .map(Song::getYear)
                .distinct()
                .collect(Collectors.toList())));
        logger.info("Previous Song pressed");
        genreChoiceBox.getItems().setAll(FXCollections.observableArrayList(allSongsList.stream()
                .map(Song::getGenre)
                .distinct()
                .collect(Collectors.toList())));
        logger.info("ChoiceBoxes items updated");
    }

    /**
     * Update method to track progression of a song.
     */
    private void updateSlider() {
        if (MediaModel.isSet()) {
            playSlider.setValue(MediaModel.getSeek());
            logger.info("Media is playing, can seek");
        }
    }

    /**
     * Creates <a href="file:../../../../src/main/resources/Songs.xml">Songs.xml</a>,<a href="file:../../../../src/main/resources/Locations.xml">Locations.xml</a> documents.
     * If does not exists.
     *
     * @throws ParserConfigurationException cannot parse from document
     * @throws TransformerException         cannot transform to document
     */
    private void makeXMLFiles() throws ParserConfigurationException, TransformerException {
        if (!new File("src/main/resources/Songs.xml").exists()) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("songs");
            doc.appendChild(rootElement);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File("src/main/resources/Songs.xml"));

            transformer.transform(source, result);
            logger.info("Songs.xml created");
        }
        //-------------------------------------------------------------

        if (!new File("src/main/resources/Locations.xml").exists()) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("locations");
            doc.appendChild(rootElement);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File("src/main/resources/Locations.xml"));

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
     * Changes the image if {@link #playButton} is pressed.
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
     * Skips to the number {@link #index} song in {@link #playlistList} .
     */
    private void changeSong() {
        if (isOnShuffle) {
            index = new Random().nextInt(playlistList.getItems().size());
        }
        if (index < 0) {
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
     *
     * @param mouseEvent clicks
     */
    public void doubleClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            startMusic();
            index = playlistList.getSelectionModel().getSelectedIndex();
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
     *
     * @param d amount value
     */
    private void setVolume(double d) {
        MediaModel.setVolume(d);
        logger.info("Volume set");
    }

    public String getSongCount(ObservableList <String> list) {
        if (list != null) {
            return list.size() + " songs";
        } else {
            return "0 song";
        }
    }

    public String getTime(ObservableList <String> list) {
        double time = 0;
        int hours = 0, mins = 0, secs = 0;
        if (list != null) {
            for (String s : list) {
                time += Double.parseDouble(songHashMap.get(s).getLenghtInSecs());
            }
            hours = (int) time / 3600;
            secs = (int) time - hours * 3600;
            mins = secs / 60;
            secs = secs - mins * 60;
        }
        return String.format("%02d:%02d:%02d", hours, mins, secs);
    }

    /**
     * Cancel timer when the window is closed.
     */
    @Override
    @Deprecated
    protected void finalize() {
        timer.cancel();
    }
}
