import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {
    private String[] players;
    private int typeOfGame;             //0 is normal game, we could have other game types tho.
    private Map[] mapList;

    public Game(String[] p, int type)
    {
        players = p;
        typeOfGame = type;
        Map[] maps;

        int mapTypeGenerator = (int) (Math.random() * 109);     //sets probabilities for different types of solar system maps - rn there will be only one for testing purposes.

        if((mapTypeGenerator >= 0)&&(mapTypeGenerator <= 99))       //the 'normal' solar system
        {
            maps = new Map[8];
            maps[0] = new Map(0, 0);
        } else {                                                //must have else to initialize maps
            maps = new Map[1];
            maps[0] = new Map(0, 0);
        }

        mapList = maps;
    }

    public Map[] getMaps(){
        return mapList;
    }
}
