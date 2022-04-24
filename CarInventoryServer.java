package Inventory;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class CarInventoryServer extends JFrame {

    JTextArea output;
    JScrollPane scroller;
    ServerSocket s;
    int portServer;
    Scanner in;
    final InventoryManager inventoryManager = new InventoryManager();
    String sendMessage;

    public CarInventoryServer(int port) {
        super("Rental Car Inventory Server");
        Container c = getContentPane();
        output = new JTextArea(10, 20);
        scroller = new JScrollPane(output);
        c.add(scroller, BorderLayout.CENTER);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        portServer = port;

        // Serverın TCP soket kurulumu
        try {
            s = new ServerSocket(portServer);
            waitForConnect();
;
        } catch (UnknownHostException ex) {
            ex.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void waitForConnect() {

        
        while (true) {
            try {
                Socket client = s.accept();

                System.out.print("A client has connected\n");

                Runnable runnableClient = new Runnable() {
                    @Override
                    public void run() {
                        clientHandler(client);
                    }
                };
                Thread newClientSocket = new Thread(runnableClient);
                newClientSocket.start();

            } catch (UnknownHostException ex) {
                ex.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }

    private void clientHandler(Socket client)
    {
        //lokal değişkenler oluşturma
        Message message;
        ObjectOutputStream toClient;
        ObjectInputStream fromClient;
        try {
            toClient = new ObjectOutputStream(client.getOutputStream());
            toClient.flush();
            fromClient = new ObjectInputStream(client.getInputStream());

            //loop:
            boolean stop = false;
            int position = 0;
            while (!stop) {
                if ((message = (Message) fromClient.readObject()) != null) {
                    switch (message.command) {
                        case "ADD":
                            displayMessage(message);
                            toClient.writeObject(inventoryManager.addCar(message.car));
                            toClient.reset();
                            position = 0;
                            break;
                        case "SELL":
                            displayMessage(message);
                            toClient.writeObject(inventoryManager.rentCar(message.car));
                            toClient.reset();
                            position = 0;
                            break;
                        case "GET":
                            displayMessage(message);
                            Message msg = inventoryManager.getCar(message, position);
                            toClient.writeObject(msg);
                            position++;
                            toClient.reset();
                            if (msg.status.equals("Failure")) {
                                position = 0;
                            }
                            inventoryManager.display();
                            break;
                        case "CLOSE":
                            displayMessage(message);
                            toClient.close();
                            fromClient.close();
                            client.close();
                            stop = true;
                            System.out.println("client closed");
                            break;
                    }

                }
            }
            toClient.reset();
        } catch (UnknownHostException ex) {
            System.out.println("client closed");
        } catch (IOException e) {
        } catch (Exception e) {
        }

    }


    private void displayMessage(Message message) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                output.append(message + "\n");
            }
        }
        );
    }


    public static void main(String[] args) {
        int port;

        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 6789;
        }
        CarInventoryServer app = new CarInventoryServer(port);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.waitForConnect();
    }

}
