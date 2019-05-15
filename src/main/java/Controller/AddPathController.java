package Controller;

import View.EditWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
 * Controller for AddPathWindow.
 */
public class AddPathController {

    /**
     * Textfield for path.
     */
    public TextField pathTextField;

    /**
     * Button for opening filebrowser.
     */
    public Button browseButton;
    /**
     * Button for adding path to locations.
     */
    public Button addButton;
    /**
     * Button for editing available locations.
     */
    public Button editButton;

    /**
     * Method used for opening a Directory Chooser window, where the selected directory will be the content of the TextField.
     */
    @FXML
    public void browse() {
        Stage primaryStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File selected = directoryChooser.showDialog(primaryStage);

        pathTextField.setText(selected.getAbsolutePath());
        LoggerFactory.getLogger(AddPathController.class).info("Browse pressed");
    }

    /**
     * Check if any path is defined in the TextField and then reading.
     * The previously generated Locations from an xml database, and definining new location element in it.
     * Also shows an Alert when the TextField is empty.
     */
    @FXML
    public void addAction() {
        try {
            if (!"".equals(pathTextField.getText())) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                Document document = documentBuilder.parse("Locations.xml");
                document.getDocumentElement().normalize();

                Element root = document.getDocumentElement();

                Element newLocation = document.createElement("location");
                newLocation.setAttribute("path", pathTextField.getText());

                root.appendChild(newLocation);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(document);

                StreamResult result = new StreamResult(new File("Locations.xml"));

                transformer.transform(source, result);
                LoggerFactory.getLogger(AddPathController.class).info("Location added");
            } else {
                LoggerFactory.getLogger(AddPathController.class).warn("No selected directory");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No directory selected");
                alert.showAndWait();
            }
            LoggerFactory.getLogger(AddPathController.class).info("Add done");

        } catch (TransformerException e) {
            LoggerFactory.getLogger(AddPathController.class).error("TransformerException");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            LoggerFactory.getLogger(AddPathController.class).error("ParserConfigurationException");
            e.printStackTrace();
        } catch (SAXException e) {
            LoggerFactory.getLogger(AddPathController.class).error("SAXException");
            e.printStackTrace();
        } catch (IOException e) {
            LoggerFactory.getLogger(AddPathController.class).error("IOException");
            e.printStackTrace();
        }
    }

    /**
     * Opens a new Window to delete the existing location elements.
     * @throws IOException if EditWindow.fxml does not exists
     */
    @FXML
    public void editAction() throws IOException {
        new EditWindow();
    }
}
