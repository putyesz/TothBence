package Controller;

import Model.Song;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class AddPathController {

    public TextField pathTextField;

    public Button browseButton;
    public Button addButton;
    public Button editButton;

    //TODO
    // Add method, to add String path to my database


        private void Add(String filepath){
            try {
                //new File(getClass().getResource("/sourceFile/songLocations.xml").toString()
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = (Document) docBuilder.parse(new File(getClass().getResource("/Songs.xml").toString()));
                Element prevSongs = doc.getDocumentElement();

                Collection <Song> songs = new ArrayList <Song>();
                //songs.add(new Song());

                for (Song song : songs) {
                    Element location = doc.createElement("location");

                    location.setAttribute("path", filepath);
                }

                DOMSource source = new DOMSource(doc);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                StreamResult result = new StreamResult(new File(String.valueOf(getClass().getResource("/sourceFile/songLocations.xml"))));
                transformer.transform(source, result);
            } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
                System.out.println("Something went wrong");
                e.printStackTrace();
            }
        }

    @FXML
    public void Browse() {
        Stage primaryStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File selected = directoryChooser.showDialog(primaryStage);

        pathTextField.setText(selected.getAbsolutePath());
    }

    @FXML
    public void AddAction(){
        try {
            if (!"".equals(pathTextField.getText())) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                Document document = documentBuilder.parse("Locations.xml");
                document.getDocumentElement().normalize();

                Element root = document.getDocumentElement();

                Element newLocation = document.createElement("location");
                newLocation.setAttribute("path", pathTextField.getText() );

                root.appendChild(newLocation);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(document);

                StreamResult result = new StreamResult(new File("Locations.xml"));

                transformer.transform(source, result);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No directory selected");
                alert.showAndWait();
            }
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }

    public void editAction() {
        //TODO
        // Show a list of available paths in a new window and maybe delete lines

    }
}
