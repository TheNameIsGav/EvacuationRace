import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Map {
    private int rows;
    private int cols;
    private int mapType;
    private int numOrbits;
    private int[][] map;
    private Logger logger;

    public Map(int rs, int cs, int mType) {
        logger = LoggerFactory.getLogger(Map.class);
        rows = rs;
        cols = cs;
        mapType = mType;
        map = new int[rows][cols];

        if(mType == 0) {        //earth-like planet
            generateTerran();
        } else if(mType == 1) {     //mercury-like planet
            generateMercurial();
        } else if(mType == 2) {     //mars-like planet
            generateMartian();
        } else if(mType == 3) {     //venus-like planet
            generateVenutian();
        } else if(mType == 4) {     //alien planet
            generateAlien();
        } else if(mType == 5) {     //jupiter-like planet
            generateJovian();
        } else if(mType == 6) {     //neptune-like planet
            generateNeptunian();
        } else if(mType == 7) {     //pluto-like planet
            generatePlutonian();
        } else {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    map[i][j] = (int) (Math.random() * 2);//metal = 0. organics = 1, ice = 2, ocean = 3, double metal = 4, ice metal = 5, double ice = 6
                }
            }
            numOrbits = 1;
        }
    }

    public int[][] getMap() {
        System.out.println("getMap was called");
        return map;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    private void generateTerran() {
        System.out.println("generateTerran was called");
        numOrbits = 2;
        mapType = 0;           //should be 0, must fix js too tho
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int tileTypeID = 0;
                if (isProb(50)) {
                    tileTypeID = 0;
                } else {
                    tileTypeID = 1;
                }
                int tileID = tileTypeID + (mapType * 100);
                map[r][c] = tileID;
            }
        }

        int bayX = ((int) (Math.random() * (rows - 6))) + 3;
        int capeX = ((int) (Math.random() * (rows - 4))) + 2;

        while(((capeX-bayX)>(0-4))&&((capeX-bayX)<4)){
            capeX = ((int) (Math.random() * (rows - 6))) + 3;
        }

        changePolar(true, 3, 4, 66, 33);
        changePolar(true, 2, 1, 66, 33);
        changePolar(false, 2, 2, 50, 50);

        changeAdjacent(capeX, 3, -1, 1, 100, 0);
        changeAdjacent(bayX,4, 3, 1, 100, 0);

        randomize(-1, 50);
    }

    private void generateMercurial() {
        System.out.println("generateMercurial was called");

    }

    private void generateMartian() {
        System.out.println("generateMartian was called");

    }

    private void generateVenutian() {
        System.out.println("generateVenutian was called");

    }

    private void generateAlien() {
        System.out.println("generateAlien was called");

    }

    private void generateJovian() {
        System.out.println("generateJovian was called");
        numOrbits = 2;
        mapType = 0;           //should be 0, must fix js too tho
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int tileTypeID = 0;
                if(isProb(100)){
                    tileTypeID = 99;
                } else {
                    tileTypeID = 1000;
                }
                int tileID = tileTypeID + (mapType * 100);
                map[r][c] = tileID;
            }
        }
    }

    private void generateNeptunian() {
        System.out.println("generateNeptunian was called");

    }

    private void generatePlutonian() {
        System.out.println("generatePlutonian was called");

    }

    private void changeAdjacent(int x, int y, int type, int r, int p, int pFade) {   //changes (x,y) and all tile surrounding for r steps. p is probability out of 100, and p fade is how much to add each step inwards (probability becomes 100% as you go in)
        int e = (x % 2);
        if(r >= 3){
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x][y+3] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x][y-3] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x+1][y+2+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x-1][y+2+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x+1][y-3+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x-1][y-3+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x+2][y+2] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x-2][y-2] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x+2][y-2] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x-2][y+2] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x+3][y-2+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x+3][y-1+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x+3][y+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x+3][y+1+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x-3][y-2+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x-3][y-1+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x-3][y+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y))){
                map[x-3][y+1+e] = type;
            }

        }
        if(r >= 2){
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x+2,y+1))){
                map[x+2][y+1] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x+2,y))){
                map[x+2][y] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x+2,y-1))){
                map[x+2][y-1] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x-2,y+1))){
                map[x-2][y+1] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x-2,y))){
                map[x-2][y] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x-2,y-1))){
                map[x-2][y-1] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x,y+2))){
                map[x][y+2] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x,y-2))){
                map[x][y-2] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x+1,y+1+e))){
                map[x+1][y+1+e] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x-1,y+1+e))){
                map[x-1][y+1+e] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x+1,y-2+e))){
                map[x+1][y-2+e] = type;
            }
            if((isProb(p + ((r-2) * pFade)))&&(isIn(x+1,y-2+e))){
                map[x-1][y-2+e] = type;
            }
        }
        if(r >= 1){
            if((isProb(p + ((r-1) * pFade)))&&(isIn(x+1,y))){
                map[x+1][y] = type;
            }
            if((isProb(p + ((r-1) * pFade)))&&(isIn(x-1,y))){
                map[x-1][y] = type;
            }
            if((isProb(p + ((r-1) * pFade)))&&(isIn(x,y+1))){
                map[x][y+1] = type;
            }
            if((isProb(p + ((r-1) * pFade)))&&(isIn(x,y-1))){
                map[x][y-1] = type;
            }
            if((isProb(p + ((r-1) * pFade)))&&(isIn(x+1,y-1+(2*e)))){
                map[x+1][y-1+(2*e)] = type;
            }
            if((isProb(p + ((r-1) * pFade)))&&(isIn(x-1,y-1+(2*e)))){
                map[x-1][y-1+(2*e)] = type;
            }
        }
        if((isProb(p + (r * pFade)))&&(isIn(x,y))){
            map[x][y] = type;
        }
    }

    private void changePolar(boolean north, int type, int r, int p, int pFade) {
        if(north){
            r--;
            while(r >= 0){
                for(int i = 0; i < rows; i++){
                    if((isProb(p))&&(isIn(i,r))){
                        map[i][r] = type;
                    }
                }
                p = p+pFade;
                r--;
            }
        } else {
            r = cols - r;
            while(r < cols){
                for(int i = 0; i < rows; i++){
                    if((isProb(p))&&(isIn(i,r))){
                        map[i][r] = type;
                    }
                }
                p = p+pFade;
                r++;
            }

        }
    }

    private void removeClumps(){        //should read through each tile on board and remove clumped tiles
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(isIn(i,j)){

                }
            }
        }
    }

    private void randomize(int t, int p){
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if(map[r][c] == t){
                    int tileTypeID = 0;
                    if(isProb(p)){
                        tileTypeID = 0;
                    } else {
                        tileTypeID = 1;
                    }
                    int tileID = tileTypeID + (mapType * 100);
                    map[r][c] = tileID;
                }
            }
        }
    }

    private boolean isIn(int x, int y){
        return ((x>=0)&&(y>=0)&&(x<rows)&&(y<cols));
    }

    private boolean isProb(int p){          //input is an int on a scale of 0 to 100 - 0 never called, 100 always called
        return (p > ((int) (Math.random() * 100)));
    }
}
/*
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
}*/