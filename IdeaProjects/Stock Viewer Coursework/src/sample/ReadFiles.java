package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * Created by sian- on 16/03/2017.
 */
public class ReadFiles {

    private ArrayList<Stock> stockList;
    private File selectedFilename;

    public void readFile(File selectedFilename) {
        // using the selectedFile and reading line by line assigning variables linked to stock
        try {
            Scanner readIn = new Scanner(new BufferedReader(new FileReader(selectedFilename)));
            while (readIn.hasNextLine()) {
                String line = readIn.nextLine();
                String[] data = line.split(",");
                String  date = data[0];
                double open = Double.parseDouble(data[1]);
                double high = Double.parseDouble(data[2]);
                double low = Double.parseDouble(data[3]);
                double close = Double.parseDouble(data[4]);
                double volume = Double.parseDouble(data[5]);
                double adjClose = Double.parseDouble(data[6]);
                Stock stockRecord = new Stock(date, open, high, low, close, volume, adjClose);
                stockList.add(stockRecord);
                System.out.println(stockList);
            }
        }
        catch(FileNotFoundException filename){
            System.out.println("Error in reading file");

        }
    }
}
