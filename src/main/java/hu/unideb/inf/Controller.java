package hu.unideb.inf;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class Controller {
/*
    final ImageView playImage = new ImageView((Element) new Image("/play.png"));
    final ImageView prevImage = new ImageView((Element) new Image("/prev.png"));
    final ImageView nextImage = new ImageView((Element) new Image("/next.png"));
    Background s = new Background();
*/
    public Button prevButt;
    public Button playButt;
    public Button nextButt;
    public Button muteButt;
    public Slider volSlider;

    protected void VolumeBarVisibility(){
        prevButt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO
                //visszalépés az előző számra
            }
        });
        nextButt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO
                //előrelépés akövetkező számra
            }
        });
        playButt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO
                //elindítani a lejátszást, illetve a képet cserélni pause-ra
                //valamint vissza
            }
        });
        muteButt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO
                //némítás
            }
        });
        //TODO
        // Hangerő = volSlider.getValue()

    }
}
