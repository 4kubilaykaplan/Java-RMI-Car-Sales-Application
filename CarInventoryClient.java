package Inventory;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarInventoryClient extends JFrame implements ActionListener {

    Container c;
    JPanel ButtonPanel;
    JButton add, review, rent, ret, clear;

    JPanel VehicleInfoPanel;
    JLabel Owner, Make, Model, Colour, Year, ID;
    JTextField tOwner, tMake, tModel, tColour, tYear, tID;
    JPanel pOwner, pMake, pModel, pColour, pYear, pID;

    JPanel ReceiptsInfoPanel;
    JLabel Status, Name, License;
    JTextField tStatus, tName, tPrice;
    JPanel pStatus, pName, pPrice;

    JPanel ResultsPanel;
    JTextArea output;
    JScrollPane scroller;

    JPanel TwoItemPanel;
    String server;
    int port;
    Scanner in;

    // TCP socket ve obje streamları
    Socket socket;
    ObjectOutputStream toServer;
    ObjectInputStream fromServer;
    Car currentCar;
    boolean sold = false;

    public CarInventoryClient(String srvr, String prt) {
        super("Selling Car Inventory Manager");
        server = srvr;
        port = Integer.parseInt(prt);
        c = getContentPane();
        c.setLayout(new FlowLayout());
        buildButtonPanel();
        buildVehicleInfoPanel();
        buildReceiptsInfoPanel();
        buildResultsPanel();
        TwoItemPanel = new JPanel();
        TwoItemPanel.setLayout(new GridLayout(2, 1));
        TwoItemPanel.add(ReceiptsInfoPanel);
        TwoItemPanel.add(ResultsPanel);
        c.add(ButtonPanel);
        c.add(VehicleInfoPanel);
        c.add(TwoItemPanel);
        pack();
        this.addWindowListener(new WindowHandler());
        setVisible(true);

        // TCP socket
        try {
            socket = new Socket(server, port);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            toServer.flush();

            fromServer = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

        //  client penceresi kapandığında sunucuya kapatma mesajı göndererek streamları kapatan listener event ekranı
    }

    private void buildButtonPanel() //  This is GUI CODE!  review so you understand
    {
        ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new GridLayout(5, 1, 15, 10));
        add = new JButton("Add Vehicle");
        ret = new JButton("Return Vehicle");
        review = new JButton("Get Vehicle Info");
        rent = new JButton("Rent Vehicle");
        clear = new JButton("Clear Fields");
        ButtonPanel.add(add);
        ButtonPanel.add(review);
        ButtonPanel.add(sell);
        ButtonPanel.add(clear);
        add.addActionListener(this);
        review.addActionListener(this);
        sell.addActionListener(this);
        clear.addActionListener(this);
    }

    private void buildVehicleInfoPanel() {
        Owner = new JLabel("Owner");
        Owner.setPreferredSize(new Dimension(45, 15));
        Make = new JLabel("Make");
        Make.setPreferredSize(new Dimension(45, 15));
        Model = new JLabel("Model");
        Model.setPreferredSize(new Dimension(45, 15));
        Colour = new JLabel("Colour");
        Type.setPreferredSize(new Dimension(45, 15));
        Year = new JLabel("Year");
        Year.setPreferredSize(new Dimension(45, 15));
        ID = new JLabel("ID");
        ID.setPreferredSize(new Dimension(45, 15));

        tOwner = new JTextField(15);
        tMake = new JTextField(15);
        tModel = new JTextField(15);
        tColour = new JTextField(15);
        tYear = new JTextField(15);
        tID = new JTextField(15);

        pOwner = new JPanel();
        pMake = new JPanel();
        pModel = new JPanel();
        pColour = new JPanel();
        pYear = new JPanel();
        pID = new JPanel();

        pOwner.add(Owner);
        pOwner.add(tOwner);
        pMake.add(Make);
        pMake.add(tMake);
        pModel.add(Model);
        pModel.add(tModel);
        pYear.add(Year);
        pYear.add(tYear);
        pColour.add(Colour);
        pColour.add(tColour);
        pID.add(ID);
        pID.add(tID);
        VehicleInfoPanel = new JPanel();
        VehicleInfoPanel.setLayout(new GridLayout(6, 1));
        VehicleInfoPanel.add(pOwner);
        VehicleInfoPanel.add(pMake);
        VehicleInfoPanel.add(pModel);
        VehicleInfoPanel.add(pYear);
        VehicleInfoPanel.add(pColour);
        VehicleInfoPanel.add(pID);
        VehicleInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Vehicle Information"));
    }

    private void buildReceiptsInfoPanel() {
        Status = new JLabel("Status");
        Status.setPreferredSize(new Dimension(55, 15));
        Name = new JLabel("Name");
        Name.setPreferredSize(new Dimension(55, 15));
        Price = new JLabel("Price#");
        Price.setPreferredSize(new Dimension(55, 15));

        tStatus = new JTextField(15);
        tName = new JTextField(15);
        tPrice = new JTextField(15);

        pStatus = new JPanel();
        pName = new JPanel();
        pPrice = new JPanel();

        pStatus.add(Status);
        pStatus.add(tStatus);
        pName.add(Name);
        pName.add(tName);
        pPrice.add(Price);
        pPrice.add(tPrice);

        ReceiptsInfoPanel = new JPanel();
        ReceiptsInfoPanel.setLayout(new GridLayout(3, 1));
        ReceiptsInfoPanel.add(pStatus);
        ReceiptsInfoPanel.add(pName);
        ReceiptsInfoPanel.add(pPrice);
        ReceiptsInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Rental Information"));
    }

    private void buildResultsPanel() {
        output = new JTextArea(5, 20);
        output.setEditable(false);
        scroller = new JScrollPane(output);
        ResultsPanel = new JPanel();
        ResultsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Server Responses"));
        ResultsPanel.add(scroller);
    }

    //  işlemler
    public void actionPerformed(ActionEvent e) {
        try {
            // set up any locals here

            String choice = e.getActionCommand();
            System.out.println("Press any key. " + choice);

            switch (choice) {
                case "Clear":
                    clearFields();
                    break;
                case "Add Vehicle":
                    add_Vehicle();
                    break;
                case "Sell Vehicle":
                    sell_Vehicle();
                    break;
                case "Get Vehicle Info":
                    get_Vehicle();
                    break;
            }
            toServer.reset();

        } catch (IOException ex) {
            Logger.getLogger(CarInventoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //  GUI field ayarlamaları
    private void clearFields() {
        tOwner.setText("");
        tMake.setText("");
        tModel.setText("");
        tColour.setText("");
        tYear.setText("");
        tID.setText("");
        tStatus.setText("");
        tName.setText("");
        tPrice.setText("");
        output.setText("");
    }

    private void resetInputs() {
        tOwner.setText("");
        tMake.setText("");
        tModel.setText("");
	tColour.setText("");
        tYear.setText("");
        tID.setText("");
        tStatus.setText("");
        tName.setText("");
        tPrice.setText("");
    }

    private void add_Vehicle() {
        boolean sold = false;
        add.repaint();

        if (checkAdd()) {
            if (tStatus.getText().equals("Sold")) {
                sold = true;
            } else {
                sold = false;
            }

            currentCar = new Car(tOwner.getText(), Integer.parseInt(tYear.getText().isEmpty() ? "0" : tYear.getText()), tMake.getText(), tModel.getText(), Integer.parseInt(tID.getText().isEmpty() ? "0" : tID.getText()), tColour.getText(), sold, tName.getText(), tPrice.getText().isEmpty() ? "0" : tPrice.getText());
            // server ile haberleşme
            try {
                toServer.writeObject(new Message("ADD", "", currentCar, "Request"));
                Message message = (Message) fromServer
                        .readObject();
                System.out.println("Client received: " + message + " from Server");
                setFields(message);
                output.append("Message received: " + message + " from Server \n");
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CarInventoryClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            output.append("Please enter all vehicle details \n");
        }
    }

    private void sell_Vehicle() {
        if (checkSell()) {
            sell.repaint();
            if (tStatus.getText().equals("Sold")) {
                sold = true;
            } else {
                sold = false;
            }
            currentCar = new Car(tOwner.getText(), Integer.parseInt(tYear.getText().isEmpty() ? "0" : tYear.getText()), tMake.getText(), tModel.getText(), Integer.parseInt(tID.getText().isEmpty() ? "0" : tID.getText()), tType.getText(), sold, tName.getText(), Integer.parseInt(tPrice.getText().isEmpty() ? "0" : tPrice.getText());
            try {
                toServer.writeObject(new Message("SELL", "", currentCar, "Request"));
                toServer.reset();
                Message message = (Message) fromServer.readObject();
                System.out.println("Client received: " + message + " from Server");
                setFields(message);
                output.append("Message received: " + message + " from Server \n");
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CarInventoryClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            output.append("Please enter owner details \n");
        }
    }


    private void get_Vehicle() {
        if (!tType.getText().equals("")) {
            review.repaint();
            currentCar = new Car(tOwner.getText(), Integer.parseInt(tYear.getText().isEmpty() ? "0" : tYear.getText()), tMake.getText(), tModel.getText(), Integer.parseInt(tID.getText().isEmpty() ? "0" : tID.getText()), tColour.getText(), sold, tName.getText(), Integer.parseInt(tPrice.getText().isEmpty() ? "0" : tPrice.getText());
            if (tStatus.getText().equals("Sold")) {
                sold = true;
            } else {
                sold = false;
            }
            try {

                if (tStatus.getText().equals("Any") || tStatus.getText().equals("")) {
                    currentCar = new Car(tOwner.getText(), Integer.parseInt(tYear.getText().isEmpty() ? "0" : tYear.getText()), tMake.getText(), tModel.getText(), Integer.parseInt(tID.getText().isEmpty() ? "0" : tID.getText()), tColour.getText(), sold, tName.getText(), Integer.parseInt(tPrice.getText().isEmpty() ? "0" : tPrice.getText());
                    toServer.writeObject(new Message("GET", "Any", currentCar, "Request"));
                } else {
                    currentCar = new Car(tOwner.getText(), Integer.parseInt(tYear.getText().isEmpty() ? "0" : tYear.getText()), tMake.getText(), tModel.getText(), Integer.parseInt(tID.getText().isEmpty() ? "0" : tID.getText()), tColour.getText(), sold, tName.getText(), Integer.parseInt(tPrice.getText().isEmpty() ? "0" : tPrice.getText());
                    toServer.writeObject(new Message("GET", "", currentCar, "Request"));
                }

                toServer.reset();
                Message message = (Message) fromServer.readObject();
                System.out.println("Client received: " + message + " from Server");
                output.append("Message received: " + message + " from Server \n");
                setFields(message);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            output.append("Please enter receipt details. \n");
        }
    }

    private boolean checkAdd() {
        if (!(tOwner.getText().equals("")) && !(tMake.getText().equals("")) && !(tModel.getText().equals("")) && !(tColour.getText().equals(""))
                && !(tYear.getText().equals("")) && !(tID.getText().equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSell() {
        if (!(tOwner.getText().equals("")) && !(tName.getText().equals("")) && !(tPrice.getText().equals(""))) {
            return true;
        } else {
            return false;
        }
    }


    class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent ev) {
            currentCar = new Car(tOwner.getText(), Integer.parseInt(tYear.getText().isEmpty() ? "0" : tYear.getText()), tMake.getText(), tModel.getText(), Integer.parseInt(tID.getText().isEmpty() ? "0" : tID.getText()), tType.getText(), sold, tName.getText(), Integer.parseInt(tPrice.getText().isEmpty() ? "0" : tPrice.getText());
            Message message = new Message("CLOSE", "none", currentCar);
            System.out.println("Close");
            try {
                toServer.writeObject(message);
                toServer.flush();
                toServer.reset();

            } catch (IOException ex) {
                Logger.getLogger(CarInventoryClient.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    toServer.close();
                    fromServer.close();
                    socket.close();
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("windowClosing");
            System.exit(0); // End the application
        }
    }
  
    public static void main(String[] args) {

        String server, port;
        if (args.length != 2) {
            server = "localhost";
            port = "6789";
        } else {
            server = args[0];
            port = args[1];
        }
        CarInventoryClient cic = new CarInventoryClient(server, port);
        cic.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setFields(Message msg) {
        if (msg.status.equals("Success")) {
            tOwner.setText(msg.car.getOwner());
            tYear.setText(msg.car.getYear() + "");
            tColour.setText(msg.car.getColour());
            tName.setText(msg.car.getDriver());
            tModel.setText(msg.car.getModel());
            tID.setText(msg.car.getID() + "");
            tMake.setText(msg.car.getMake());
            tStatus.setText(msg.car.getSold() ? "Sold" : "Available");
            tPrice.setText(msg.car.getPrice());
        } else {
            resetInputs();
        }
    }
}
