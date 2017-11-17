import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {
    private int numPlayers;
    private String[] players;
    private int typeOfGame;             //0 is normal game, we could have other game types tho.
    private PlanetarySystem[] planets;

    public Game(String[] p, int type)
    {
        numPlayers = p.length;
        players = p;
        typeOfGame = type;
        PlanetarySystem[] maps;

        int mapTypeGenerator = (int) (Math.random() * 109);     //sets probabilities for different types of solar system maps - rn there will be only one for testing purposes.

        if((mapTypeGenerator >= 0)&&(mapTypeGenerator <= 99))   //the 'normal' solar system
        {
<<<<<<< HEAD
            maps = new Map[8];
            maps[0] = new Map(0, 0);
        } else {                                                //must have else to initialize maps
            maps = new Map[1];
            maps[0] = new Map(0, 0);
=======
            maps = new PlanetarySystem[8];                      //sets number of planets
            maps[0] = new PlanetarySystem(0, 0);    //sets up homeworld
        } else {                                                //must have else to initialize maps
            maps = new PlanetarySystem[1];
            maps[0] = new PlanetarySystem(0, 0);    //sets up only homeworld
>>>>>>> Beta
        }

        planets = maps;                                         //publishes 'maps' as 'planets'
    }

    public PlanetarySystem[] getPlanets(){
        return planets;
    }
}
