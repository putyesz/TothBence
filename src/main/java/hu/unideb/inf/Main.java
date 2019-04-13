package hu.unideb.inf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent gui_fxml = FXMLLoader.load(getClass().getResource("/gui.fxml"));

        primaryStage.setTitle("Toth Bence's music player");
        primaryStage.setScene(new Scene(gui_fxml));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(400);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        //Configurator.defaultConfig().writer(new FileWriter("log.txt")).level(Level.WARNING).activate();
        launch(args);
    }
}
