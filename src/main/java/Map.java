import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Map {
    private int rows;
    private int cols;
    private int[][] map;
    private Logger logger;

    public Map(int rows, int cols) {
        logger = LoggerFactory.getLogger(Map.class);
        this.rows = rows;
        this.cols = cols;
        map = new int[rows][cols];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = (int) (Math.random() * 2);//metal = 0. organics = 1, ice = 2, ocean = 3, double metal = 4, ice metal = 5, double ice = 6
            }
        }
    }

    public int[][] getMap() {
        System.out.println("getMap was called");
        return map;
    }

    public void generateRandom() {
        System.out.println("generateRandom was called");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int tileID = (int) (Math.random() * 7); //picks a random tile from 0 to 6
                map[r][c] = tileID;
            }
        }
    }
}
