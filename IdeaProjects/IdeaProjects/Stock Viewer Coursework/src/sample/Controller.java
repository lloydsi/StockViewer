package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.scene.chart.LineChart;

public class Controller {
    /*  Loads all the SceneBuilder items */
    @FXML
    private MenuItem menuOpenCSV;
    @FXML
    private MenuItem menuFileClose;
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
    private TableColumn<Company, Double> colLatestSharePrice;
    @FXML
    private TableView tblStockDetails;
    @FXML
    private TableColumn<Stock, Date> colDate;
    @FXML
    private TableColumn<Stock, Double> colOpen;
    @FXML
    private TableColumn<Stock, Double> colHigh;
    @FXML
    private TableColumn<Stock, Double> colLow;
    @FXML
    private TableColumn<Stock, Double> colClose;
    @FXML
    private TableColumn<Stock, Double> colVolume;
    @FXML
    private TableColumn<Stock, Double> colAdjClose;

    /* loads variables */
    private String filename;
    private String selectedFilename;
    private String[] stock;
    private Double latestSharePrice;
    private ObservableList<Company> selectedCompanyDetails;
    private ObservableList<Stock> selectedStockDetails;
    private ObservableList<Company> companyDetails;
    private ObservableList<String> companyNames;
    private ObservableList<Stock> stockDetails;
    private ObservableList<Company> companyTableArray;
    private Stock lowestStock;
    private Stock highestStock;
    private Stock latestStock;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Double highest= 0.0;
    private Date highestDate;
    private Double lowest = 1000000.0;
    private Date lowestDate;
    private Double latestClosePrice;
    private Double total = 0.0;
    private int count = 0;
    private Double average;
    private Date latestDate;


    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    public void chooseFile(ActionEvent event) {
        /* choosing the csv file from file menu and getting path */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Stock File");
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
        String coName;
        String coStockSymbol;
        String coFilename;
            try {
                br = new BufferedReader(new FileReader("CompanyDetails.csv"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line;
            companyDetails = FXCollections.observableArrayList();
            companyNames = FXCollections.observableArrayList();
            companyTableArray = FXCollections.observableArrayList();
            try {
                while ((line = br.readLine()) != null) {
                    String[] co = line.split(",");
                    coStockSymbol = co[0];
                    coName = co[1];
                    coFilename = co[2];
                    Company company = new Company(coStockSymbol, coName, coFilename);
                    // creates arraylists
                    companyNames.add(coName);
                    companyDetails.add(company);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        for (Company info : companyDetails) {
            filename = info.getFilename();
            collectStockDetailsData(filename);
            System.out.println(filename);
            info.setHighestStockDate(highestDate);
            info.setHighestStockValue(highest);
            info.setLowestStockDate(lowestDate);
            info.setLowestStockValue(lowest);
            info.setLatestClosePrice(latestClosePrice);
            info.setLatestStockDate(latestDate);
            info.setAverageStock(average);
            highest = 0.0;
            lowest = 100000.0;
            count = 0;
            total = 0.0;
            companyTableArray.add(info);
         }


        // fills name and symbol columns
        colCompanyName.setCellValueFactory(
                new PropertyValueFactory<Company, String>("name")
        );
        colStockSymbol.setCellValueFactory(
                new PropertyValueFactory<Company, String>("stockSymbol")
        );
        colLatestSharePrice.setCellValueFactory(
                new PropertyValueFactory<Company, Double>("latestClosePrice")
        );
        tblLatestSharePrice.setItems(companyDetails);
        listViewCompany.setItems(companyNames);
        return companyTableArray;
    }


    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    public String getSelectionListView(MouseEvent mouseClick) {
        /* gets the selection of Company name from the List View and then finds its file */
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

    private ObservableList collectStockDetailsData(String selectedFilename) {
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

                Date date = df.parse(sto[0]);
                double open = Double.parseDouble(sto[1]);
                double high = Double.parseDouble(sto[2]);
                double low = Double.parseDouble(sto[3]);
                double close = Double.parseDouble(sto[4]);
                double volume = Double.parseDouble(sto[5]);
                double adjClose = Double.parseDouble(sto[6]);

                /*Creates the stock object*/
                Stock stock = new Stock(date, open, high, low, close, volume, adjClose);
                stockDetails.add(stock);
                this.companyStockDetails(stockDetails);
                this.findHighest(stockDetails);
                this.findLowest(stockDetails);
                this.findLatest(stockDetails);
                total = total + stock.getClose();
                count = count + 1;
                this.getAverage(stockDetails);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return stockDetails;

    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------

        public Stock findHighest(ObservableList<Stock>stockDetails) {
       /* Finds the highest stock from the stock item */
            for(Stock stockLine:stockDetails) {
                if (stockLine.getHigh() > highest) {
                    Stock highestStock = stockLine;
                    highest = stockLine.getHigh();
                    highestDate = highestStock.getDate();
                    String lblText;
                    lblText = highest.toString() + " on " + df.format(highestDate);
                    lblHighest.setText(lblText);
                }

            }

            return highestStock;
        }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------

        public Stock findLowest(ObservableList<Stock>stockDetails) {
        /*finds the lowest stock from the stock item*/
            for (Stock stockLine : stockDetails) {
                if (stockLine.getLow() < lowest) {
                    Stock lowestStock = stockLine;
                    lowest = stockLine.getLow();
                    lowestDate = lowestStock.getDate();
                    String lblTextLow;
                    lblTextLow = lowest.toString() + " on " + df.format(lowestDate);
                    lblLowest.setText(lblTextLow);
                }
            }

            return lowestStock;
        }

//------------------------------------------------------------------------------------------------------------------------------------------------------------

        public Stock findLatest(ObservableList<Stock>StockDetails) {
        /*finds the latest date from the stock items*/
            try {
                latestDate=df.parse("1900-01-01");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (Stock stockLine : stockDetails) {
                if (stockLine.getDate().after(latestDate)) {
                    latestDate = stockLine.getDate();
                    Stock latestStock = stockLine;
                    latestClosePrice = latestStock.getClose();
                    String latestDay = df.format(latestDate);
                    lblLatestSharePrice.setText(latestClosePrice + " on " + latestDay);
                }
            }
            try {
                latestDate=df.parse("1900-01-01");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return latestStock;
            }

    //---------------------------------------------------------------------------------------------------------------------------------------------------

        /*finds the average stock from closing amount using total counted above*/

       public double getAverage(ObservableList<Stock>StockDetails){

       if (count != 0) {
            average = total / count;
            Long newAverage = Math.round(average);
            String lbltextAve;
            lbltextAve = String.valueOf(newAverage);
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
    //ObservableList<Stock> stockDetails;
    //stockDetails = this.collectStockDetailsData();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        if (stockDetails != null) {
           colDate.setCellValueFactory(
                   new PropertyValueFactory<Stock, Date>("date")
           );
            colOpen.setCellValueFactory(
                    new PropertyValueFactory<Stock, Double>("open")
            );
            colHigh.setCellValueFactory(
                    new PropertyValueFactory<Stock, Double>("high")
            );
            colLow.setCellValueFactory(
                    new PropertyValueFactory<Stock, Double>("low")
            );
            colClose.setCellValueFactory(
                    new PropertyValueFactory<Stock, Double>("close")
            );
            colVolume.setCellValueFactory(
                    new PropertyValueFactory<Stock, Double>("volume")
            );
            colAdjClose.setCellValueFactory(
                    new PropertyValueFactory<Stock, Double>("adjClose")
            );
            tblStockDetails.setItems(stockDetails);
        }
        else {
            System.out.println("There is no data to display");
        }

            }}

//-----------------------------------------------------------------------------------------------------------------------------------------------------------











