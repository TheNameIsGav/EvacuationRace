import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Map {
    public boolean initialized  = false;
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
        return map;
    }

    public void generateRandom() {
        System.out.println("generateRandom was called");
        System.out.println("rows = " + rows + " and cols = " + cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int tileID = 0;
                map[cols][rows] = tileID;
                System.out.print(tileID);
            }
            System.out.println("-");
        }
    }

    public void generateInlandOceans() {
        int row = 0;
        int col = 0;
        while (row > rows - 2 || row < 2|| col > cols - 2 || col < 2) {
            row = (int) (Math.random() * rows);
            col = (int) (Math.random() * cols);
        }
        map[row][col] = 3;
        if (Math.random() <= .8) {
            map[row + 1][col] = 3; //ocean
        }
        if (Math.random() <= .8) {
            map[row + 1][col + 1] = 3;
        }
        if (Math.random() <= .8) {
            map[row + 1][col - 1] = 3;
        }
        if (Math.random() <= .8) {
            map[row][col + 1] = 3;
        }
        if (Math.random() <= .8) {
            map[row][col - 1] = 3;
        }
        if (Math.random() <= .8) {
            map[row + 1][col + 1] = 3;
        }
        if (Math.random() <= .8) {
            map[row - 1][col - 1] = 3;
        }

        for (int i = 0; i < 5; i++) {
            map[(int) (Math.random() * rows)][(int) (Math.random() * cols)] = 3;
        }
    }

    private static int randomTile(int[] tiles) {
        return tiles[(int)(Math.random() * tiles.length)];
    }

    public void generatePeninsulas() {
        for (int i = 0; i < 2; i++) {
            int r = (int) (Math.random() * rows);
            int c = 3;
            int[] tiles = {0, 1};
            map[r][c] = randomTile(tiles);
            if (r % 2 == 0) {
                if (r - 1 >= 0) {
                    map[r - 1][c] = randomTile(tiles);
                }
                if (r + 1 < rows) {
                    map[r + 1][c] = randomTile(tiles);
                }
                if (c - 1 >= 0) {
                    map[r][c - 1] = randomTile(tiles);
                }
                if (c + 1 < cols) {
                    map[r][c + 1] = randomTile(tiles);
                }
                if (r + 1 < rows && c - 1 >= 0) {
                    map[r + 1][c - 1] = randomTile(tiles);
                }
                if (r - 1 >= 0 && c - 1 >= 0) {
                    map[r - 1][c - 1] = randomTile(tiles);
                }
            } else {
                if (r - 1 >= 0) {
                    map[r - 1][c] = randomTile(tiles);
                }
                if (r + 1 < rows) {
                    map[r + 1][c] = randomTile(tiles);
                }
                if (c - 1 >= 0) {
                    map[r][c - 1] = randomTile(tiles);
                }
                if (c + 1 < cols) {
                    map[r][c + 1] = randomTile(tiles);
                }
                if (r + 1 < rows && c + 1 < cols) {
                    map[r + 1][c + 1] = randomTile(tiles);
                }
                if (r - 1 >= 0 && c + 1 < cols) {
                    map[r - 1][c + 1] = randomTile(tiles);
                }
            }
        }
    }

    public void generateIceWaterPoles() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 2; j++) {
                map[i][j] = 2; //ice
                if (Math.random() <= .2) {
                    map[i][j] = 3;
                }
            }
            for (int j = 1; j < 4; j++) {
                map[i][j] = 3;
            }
            for (int j = cols - 1; j < cols; j++) {
                map[i][j] = 2;
                double resource = Math.random();
                if (resource <= .2) {
                    map[i][j] = 0;
                } else if (resource <= .3) {
                    map[i][j] = 3;
                }
            }
        }
    }

    public void generateDoubleHexes() {
        int[] types = {4, 5, 6};
        for (int i = 0; i < (int)(Math.random() * 2) + 2; i++) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            while (map[row][col] != 0 || (row > rows - 2 || row < 2|| col > cols - 2 || col < 2) || adjacentTypes(row, col, types)) {
                row = (int) (Math.random() * rows);
                col = (int) (Math.random() * cols);
            }
            map[row][col] = 4; //double metal
        }
        for (int i = 0; i < (int) (Math.random() * 2) + 2; i++) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            while ((map[row][col] != 2 && map[row][col] != 0) || adjacentTypes(row, col, types)) {
                row = (int) (Math.random() * rows);
                col = (int) (Math.random() * cols);
            }
            map[row][col] = 5; //ice metal
        }
        for (int i = 0; i < (int) (Math.random() * 2) + 3; i++) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            while (map[row][col] != 2 || adjacentTypes(row, col, types)) {
                row = (int) (Math.random() * rows);
                col = (int) (Math.random() * cols);
            }
            map[row][col] = 6; //ice ice
        }
    }

    private boolean adjacentTypes(int r, int c, int[] types) { //checks if a given tile has a tile of the same type bordering it
        int[] surrounding = getAdjacentTileTypes(r, c);
        for (int i : types) {
            for (int j : surrounding) {
                if (i == j) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] getAdjacentTileTypes(int r, int c) {
        int[] tiles = new int[6];
        if (r % 2 == 0) {
            if (r - 1 >= 0) {
                tiles[0] = map[r - 1][c];
            } else {
                tiles[0] = -1;
            }
            if (r + 1 < rows) {
                tiles[1] = map[r + 1][c];
            } else {
                tiles[1] = -1;
            }
            if (c - 1 >= 0) {
                tiles[2] = map[r][c - 1];
            } else {
                tiles[2] = -1;
            }
            if (c + 1 < cols) {
                tiles[3] = map[r][c + 1];
            } else {
                tiles[3] = -1;
            }
            if (r + 1 < rows && c - 1 >= 0) {
                tiles[4] = map[r + 1][c - 1];
            } else {
                tiles[4] = -1;
            }
            if (r - 1 >= 0 && c - 1 >= 0) {
                tiles[5] = map[r - 1][c - 1];
            } else {
                tiles[5] = -1;
            }
            return tiles;
        } else {
            if (r - 1 >= 0) {
                tiles[0] = map[r - 1][c];
            } else {
                tiles[0] = -1;
            }
            if (r + 1 < rows) {
                tiles[1] = map[r + 1][c];
            } else {
                tiles[1] = -1;
            }
            if (c - 1 >= 0) {
                tiles[2] = map[r][c - 1];
            } else {
                tiles[2] = -1;
            }
            if (c + 1 < cols) {
                tiles[3] = map[r][c + 1];
            } else {
                tiles[3] = -1;
            }
            if (r + 1 < rows && c + 1 < cols) {
                tiles[4] = map[r + 1][c + 1];
            } else {
                tiles[4] = -1;
            }
            if (r - 1 >= 0 && c + 1 < cols) {
                tiles[5] = map[r - 1][c + 1];
            } else {
                tiles[5] = -1;
            }
            return tiles;
        }
    }

    private boolean allAdjacentSameTypes(int r, int c) { //checks if a tile's surrounding tiles are all of the same type as a given tile
        int[] surrounding = getAdjacentTileTypes(r, c);
        int counter = 1;
        for (int j : surrounding) {
            if (map[r][c] == j) {
                counter++;
            }
        }
        return counter == 6;
    }

    public void removeClumps() { //if a group of 7 tiles is all the same type, it changes the center tile
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (allAdjacentSameTypes(i, j) && !(map[i][j] == 3)) {
                    map[i][j] = (int) (Math.random() * 4);
                    j--;
                }
            }
        }
    }

    public void evenOutPoles() {

    }
}
