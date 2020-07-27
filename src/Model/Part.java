package Model;

public abstract class Part {

    protected int partID;
    protected String partName;
    protected double partPrice;
    protected int partInStock;
    protected int partMinStock;
    protected int partMaxStock;

    public Part(){}
    public Part(int partID, String partName, double partPrice, int partInStock, int partMinStock, int partMaxStock) {
        this.partID = partID;
        this.partName = partName;
        this.partPrice = partPrice;
        this.partInStock = partInStock;
        this.partMinStock = partMinStock;
        this.partMaxStock = partMaxStock;
    }

    public void setPartID(int partID){
        this.partID = partID;
    }
    public void setPartName(String partName){
        this.partName = partName;
    }
    public void setPartPrice(double partPrice){
        this.partPrice = partPrice;
    }
    public void setPartInStock(int partInStock){
        this.partInStock = partInStock;
    }
    public void setPartMinStock(int partMinStock){
        this.partMinStock = partMinStock;
    }
    public void setPartMaxStock(int partMaxStock){
        this.partMaxStock = partMaxStock;
    }
    public int getPartID(){
        return partID;
    }
    public String getPartName(){
        return partName;
    }
    public double getPartPrice(){
        return partPrice;
    }
    public int getPartInStock(){
        return partInStock;
    }
    public int getPartMinStock(){
        return partMinStock;
    }
    public int getPartMaxStock(){
        return partMaxStock;
    }
}
