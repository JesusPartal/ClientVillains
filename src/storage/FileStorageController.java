package storage;

import model.Person;
import model.SuperHero;
import model.SuperVillain;
import java.io.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * FileStorageController.java
 * Purpose: Store and read serializable objects
 * @author jesus.partal@mycit.ie
 * @author Jesus Partal
 * @version 1.4
 * @since 07-10-2019
 */
public class FileStorageController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static String BZ_DIRECTORY = "../BattleFolder/";
    private static String BZ_FILENAME = "battlezone_";
    private int bz_fileNumber;

    private static String SW_DIRECTORY = "../SaveTheWorld/";
    private static String SW_FILENAME = "SaveTheWorld_";
    private int sw_fileNumber;

    private static String FILE_EXTENSION = ".txt";

    private static Lock lock;
    private static boolean isDirectoryAvailable = true;
    private Condition availableDirectory;
    private static int checkNumber;

    public FileStorageController() throws InterruptedException {
        lock = new ReentrantLock();
        getFileNumberBattlezone();
        getFileNumberSaveTheWorld();
    }

    private void getFileNumberBattlezone() throws InterruptedException {
        availableDirectory = lock.newCondition();
        lock.lock();

        try {
            while(isDirectoryAvailable == false) {
                availableDirectory.await();
            }
            checkNumber = 1;
            File file = new File(BZ_DIRECTORY
                    + BZ_FILENAME
                    + checkNumber
                    + FILE_EXTENSION);

            while(file.exists()) {
                checkNumber++;
                file = new File(BZ_DIRECTORY
                        + BZ_FILENAME
                        + checkNumber
                        + FILE_EXTENSION
                );
            }
            this.bz_fileNumber = checkNumber;
            isDirectoryAvailable = true;
            availableDirectory.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private synchronized void getFileNumberSaveTheWorld() {
        int checkNumber = 1;

        lock.lock();
        try {
            File file = new File(SW_DIRECTORY
                    + SW_FILENAME
                    + checkNumber
                    + FILE_EXTENSION);

            while(file.exists()) {
                checkNumber++;
                file = new File(SW_DIRECTORY
                        + SW_FILENAME
                        + checkNumber
                        + FILE_EXTENSION
                );
            }
            this.sw_fileNumber = checkNumber;
        } finally {
            lock.unlock();
        }
    }

    public synchronized String savePerson(Person person) throws InterruptedException {
        lock.lock();
        String savePath = null;

        if (person instanceof SuperVillain) {
            getFileNumberBattlezone();
            savePath = BZ_DIRECTORY
                    + BZ_FILENAME
                    + bz_fileNumber
                    + FILE_EXTENSION;
        } else if (person instanceof SuperHero) {
            getFileNumberSaveTheWorld();
            savePath = SW_DIRECTORY
                    + SW_FILENAME
                    + sw_fileNumber
                    + FILE_EXTENSION;
        }

        File savedFile = new File(savePath);

        if(!savedFile.getParentFile().exists())
            savedFile.getParentFile().mkdir();

        try {
            storeObject(person, savePath);
        } catch (Exception errorWritingToFile){
            System.out.println("Error while writing file...");
        }

        if (person instanceof SuperVillain) {
            bz_fileNumber++;
        } else if (person instanceof SuperHero) {
            sw_fileNumber++;
        }
        lock.unlock();
        return savePath;
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    private static void storeObject(Object object, String filename) {

        try {
            FileOutputStream fileOutput = new FileOutputStream(filename);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);

            objectOutput.writeObject(object);
            objectOutput.close();
            fileOutput.close();
        } catch (IOException iOException) {
            storeObject(iOException, "IOException.ser");
        }
    }

}