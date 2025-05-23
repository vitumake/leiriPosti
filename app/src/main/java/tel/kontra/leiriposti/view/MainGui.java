package tel.kontra.leiriposti.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGui extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Leiriposti");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String args[]) {
        MainGui.launch(MainGui.class, args);
    }
}
