package Inventory;

import java.io.Serializable;

@SuppressWarnings("serial")

public class Message implements Serializable {

//private variables
    public String command;
    public String optionText;
    public Car car;
    public String status;

    public Message(String command, String optionText, Car car) {
        this.command = command;
        this.optionText = optionText;
        this.car = car;
    }

    public Message(String command, String optionText, Car car, String status) {
        this.command = command;
        this.optionText = optionText;
        this.car = car;
        this.status = status;
    }

    @Override
    public String toString() {
        return status + " " + command + " " + optionText + " " + (this.car != null ? this.car.toString() : " ");
    }

}
