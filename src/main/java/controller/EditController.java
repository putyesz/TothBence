package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

/**
 * controller for Editing available locations.
 */
public class EditController {

    /**
     * ListView to show locations.
     */
    public ListView<String> locationsList;
    /**
     * Button to delete item from locations list.
     */
    public Button deleteButton;

    /**
     * Initializer method to read <a href="file:../../../../src/main/resources/Locations.xml">Locations.xml</a>.
     * Pass it to a ListView element.
     */
    @FXML
    private void initialize() {
        try {
            File inputFile = new File("src/main/resources/Locations.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(inputFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("location");
            ObservableList<String> observableList = FXCollections.observableArrayList();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                observableList.add(element.getAttribute("path"));
            }

            locationsList.setItems(observableList);
            LoggerFactory.getLogger(EditController.class).info("Initialized EditWindow");

        } catch (ParserConfigurationException e) {
            LoggerFactory.getLogger(EditController.class).error("ParserConfigurationException");
            e.printStackTrace();
        } catch (IOException e) {
            LoggerFactory.getLogger(EditController.class).error("IOException");
            e.printStackTrace();
        } catch (SAXException e) {
            LoggerFactory.getLogger(EditController.class).error("SAXException");
            e.printStackTrace();
        }
    }

    /**
     * Delete method for {@link #deleteButton}, to remove the selected location from the Location list.
     */
    @FXML
    public void delete() {
        try {
            File inputFile = new File("src/main/resources/Locations.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(inputFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("location");
            ObservableList<String> observableList = FXCollections.observableArrayList();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                if (element.getAttribute("path")
                        .equals(locationsList.getSelectionModel().getSelectedItem())) {
                    Node parent = element.getParentNode();
                    parent.removeChild(element);
                    i--;
                } else {
                    observableList.add(element.getAttribute("path"));
                }
            }

            locationsList.setItems(observableList);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);

            StreamResult result = new StreamResult(new File("src/main/resources/Locations.xml"));

            transformer.transform(source, result);
            LoggerFactory.getLogger(EditController.class).info("Item deleted");

        } catch (ParserConfigurationException e) {
            LoggerFactory.getLogger(EditController.class).error("ParserConfigurationException");
            e.printStackTrace();
        } catch (IOException e) {
            LoggerFactory.getLogger(EditController.class).error("IOException");
            e.printStackTrace();
        } catch (SAXException e) {
            LoggerFactory.getLogger(EditController.class).error("SAXException");
            e.printStackTrace();
        } catch (TransformerException e) {
            LoggerFactory.getLogger(EditController.class).error("TransformerException");
            e.printStackTrace();
        }

        locationsList.refresh();
    }
}
