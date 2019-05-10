import View.MyApplication;
import javafx.application.Application;

import static javafx.application.Application.launch;

public class Main{
/*
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
*/

    public static void main(String[] args) {
        Application.launch(MyApplication.class, args);
    }
}
