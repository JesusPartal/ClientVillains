package customThreads;

import app.Main;
import model.PersonGenerator;
import model.Person;
import model.SuperHero;
import storage.FileStorageController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExtendsThread extends Thread{
    private FileStorageController fSC;
    private PersonGenerator personGenerator;
    private int spawns;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String server = "127.0.0.1";
    private int port = 8000;
    private String savePathVillain;

    public ExtendsThread(int spawns) throws InterruptedException {
        this.spawns = spawns;
        personGenerator = new PersonGenerator();
        fSC = new FileStorageController();
    }

    public void run() {
        System.out.println("\t**** Starting " + this.getName() + " ****");

        for(int i = 0; i < this.spawns; i++) {
            this.setPriority(5);
            Person newPerson = personGenerator.getPerson();
            System.out.println("\t" + this.getName() + "-Spawn: " + (i+1) + " \n\t\t" + newPerson.getStats());

            if(newPerson instanceof model.SuperVillain) {
                this.setPriority(10);
                try {
                    savePathVillain = fSC.savePerson(newPerson);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SuperHero superHero = sendVillainGetHero(newPerson);
                FileStorageController.deleteFile(savePathVillain);
                try {
                    fSC.savePerson(superHero);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(Main.cadency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.yield(); // yield after each spawn
        }
        System.out.println("\t**** Ending " + this.getName() + " ****");
    }


    public SuperHero sendVillainGetHero(Person newPerson) {
        SuperHero superHero = null;
        try {
            socket = new Socket(server, port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(newPerson);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            superHero = (SuperHero) objectInputStream.readObject();
            System.out.println("\t\t <= Superhero received!  " + superHero.getStats());
            System.out.println("\t\t xx SUPERVILLAIN DEFEATED! xx");
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + server);
        } catch (IOException e) {
            System.err.println("Unable to get streams from server");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return superHero;
    }
}