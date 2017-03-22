package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import static java.util.Locale.UK;
import static java.text.DateFormat.SHORT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;

public class Controller {
 /*  Loads all the SceneBuilder items */
    @FXML private MenuItem menuOpenCSV;
    @FXML private MenuItem menuFileClose;
    @FXML private Menu menuHelp;
    @FXML private TabPane tabPane;
    @FXML private Tab tabOverview;
    @FXML private Tab tabDetailedView;
    @FXML private Tab tabGraphicalView;
    @FXML private ListView listViewCompany;
    @FXML private Label lblHighest;
    @FXML private Label lblLowest;
    @FXML private Label lblAverage;
    @FXML private DatePicker toDatePicker;
    @FXML private DatePicker fromDatePicker;
    @FXML private TableView<Company> tblLatestSharePrice;
    @FXML private TableColumn<Company, String> colCompanyName;
    @FXML private TableColumn<Company, String> colStockSymbol;
    @FXML private TableColumn<Company, String> colLatestSharePrice;
    @FXML private TableView tblStockDetails;
    @FXML private TableColumn<Stock, Date> colDate;
    @FXML private TableColumn<Stock, Double > colOpen;
    @FXML private TableColumn<Stock, Double > colHigh;
    @FXML private TableColumn<Stock, Double > colLow;
    @FXML private TableColumn<Stock, Double > colClose;
    @FXML private TableColumn<Stock, Double > colVolume;
    @FXML private TableColumn<Stock, Double > colAdjClose;

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



    public void chooseFile(ActionEvent event) {
        /* choosing the csv file from file menu and getting path */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Stock File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
           for(int i=0; i<selectedFiles.size();i++) {    // gets the filenames and then takes 4 off the length of the name thereby removing the .csv bit
               int filenameLength = filename.length();
               String strippedFilename = filename.substring(0,filenameLength-4);
               String stockSymbol = String.format("%s.L", strippedFilename);
                 System.out.println(stockSymbol);
                 }
        } else {
            System.out.println("File is not valid");
        }
    }

    public void companyDetails(ActionEvent event) {
        /* Reads the csv file containing all the company details and uses the details to fill the TableView */

        BufferedReader br = null;
        String coName;
        String coStockSymbol;
        String coFilename;
        try {
            br = new BufferedReader(new FileReader("CompanyDetails.csv"));
            String line;
            companyDetails = FXCollections.observableArrayList();
            companyNames = FXCollections.observableArrayList();
            while ((line = br.readLine()) != null) {
                String[] co = line.split(",");
                coStockSymbol = co[0];
                coName = co[1];
                coFilename = co[2];
                Company company = new Company( coStockSymbol, coName, coFilename);
                // creates arraylists
                companyNames.add(coName);
                companyDetails.add(company);
                }
            // fills name and symbol columns
            colCompanyName.setCellValueFactory(
                    new PropertyValueFactory<Company, String>("name")
            );
            colStockSymbol.setCellValueFactory(
                    new PropertyValueFactory<Company, String>("stockSymbol")
            );
            tblLatestSharePrice.setItems(companyDetails);
            listViewCompany.setItems(companyNames);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        finally{
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}

    public String getSelection(MouseEvent mouseclick){
        /* uses the selection from the Table View on Overview page and takes the filename */
        Company company = tblLatestSharePrice.getSelectionModel().getSelectedItem();
        String selectedFilename = company.getFilename();
        System.out.println(selectedFilename);
        this.collectStockDetailsData(selectedFilename);
        return selectedFilename;
   }

   public String getSelectionListView(MouseEvent mouseclick) {
        /* gets the selection of Company name from the List View and then finds its file */
       Object Selected = listViewCompany.getSelectionModel().getSelectedItem();
       String selectedCoFileName = Selected.toString();
       int pos = companyNames.indexOf(selectedCoFileName);
       Company selectedCompany = companyDetails.get(pos);
       System.out.println("Filename is " + selectedCompany.getFilename());
       String selectedCoFilename = selectedCompany.getFilename();
       this.collectStockDetailsData(selectedCoFilename);
       return selectedCoFileName;
   }

    public void UseListViewToFillStockDetailsTable(){}

    private ObservableList collectStockDetailsData(String selectedFilename) {
        /* creates a new stock from a csv file */
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(selectedFilename));
            br.readLine();
            String line;
            stockDetails = FXCollections.observableArrayList();
            Double highest = 0.0;
            Double lowest = 1000000.0;
            Double total = 0.0;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date latestDate = df.parse("1900-01-01");
            System.out.println("latest date is = " + latestDate);
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] sto = line.split(",");
                System.out.println(sto[0]);
                Date date = df.parse(sto[0]);
                System.out.println("df = " + date);
                System.out.println("Back to string " + df.format(date));
                double open = Double.parseDouble(sto[1]);
                double high = Double.parseDouble(sto[2]);
                double low = Double.parseDouble(sto[3]);
                double close = Double.parseDouble(sto[4]);
                double volume = Double.parseDouble(sto[5]);
                double adjClose = Double.parseDouble(sto[6]);

               /* Finds the highest stock from the stock item */
                if (high > highest) {
                    Stock highestStock = new Stock(date, open, high, low, close, volume, adjClose);
                    highest = high;
                    String lbltext;
                    lbltext = highest.toString() + " on " + df.format(highestStock.getDate(date));
                    lblHighest.setText(lbltext);

                }

                /*finds the lowest stock from the stock item*/
                if (low < lowest) {
                    Stock lowestStock = new Stock(date, open, high, low, close, volume, adjClose);
                    lowest = low;
                    String lbltextlow;
                    lbltextlow = lowest.toString() + " on " + df.format(lowestStock.getDate(date));
                    lblLowest.setText(lbltextlow);
                }

                /*finds the latest date from the stock items*/
                if (date.after(latestDate)){
                    latestDate = date;
                    Stock latestStock = new Stock(date,open,high,low,close,volume,adjClose);
                    Double latestSharePrice = latestStock.getClose();
                    String latestDay = df.format(latestDate);
                    System.out.println("Latest date is: " + latestDay + " and latest Share price is  " + latestSharePrice);
                }

                /*Creates the stock object*/
                Stock stock = new Stock(date, open, high, low, close, volume, adjClose);
                System.out.println("Stock is " + stock);

                /*Appends it to the observable list*/
                stockDetails.add(stock);
                total = total + close;
                count = count + 1;

            }

                /*finds the average stock from closing amount using total counted above*/
                Double average = total/count;
                long newAverage;
                newAverage = Math.round(average);
                String lbltextAve;
                lbltextAve = String.valueOf(newAverage);
                System.out.println("Average is:  "+average);
                lblAverage.setText(lbltextAve);

                /*Adds all the above stock details to the tableview by calling the following function*/
                this.companyStockDetails(stockDetails);
            }
        catch(IOException ex)
            {ex.printStackTrace();} catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stockDetails;
    }


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













