package Controllers;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {

    Inventory stock;
    private int assignPartID = 1;
    private boolean isPartOutsourced;
    @FXML private RadioButton rbInHouse;
    @FXML private RadioButton rbOutsourced;
    @FXML private TextField txtPartID;
    @FXML private TextField txtPartName;
    @FXML private TextField txtPartInvQty;
    @FXML private TextField txtPartPrice;
    @FXML private TextField txtPartMinQty;
    @FXML private TextField txtCoNameOrMachID;
    @FXML private TextField txtPartMaxQty;
    @FXML private Label labelCoNameOrMachID;

    public AddPartController(Inventory stock) {
        this.stock = stock;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // @param location
        // @param resource
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
    private void handleInHouseSelected(ActionEvent actionEvent) {
        isPartOutsourced = false;
        labelCoNameOrMachID.setText("Machine ID");
        txtCoNameOrMachID.setPromptText("Machine ID");
    }

    @FXML
    private void handleOutsourcedSelected(ActionEvent actionEvent) {
        isPartOutsourced = true;
        labelCoNameOrMachID.setText("Co. Name");
        txtCoNameOrMachID.setPromptText("Co. Name");
    }

    @FXML
    private void handleSaveAddPart(ActionEvent actionEvent) throws IOException {

         boolean isPartValid = true;

        if (!rbInHouse.isSelected() && !rbOutsourced.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Adding Part");
            alert.setContentText("Please Specify if Part is Outsourced or In-House");
            alert.showAndWait();
            isPartValid = false;
        }
        for (Part part : stock.getAllParts()) {
            if (part.getPartID() >= assignPartID) {
                assignPartID = part.getPartID() + 1;
            }
            txtPartID.setText(String.valueOf(assignPartID));
        }
        String userInputPartName = txtPartName.getText();
        String userInputPartInvQty = txtPartInvQty.getText();
        String userInputPartPrice = txtPartPrice.getText();
        String userInputPartMinQty = txtPartMinQty.getText();
        String userInputPartMaxQty = txtPartMaxQty.getText();
        String userInputCoNameOrMachID = txtCoNameOrMachID.getText();

        if (userInputPartName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Adding Part");
            alert.setContentText("Please Provide Part Name");
            alert.showAndWait();
            isPartValid = false;
        }
        if (isPartOutsourced && "".equals(userInputCoNameOrMachID)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Adding Outsourced Part");
            alert.setContentText("Please Provide Company Name");
            alert.showAndWait();
            isPartValid = false;
        } else if (!isPartOutsourced) {
            if ("".equals(userInputCoNameOrMachID)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error Occurred While Adding In-House Part");
                alert.setContentText("Please Provide Machine ID");
                alert.showAndWait();
                isPartValid = false;
            } else {
                try {
                    Integer.parseInt(userInputCoNameOrMachID);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Invalid Input Provided for In-House Part's Machine ID");
                    alert.setContentText("Machine ID Must be Provided as an Integer Value");
                    isPartValid = false;
                }
            }
        }
        try {
            Integer.parseInt(userInputPartInvQty);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("Invalid Part Inventory Quantity Provided. Part Inventory Quantity Must be an Integer Value");
            alert.setContentText("Click 'OK' to Set the Part's Inventory Quantity to 1 or 'Cancel' to Provide a Custom Value");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtPartInvQty.setText("1");
                userInputPartInvQty = "1";
            } else {
                isPartValid = false;
            }
        }
        try {
            Integer.parseInt(userInputPartMinQty);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setHeaderText("Invalid Part Minimum Quantity Provided. Part Minimum Quantity Must be an Integer Value");
            alert.setContentText("Click 'OK' to Set the Minimum Quantity to 0 or 'Cancel' to Provide a Custom Value");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtPartMinQty.setText("0");
                userInputPartMinQty = "0";
            } else {
                isPartValid = false;
            }
        }
        try {
            Integer.parseInt(userInputPartMaxQty);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Part Maximum Quantity Provided.");
            alert.setContentText("Part Maximum Quantity Must be an Integer Value Greater Than the Minimum Quantity.");
            alert.showAndWait();
            isPartValid = false;
        }
        try {
            Double.parseDouble(userInputPartPrice);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Part Price Provided");
            alert.setContentText("Part Price Must be Provided in Decimal Format '1.50'");
            alert.showAndWait();
            isPartValid = false;
        }
        if (Integer.parseInt(userInputPartInvQty) > Integer.parseInt(userInputPartMaxQty)
           || Integer.parseInt(userInputPartMinQty) > Integer.parseInt(userInputPartMaxQty)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Part Inventory Quantity Range Detected");
            alert.setContentText("The Part Inventory Quantity & Part Minimum Quantity Cannot Be Greater Than the Maximum Quantity Value");
            alert.showAndWait();
            isPartValid = false;
        }
        if (Integer.parseInt(userInputPartInvQty) < Integer.parseInt(userInputPartMinQty)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Part Inventory Quantity Range Detected");
            alert.setContentText("The Part Inventory Quantity Cannot Be Less Than The Part Minimum Quantity Value");
            alert.showAndWait();
            isPartValid = false;
        }
        if (isPartValid) {
            if(!isPartOutsourced) {
                stock.addPart(new InHouse(Integer.parseInt(txtPartID.getText()), userInputPartName,
                Double.parseDouble(userInputPartPrice), Integer.parseInt(userInputPartInvQty),
                Integer.parseInt(userInputPartMinQty), Integer.parseInt(userInputPartMaxQty),
                Integer.parseInt(userInputCoNameOrMachID)));
            } else {
                stock.addPart(new Outsourced(Integer.parseInt(txtPartID.getText()), userInputPartName,
                Double.parseDouble(userInputPartPrice), Integer.parseInt(userInputPartInvQty),
                Integer.parseInt(userInputPartMinQty), Integer.parseInt(userInputPartMaxQty),
                userInputCoNameOrMachID));
            }
            returnToMainScreen(actionEvent);
        }
    }

    @FXML
    private void handleCancelAddPart (ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("Do You Want to Cancel Adding " + txtPartName.getText() + " to Inventory?");
        alert.setContentText("Click 'OK' to Confirm & Return to the Main Menu");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToMainScreen(actionEvent);
        }
    }
}