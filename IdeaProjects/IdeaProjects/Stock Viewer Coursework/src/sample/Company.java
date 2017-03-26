package sample;


import groovyjarjarasm.asm.tree.TryCatchBlockNode;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

/**
 * Created by sian- on 11/03/2017.
 */
public class Company {
        private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        private int coNumber;
        private int counter=0;
        private String name;
        private String stockSymbol;
        private String filename;
        private BigDecimal latestClosePrice;
        private Date latestStockDate;
        private Date highestStockDate;
        private Date lowestStockDate;
        private BigDecimal AverageStock;
        private BigDecimal lowestStockValue;
        private BigDecimal highestStockValue;

        public Company()
        {   this.coNumber = counter;
            this.name = "";
            this.stockSymbol = "";
            this.filename="";
            this.counter = counter ++;
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

        public Company(String stockSymbol, String name, BigDecimal latestClosePrice) {
            this.stockSymbol = stockSymbol;
            this.name = name;
            this.latestClosePrice = latestClosePrice;

        }
         public Company (String filename)
         {
              this.filename = filename;
         }


        /*Getters and Setters*/
        public int getCoNumber(){ return coNumber;}
        public void setLatestClosePrice(BigDecimal latestClosePrice){
            this.latestClosePrice = latestClosePrice;
        }

        public BigDecimal getLatestClosePrice(){
            return latestClosePrice;
        }

        public void setHighestStockDate (Date highestStockDate){
            this.highestStockDate = highestStockDate;
        }

        public Date getHighestStockDate(){
            return highestStockDate;
        }

        public Date getLowestStockDate(){
            return lowestStockDate;
        }

        public void setLowestStockDate (Date lowestStockDate){
            this.lowestStockDate = lowestStockDate;
        }
        public void setLowestStockValue(BigDecimal lowestStockValue){
            this.lowestStockValue = lowestStockValue;
        }
        public BigDecimal getLowestStockValue(){
            return lowestStockValue;
        }
        public void setHighestStockValue(BigDecimal highestStockValue){
            this.highestStockValue = highestStockValue;
        }
        public BigDecimal getHighestStockValue(){
            return highestStockValue;
        }
        public void  setLatestSharePrice(BigDecimal latestSharePrice){
            this.latestClosePrice = latestSharePrice;
        }
        public BigDecimal getLatestSharePrice(){
            return latestClosePrice;
        }
        public void setLatestStockDate(Date latestStockDate){
            this.latestStockDate = latestStockDate;
        }
        public Date getLatestStockDate(){
            return latestStockDate;
        }

        public void setAverageStock(BigDecimal AverageStock){
            this.AverageStock = AverageStock;
        }
        public BigDecimal getAverageStock(){
            return AverageStock;
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
            return "\n\nCompany Number:  " + coNumber +"\nCompany Name: " + this.name + "\nStock Symbol: "+ this.stockSymbol +
                    "\nHighest:  " + this.highestStockValue + "\nDate of Highest:  " + df.format(this.highestStockDate) +
                "\nLowest:  " + this.lowestStockValue + "\nDate of Lowest:  " + df.format(this.lowestStockDate) + "\nAverage Close:  " + this.AverageStock +"" +
                "\nClose:  " + this.latestClosePrice +".";
        }
    }



