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

public class AddProductController implements Initializable {

    Inventory stock;
    private int assignProductID = 1;
    private double productCostMin = 0.00;
    @FXML private TextField productIDField;
    @FXML private TextField productNameField;
    @FXML private TextField productInvQtyField;
    @FXML private TextField productPriceField;
    @FXML private TextField productMinField;
    @FXML private TextField productMaxField;
    @FXML private TextField addProductSearchField;
    @FXML private TableView<Part> existingPartsTableView;
    @FXML private TableView<Part> associatedPartsTableView;
    private ObservableList<Part> allAvailableParts = FXCollections.observableArrayList();
    private ObservableList<Part> matchingAssociatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> associatedPartsToShow = FXCollections.observableArrayList();

    public AddProductController(Inventory stock) {
        this.stock = stock;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setExistingPartsTableView();
        // @param location
        // @param resource
    }

    Product productToAdd = new Product();

    private void setExistingPartsTableView() {
        allAvailableParts.setAll(stock.getAllParts());
        existingPartsTableView.setItems(allAvailableParts);
        existingPartsTableView.refresh();
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

    @FXML // SEARCH ALL AVAILABLE PARTS BY NAME OR ID# BASED ON USER PROVIDED CRITERIA
    private void handleSearchAssociatedParts(ActionEvent actionEvent) {
        matchingAssociatedParts.clear();
        String userInputSearchPartToAssociate = addProductSearchField.getText();
        matchingAssociatedParts = stock.lookupPartName(userInputSearchPartToAssociate);
        if (matchingAssociatedParts.size() > 0) {
            existingPartsTableView.setItems(matchingAssociatedParts);
            existingPartsTableView.refresh();
        }
        else {
            int userInputAssociatedPartID = Integer.parseInt(addProductSearchField.getText());
            matchingAssociatedParts.add(stock.lookupPartID(userInputAssociatedPartID));
            if (matchingAssociatedParts.size() > 0) {
                existingPartsTableView.setItems(matchingAssociatedParts);
                existingPartsTableView.refresh();
            }
        }
    }

    @FXML // USER ADDED ASSOCIATED PART TO PRODUCT. ADD TO THE TEMP LIST & TABLE VIEW ONLY UNTIL THEY SAVE THE PRODUCT
    private void handleAddAssociatedPart(ActionEvent actionEvent) throws IOException{
        if(existingPartsTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please Select a Part To Associate with Product");
            alert.showAndWait();
        } else {
            Part partToAssociate = existingPartsTableView.getSelectionModel().getSelectedItem();
            associatedPartsToShow.add(partToAssociate);
            associatedPartsTableView.setItems(associatedPartsToShow);
            associatedPartsTableView.refresh();
        }
    }

    @FXML // ALLOW USER TO DELETE ANY OR ALL ASSOCIATED PARTS(CHECKS FOR MIN OF 1 ASSOCIATED PART WHEN USER TRIES TO SAVE THE PRODUCT)
    private void handleDeleteAssociatedPart(ActionEvent actionEvent) {
        if(associatedPartsTableView.getSelectionModel().getSelectedItems().isEmpty() && associatedPartsToShow.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("Do You Want To Remove All Associated Parts From The Product?");
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

    @FXML // ADD THE PRODUCT & ITS ASSOCIATED PARTS TO INVENTORY SYSTEM IF ALL REQUIREMENTS ARE MET
    private void handleSaveAddProduct(ActionEvent actionEvent) throws IOException {
        boolean isProductValid = true;
        if(associatedPartsToShow.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Adding Product");
            alert.setContentText("All Products Must Have A Minimum of 1 Associated Part");
            alert.showAndWait();
            isProductValid = false;
        }
        String userInputProductName = productNameField.getText();
        String userInputProductInv = productInvQtyField.getText();
        String userInputProductPrice = productPriceField.getText();
        String userInputProductMin = productMinField.getText();
        String userInputProductMax = productMaxField.getText();

        for (Product product : stock.getAllProducts()) {
            if (product.getProductID() >= assignProductID) {
                assignProductID = product.getProductID() + 1;
            }
            productIDField.setText(String.valueOf(assignProductID));
        }
        if (userInputProductName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Adding Product");
            alert.setContentText("Please Provide Product Name");
            alert.showAndWait();
            isProductValid = false;
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
                isProductValid = false;
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
                isProductValid = false;
            }
        }
        try {
            Integer.parseInt(userInputProductMax);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Maximum Quantity Provided.");
            alert.setContentText("Product Maximum Quantity Must be an Integer Value Greater Than the Minimum Quantity.");
            alert.showAndWait();
            isProductValid = false;
        }
        try {
            Double.parseDouble(userInputProductPrice);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Price Provided");
            alert.setContentText("Product Price Must be Provided in Decimal Format EX:'1.50'");
            alert.showAndWait();
            isProductValid = false;
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
            isProductValid = false;
            productCostMin = 0.00;
        }
        if (Integer.parseInt(userInputProductInv) > Integer.parseInt(userInputProductMax)
                || Integer.parseInt(userInputProductMin) > Integer.parseInt(userInputProductMax)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Inventory Quantity Range Detected");
            alert.setContentText("The Product Inventory Quantity & Product Minimum Quantity Cannot Be Greater Than the Maximum Quantity Value");
            alert.showAndWait();
            isProductValid = false;
        }
        if (Integer.parseInt(userInputProductInv) < Integer.parseInt(userInputProductMin)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Product Inventory Quantity Range Detected");
            alert.setContentText("The Product Inventory Quantity Cannot Be Less Than The Product Minimum Quantity Value");
            alert.showAndWait();
            isProductValid = false;
        }
        if (isProductValid) {
                for (Part p : associatedPartsToShow) {
                   productToAdd.addAssociatedPart(p);
                }
            stock.addProduct(new Product(Integer.parseInt(productIDField.getText()), userInputProductName,
            Double.parseDouble(userInputProductPrice), Integer.parseInt(userInputProductInv),
            Integer.parseInt(userInputProductMin), Integer.parseInt(userInputProductMax)));
            returnToMainScreen(actionEvent);
        }
    }

    @FXML // RETURN TO THE MAIN SCREEN UPON USER CONFIRMATION OF ADD PRODUCT CANCELLATION
    private void handleCancelAddProduct(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("Do You Want to Cancel Adding " + productNameField.getText() + " to Inventory?");
        alert.setContentText("Click 'OK' to Confirm & Return to the Main Menu");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToMainScreen(actionEvent);
        }
    }
}