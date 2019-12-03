package model;

import java.io.Serializable;

public class GoodStrongMan extends SuperHero implements StrengthInterface, Serializable {

    private static final long serialVersionUID = 1L;

    public GoodStrongMan(int goodness) {
        super(goodness);
    }

    public void strength() {
        System.out.println("GoodStrongMan is using his strength power");
    }


    @Override
    public String getStats() {
        return "GoodStrongMan -> HP: " + this.getHealthPoints() +
                " - GP: " + this.getGoodnessPower();
    }
}