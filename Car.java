package Inventory;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class Car implements Serializable {
    //private değişkenler

    private String owner;
    private int year;
    private String make;
    private String model;
    private int ID;
    private String colour;
    private boolean sold;
    private int weight;
    private int price;

    //public metotlar
    public Car(String owner, int year, String make, String model, int ID, String colour, boolean sold, int weight, int price) {
        this.setOwner(owner);
        this.setYear(year);
        this.setMake(make);
        this.setModel(model);
        this.setID(ID);
        this.setColour(colour);
        this.setSold(sold);
        this.setWeight(weight);
        this.setPrice(price);

    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    //get

    public String getOwner() {
        return this.owner;
    }

    public int getYear() {
        return this.year;
    }

    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public int getID() {
        return this.ID;
    }

    public String getColour() {
        return this.colour;
    }

    public boolean getSold() {
        return this.sold;
    }
    
    public int getWeight() {
        return this.weight;
    }

    public int getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return this.owner + "-" + this.year + "-" + this.make + "-" + this.model + "-"
                + this.ID + "-" + this.colour + "-" + this.sold + "-" + this.weight + "-" + this.price;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.owner);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if (!Objects.equals(this.owner, other.owner)) {
            return false;
        }
        return true;
    }

}
