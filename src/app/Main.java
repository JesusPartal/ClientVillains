package app;

import communication.UDPClient;
import subApplications.SubAppInterface;
import subApplications.ThreadApp;
import subApplications.ThreadExtAndImpApp;
import subApplications.ThreadPoolsApp;
import sun.awt.windows.ThemeReader;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;

public class Main {
    public static int cadency = 2 * 1000;
    private static SubAppInterface subApp;
    private static Scanner scanChoice;
    private static int choiceInput = -1;
    private static UDPClient udpClient;


    public static void main(String[] args) throws IOException, InterruptedException {
        scanChoice = new Scanner(System.in);

        udpClient = new UDPClient();
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                try {
                    buildMenu();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t1 = new Thread(r1);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(udpClient);
        executorService.submit(t1);
    }

    public static void buildMenu() throws InterruptedException, IOException {

        System.out.println("Welcome to Villain Generator 2.0\n");
        System.out.println("Please select the app functionality you want to try:");
        System.out.println("----------------------------------------------------");
        System.out.println("\t1. Using Threads with yield(), join(), priority()");
        System.out.println("\t2. Using the Thread class and using the Runnable interface");
        System.out.println("\t3. Using Executors; FixedThreadPool and CachedThreadPool");
        System.out.println("\n\t0. Exit application");
        System.out.println("----------------------------------------------------\n");

        choiceInput = integerValidatorWithMessage(scanChoice, 0, 3, "Please select your choice (0-4)> ");
        subAppLauncher();
    }

    private static void subAppLauncher() throws InterruptedException, IOException {
        switch(choiceInput){
            case 0:
                System.out.println("Bye!");
                scanChoice.close();
                udpClient.sendEcho("e");
                System.exit(0);
                break;
            case 1:
                subApp = new ThreadApp();
                subApp.runSubApp();
                udpClient.sendEcho("e");
                break;
            case 2:
                subApp = new ThreadExtAndImpApp();
                subApp.runSubApp();
                udpClient.sendEcho("e");
                break;
            case 3:
                subApp = new ThreadPoolsApp();
                subApp.runSubApp();
                udpClient.sendEcho("e");
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
