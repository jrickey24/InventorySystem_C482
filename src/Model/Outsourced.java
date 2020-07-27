package Model;

public class Outsourced extends Part {

    private String companyName;

    public Outsourced(int partID, String partName, double partPrice, int partInStock,
                      int partMinStock, int partMaxStock, String companyName) {
        super(partID, partName, partPrice, partInStock, partMinStock, partMaxStock);
        this.companyName = companyName;
    }

    public void setCompanyName() {
        this.companyName = companyName;
    }
    public String getCompanyName() {
        return companyName;
    }
}
