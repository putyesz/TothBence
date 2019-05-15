package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View for MainWindow.
 */
public class MyApplication extends Application {

    /**
     * Initializing Mainwindow.
     * @param primaryStage first Stage to show
     * @throws IOException if MainWindow does not exists
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Music Player");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
