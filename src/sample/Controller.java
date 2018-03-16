package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.util.regex.Pattern;


public class Controller {
    @FXML
    Button timeIntervalButton;
    @FXML
    Button loadFileButton, openFileWithButton, exitButton, saveChangesButton;
    @FXML
    RadioButton speedUpButton, slowDownButton;
    @FXML
    CheckBox removeTagsBox;
    @FXML
    TextField nameField, milliSecondsField;
    @FXML
    Text nameOfLoadedFile;
    private Desktop desktop = Desktop.getDesktop();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Pattern timeIntervalRegex = Pattern.compile("\\A\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}\\z");
    private Pattern tagRegex = Pattern.compile("<[^>]*>");

    public void loadFile() {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, "Open Subtitle File");
        Main.subtitleFile = fileChooser.showOpenDialog(new Stage());
        SetTextField();
    }

    private void SetTextField() {
        nameOfLoadedFile.setText(Main.subtitleFile.getName());
        nameField.setText(Main.subtitleFile.getAbsolutePath());
    }

    private void configureFileChooser(final FileChooser fileChooser, String message) {
        fileChooser.setTitle(message);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Subtitles", "*.srt", "*.sub"),
                new FileChooser.ExtensionFilter("Sub", "*.sub"),
                new FileChooser.ExtensionFilter("Srt", "*.srt")
        );
    }

    public void openWith() {
        try {
            desktop.open(Main.subtitleFile);
        } catch (NullPointerException | FileNotFoundException e) {
            MessageBox.display("Open Subtitles File First");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        System.exit(0);
    }

    public void saveChanges() {
        try {
            File chosenLocation = new File(nameField.getText());
            String line = null;
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Main.subtitlesEdited), "Windows-1251"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(chosenLocation), "Windows-1251"));
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
            }
            Main.subtitleFile = Main.subtitlesEdited;
            bufferedReader.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (UnsupportedEncodingException e) {
            MessageBox.display("Your system does not support Windows-1251 encoding");
            e.printStackTrace();
        } catch (NullPointerException | FileNotFoundException e) {
            MessageBox.display("File Not Found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeTags() {
        try {
            Main.subtitlesEdited = new File("sample.tempSubtitles.srt");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Main.subtitleFile), "Windows-1251"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Main.subtitlesEdited), "Windows-1251"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = removeTags(line);
                bufferedWriter.write(line + "\n");
            }
            bufferedReader.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (NullPointerException | FileNotFoundException e) {
            MessageBox.display("Open Subtitles File First");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String removeTags(String line) {
        return line.replaceAll(tagRegex.pattern(), "");
    }

    public void changeTimeIntervals() {
        try {
            int timeInterval = Integer.parseInt(milliSecondsField.getText());
        }catch (NumberFormatException e) {
            MessageBox.display("Change the milliseconds");
            e.printStackTrace();
        }
    }

    public void activateSpeedUp() {
        slowDownButton.selectedProperty().setValue(false);
    }

    public void activateSlowDown() {
        speedUpButton.selectedProperty().setValue(false);
    }
}
