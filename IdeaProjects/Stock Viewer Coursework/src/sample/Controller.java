package sample;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;


public class Controller {

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
    /*@FXML
    private DatePicker toDatePicker;
    @FXML
    private DatePicker fromDatePicker;*/
    @FXML
    private TableView<Company> tblLatestSharePrice;
    @FXML
    private TableColumn<Company, String> colCompanyName;
    @FXML
    private TableColumn<Company, String> colStockSymbol;
    @FXML
    private TableColumn<Company, String> colLatestSharePrice;

    @FXML
    private TableView tblStockDetails;
    @FXML
    private TableColumn<Stock, String> colDate;
    @FXML
    private TableColumn<Stock, Double > colOpen;
    @FXML
    private TableColumn<Stock, Double > colHigh;
    @FXML
    private TableColumn<Stock, Double > colLow;
    @FXML
    private TableColumn<Stock, Double > colClose;
    @FXML
    private TableColumn<Stock, Double > colVolume;
    @FXML
    private TableColumn<Stock, Double > colAdjClose;


    private String filename;
    private String selectedFilename;
    private String[] stock;
    private ObservableList<Company> selectedCompanyDetails;
    private ObservableList<Stock> selectedStockDetails;
    public ObservableList<Company> companyDetails;
    public ObservableList<String> companyNames;



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

    public String getSelection(){
        /* uses the selection from the Table View on Overview page and takes the filename */
        Company company = tblLatestSharePrice.getSelectionModel().getSelectedItem();
        String selectedFilename = company.getFilename();
        System.out.println(selectedFilename);
        return selectedFilename;
   }

   public String getSelectionListView(MouseEvent mouseclick) {
        /* gets the selection of Company name from the List View */
       Object Selected = listViewCompany.getSelectionModel().getSelectedItem();
       String selectedName = Selected.toString();
       System.out.println(selectedName);
       return selectedName;
   }


    public ObservableList collectStockDetailsData() {
        /* creates a new stock from a csv file */
        ObservableList<Stock> stockDetails=null;
        BufferedReader br = null;
        try {
            String selectedFilename;
            selectedFilename = this.getSelection();
            br = new BufferedReader(new FileReader(selectedFilename));
            //System.out.println(selectedFilename);
            br.readLine();
            String line;
            stockDetails = FXCollections.observableArrayList();
            while ((line = br.readLine()) != null) {
                String[] sto = line.split(",");
                String linkedDate = sto[0];
                double open = Double.parseDouble(sto[1]);
                double high = Double.parseDouble(sto[2]);
                double low = Double.parseDouble(sto[3]);
                double volume = Double.parseDouble(sto[4]);
                double close = Double.parseDouble(sto[5]);
                double adjClose = Double.parseDouble(sto[6]);
                Stock stock = new Stock(linkedDate, open, high, low, close, volume, adjClose);
                stockDetails.add(stock);
                System.out.println(stockDetails);
            }}
        catch(IOException ex)
            {ex.printStackTrace();}
        finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stockDetails;
    }


    public void companyStockDetails(MouseEvent mouseclick){
        /* uses stock data to fill the stock details table */
        ObservableList<Stock> stockDetails;
        stockDetails = this.collectStockDetailsData();
        if (stockDetails != null) {
            colDate.setCellValueFactory(
                    new PropertyValueFactory<Stock, String>("linkedDate")
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
                    new PropertyValueFactory<Stock, Double>("volume")
            );
            colVolume.setCellValueFactory(
                    new PropertyValueFactory<Stock, Double>("close")
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













