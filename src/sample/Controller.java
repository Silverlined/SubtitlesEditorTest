package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class Controller {

    public Button loadFileButton, openFileWithButton, exitButton, saveChangesButton;
    public RadioButton speedUpButton, slowDownButton;
    public CheckBox removeTagsBox;
    public TextField nameField, miliSecondsField;
    public Text nameOfLoadedFile;

    public void loadFile() {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        Main.subtitleFile = fileChooser.showOpenDialog(new Stage());
        SetTextField();
    }

    private void SetTextField() {
        nameOfLoadedFile.setText(Main.subtitleFile.getName());
        nameField.setText(Main.subtitleFile.getAbsolutePath());
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
        System.exit(0);
    }

    public void saveChanges() {

    }

}
