package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by sian- on 23/03/2017.
 */
public class Report extends Company {
   ObservableList<Company> company;


    public Report(ObservableList<Company> company){
        this.company=company;
        System.out.println(
                "COMPANY REPORTS "+
                "\n============================================================="+
                "\n" +company);}

    }



