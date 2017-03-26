package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.math.BigDecimal;
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
                String[] sto = line.split(",");
                Date date = StringToDate.changeStringToDate(sto[0]);
                String dateStr = sto[0];
                BigDecimal open = new BigDecimal((sto[1]));
                BigDecimal open2 = open.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal high = new BigDecimal((sto[2]));
                BigDecimal high2 = high.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal low = new BigDecimal((sto[3]));
                BigDecimal low2 = low.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal close = new BigDecimal((sto[4]));
                BigDecimal close2 = high.setScale(2, BigDecimal.ROUND_HALF_UP);
                Double volume = Double.parseDouble(sto[5]);
                BigDecimal adjClose = new BigDecimal((sto[6]));
                BigDecimal adjClose2 = high.setScale(2, BigDecimal.ROUND_HALF_UP);
                Stock stockRecord = new Stock(date, dateStr, open, high, low, close, volume, adjClose);
                stockList.add(stockRecord);
                System.out.println(stockList);
            }
        }
        catch(FileNotFoundException filename){
            System.out.println("Error in reading file");

        }
    }
}
