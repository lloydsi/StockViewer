package sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StringToDate {

    public StringToDate() {
      }
    private String date;
    private static Date newDate;

    public static Date  changeStringToDate(String date) {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern); //creates pattern
        try {
            Date newDate = format.parse(date);  // inputs string date in that pattern and converts to date

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;

    }

    public  String ToString(Date newDate){
        String stringDate;
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        stringDate = format.format(newDate);
        return stringDate;
        }
}


