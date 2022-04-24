package Inventory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager implements Serializable {

    final Map<String, Car> cars = new HashMap<>();

    public InventoryManager() {
        cars.put("aaa", new Car("aaa", 2004, "Toyota", "Camry", 13222, "Red", false, "", ""));
        cars.put("bbb", new Car("bbb", 2004, "Toyota", "Camry", 13222, "Red", false, "", ""));
    }

    public Message addCar(Car car) {
        if (!cars.containsKey(car.getOwner())) {
            this.cars.put(car.getOwner(), car);
            Message message = new Message("REPLY", "Car added", car);
            message.status = "Success";
            return message;
        }
        Message message = new Message("REPLY", "Car not added", car);
        message.status = "Failure";
        return message;
    }

    public Message sellCar(Car car) {
        Car myCar = cars.get(car.getOwner());
        if (myCar != null && !myCar.getSold()) {
            myCar.setDriver(car.getDriver());
            myCar.setPrice(car.getPrice());
            myCar.setSold(true);
            Message message = new Message("REPLY", "Car sale successfully ", myCar);
            message.status = "Success";
            cars.put(myCar.getOwner(), myCar);
            return message;
        } else {
            Message message = new Message("REPLY", "Car not sold", car);
            message.status = "Failure";
            return message;
        }

    }

    public Message getCar(Message inMessage, int position) {
        System.out.println("Looking for + " + inMessage.optionText + " at position " + position + " ID " + inMessage.car.getID());
        boolean any = inMessage.optionText.equals("Any");
        Message message = new Message("REPLY", "", null);
        int index = -1;
        if (any) {
            for (Car car1 : cars.values()) {
                if (car1.getID().equals(inMessage.car.getID())) {
                    index++;
                    message.car = car1;
                    message.optionText = "Next found";
                    message.status = "Success";
                    if (index == position) {
                        return message;
                    }
                }
            }
        } else {
            for (Car car1 : cars.values()) {
                if (car1.getID().equals(inMessage.car.getID())
                        && car1.getSold() == inMessage.car.getSold()) {
                    index++;
                    message.car = car1;
                    message.optionText = "Next found";
                    message.status = "Success";
                    if (index == position) {
                        return message;
                    }
                }
            }
        }
        message.car = null;
        message.optionText = "Next not found";
        message.status = "Failure";
        return message;
    }

    public void display() {
        for (Map.Entry<String, Car> entry : cars.entrySet()) {
            System.out.println(entry.getValue());

        }
    }

}
