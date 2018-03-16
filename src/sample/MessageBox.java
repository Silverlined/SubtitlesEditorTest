package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageBox {

    public static void display(String message) {
        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("SubtitlesEditor");
        primaryStage.setMinWidth(200);
        primaryStage.setMinHeight(40);

        Label label = new Label(message);

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(label);

        Scene scene = new Scene(layout, 200, 40);

        primaryStage.setScene(scene);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.showAndWait();
    }
}
