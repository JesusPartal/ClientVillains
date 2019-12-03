package app;

import communication.StatusMessage;
import communication.StatusMessageType;
import subApplications.SubAppInterface;
import subApplications.ThreadApp;
import subApplications.ThreadExtAndImpApp;
import subApplications.ThreadPoolsApp;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientApp {
    public static int cadency = 2 * 1000;
    private static SubAppInterface subApp;
    private static Scanner scanChoice;
    private static int choiceInput = -1;
    private static String server = "127.0.0.1";
    private static int port = 8000;

    private static DatagramSocket socket;
    private static InetAddress address;
    private static Condition connectedToServer;
    private static Lock lock;
    private static boolean isConnectedToServer = false;


    public static void main(String[] args) throws InterruptedException, UnknownHostException, IOException {
        lock = new ReentrantLock();
        connectedToServer = lock.newCondition();
        scanChoice = new Scanner(System.in);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                checkServerConnectivityUDP();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    while(isConnectedToServer == false) {
                        System.out.println("Not connected to server");
                        connectedToServer.await();
                    }
                    buildMenu();
                    try {
                        subAppLauncher();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connectedToServer.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        scanChoice.close();
    }

    private static void checkServerConnectivityUDP() {
        lock.lock();

        try {
            address = InetAddress.getByName(server);
            socket = new DatagramSocket();

            while(!isConnectedToServer) {
                System.out.println("Connecting to server...");
                DatagramPacket request = new DatagramPacket(new byte[1], 1, address, port);
                socket.send(request);

                byte[] buffer = new byte[512];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);

                String quote = new String(buffer, 0, response.getLength());
                if (quote.equals("Connected")) {
                    System.out.println("Connected to " + address + ":" + port);
                    isConnectedToServer = true;
                    connectedToServer.signalAll();
                }
                Thread.sleep(3000);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

    private static void buildMenu() {

        System.out.println("Welcome to Villain Generator 2.0\n");
        System.out.println("Please select the app functionality you want to try:");
        System.out.println("----------------------------------------------------");
        System.out.println("\t1. Using Threads with yield(), join(), priority()");
        System.out.println("\t2. Using the Thread class and using the Runnable interface");
        System.out.println("\t3. Using Executors; FixedThreadPool and CachedThreadPool");
        System.out.println("\n\t0. Exit application");
        System.out.println("----------------------------------------------------\n");

        choiceInput = integerValidatorWithMessage(scanChoice, 0, 3, "Please select your choice (0-4)> ");
    }

    private static void subAppLauncher() throws UnknownHostException, IOException, InterruptedException {
        switch(choiceInput){
            case 0:
                System.out.println("Bye!");
                scanChoice.close();
                socket.close();
                break;
            case 1:
                subApp = new ThreadApp();
                subApp.runSubApp();
                break;
            case 2:
                subApp = new ThreadExtAndImpApp();
                subApp.runSubApp();
                break;
            case 3:
                subApp = new ThreadPoolsApp();
                subApp.runSubApp();
                break;
        }
    }

    /**
     *
     * @param scanner
     * @param min
     * @param max
     * @param message
     * @return
     */
    public static int integerValidatorWithMessage(Scanner scanner, int min, int max, String message) {

        boolean validated = false;
        int intValidated = -1;

        do {
            System.out.print(message);
            try {
                intValidated = scanner.nextInt();
                if (intValidated >= min && intValidated <= max) {
                    validated = true;
                } else {
                    System.out.println("Invalid selection; must be between" + min + " and " + max);
                    scanner.nextLine();
                }
            } catch (Exception exception) {
                System.out.println("Invalid selection; not a number");
                scanner.nextLine();
            }
        } while (!validated);

        return intValidated;
    }
}
