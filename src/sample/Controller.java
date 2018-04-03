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
import java.util.Scanner;
import java.util.regex.Pattern;

import static sample.Main.primaryStage;


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
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

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
            Desktop desktop = Desktop.getDesktop();
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
        File dataFile = null;
        try {
            File chosenLocation = new File(nameField.getText());

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save As");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("SRT file (*.srt)", ".srt")
            );


            String line = null;
            Main.subtitlesEdited = new File("sample.tempSubtitles.srt");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Main.subtitlesEdited), "Windows-1251"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(chosenLocation), "Windows-1251"));
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Main.subtitlesEdited), "Windows-1251"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(chosenLocation), "Windows-1251"));
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
            }

            //needs a fix
            File userRelated = fileChooser.showSaveDialog(Main.primaryStage);

            if (userRelated != null) {
                try (Scanner scanner = new Scanner(userRelated)) {
                    String content = scanner.useDelimiter("\\Z").next();
                    dataFile = userRelated;
                    saveChangesButton.setDisable(false);
                }
            }


        } catch (UnsupportedEncodingException e) {
            MessageBox.display("Your system does not support Windows-1251 encoding");
            e.printStackTrace();
        } catch (NullPointerException | FileNotFoundException e) {
            MessageBox.display("File Not Found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                bufferedWriter.flush();
                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
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
            Pattern tagRegex = Pattern.compile("<[^>]*>");
            while ((line = bufferedReader.readLine()) != null) {
                line = removeTags(line, tagRegex);
                bufferedWriter.write(line + "\n");
            }

        } catch (NullPointerException | FileNotFoundException e) {
            MessageBox.display("Open Subtitles File First");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                bufferedReader.close();
                bufferedWriter.flush();
                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String removeTags(String line, Pattern tagRegex) {
        return line.replaceAll(tagRegex.pattern(), "");
    }

    public void changeTimeIntervals() {
        String line = null;

        try {
            int timeInterval = Integer.parseInt(milliSecondsField.getText());
            Main.subtitlesEdited = new File("sample.tempSubtitles.srt");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Main.subtitleFile), "Windows-1251"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Main.subtitlesEdited), "Windows-1251"));
            line = bufferedReader.readLine();
            while (line != null) {
                if (line.contains("-->")) {
                    String[] timeIntervals = line.split(" --> ");
                    String[] startingTimes = timeIntervals[0].split(":");
                    String[] endingTimes = timeIntervals[1].split(":");
                    startingTimes[2] = removeTheComma(startingTimes[2]);
                    endingTimes[2] = removeTheComma(endingTimes[2]);
                    long startingTimeMilliSec = 0;
                    long endingTimeMilliSec = 0;
                    for (int i = 0; i < 3; i++) {
                        switch (i) {
                            case 0:
                                startingTimeMilliSec += Integer.parseInt(startingTimes[i]) * 3600000;
                                endingTimeMilliSec += Integer.parseInt(endingTimes[i]) * 3600000;
                                break;
                            case 1:

                                startingTimeMilliSec += Integer.parseInt(startingTimes[i]) * 60000;
                                endingTimeMilliSec += Integer.parseInt(endingTimes[i]) * 60000;
                                break;
                            case 2:
                                startingTimeMilliSec += Integer.parseInt(startingTimes[i]);
                                endingTimeMilliSec += Integer.parseInt(endingTimes[i]);
                                break;
                        }
                    }

                    long startingTimeEdited;
                    long endingTimeEdited;

                    if (slowDownButton.selectedProperty().getValue()) {
                        startingTimeEdited = startingTimeMilliSec - timeInterval;
                        endingTimeEdited = endingTimeMilliSec - timeInterval;
                        if (startingTimeEdited < 0) {
                            startingTimeEdited = 0;
                            if (endingTimeEdited < 0) {
                                endingTimeEdited = 0;
                            }
                        }
                    } else if (speedUpButton.selectedProperty().getValue()) {
                        startingTimeEdited = startingTimeMilliSec + timeInterval;
                        endingTimeEdited = endingTimeMilliSec + timeInterval;
                    } else {
                        startingTimeEdited = startingTimeMilliSec;
                        endingTimeEdited = endingTimeMilliSec;
                    }

                    long millisecondsOfStartingEditedTime = startingTimeEdited % 1000;
                    long secondsOfStartingEditedTime = (startingTimeEdited / 1000) % 60;
                    long minutesOfStartingEditedTime = (startingTimeEdited / (1000 * 60)) % 60;
                    long hoursOfStartingEditedTime = (startingTimeEdited / (1000 * 60 * 60)) % 24;

                    long millisecondsOfEndingEditedTime = endingTimeEdited % 1000;
                    long secondsOfEndingEditedTime = (endingTimeEdited / 1000) % 60;
                    long minutesOfEndingEditedTime = (endingTimeEdited / (1000 * 60)) % 60;
                    long hoursOfEndingEditedTime = (endingTimeEdited / (1000 * 60 * 60)) % 24;

                    String editedStartingTime = String.format("%02d:%02d:%02d,%03d", hoursOfStartingEditedTime,
                            minutesOfStartingEditedTime, secondsOfStartingEditedTime, millisecondsOfStartingEditedTime);
                    String editedEndingTime = String.format("%02d:%02d:%02d,%03d", hoursOfEndingEditedTime,
                            minutesOfEndingEditedTime, secondsOfEndingEditedTime, millisecondsOfEndingEditedTime);
                    bufferedWriter.write(editedStartingTime + " --> " + editedEndingTime + "\n");
                } else {
                    bufferedWriter.write(line + "\n");
                }
                line = bufferedReader.readLine();
            }
        } catch (NumberFormatException e) {
            MessageBox.display("Change the milliseconds");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            MessageBox.display("Your system does not support Windows-1251 encoding");
            e.printStackTrace();
        } catch (NullPointerException | FileNotFoundException e) {
            MessageBox.display("File Not Found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String removeTheComma(String str) {
        return str.replace(",", "");
    }

    public void activateSpeedUp() {
        slowDownButton.selectedProperty().setValue(false);
    }

    public void activateSlowDown() {
        speedUpButton.selectedProperty().setValue(false);
    }
}
