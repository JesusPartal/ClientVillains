package model;

import java.io.Serializable;
import java.util.Random;

public class SuperVillain extends SuperThing implements Serializable {
    private static final long serialVersionUID = 1L;
    private int badnessPower;

    public SuperVillain() {
        super();
        Random random = new Random();
        badnessPower = random.nextInt(100) + 1;
    }

    public int getBadnessPower() {
        return badnessPower;
    }
}