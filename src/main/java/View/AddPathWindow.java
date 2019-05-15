package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View for Adding Path.
 */
public class AddPathWindow {

    /**
     * Initializing window.
     * @throws IOException if AddPathWindow does not exists
     */
    public AddPathWindow() throws IOException {
        FXMLLoader fl = new FXMLLoader(getClass().getResource("/AddPathWindow.fxml"));
        Parent root = fl.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Add Path");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
