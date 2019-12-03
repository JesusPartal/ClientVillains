package subApplications;

import app.ClientApp;
import customThreads.ExtendsThread;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolsApp implements SubAppInterface {
    private int choiceSpawns, choicePool, choiceThreads;
    private Scanner scanner;
    private ExecutorService executor;

    public ThreadPoolsApp() {
        scanner = new Scanner(System.in);
        System.out.println("Launching Sub Application 3");
        System.out.println("---------------------------");
        choicePool = ClientApp.integerValidatorWithMessage(scanner, 1, 2, "Do you want to use Fixed Thead Pool (1) or Cached Thread Pool(2) ");
        if (choicePool == 1)
            choiceThreads = ClientApp.integerValidatorWithMessage(scanner, 1, 100, "How many Threads? (1-100) ");
        choiceSpawns = ClientApp.integerValidatorWithMessage(scanner, 1, 100, "How many spawns per Thread? (1-100) ");
    }

    @Override
    public void runSubApp() throws InterruptedException {
        switch (choicePool) {
            case 1:
                useFixedPool();
                break;
            case 2:
                useCachedPool();
                break;
        }
    }

    private void useFixedPool() throws InterruptedException {
        int choiceThreadSizePool = ClientApp.integerValidatorWithMessage(scanner, 1, 10, "What's the pool size? (1-10) ");
        executor = Executors.newFixedThreadPool(choiceThreadSizePool);
        for (int i = 0; i < choiceThreads; i++) {
            ExtendsThread thread0 = new ExtendsThread(choiceSpawns);
            executor.execute(thread0);
            Thread.sleep(ClientApp.cadency);
        }
        executor.shutdown();
    }

    private void useCachedPool() throws InterruptedException {
        executor = Executors.newCachedThreadPool();
        for (int i = 0; i < choiceThreads; i++) {
            ExtendsThread thread0 = new ExtendsThread(choiceSpawns);
            executor.execute(thread0);
            Thread.sleep(ClientApp.cadency);
        }
        executor.shutdown();
    }
}