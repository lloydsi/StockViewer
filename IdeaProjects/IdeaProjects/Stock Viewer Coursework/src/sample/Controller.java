package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Controller {
    /*  Loads all the SceneBuilder items */
    @FXML
    private MenuItem menuOpenCSV;
    @FXML
    private MenuItem menuFileClose;
    @FXML
    private MenuItem menuAbout;
    @FXML
    private Menu menuHelp;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabOverview;
    @FXML
    private Tab tabDetailedView;
    @FXML
    private Tab tabGraphicalView;
    @FXML
    private ListView listViewCompany;
    @FXML
    private Label lblHighest;
    @FXML
    private Label lblLowest;
    @FXML
    private Label lblAverage;
    @FXML
    private Label lblLatestSharePrice;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private TableView<Company> tblLatestSharePrice;
    @FXML
    private TableColumn<Company, String> colCompanyName;
    @FXML
    private TableColumn<Company, String> colStockSymbol;
    @FXML
    private TableColumn<Company, BigDecimal> colLatestSharePrice;
    @FXML
    private TableView tblStockDetails;
    @FXML
    private TableColumn<Stock, String> colDate;
    @FXML
    private TableColumn<Stock, BigDecimal> colOpen;
    @FXML
    private TableColumn<Stock, BigDecimal> colHigh;
    @FXML
    private TableColumn<Stock, BigDecimal> colLow;
    @FXML
    private TableColumn<Stock, BigDecimal> colClose;
    @FXML
    private TableColumn<Stock, Double> colVolume;
    @FXML
    private TableColumn<Stock, BigDecimal> colAdjClose;
    @FXML
    private BarChart<String, BigDecimal> chartStockMonitoring;
    @FXML
    private Button btnLineChart;
    @FXML
    private Button btnLowest;
    @FXML
    private Button btnAverage;


    /* loads variables */
    private String filename;
    private String selectedFilename;
    private String[] stock;
    private BigDecimal latestSharePrice;
    private ObservableList<Company> selectedCompanyDetails;
    private ObservableList<Stock> selectedStockDetails;
    private ObservableList<Company> companyDetails;
    private ObservableList<String> companyNames;
    private ObservableList<Stock> stockDetails;
    private ObservableList<Company> companyTableArray;
    private Stock lowestStock;
    private Stock highestStock;
    private Stock latestStock;
    private DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private BigDecimal highest= (BigDecimal.valueOf(0.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
    private LocalDate highestDate;
    private BigDecimal lowest = (BigDecimal.valueOf(1000000.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
    private LocalDate lowestDate;
    private BigDecimal latestClosePrice;
    private BigDecimal total = (BigDecimal.valueOf(0.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
    private int count = 0;
    private BigDecimal average;
    private LocalDate latestDate;
    private LocalDate earliestDate;
    private String firstFilename;


    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    public void chooseFile(ActionEvent event) {
        /* choosing the csv file from file menu and getting path */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (int i = 0; i < selectedFiles.size(); i++) {    // gets the filenames and then takes 4 off the length of the name thereby removing the .csv bit
                int filenameLength = filename.length();
                String strippedFilename = filename.substring(0, filenameLength - 4);
                String stockSymbol = String.format("%s.L", strippedFilename);
                System.out.println(stockSymbol);
            }
        } else System.out.println("File is not valid");
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    public ObservableList<Company> companyDetails(ActionEvent event) {
        /* Reads the csv file containing all the company details and uses the details to fill the TableView */

        BufferedReader br = null;

        companyDetails = FXCollections.observableArrayList();
        companyNames = FXCollections.observableArrayList();

        String coName;
        String coStockSymbol;
        String coFilename;

        try {
            br = new BufferedReader(new FileReader("CompanyDetails.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;

        try {
            while ((line = br.readLine()) != null) {
                String[] co = line.split(",");

                coStockSymbol = co[0];
                coName = co[1];
                coFilename = co[2];

                Company company = new Company(coStockSymbol, coName, coFilename);
                // creates arrayLists
                companyNames.add(coName);
                companyDetails.add(company);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setCompanyDetails(companyDetails);
        this.completeTableLatestSharePrices(companyDetails);
        this.completeCompanyNamesList(companyNames);
        this.collectStockDetailsData(firstFilename);
        this.companyStockDetails(stockDetails);
        return companyDetails;

    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    public ObservableList<Company> setCompanyDetails(ObservableList<Company> companyDetails){
    /* Sets all the company variables*/

        companyTableArray = FXCollections.observableArrayList();

        for (Company info : companyDetails) {
            filename = info.getFilename();
            collectStockDetailsData(filename);
            System.out.println(filename);
            info.setFilename(filename);
            info.setHighestStockDate(highestDate);
            info.setHighestStockValue(highest);
            info.setLowestStockDate(lowestDate);
            info.setLowestStockValue(lowest);
            info.setLatestClosePrice(latestClosePrice);
            info.setLatestStockDate(latestDate);
            info.setAverageStock(average);
            info.setEarliestStockDate(earliestDate);
            this.btnForLineChart();
            companyTableArray.add(info);
            this.resetCoVariables();
        }


        return companyTableArray;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------
    public void resetCoVariables(){

        highest = BigDecimal.valueOf(0.0);
        lowest = BigDecimal.valueOf(100000.0);
        count = 0;
        total = BigDecimal.valueOf(0.0);
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------
    public void completeTableLatestSharePrices(ObservableList<Company> companyDetails) {
        // fills name and symbol columns
        for (Company companies : companyDetails) {
            colCompanyName.setCellValueFactory(
                    new PropertyValueFactory<Company, String>("name")
            );
            colStockSymbol.setCellValueFactory(
                    new PropertyValueFactory<Company, String>("stockSymbol")
            );
            colLatestSharePrice.setCellValueFactory(
                    new PropertyValueFactory<Company, BigDecimal>("latestClosePrice")
            );
            tblLatestSharePrice.setItems(companyDetails);

        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------

    public String completeCompanyNamesList(ObservableList<String> companyNames){
                  listViewCompany.setItems(companyNames);
                  listViewCompany.getSelectionModel().selectFirst();
                  firstFilename = companyDetails.get(0).getFilename();
                  return firstFilename;

    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    public String getSelectionListView(MouseEvent mouseClick) {
        /* gets the selection of Company name from the List View and then finds its file */
        resetCoVariables();
        Object Selected = listViewCompany.getSelectionModel().getSelectedItem();
        String selectedCoFileName = Selected.toString();
        int pos = companyNames.indexOf(selectedCoFileName);
        Company selectedCompany = companyDetails.get(pos);
        System.out.println("Filename is " + selectedCompany.getFilename());
        String selectedCoFilename = selectedCompany.getFilename();

        /* Creates stock files and fills the stock table */
        this.collectStockDetailsData(selectedCoFilename);
        return selectedCoFileName;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------

    private void collectStockDetailsData(String selectedFilename) {
        /* creates a new stock from a csv file */
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(selectedFilename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        stockDetails = FXCollections.observableArrayList();

        try {
            while ((line = br.readLine()) != null) {
                String[] sto = line.split(",");
                //int decimalsToConsider = 2;
                BigDecimal open1 = new BigDecimal((sto[1]));
                BigDecimal open = open1.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal high1 = new BigDecimal((sto[2]));
                BigDecimal high = high1.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal low1 = new BigDecimal((sto[3]));
                BigDecimal low = low1.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal close1 = new BigDecimal((sto[4]));
                BigDecimal close = close1.setScale(2, BigDecimal.ROUND_HALF_UP);
                Double volume = Double.parseDouble(sto[5]);
                BigDecimal adjClose1 = new BigDecimal((sto[6]));
                BigDecimal adjClose = adjClose1.setScale(2, BigDecimal.ROUND_HALF_UP);
                LocalDate date = LocalDate.parse(sto[0],df1);
                String dateStr = sto[0];

                Stock stock = new Stock(date, dateStr, open, high, low, close, volume, adjClose);
                createStockObject(stock);
                stockDetails.add(stock);
            }
            companyStockDetails(stockDetails);
            //resetCoVariables();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------

        public ObservableList<Stock> createStockObject(Stock stock){
                /*Creates the stock object*/

                //this.companyStockDetails(stockDetails);
                this.findHighest(stockDetails);  /// sends all stocks through a sorter to find highest etc
                this.findLowest(stockDetails);
                this.findLatest(stockDetails);
                this.findEarliest(stockDetails,latestDate);
                total = total.add(stock.getClose());
                count = count + 1;
                this.getAverage(stockDetails);
                return stockDetails;

            }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------

        public Stock findHighest(ObservableList<Stock>stockDetails) {
       /* Finds the highest stock from the stock item */
            for(Stock stockLine:stockDetails) {
                if ((stockLine.getHigh()).compareTo(highest) >0) {
                    Stock highestStock = stockLine;
                    highest = stockLine.getHigh();
                    highestDate = highestStock.getDate();
                    String lblText;
                    lblText = highest.toString() + " on " + df1.format(highestDate);
                    lblHighest.setText(lblText);

                }


            }

            return highestStock;
        }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------

        public Stock findLowest(ObservableList<Stock>stockDetails) {
        /*finds the lowest stock from the stock item*/
            for (Stock stockLine : stockDetails) {
                if ((stockLine.getLow()).compareTo(lowest) <0) {
                    Stock lowestStock = stockLine;
                    lowest = stockLine.getLow();
                    lowestDate = lowestStock.getDate();
                    String lblTextLow;
                    lblTextLow = lowest.toString() + " on " + df1.format(lowestDate);
                    lblLowest.setText(lblTextLow);
                }

            }

            return lowestStock;
        }

//------------------------------------------------------------------------------------------------------------------------------------------------------------

        public Stock findLatest(ObservableList<Stock>StockDetails) {
        /*finds the latest date from the stock items*/
            latestDate=LocalDate.parse("1900-01-01",df1);

            for (Stock stockLine : stockDetails) {
                if (stockLine.getDate().isAfter(latestDate)) {
                    latestDate = stockLine.getDate();
                    Stock latestStock = stockLine;
                    latestClosePrice = latestStock.getClose();
                   String latestDay = df1.format(latestDate);
                   lblLatestSharePrice.setText(latestClosePrice + " on " + latestDay);
                }
            }
            toDatePicker.setValue(latestDate);
            return latestStock;
            }

    //----------------------------------------------------------------------------------------------------------------------------------------------------

        public LocalDate findEarliest(ObservableList<Stock>stockDetails, LocalDate latestDate){

            for(Stock stockLine : stockDetails){
                if (stockLine.getDate().isBefore(latestDate)) {
                    earliestDate = stockLine.getDate();}
                }
            fromDatePicker.setValue(earliestDate);
            return earliestDate;
        }

    //---------------------------------------------------------------------------------------------------------------------------------------------------
        public void seeAbout (ActionEvent e){
            MessageBox aboutMsg = new MessageBox();
            aboutMsg.show("See latest stock prices.\r\nSee historical details. \r\nSee graphical details.","Stock Viewer 2017");
        }

    //---------------------------------------------------------------------------------------------------------------------------------------------------

        /*finds the average stock from closing amount using total counted above*/

       public BigDecimal getAverage(ObservableList<Stock>StockDetails){

       if (count != 0) {
            BigDecimal average1;
            average = total.divide(BigDecimal.valueOf(count), 2, RoundingMode.CEILING);
            //BigDecimal average = average1.setScale(2, BigDecimal.ROUND_HALF_UP);
            String lbltextAve;
            lbltextAve = String.valueOf(average);
            lblAverage.setText(lbltextAve);
        } else {
            System.out.println("Average cannot be calculated.");
        }
        return average;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------------------
    public void printReport(ActionEvent event){
            Report outReport = new Report(companyTableArray);
     }


    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    private void companyStockDetails(ObservableList<Stock> stockDetails){
    /* uses stock data to fill the stock details table */

        if (stockDetails != null) {

            colDate.setCellValueFactory(
                   new PropertyValueFactory<Stock, String>("dateStr")
           );
            colOpen.setCellValueFactory(
                    new PropertyValueFactory<Stock, BigDecimal>("open")
            );
            colHigh.setCellValueFactory(
                    new PropertyValueFactory<Stock, BigDecimal>("high")
            );
            colLow.setCellValueFactory(
                    new PropertyValueFactory<Stock, BigDecimal>("low")
            );
            colClose.setCellValueFactory(
                    new PropertyValueFactory<Stock , BigDecimal>("close")
            );
            colVolume.setCellValueFactory(
                    new PropertyValueFactory<Stock , Double>("volume")
            );
            colAdjClose.setCellValueFactory(
                    new PropertyValueFactory<Stock , BigDecimal>("adjClose")
            );
            tblStockDetails.setItems(stockDetails);
        }
        else {
            System.out.println("There is no data to display");
        }

            }


//-----------------------------------------------------------------------------------------------------------------------------------------------------------

        public void btnForLineChart(){
            XYChart.Series<String,BigDecimal> series = new XYChart.Series<String,BigDecimal>();
            chartStockMonitoring.getData().clear();
            if (companyTableArray != null && companyTableArray.size()!=0){
            for(Company co:companyTableArray) {
                series.getData().add(new XYChart.Data<String, BigDecimal>(co.getStockSymbol(), co.getAverageStock()));
            }
            chartStockMonitoring.getData().add(series);
            series.setName("Company Average Stock ");

        }}

  //----------------------------------------------------------------------------------------------------------------------------------------------------------

    public void btnForLineChartHighest(ActionEvent event){
        XYChart.Series<String,BigDecimal> series1 = new XYChart.Series<String,BigDecimal>();
        btnForLineChart();
        if (companyTableArray != null && companyTableArray.size()!=0){
        for(Company co:companyTableArray) {
            series1.getData().add(new XYChart.Data<String, BigDecimal>(co.getStockSymbol(), co.getHighestStockValue()));
        }
        chartStockMonitoring.getData().add(series1);
        series1.setName("Company Highest Stock ");

    }}
//----------------------------------------------------------------------------------------------------------------------------------------------------------
    public void btnForLineChartLowest(ActionEvent event){
        XYChart.Series<String,BigDecimal> series2 = new XYChart.Series<String,BigDecimal>();
        btnForLineChart();
        if (companyTableArray != null && companyTableArray.size()!=0){
        for(Company co:companyTableArray) {
            series2.getData().add(new XYChart.Data<String, BigDecimal>(co.getStockSymbol(), co.getLowestStockValue()));
        }
        chartStockMonitoring.getData().add(series2);
        series2.setName("Company Lowest Stock ");

    }}
//----------------------------------------------------------------------------------------------------------------------------------------------------------
    public void btnForLineChartAverage(ActionEvent event){
        XYChart.Series<String,BigDecimal> series2 = new XYChart.Series<String,BigDecimal>();
        btnForLineChart();

        }}

//-----------------------------------------------------------------------------------------------------------------------------------------------------------









