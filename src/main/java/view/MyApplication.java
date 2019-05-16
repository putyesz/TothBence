package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * view for MainWindow.
 */
public class MyApplication extends Application {

    /**
     * Initializing Window.
     *
     * @param primaryStage first Stage to show
     * @throws IOException if <a href="file:../resources/MainWindow.fxml">/MainWindow.fxml</a> does not exists
     */
    @Override
    final public void start(final Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Music Player");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
