package sample;

import javafx.application.Application;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.List;

import static org.apache.tools.ant.types.resources.MultiRootFileSet.SetType.file;

/**
 * Created by sian- on 16/03/2017.
 */
public  class ReadFiles  {

    private String filename;
    private FileWriter fileW;
    private BufferedReader input;
    private File txtFile;
    private String txtFilename;

    public void read(String txtFilename)  {
        ;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("View Reports");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {

            try {
                input = new BufferedReader(new FileReader(selectedFile));
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




