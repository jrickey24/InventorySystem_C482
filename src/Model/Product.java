package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private int productID;
    private String productName;
    private double productPrice;
    private int productInStock;
    private int productMinStock;
    private int productMaxStock;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product(){}
    public Product(int productID, String productName, double productPrice,
                   int productInStock, int productMinStock, int productMaxStock){
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productInStock = productInStock;
        this.productMinStock = productMinStock;
        this.productMaxStock = productMaxStock;
    }

    public void setProductID(int productID){
        this.productID = productID;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public void setProductPrice(double productPrice){
        this.productPrice = productPrice;
    }
    public void setProductInStock(int productInStock){
        this.productInStock = productInStock;
    }
    public void setProductMinStock(int productMinStock){
        this.productMinStock = productMinStock;
    }
    public void setProductMaxStock(int productMaxStock){
        this.productMaxStock = productMaxStock;
    }
    public int getProductID(){
        return productID;
    }
    public String getProductName(){
        return productName;
    }
    public double getProductPrice(){
        return productPrice;
    }
    public int getProductInStock(){
        return productInStock;
    }
    public int getProductMinStock(){
        return productMinStock;
    }
    public int getProductMaxStock(){
        return productMaxStock;
    }

    public ObservableList<Part> getAssociatedParts() { return associatedParts; }

    public void addAssociatedPart(Part partToAssociate) {
        this.associatedParts.add(partToAssociate);
    }

    public boolean deleteAssociatedPart(Part partToDisassociate) {
        associatedParts.remove(partToDisassociate);
        return true;
    }
}
