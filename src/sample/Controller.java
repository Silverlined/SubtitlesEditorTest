package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class Controller {

    @FXML
    Button loadFileButton, openFileWithButton, exitButton, saveChangesButton;
    @FXML
    RadioButton speedUpButton, slowDownButton;
    @FXML
    CheckBox removeTagsBox;
    @FXML
    TextField nameField, miliSecondsField;
    @FXML
    Text nameOfLoadedFile;

    public void loadFile() {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        Main.subtitleFile = fileChooser.showOpenDialog(new Stage());
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Open Subtitle File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Subtitles", "*.srt", "*.sub"),
                new FileChooser.ExtensionFilter("Sub", "*.sub"),
                new FileChooser.ExtensionFilter("Srt", "*.srt")
        );
    }

    public void openWith() {

    }

    public void exit() {

    }

    public void saveChanges() {

    }

}
