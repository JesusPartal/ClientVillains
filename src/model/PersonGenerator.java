package model;

import java.util.Random;

public class PersonGenerator {
    private boolean isVillain;

    public Person getPerson() {
        isVillain = getRandomBoolean();

        if (isVillain) {
            boolean isStrong = getRandomBoolean();
            SuperVillain superVillain = (isStrong)
                    ? VillainFactory.buildVillain(VillainType.BADSTRONGMAN)
                    : VillainFactory.buildVillain(VillainType.BADFLYPERSON);
            return superVillain;
        }
        return new Person();
    }

    private boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
