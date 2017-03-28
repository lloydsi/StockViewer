package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.apache.tools.ant.types.resources.MultiRootFileSet.SetType.file;

/**
 * Created by sian- on 23/03/2017.
 */
public class Report extends Company {
    ObservableList<Company> company;
    FileWriter fileW;
    BufferedWriter output;
    private DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public Report(ObservableList<Company> company)  {
        this.company = company;
        int count = 1;
        try {
            File reportFile = new File("latestReport.txt");
            if (!reportFile.exists()) {
                reportFile.createNewFile();}
                FileWriter fileW = new FileWriter(reportFile);
                output = new BufferedWriter(fileW);
                output.write("Report on Latest Stock Prices.  Produced:  "+df1.format(LocalDate.now())+"\r\n");
                for (Company companies : company) {
                    output.write("\r\n\r\n=====================================================\r\n" +
                            "\r\n          COMPANY REPORT FOR COMPANY " + count + "\r\n" +
                            "\r\n=====================================================\r\n" +
                            "" + companies);
                    count++;

                }output.close();

            }
            catch (IOException e) {
            e.printStackTrace();
            System.out.println("You have an IOException");
        }
        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "latestReport.txt");
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }



