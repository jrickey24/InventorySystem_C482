package Controllers;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.Optional;
import java.net.URL;
import java.util.ResourceBundle;


public class ModifyProductController implements Initializable {

    Inventory stock;
    Product productToModify;
    private double productCostMin = 0.00;
    @FXML private TextField productIDField;
    @FXML private TextField productNameField;
    @FXML private TextField productInvQtyField;
    @FXML private TextField productPriceField;
    @FXML private TextField productMinField;
    @FXML private TextField productMaxField;
    @FXML private TextField modifyProductSearchField;
    @FXML private TableView<Part> searchPartsTableView;
    @FXML private TableView<Part> associatedPartsTableView;
    private ObservableList<Part> allAvailableParts = FXCollections.observableArrayList();
    private ObservableList<Part> matchingAssociatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> associatedPartsToShow = FXCollections.observableArrayList();

    public ModifyProductController(Inventory stock, Product productToModify) {
        this.stock = stock;
        this.productToModify = productToModify;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSearchPartsTableView();
        setProductToModify();
        // @param location
        // @param resources
    }

    private void setSearchPartsTableView() {
        allAvailableParts.setAll(stock.getAllParts());
        searchPartsTableView.setItems(allAvailableParts);
        searchPartsTableView.refresh();
    }

    public void setProductToModify() {
        productIDField.setText(Integer.toString(productToModify.getProductID()));
        productNameField.setText(productToModify.getProductName());
        productInvQtyField.setText(Integer.toString(productToModify.getProductInStock()));
        productMinField.setText(Integer.toString(productToModify.getProductMinStock()));
        productMaxField.setText(Integer.toString(productToModify.getProductMaxStock()));
        productPriceField.setText(Double.toString(productToModify.getProductPrice()));
        associatedPartsToShow.clear();
        associatedPartsToShow.setAll(productToModify.getAssociatedParts());
        associatedPartsTableView.setItems(associatedPartsToShow);
        associatedPartsTableView.refresh();
    }

    public void returnToMainScreen(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainScreen.fxml"));
        MainScreenController controller = new MainScreenController(stock);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleSearchAssociatedParts(ActionEvent actionEvent) {
        matchingAssociatedParts.clear();
        String userInputSearchPartToAssociate = modifyProductSearchField.getText();
        matchingAssociatedParts = stock.lookupPartName(userInputSearchPartToAssociate);
        if (matchingAssociatedParts.size() > 0) {
            searchPartsTableView.setItems(matchingAssociatedParts);
            searchPartsTableView.refresh();
        }
        else {
            int userInputAssociatedPartID = Integer.parseInt(modifyProductSearchField.getText());
            matchingAssociatedParts.add(stock.lookupPartID(userInputAssociatedPartID));
            if (matchingAssociatedParts.size() > 0) {
                searchPartsTableView.setItems(matchingAssociatedParts);
                searchPartsTableView.refresh();
            }
        }
    }

    @FXML
    private void handleAddAssociatedPart(ActionEvent actionEvent) throws IOException {
        if(searchPartsTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please Select a Part To Associate with the Product");
            alert.showAndWait();
        } else {
            Part partToAssociate = searchPartsTableView.getSelectionModel().getSelectedItem();
            associatedPartsToShow.add(partToAssociate);
            associatedPartsTableView.setItems(associatedPartsToShow);
            associatedPartsTableView.refresh();
        }
    }

    @FXML
    private void handleDeleteAssociatedPart(ActionEvent actionEvent) {
        if(associatedPartsTableView.getSelectionModel().getSelectedItems().isEmpty() && associatedPartsToShow.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("Do You Want To Remove All Currently Associated Parts From The Product?");
            alert.setContentText("Click 'OK' to Confirm");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedPartsToShow.clear();
                associatedPartsTableView.refresh();
            }
        } else {
            Part removeAssociatedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("Do You Want To Remove " +removeAssociatedPart.getPartName()+ " From The Product?");
            alert.setContentText("Click 'OK' to Confirm");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedPartsToShow.remove(removeAssociatedPart);
                associatedPartsTableView.setItems(associatedPartsToShow);
                associatedPartsTableView.refresh();
            }
        }
    }

    @FXML
    private void handleSaveModifyProduct(ActionEvent actionEvent) throws IOException {
        boolean isModifyValid = true;
        if(associatedPartsToShow.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Modifying Product");
            alert.setContentText("All Products Must Have A Minimum of 1 Associated Part");
            alert.showAndWait();
            isModifyValid = false;
        }
        String userInputProductName = productNameField.getText();
        String userInputProductInv = productInvQtyField.getText();
        String userInputProductPrice = productPriceField.getText();
        String userInputProductMin = productMinField.getText();
        String userInputProductMax = productMaxField.getText();

        if (userInputProductName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Modifying Product");
            alert.setContentText("Please Provide Product Name");
            alert.showAndWait();
            isModifyValid = false;
        }
        try {
            Integer.parseInt(userInputProductInv);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("Invalid Product Inventory Quantity Provided. Product Inventory Quantity Must be an Integer Value");
            alert.setContentText("Click 'OK' to Set the Product's Inventory Quantity to 1 or 'Cancel' to Provide a Custom Value");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                productInvQtyField.setText("1");
                userInputProductInv = "1";
            } else {
                isModifyValid = false;
            }
        }
        try {
            Integer.parseInt(userInputProductMin);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("Invalid Product Minimum Quantity Provided. Product Minimum Quantity Must be an Integer Value");
            alert.setContentText("Click 'OK' to Set the Minimum Quantity to 0 or 'Cancel' to Provide a Custom Value");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                productMinField.setText("0");
                userInputProductMin = "0";
            } else {
                isModifyValid = false;
            }
        }
        try {
            Integer.parseInt(userInputProductMax);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Maximum Quantity Provided.");
            alert.setContentText("Product Maximum Quantity Must be an Integer Value Greater Than the Minimum Quantity.");
            alert.showAndWait();
            isModifyValid = false;
        }
        try {
            Double.parseDouble(userInputProductPrice);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Price Provided");
            alert.setContentText("Product Price Must be Provided in Decimal Format EX:'1.50'");
            alert.showAndWait();
            isModifyValid = false;
        }
        for (Part p : associatedPartsToShow) {
            productCostMin = productCostMin + p.getPartPrice();
        }
        System.out.println(productCostMin);
        if (Double.parseDouble(userInputProductPrice) < productCostMin) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Price Provided");
            alert.setContentText("Product Price Cannot Be Less Than The Sum Of Its Associated Parts");
            alert.showAndWait();
            isModifyValid = false;
            productCostMin = 0.00;
        }
        if (Integer.parseInt(userInputProductInv) > Integer.parseInt(userInputProductMax)
                || Integer.parseInt(userInputProductMin) > Integer.parseInt(userInputProductMax)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Inventory Quantity Range Detected");
            alert.setContentText("The Product Inventory Quantity & Product Minimum Quantity Cannot Be Greater Than the Maximum Quantity Value");
            alert.showAndWait();
            isModifyValid = false;
        }
        if (Integer.parseInt(userInputProductInv) < Integer.parseInt(userInputProductMin)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Inventory Quantity Range Detected");
            alert.setContentText("The Product Inventory Quantity Cannot Be Less Than The Product Minimum Quantity Value");
            alert.showAndWait();
            isModifyValid = false;
        }
        if (isModifyValid) {
            Product productToModify = new Product();
            for (Part p : associatedPartsToShow) {
                productToModify.addAssociatedPart(p);
            }
            productToModify.setProductID(Integer.parseInt(productIDField.getText()));
            productToModify.setProductName(userInputProductName);
            productToModify.setProductInStock(Integer.parseInt(userInputProductInv));
            productToModify.setProductPrice(Double.parseDouble(userInputProductPrice));
            productToModify.setProductMinStock(Integer.parseInt(userInputProductMin));
            productToModify.setProductMaxStock(Integer.parseInt(userInputProductMax));
            stock.updateProduct(productToModify);
            returnToMainScreen(actionEvent);
        }
    }

    @FXML
    private void handleCancelModifyProduct(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("Do You Want to Cancel Modifying This Product?");
        alert.setContentText("Click 'OK' to Confirm & Return to the Main Menu");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToMainScreen(actionEvent);
        }
    }
}