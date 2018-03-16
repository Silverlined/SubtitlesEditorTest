package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.regex.Pattern;

public class Main extends Application {

    public static File subtitleFile;
    public static File subtitlesEdited;
    Pattern regex = Pattern.compile("\\A\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}\\z");

    @FXML
    static BorderPane contentArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        SetUpGUI(primaryStage);

    }

    private void SetUpGUI(Stage primaryStage) throws java.io.IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditorGUI.fxml"));
        Controller controller = fxmlLoader.getController();
        fxmlLoader.setRoot(contentArea);
        primaryStage.setTitle("Subtitles Editor");
        primaryStage.setScene(new Scene(fxmlLoader.load(), 550, 350));
        primaryStage.show();
    }
}
