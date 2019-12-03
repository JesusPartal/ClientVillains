package model;

import java.io.Serializable;

public class BadFlyPerson extends SuperVillain implements FlyInterface, Serializable {

    private static final long serialVersionUID = 1L;

    public BadFlyPerson() {
        super();
    }

    public void fly() {
        System.out.println("BadFlyPerson is using his fly power");
    }

    @Override
    public String getStats() {
        return "BadFlyPerson -> HP: " + this.getHealthPoints() +
                " - BP: " + this.getBadnessPower();
    }
}