package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

public class AddPathController {

    public TextField pathTextField;

    public Button browseButton;
    public Button addButton;
    public Button editButton;

    /*
        private void Add(String filepath) throws ParserConfigurationException, TransformerException, IOException, URISyntaxException, SAXException {
            //new File(getClass().getResource("/sourceFile/songLocations.xml").toString()
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = (Document) docBuilder.parse(new File(getClass().getResource("/sourceFile/songLocations.xml").toURI()));
            Element prevSongs = doc.getDocumentElement();

            Collection <Rootpackage.Model.Song> songs = new ArrayList <Rootpackage.Model.Song>();
            songs.add(new Rootpackage.Model.Song());

            for (Rootpackage.Model.Song song : songs){
                Element newSongElement = doc.createElement("song");

                Element URL = doc.createElement("URL");
                URL.setAttribute("location", filepath);
                newSongElement.appendChild(URL);

                prevSongs.appendChild(newSongElement);

            }

            // write the content into xml file
            DOMSource source = new DOMSource(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(new File(String.valueOf(getClass().getResource("/sourceFile/songLocations.xml"))));
            transformer.transform(source, result);
        }
    */
    @FXML
    public void Browse(ActionEvent actionEvent) {
        Stage primaryStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File selected = directoryChooser.showDialog(primaryStage);

        pathTextField.setText(selected.getAbsolutePath());
    }

    @FXML
    public void AddAction(ActionEvent actionEvent){
        if (!"".equals(pathTextField.getText())) {
            //TODO
            // Add(pathTextField.getText());

        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No directory selected");
            alert.showAndWait();
        }
    }

    public void editAction(ActionEvent actionEvent) {

    }
}