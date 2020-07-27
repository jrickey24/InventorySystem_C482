package Controllers;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {

    Inventory stock;
    Part partToModify;
    boolean isPartOutsourced;
    @FXML private RadioButton rbInHouse;
    @FXML private RadioButton rbOutsourced;
    @FXML private ToggleGroup modifyPartRadioButtons;
    @FXML private TextField txtPartID;
    @FXML private TextField txtPartName;
    @FXML private TextField txtPartInvQty;
    @FXML private TextField txtPartPrice;
    @FXML private TextField txtPartMinQty;
    @FXML private TextField txtCoNameOrMachID;
    @FXML private TextField txtPartMaxQty;
    @FXML private Label labelCoNameOrMachID;

    public ModifyPartController(Inventory stock, Part partToModify) {
        this.stock = stock;
        this.partToModify = partToModify;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPartToModify();
        // @param location
        // @param resources
    }

    public void setPartToModify() {
        txtPartID.setText(Integer.toString(partToModify.getPartID()));
        txtPartName.setText(partToModify.getPartName());
        txtPartInvQty.setText(Integer.toString(partToModify.getPartInStock()));
        txtPartMinQty.setText(Integer.toString(partToModify.getPartMinStock()));
        txtPartMaxQty.setText(Integer.toString(partToModify.getPartMaxStock()));
        txtPartPrice.setText(Double.toString(partToModify.getPartPrice()));
        if (partToModify instanceof InHouse) {
            isPartOutsourced = false;
            rbInHouse.setSelected(true);
            InHouse modifyInHousePart = (InHouse) partToModify;
            txtCoNameOrMachID.setText(Integer.toString(modifyInHousePart.getMachineID()));
            labelCoNameOrMachID.setText("Machine ID");
        } else if (partToModify instanceof Outsourced) {
            isPartOutsourced = true;
            rbOutsourced.setSelected(true);
            Outsourced modifyOutsourcedPart = (Outsourced) partToModify;
            txtCoNameOrMachID.setText(modifyOutsourcedPart.getCompanyName());
            labelCoNameOrMachID.setText("Co. Name");
        }
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
        if (isPartOutsourced) isPartOutsourced = false;
        txtCoNameOrMachID.clear();
        labelCoNameOrMachID.setText("Machine ID");
        txtCoNameOrMachID.setPromptText("Machine ID");
    }

    @FXML
    private void handleOutsourcedSelected(ActionEvent actionEvent) {
        isPartOutsourced = true;
        txtCoNameOrMachID.clear();
        labelCoNameOrMachID.setText("Co. Name");
        txtCoNameOrMachID.setPromptText("Co. Name");
    }

    @FXML
    private void handleSaveModifyPart(ActionEvent actionEvent) throws IOException {
        boolean isModifyValid = true;
        String userInputPartName = txtPartName.getText();
        String userInputPartInvQty = txtPartInvQty.getText();
        String userInputPartPrice = txtPartPrice.getText();
        String userInputPartMinQty = txtPartMinQty.getText();
        String userInputPartMaxQty = txtPartMaxQty.getText();
        String userInputCoNameOrMachID = txtCoNameOrMachID.getText();
        if (userInputPartName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Modifying Part");
            alert.setContentText("Please Provide the Part Name");
            alert.showAndWait();
            isModifyValid = false;
        }
        if (isPartOutsourced && "".equals(userInputCoNameOrMachID)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occurred While Modifying Outsourced Part");
            alert.setContentText("Please Provide Company Name");
            alert.showAndWait();
            isModifyValid = false;
        }
        if (!isPartOutsourced) {
            if ("".equals(userInputCoNameOrMachID)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error Occurred While Modifying In-House Part");
                alert.setContentText("Please Provide an Integer Value for Machine ID");
                alert.showAndWait();
                isModifyValid = false;
            } else {
                try {
                    Integer.parseInt(userInputCoNameOrMachID);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Invalid Input Provided for In-House Part's Machine ID");
                    alert.setContentText("Machine ID Must be Provided as an Integer Value");
                    isModifyValid = false;
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
                isModifyValid = false;
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
                isModifyValid = false;
            }
        }
        try {
            Integer.parseInt(userInputPartMaxQty);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Part Maximum Quantity Provided.");
            alert.setContentText("Part Maximum Quantity Must be an Integer Value Greater Than the Minimum Quantity.");
            alert.showAndWait();
            isModifyValid = false;
        }
        try {
            Double.parseDouble(userInputPartPrice);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Part Price Provided");
            alert.setContentText("Part Price Must be Provided in Decimal Format '1.50'");
            alert.showAndWait();
            isModifyValid = false;
        }
        if (Integer.parseInt(userInputPartInvQty) > Integer.parseInt(userInputPartMaxQty)
            || Integer.parseInt(userInputPartMinQty) > Integer.parseInt(userInputPartMaxQty)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Part Inventory Quantity Range Detected");
            alert.setContentText("The Part Inventory Quantity &/or Part Minimum Quantity Cannot Be Greater Than the Maximum Quantity Value");
            alert.showAndWait();
            isModifyValid = false;
        }
        if (Integer.parseInt(userInputPartInvQty) < Integer.parseInt(userInputPartMinQty)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Part Inventory Quantity Range Detected");
            alert.setContentText("The Part Inventory Quantity Cannot Be Less Than The Part Minimum Quantity Value");
            alert.showAndWait();
            isModifyValid = false;
        }
        if (isModifyValid && !isPartOutsourced) {
            System.out.println("This part is InHouse: " + userInputPartName + " " + userInputPartPrice + " " + userInputPartInvQty + " " + userInputPartMinQty + " " + userInputPartMaxQty + " " + userInputCoNameOrMachID);
            stock.updatePart(new InHouse(Integer.parseInt(txtPartID.getText()), userInputPartName,
            Double.parseDouble(userInputPartPrice), Integer.parseInt(userInputPartInvQty),
            Integer.parseInt(userInputPartMinQty), Integer.parseInt(userInputPartMaxQty),
            Integer.parseInt(userInputCoNameOrMachID)));
            returnToMainScreen(actionEvent);
        } else if (isModifyValid) {
            System.out.println("This part is Outsourced: " + userInputPartName + " " + userInputPartPrice + " " + userInputPartInvQty + " " + userInputPartMinQty + " " + userInputPartMaxQty + " " + userInputCoNameOrMachID);
            stock.updatePart(new Outsourced(Integer.parseInt(txtPartID.getText()), userInputPartName,
            Double.parseDouble(userInputPartPrice), Integer.parseInt(userInputPartInvQty),
            Integer.parseInt(userInputPartMinQty), Integer.parseInt(userInputPartMaxQty),
            userInputCoNameOrMachID));
            returnToMainScreen(actionEvent);
        }
    }
    @FXML
    private void handleCancelModifyPart(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("Do You Want to Cancel Modifying This Part?");
        alert.setContentText("Click 'OK' to Confirm & Return to the Main Menu");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToMainScreen(actionEvent);
        }
    }
}