package app;

import communication.GetHeroI;
import model.Person;
import model.PersonGenerator;
import model.SuperHero;
import model.SuperVillain;
import storage.FileStorageController;

import java.rmi.Naming;
import java.util.Scanner;

public class MainClientRMI {
    private static Scanner scanner;
    private static int spawnChoice;
    private static PersonGenerator personGenerator;
    private static FileStorageController fSC;
    private static GetHeroI getHeroI;
    private static String savePathVillain;

    public static void main(String[] args) throws Exception {
        scanner = new Scanner(System.in);
        getHeroI = (GetHeroI) Naming.lookup("GET");

        personGenerator = new PersonGenerator();
        fSC = new FileStorageController();

        System.out.println("Welcome to the RMI test");
        spawnChoice = integerValidatorWithMessage(scanner, 1, 30, "How many spawns? (1-30) ");

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < spawnChoice; i++) {
                    Person newPerson = personGenerator.getPerson();
                    System.out.println("Spawn: " + (i + 1) + " \n\t" + newPerson.getStats());

                    if(newPerson instanceof model.SuperVillain) {
                        try {
                            savePathVillain = fSC.savePerson(newPerson);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SuperHero superHero = null;
                        try {
                            superHero = getHeroI.getHero((SuperVillain) newPerson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FileStorageController.deleteFile(savePathVillain);
                        try {
                            fSC.savePerson(superHero);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        };

        Thread t1 = new Thread(r1);

        t1.start();


    }

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
