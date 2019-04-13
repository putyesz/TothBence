package hu.unideb.inf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;


public class Controller {
    @FXML public Button prevButt;
    @FXML public Button playButt;
    @FXML public Button nextButt;
    @FXML public Button muteButt;
    @FXML public Slider volSlider;

    Logger logger = LoggerFactory.getLogger(Main.class);

    @FXML protected void handlePrevButtonPressed(ActionEvent event){
        prevButt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logger.info("HEnlo");
                //TODO
                // visszalepes az elozo szamra
                //prevButt.prevSong();
            }
        });
    }

    protected void VolumeBarVisibility(){
        nextButt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO
                // elorelepes a kovetkezo szamra
            }
        });
        playButt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO
                // elinditani a lejatszast, illetve a kepet cserelni
                // pause-ra valamint vissza
            }
        });
        muteButt.setOnMouseClicked(new EventHandler <MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO
                // nemitas
            }
        });
        //TODO
        // Hangero = volSlider.getValue()

    }
}
