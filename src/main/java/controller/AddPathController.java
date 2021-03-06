package controller;

import org.w3c.dom.NodeList;
import view.EditWindow;
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
 * controller for AddPathWindow.
 */
public class AddPathController {

    /**
     * TextField for path.
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
        if (selected != null) {
            pathTextField.setText(selected.getAbsolutePath());
            LoggerFactory.getLogger(AddPathController.class).info("Browse done");
        } else {
            LoggerFactory.getLogger(AddPathController.class).info("no selected dir");
        }
    }

    /**
     * Check if any path is defined in the TextField and then reading.
     * The previously generated locations from <a href="file:../../../../src/main/resources/Locations.xml">Locations.xml</a> database, and define new location element in it.
     * Also shows an Alert when the TextField is empty.
     */
    @FXML
    final public void addAction() {
        try {
            if (!"".equals(pathTextField.getText())) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                Document document = documentBuilder.parse("src/main/resources/Locations.xml");
                document.getDocumentElement().normalize();

                Element root = document.getDocumentElement();
                NodeList nodeList = document.getElementsByTagName("location");

                if (nodeList.getLength() != 0
                        && alreadyInList(nodeList, pathTextField.getText())){
                    return;
                }
                Element newLocation = document.createElement("location");
                newLocation.setAttribute("path", pathTextField.getText());

                root.appendChild(newLocation);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(document);

                StreamResult result = new StreamResult(new File("src/main/resources/Locations.xml"));

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

    private boolean alreadyInList(NodeList nodeList, String text) {
        for (int i = 0; i < nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if (element.getAttribute("path").equals(text)){
                LoggerFactory.getLogger(AddPathController.class).info("Already existing location");
                return true;
            }
        }
        return false;
    }

    /**
     * Opens a new EditWindow  to delete the existing location elements.
     *
     * @throws IOException if <a href="file:src/main/resources/EditWindow.fxml">EditWindow.fxml</a> does not exists
     */
    @FXML
    final public void editAction() throws IOException {
        new EditWindow();
    }
}
