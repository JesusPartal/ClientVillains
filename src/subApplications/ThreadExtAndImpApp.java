package subApplications;

import app.ClientApp;
import customThreads.ExtendsThread;
import customThreads.ImplementsRunnableThread;
import java.util.Scanner;

public class ThreadExtAndImpApp implements SubAppInterface {
    private int choiceSpawns;
    private Scanner scanner;

    public ThreadExtAndImpApp() {
        scanner = new Scanner(System.in);
        System.out.println("Launching Sub-Application 2");
        System.out.println("---------------------------");
        System.out.println("This application uses two threads; one extending the Thread class and" +
                "\none implementing the Runnable interface");
        choiceSpawns = ClientApp.integerValidatorWithMessage(scanner, 1, 100, "How many spawns per Thread? (1-100) ");
        scanner.close();
    }

    @Override
    public void runSubApp() throws InterruptedException {
        System.out.println("    Launching Thread-0 and Thread-1");
        ExtendsThread t0 = new ExtendsThread(choiceSpawns);
        ImplementsRunnableThread runnable1 = new ImplementsRunnableThread(ClientApp.cadency, choiceSpawns);
        Thread t1 = new Thread(runnable1);

        t0.setPriority(1);
        t1.setPriority(10);

        t0.join();
        t1.join();

        t0.start();
        t1.start();
        Thread.sleep(ClientApp.cadency);
    }
}
