package subApplications;

import app.ClientApp;
import customThreads.ExtendsThread;
import java.util.Scanner;

public class ThreadApp implements SubAppInterface {
    private int choiceThreads, choiceSpawns;
    private Scanner scanner;

    public ThreadApp() {
        scanner = new Scanner(System.in);
        System.out.println("Launching Sub-application 1");
        System.out.println("---------------------------");
        choiceThreads = ClientApp.integerValidatorWithMessage(scanner, 1, 20, "How many Threads? (1-20) ");
        choiceSpawns = ClientApp.integerValidatorWithMessage(scanner, 1, 100, "How many spawns per Thread? (1-100) ");
        scanner.close();
    }

    @Override
    public void runSubApp() throws InterruptedException {
        System.out.println("Launching " + choiceThreads + " threads");
        for (int i = 0; i < choiceThreads; i++) {
            ExtendsThread thread = new ExtendsThread(choiceSpawns);
            thread.join();
            thread.start();
            thread.sleep(ClientApp.cadency);
        }
    }
}
