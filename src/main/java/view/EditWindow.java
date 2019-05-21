package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * view for Editing Paths.
 */
public class EditWindow {

    /**
     * Initializing window.
     *
     * @throws IOException if <a href="file:../../../../src/main/resources/EditWindow.fxml">    EditWindow.fxml</a> does not exists
     */
    public EditWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EditWindow.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Edit");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
