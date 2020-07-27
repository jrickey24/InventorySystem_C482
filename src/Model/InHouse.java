package Model;

public class InHouse extends Part {

    private int machineID;

    public InHouse(int partID, String partName, double partPrice, int partInStock,
                   int partMinStock, int partMaxStock, int machineID) {
        super(partID, partName, partPrice, partInStock, partMinStock, partMaxStock);
        this.machineID = machineID;
    }

    public void setMachineID(int id) {
        this.machineID = id;
    }
    public int getMachineID() {
        return machineID;
    }
}
