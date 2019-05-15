package Controller;

import View.EditWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
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

public class AddPathController {

    public TextField pathTextField;

    public Button browseButton;
    public Button addButton;
    public Button editButton;

    @FXML
    public void browse() {
        Stage primaryStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File selected = directoryChooser.showDialog(primaryStage);

        pathTextField.setText(selected.getAbsolutePath());
    }

    @FXML
    public void addAction(){
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

    @FXML
    public void editAction() throws IOException {
        new EditWindow();
    }
}
