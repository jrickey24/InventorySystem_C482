package Main;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author jRickey
 */

public class InventorySystem extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Inventory stock = new Inventory();
        addSampleData(stock);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainScreen.fxml"));
        Controllers.MainScreenController controller = new Controllers.MainScreenController(stock);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    void addSampleData(Inventory stock) {
        Part partA = new Outsourced(1, "Raspberry Pi", 49.99, 20, 10, 300, "Raspberry Pi Foundation");
        stock.addPart(partA);
        Part partB = new Outsourced(2, "Sense Hat", 30.55, 35, 5, 250, "Amazon Inc.");
        stock.addPart(partB);
        Part partC = new InHouse(3, "Robotics Transmitter", 15.75, 80, 30, 200, 101);
        stock.addPart(partC);
        Part partD = new InHouse(4, "Robotics Receiver", 5.75, 50, 30, 150, 102);
        stock.addPart(partD);
        Product product1 = new Product(10, "Robotic Arm", 175.89, 20, 5, 50);
        product1.addAssociatedPart(partA);
        stock.addProduct(product1);
        Product product2 = new Product(11, "Robotic Claw", 154.78, 15, 4, 45);
        product2.addAssociatedPart(partA);
        product2.addAssociatedPart(partB);
        stock.addProduct(product2);
        Product product3 = new Product(14, "Handheld Controller", 96.29, 10, 8, 15);
        product3.addAssociatedPart(partA);
        product3.addAssociatedPart(partB);
        product3.addAssociatedPart(partC);
        stock.addProduct(product3);
    }

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        launch(args);
    }
}


