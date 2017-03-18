package sample;
import java.io.File;

/**
 * Created by sian- on 11/03/2017.
 */
public class Stock extends Company {

        private String date;
        private double open;
        private double high;
        private double low;
        private double close;
        private double volume;
        private double adjClose;


        public Stock() {
            this.date = "";
            this.open = 0.0;
            this.high = 0.0;
            this.low = 0.0;
            this.close = 0.0;
            this.volume = 0.0;
            this.adjClose = 0.0;
        }
        public Stock(String date, double open, double high, double low, double close, double volume, double adjClose){
            this.date = date;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
            this.adjClose = adjClose;
        }

    public void setDate(String date)
        {
            this.date = date;
        }
        public String getDate(String date)
        {
            return this.date;
        }
        public void setOpen(double open)
        {
            this.open = open;
        }
        public double getOpen()
        {
            return this.open;
        }
        public void setHigh(double high)
        {
            this.high = high;
        }
        public double getHigh()
        {
            return this.high;
        }
        public void setLow(double low)
        {
            this.low = low;
        }
        public double getLow()
        {
            return this.low;
        }
        public void setClose(double close)
        {
            this.close = close;
        }
        public double getClose()
        {
            return this.close;
        }
        public void setVolume(double volume)
        {
            this.volume = volume;
        }
        public double getVolume()
        {
            return this.volume;
        }
        public void setAdjClose(double AdjClose)
        {
            this.adjClose = adjClose;
        }

        public double getAdjClose()
        {
            return this.adjClose;
        }
        @Override
        public String toString(){
            return (
                    "\nDate:      " + date +
                            "\nOpen:      " + open +
                            "\nHigh:      " + high +
                            "\nLow:       " + low +
                            "\nClose:     " + close +
                            "\nVolume:    " + volume +
                            "\nAdj Close: " + adjClose);


        }

    }

