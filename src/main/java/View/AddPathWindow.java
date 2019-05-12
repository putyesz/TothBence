package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPathWindow {

    public AddPathWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AddPathWindow.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Add Path");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
