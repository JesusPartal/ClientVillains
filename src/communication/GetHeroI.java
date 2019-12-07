package communication;

import model.SuperHero;
import model.SuperVillain;
import java.rmi.Remote;

public interface GetHeroI extends Remote {
    public SuperHero getHero(SuperVillain superVillain) throws Exception;
}
