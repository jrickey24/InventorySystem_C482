package Controllers;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Optional;
import java.net.URL;

public class MainScreenController implements Initializable {

    Inventory stock;
    private ObservableList<Part> existingParts = FXCollections.observableArrayList();
    private ObservableList<Part> matchingParts = FXCollections.observableArrayList();
    private ObservableList<Product> existingProducts = FXCollections.observableArrayList();
    private ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
    @FXML private TableView<Part> partsTableView;
    @FXML private TableView<Product> productsTableView;
    @FXML private TextField partSearchField;
    @FXML private TextField productSearchField;

    public MainScreenController(Inventory stock) {
        this.stock = stock;
    }

    @Override // POPULATE THE PARTS & PRODUCTS TABLE VIEWS WITH THE CURRENT INVENTORY STOCK
    public void initialize(URL location, ResourceBundle resources) {
        populatePartsTableView();
        populateProductsTableView();
    }

    private void populatePartsTableView() {
        existingParts.setAll(stock.getAllParts());
        partsTableView.setItems(existingParts);
        partsTableView.refresh();
    }

    private void populateProductsTableView() {
        existingProducts.setAll(stock.getAllProducts());
        productsTableView.setItems(existingProducts);
        productsTableView.refresh();
    }

    @FXML // NAVIGATE TO ADD PART SCREEN UPON USER SELECTION OF THE PARTS "ADD" BUTTON
    private void handleAddPart(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddPart.fxml"));
            AddPartController controller = new AddPartController(stock);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML  // NAVIGATE TO MODIFY PART SCREEN UPON USER SELECTION OF THE PARTS "MODIFY" BUTTON && SELECTED PART
    private void handleModifyPart(ActionEvent actionEvent) {
        try {
            Part partToModify = partsTableView.getSelectionModel().getSelectedItem();
            if (!existingParts.isEmpty() && partToModify != null ) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(stock, partToModify);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setHeaderText("No Part Selected to Modify");
                alert.setContentText("Please Select the Part You Want to Modify");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML // SEARCH THE INVENTORY SYSTEM PARTS BY PART NAME OR PART ID#
    private void handleSearchParts(ActionEvent actionEvent) {
        matchingParts.clear();
        String userInputPartSearch = partSearchField.getText();
        matchingParts = stock.lookupPartName(userInputPartSearch);
        if (matchingParts.size() > 0) {
            partsTableView.setItems(matchingParts);
            partsTableView.refresh();
        }
        else {
            int userInputPartID = Integer.parseInt(userInputPartSearch);
            matchingParts.add(stock.lookupPartID(userInputPartID));
            if (matchingParts.size() > 0) {
                partsTableView.setItems(matchingParts);
                partsTableView.refresh();
            }
        }
    }

    @FXML // DELETE THE USER SELECTED PART FROM THE INVENTORY SYSTEM UPON CONFIRMATION
    private void handleDeletePart(ActionEvent actionEvent) {
        Part partToDelete = partsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("Do You Want to Delete " + partToDelete.getPartName() + " From the Inventory System?");
        alert.setContentText("Click 'OK' to Confirm Part Deletion");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stock.deletePart(partToDelete);
            populatePartsTableView();
        }
    }

    @FXML // NAVIGATE TO ADD PRODUCT SCREEN UPON USER SELECTION OF THE PRODUCTS "ADD" BUTTON
     private void handleAddProduct(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddProduct.fxml"));
            AddProductController controller = new AddProductController(stock);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
     }

     @FXML // NAVIGATE TO MODIFY PRODUCT SCREEN UPON USER SELECTION OF PRODUCTS "MODIFY" BUTTON && USER SELECTED PRODUCT
     private void handleModifyProduct(ActionEvent actionEvent) {
         try {
             Product productToModify = productsTableView.getSelectionModel().getSelectedItem();
             if (!existingProducts.isEmpty() && productToModify != null) {
                 FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyProduct.fxml"));
                 ModifyProductController controller = new ModifyProductController(stock, productToModify);
                 loader.setController(controller);
                 Parent root = loader.load();
                 Scene scene = new Scene(root);
                 Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                 stage.setScene(scene);
                 stage.show();
             } else {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.initModality(Modality.NONE);
                 alert.setHeaderText("No Product Selected to Modify");
                 alert.setContentText("Please Select the Product You Want to Modify");
                 alert.showAndWait();
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     @FXML // DELETE THE USER SELECTED PRODUCT UPON CONFIRMATION
      private void handleDeleteProduct(ActionEvent actionEvent) {
            Product productToDelete = productsTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("Do You Want to Delete " + productToDelete.getProductName() + " From the Inventory System?");
            alert.setContentText("Click 'OK' to Confirm Product Deletion");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stock.deleteProduct(productToDelete);
                populateProductsTableView();
            }
      }

     @FXML // INVOKE PRODUCTS SEARCH BUTTON-RETURN SUBSET OF MATCHING PRODUCTS BASED ON SEARCH CRITERIA
     private void handleSearchProducts(ActionEvent actionEvent) {
        matchingProducts.clear();
        String userInputProductSearch = productSearchField.getText();
        matchingProducts = stock.lookupProductName(userInputProductSearch);
        if (matchingProducts.size() > 0) {
            productsTableView.setItems(matchingProducts);
            productsTableView.refresh();
        }
        else {
            int userInputProductID = Integer.parseInt(userInputProductSearch);
            matchingProducts.add(stock.lookupProductID(userInputProductID));
            if (matchingProducts.size() > 0) {
                productsTableView.setItems(matchingProducts);
                productsTableView.refresh();
            }
        }
     }

     @FXML
     private void exitProgram(MouseEvent event) {
        Platform.exit();
    }
}