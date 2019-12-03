package model;

import java.io.Serializable;
import java.util.Random;

public class SuperThing extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private int healthPoints;

    public SuperThing() {
        Random random = new Random();
        this.healthPoints = random.nextInt(100) + 1;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public void receiveDamage(int damagePoints) {
        setHealthPoints(healthPoints - damagePoints);
    }
}
