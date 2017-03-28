package sample;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by sian- on 11/03/2017.
 */
public class Stock extends Company {

        private LocalDate date;
        private String dateStr;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private Double  volume;
        private BigDecimal adjClose;


        public Stock() {
            this.date = null;
            this.dateStr = "";
            this.open = BigDecimal.valueOf(0.00);
            this.high = BigDecimal.valueOf(0.00);
            this.low = BigDecimal.valueOf(0.00);
            this.close = BigDecimal.valueOf(0.00);
            this.volume = 00.00;
            this.adjClose = BigDecimal.valueOf(0.00);
        }
        public Stock(LocalDate date, String dateStr, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Double volume, BigDecimal adjClose){
            this.date = date;
            this.dateStr = dateStr;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
            this.adjClose = adjClose;
        }
        //--------------------------------------------------------------------------------------------------
        public void setDate(LocalDate date)
        {
            this.date = date;
        }
        public LocalDate getDate()
        {
            return this.date;
        }
        //--------------------------------------------------------------------------------------------------
        public void setDateStr(String dateStr)
        {
            this.dateStr = dateStr;
        }
        public String getDateStr()
        {
            return this.dateStr;
        }
        //--------------------------------------------------------------------------------------------------
        public void setOpen(BigDecimal open)
        {
            this.open = open;
        }
        public BigDecimal getOpen()
        {
            return this.open;
        }
        //-------------------------------------------------------------------------------------------------
        public void setHigh(BigDecimal high)
        {
            this.high = high;
        }
        public BigDecimal getHigh()
        {
            return this.high;
        }
        //-------------------------------------------------------------------------------------------------
        public void setLow(BigDecimal low)
        {
            this.low = low;
        }
        public BigDecimal getLow()
        {
            return this.low;
        }
        //-------------------------------------------------------------------------------------------------
        public void setClose(BigDecimal close)
        {
            this.close = close;
        }
        public BigDecimal getClose()
        {
            return this.close;
        }
        //-------------------------------------------------------------------------------------------------
        public void setVolume(Double volume)
        {
            this.volume = volume;
        }
        public Double getVolume()
        {
            return this.volume;
        }
        //--------------------------------------------------------------------------------------------------
        public void setAdjClose(BigDecimal AdjClose)
        {
            this.adjClose = adjClose;
        }
        public BigDecimal getAdjClose()
        {
            return this.adjClose;
        }
        //--------------------------------------------------------------------------------------------------

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

