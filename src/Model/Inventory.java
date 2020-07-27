package Model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private ArrayList<Part> allParts;
    private ArrayList<Product> allProducts;

    public ArrayList<Part> getAllParts(){
        return allParts;
    }
    public ArrayList<Product> getAllProducts(){
        return allProducts;
    }

    public Inventory() {
        allParts = new ArrayList<>();
        allProducts = new ArrayList<>();
    }

    public void addPart(Part newPart) {
        if (newPart != null)
            this.allParts.add(newPart);
    }

    public void updatePart(Part partToModify) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getPartID() == partToModify.partID) {
                allParts.set(i, partToModify);
            }
        }
    }

    public boolean deletePart(Part partToDelete) {
        allParts.remove(partToDelete);
        return true;
    }

    public ObservableList<Part> lookupPartName(String partNameSearched) {
        if(!allParts.isEmpty()) {
            ObservableList<Part> searchForPartsList = FXCollections.observableArrayList();
            for (Part part : getAllParts()) {
                if (part.getPartName().contains(partNameSearched)) {
                    searchForPartsList.add(part);
                }
            }
            return searchForPartsList;
        }
        return null;
    }

    public Part lookupPartID(int partIDSearched) {
        for (Part part : allParts) {
            if (part.getPartID() == (partIDSearched)) {
                return part;
            }
        }
        return null;
    }

    public void addProduct(Product newProduct) {
        if (newProduct != null) {
            this.allProducts.add(newProduct);
        }
    }

    public void updateProduct(Product productToModify) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductID() == productToModify.getProductID()) {
                allProducts.set(i, productToModify);
                break;
            }
        }
    }

    public boolean deleteProduct(Product productToDelete) {
        allProducts.remove(productToDelete);
        return true;
    }

    public ObservableList<Product> lookupProductName(String productNameSearched) {
        if(!allProducts.isEmpty()) {
            ObservableList<Product> searchForProductsList = FXCollections.observableArrayList();
            for (Product product : getAllProducts()) {
                if (product.getProductName().contains(productNameSearched)) {
                    searchForProductsList.add(product);
                }
            }
            return searchForProductsList;
        }
        return null;
    }

    public Product lookupProductID(int productIDSearched) {
        for (Product product: allProducts) {
            if (product.getProductID() == (productIDSearched)) {
                return product;
            }
        }
        return null;
    }
}