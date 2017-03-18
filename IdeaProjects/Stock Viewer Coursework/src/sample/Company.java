package sample;


import groovyjarjarasm.asm.tree.TryCatchBlockNode;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by sian- on 11/03/2017.
 */
public class Company {
        private String name;
        private String stockSymbol;
        private String filename;
        private String line;

        public Company()
        {
            this.name = "";
            this.stockSymbol = "";
            this.filename="";
        }

        public Company(String name, String stockSymbol)
        {
            this.name = name;
            this.stockSymbol = stockSymbol;
        }

        public Company(String stockSymbol,String name, String filename)
        {    this.stockSymbol = stockSymbol;
             this.name = name;
             this.filename = filename;
        }

        public Company (String filename){
            this.filename=filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getFilename(){
                return this.filename;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }

        public void setStockSymbol(String stockSymbol)
        {
            this.stockSymbol = stockSymbol;
        }

        public String getStockSymbol()
        {
            return this.stockSymbol;
        }

        public void getFilenameFromStockSymbol(String stockSymbol){
            int symbolLength = stockSymbol.length();
            String strippedSymbol = stockSymbol.substring(0,symbolLength-2);
            String filename = strippedSymbol + ".csv";
            this.setFilename(filename);
        }

        public  void getStockSymbolFromFilename(String filename){
            int filenameLength = filename.length();
            String strippedFilename = filename.substring(0,filenameLength-4);
            String stockSymbol = String.format("%s.L", strippedFilename);
            this.setStockSymbol(stockSymbol);
        }





        @Override
        public String toString()
        {
            return ("\n\nCompany Name: " + this.name + "\nStock Symbol: "+ this.stockSymbol);
        }
    }



