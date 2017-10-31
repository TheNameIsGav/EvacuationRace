import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Map {
    private int cols;
    private int rows;
    private int mapType;
    private int subType;
    private int numOrbits;
    private int[][] map;
    private Logger logger;

    public Map(int mType, int sType) {
        logger = LoggerFactory.getLogger(Map.class);
        mapType = mType;
        subType = sType;

        if(mType == -1) {                //test case
            cols = 16;
            rows = 12;
            map = new int[cols][rows];

            generateTest();
        } else if(mType == 0) {        //earth-like planet
            if(sType == 0){
                cols = 16;
                rows = 12;
                map = new int[cols][rows];

                generateTerran0();
            } else if(sType == 1){
                cols = 16;
                rows = 12;
                map = new int[cols][rows];

                generateTerran1();
            }
        } else if(mType == 1) {     //mercury-like planet
            if(sType == 0){
                cols = 10;
                rows = 8;
                map = new int[cols][rows];

                generateMercurial0();
            } else if(sType == 1){
                cols = 8;
                rows = 6;
                map = new int[cols][rows];

                generateMercurial1();
            }
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
        } else {                    //default
            cols = 16;
            rows = 12;
            map = new int[cols][rows];

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

    public int getCols() {
        return this.cols;
    }

    public int getRows() {
        return this.rows;
    }

    private void generateTest() {
        System.out.println("generateTest was called");
        numOrbits = 2;
        mapType = -1;
        for (int r = 0; r < cols; r++) {
            for (int c = 0; c < rows; c++) {
                map[r][c] = 0;
            }
        }

        changeAdjacent(cols/2, rows/2, 3, 3, 100, 0);
    }

    private void generateTerran0() {
        System.out.println("generateTerran0 was called");
        numOrbits = 2;
        mapType = 0;           //should be 0, must fix js too tho
        for (int r = 0; r < cols; r++) {
            for (int c = 0; c < rows; c++) {
                map[r][c] = -1;
            }
        }

        int bayX = ((int) (Math.random() * (cols - 6))) + 3;
        int capeX = ((int) (Math.random() * (cols - 6))) + 3;
        int lakeX = ((int) (Math.random() * (cols - 6))) + 3;   //may be defunct, see below

        while(((capeX-bayX)>(0-4))&&((capeX-bayX)<4)){          //can cause an infinite loop if there is no valid col
            capeX = ((int) (Math.random() * (cols - 6))) + 3;
        }

        changePolar(true, 3, 4, 67, 33);

        int lake1X = cols-1-((int) (Math.random() * ((cols)-6)))-3;    //lakes currently generated on the side of maps, needs to be fixed
        int lake2X = ((int) (Math.random() * ((cols)-4)))+2;

        while(((lake1X-lake2X)>(0-3))&&((lake1X-lake2X)<3)){          //can cause an infinite loop if there is no valid col
            lake2X = ((int) (Math.random() * ((cols)-4)))+2;
        }

        changeAdjacent(capeX, 3, -1, 1, 100, 0);
        changeAdjacent(bayX,4, 3, 2, 25, 75);
        changeAdjacent(lake1X, rows-3, 3,2,60, 20);
        changeAdjacent(lake2X, rows-3, 3,1,75, 25);

        randomize(-1, 45 + (int)(Math.random() * 16), 0,1);  //sets iron and organic tiles up with slight variation in occurance
        removeClumps(0,1);

        changePolar(true, 2, 1, 67, 33);
        changePolar(false, 2, 2, 50, 50);

        addUnique(4, 0, (int) (Math.random() * 2) + 2, 3); //adds iron-iron
        addUnique(6, 2, (int) (Math.random() * 2) + 2, 3); //adds ice-ice
        addUnique(5, 2, (int) (Math.random() * 2) + 2, 0); //adds iron-ice
    }

    private void generateTerran1() {
        System.out.println("generateTerran1 was called");
        numOrbits = 2;
        for (int r = 0; r < cols; r++) {
            for (int c = 0; c < rows; c++) {
                map[r][c] = -1;
            }
        }

        changePolar(true, 2, 2, 50, 50);
        changePolar(false, 2, 2, 50, 50);

        changeAdjacent((cols/2)+2,(rows/2), 3, 3, 25, 25);
        changeAdjacent((cols/2)-2,(rows/2),3,3,25,25);

        randomize(-1, 45 + (int)(Math.random() * 16), 0,1);  //sets iron and organic tiles up
        removeClumps(0,1);

        addUnique(4, 0, (int) (Math.random() * 2) + 2, 3);
        addUnique(6, 2, (int) (Math.random() * 2) + 2, 3);
        addUnique(5, 2, (int) (Math.random() * 2) + 2, 3);

    }


    private void generateMercurial0() {
        System.out.println("generateMercurial0 was called");
        for (int r = 0; r < cols; r++) {
            for (int c = 0; c < rows; c++) {
                map[r][c] = -1;
            }
        }
        randomize(-1, 45 + (int)(Math.random() * 16), 0,1);  //sets iron and organic tiles up
        addUnique(7,1,3, 0);
    }

    private void generateMercurial1() {
        System.out.println("generateMercurial1 was called");

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
        for (int r = 0; r < cols; r++) {
            for (int c = 0; c < rows; c++) {
                int tileTypeID = 0;
                if(isProb(100)){
                    tileTypeID = 99;
                } else {
                    tileTypeID = 1000;
                }
                map[r][c] = tileTypeID;
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
        if(r >= 3){                                                 //third ring
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y+3))){   //add %rows to x coord and isIn
                map[x][y+3] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x,y-3))){
                map[x][y-3] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x+1,y+2+e))){
                map[x+1][y+2+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x-1,y+2+e))){
                map[x-1][y+2+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x+1,y-3+e))){
                map[x+1][y-3+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x-1,y-3+e))){
                map[x-1][y-3+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x+2,y+2))){
                map[x+2][y+2] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x-2,y-2))){
                map[x-2][y-2] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x+2,y-2))){
                map[x+2][y-2] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x-2,y+2))){
                map[x-2][y+2] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x+3,y-2+e))){
                map[x+3][y-2+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x+3,y-1+e))){
                map[x+3][y-1+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x+3,y+e))){
                map[x+3][y+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x+3,y+1+e))){
                map[x+3][y+1+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x-3,y-2+e))){
                map[x-3][y-2+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x-3,y-1+e))){
                map[x-3][y-1+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x-3,y+e))){
                map[x-3][y+e] = type;
            }
            if((isProb(p + ((r-3) * pFade)))&&(isIn(x-3,y+1+e))){
                map[x-3][y+1+e] = type;
            }

        }
        if(r >= 2){                                                 //second ring
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
        if(r >= 1){                                                 //first ring
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
        if((isProb(p + (r * pFade)))&&(isIn(x,y))){   //center always subjected to probability
            map[x][y] = type;
        }
    }

    private void changePolar(boolean north, int type, int r, int p, int pFade) {    //applys a probability p with fade pFade to a radius r around a polar region.
        if(north){
            r--;
            while(r >= 0){
                for(int i = 0; i < cols; i++){
                    if((isProb(p))&&(isIn(i,r))){
                        map[i][r] = type;
                    }
                }
                p = p+pFade;
                r--;
            }
        } else {
            r = rows - r;
            while(r < rows){
                for(int i = 0; i < cols; i++){
                    if((isProb(p))&&(isIn(i,r))){
                        map[i][r] = type;
                    }
                }
                p = p+pFade;
                r++;
            }

        }
    }

    private void removeClumps(int c1, int c2){        //reads through each c1 and c2 tile on board and remove clumped tiles, switching between c1 and c2
        for(int i = 0; i < cols; i++){
            for(int j = 0; j < rows; j++){
                int type = map[i][j];
                if(isIn(i,j)&&(numBordering(i,j,type) >= 4)){
                    if(type == c1){
                        map[i][j] = c2;
                    }
                    if(type == c2){
                        map[i][j] = c1;
                    }
                }
            }
        }
    }               //note: the above method doesn't eliminate clumps because it generates them as fast as it removes them. It does shuffle the map though, which isn't bad

                            //(5, 0, 3, 3)
    private void addUnique(int t, int repl, int n, int nexTo){   //changes n tiles of type repl to type t - higher probability if next to many nexTo
        System.out.println("addUnique called");
        while (n > 0){
            System.out.println(n);
            int r = (int) (Math.random() * cols);
            int c = (int) (Math.random() * rows);

            System.out.println(r + " " + c + " " + map[r][c]);

            if((map[r][c] == repl) && isProb(15*(numBordering(r,c,nexTo))+10) && (numBordering(r,c,t) == 0))
            {
                map[r][c] = t;
                n--;
            }
        }
    }

    private void randomize(int t, int p, int c1, int c2) {  //searches for tiles of type t and randomizes them between c1 and c2 according to p (probability of c1)
        for (int r = 0; r < cols; r++) {
            for (int c = 0; c < rows; c++) {
                if ((map[r][c] % 100) == t) {
                    int tileTypeID;
                    if (isProb(p)) {
                        tileTypeID = c1;
                    } else {
                        tileTypeID = c2;
                    }
                    map[r][c] = tileTypeID;
                }
            }
        }
    }

    private int numBordering(int x, int y, int t){      //returns number of tiles of type t that are hex-adjacent to map[x][y]
        int count = 0;
        int e = (x%2);

        if(isIn(x+1,y)&&(map[x+1][y] == t)){
            count++;
        }
        if(isIn(x-1,y)&&(map[x-1][y] == t)){
            count++;
        }
        if(isIn(x,y+1)&&(map[x][y+1] == t)){
            count++;
        }
        if(isIn(x,y-1)&&(map[x][y-1] == t)){
            count++;
        }
        if(isIn(x+1,y-1+(2*e))&&(map[x+1][y-1+(2*e)] == t)){
            count++;
        }
        if(isIn(x-1,y-1+(2*e))&&(map[x-1][y-1+(2*e)] == t)){
            count++;
        }

        return count;
    }

    private boolean isIn(int x, int y){     //checks to see if tile at x,y is in map.
        return ((x>=0)&&(y>=0)&&(x< cols)&&(y< rows));
    }

    private boolean isProb(int p){          //input is an int on a scale of 0 to 100 - 0 never called, 100 always called
        return (p > ((int) (Math.random() * 100)));
    }
}

























/*
    public void generateInlandOceans() {
        int row = 0;
        int col = 0;
        while (row > cols - 2 || row < 2|| col > rows - 2 || col < 2) {
            row = (int) (Math.random() * cols);
            col = (int) (Math.random() * rows);
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
            map[(int) (Math.random() * cols)][(int) (Math.random() * rows)] = 3;
        }
    }

    private static int randomTile(int[] tiles) {
        return tiles[(int)(Math.random() * tiles.length)];
    }

    public void generatePeninsulas() {
        for (int i = 0; i < 2; i++) {
            int r = (int) (Math.random() * cols);
            int c = 3;
            int[] tiles = {0, 1};
            map[r][c] = randomTile(tiles);
            if (r % 2 == 0) {
                if (r - 1 >= 0) {
                    map[r - 1][c] = randomTile(tiles);
                }
                if (r + 1 < cols) {
                    map[r + 1][c] = randomTile(tiles);
                }
                if (c - 1 >= 0) {
                    map[r][c - 1] = randomTile(tiles);
                }
                if (c + 1 < rows) {
                    map[r][c + 1] = randomTile(tiles);
                }
                if (r + 1 < cols && c - 1 >= 0) {
                    map[r + 1][c - 1] = randomTile(tiles);
                }
                if (r - 1 >= 0 && c - 1 >= 0) {
                    map[r - 1][c - 1] = randomTile(tiles);
                }
            } else {
                if (r - 1 >= 0) {
                    map[r - 1][c] = randomTile(tiles);
                }
                if (r + 1 < cols) {
                    map[r + 1][c] = randomTile(tiles);
                }
                if (c - 1 >= 0) {
                    map[r][c - 1] = randomTile(tiles);
                }
                if (c + 1 < rows) {
                    map[r][c + 1] = randomTile(tiles);
                }
                if (r + 1 < cols && c + 1 < rows) {
                    map[r + 1][c + 1] = randomTile(tiles);
                }
                if (r - 1 >= 0 && c + 1 < rows) {
                    map[r - 1][c + 1] = randomTile(tiles);
                }
            }
        }
    }

    public void generateIceWaterPoles() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < 2; j++) {
                map[i][j] = 2; //ice
                if (Math.random() <= .2) {
                    map[i][j] = 3;
                }
            }
            for (int j = 1; j < 4; j++) {
                map[i][j] = 3;
            }
            for (int j = rows - 1; j < rows; j++) {
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
            int row = (int) (Math.random() * cols);
            int col = (int) (Math.random() * rows);
            while (map[row][col] != 0 || (row > cols - 2 || row < 2|| col > rows - 2 || col < 2) || adjacentTypes(row, col, types)) {
                row = (int) (Math.random() * cols);
                col = (int) (Math.random() * rows);
            }
            map[row][col] = 4; //double metal
        }
        for (int i = 0; i < (int) (Math.random() * 2) + 2; i++) {
            int row = (int) (Math.random() * cols);
            int col = (int) (Math.random() * rows);
            while ((map[row][col] != 2 && map[row][col] != 0) || adjacentTypes(row, col, types)) {
                row = (int) (Math.random() * cols);
                col = (int) (Math.random() * rows);
            }
            map[row][col] = 5; //ice metal
        }
        for (int i = 0; i < (int) (Math.random() * 2) + 3; i++) {
            int row = (int) (Math.random() * cols);
            int col = (int) (Math.random() * rows);
            while (map[row][col] != 2 || adjacentTypes(row, col, types)) {
                row = (int) (Math.random() * cols);
                col = (int) (Math.random() * rows);
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
            if (r + 1 < cols) {
                tiles[1] = map[r + 1][c];
            } else {
                tiles[1] = -1;
            }
            if (c - 1 >= 0) {
                tiles[2] = map[r][c - 1];
            } else {
                tiles[2] = -1;
            }
            if (c + 1 < rows) {
                tiles[3] = map[r][c + 1];
            } else {
                tiles[3] = -1;
            }
            if (r + 1 < cols && c - 1 >= 0) {
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
            if (r + 1 < cols) {
                tiles[1] = map[r + 1][c];
            } else {
                tiles[1] = -1;
            }
            if (c - 1 >= 0) {
                tiles[2] = map[r][c - 1];
            } else {
                tiles[2] = -1;
            }
            if (c + 1 < rows) {
                tiles[3] = map[r][c + 1];
            } else {
                tiles[3] = -1;
            }
            if (r + 1 < cols && c + 1 < rows) {
                tiles[4] = map[r + 1][c + 1];
            } else {
                tiles[4] = -1;
            }
            if (r - 1 >= 0 && c + 1 < rows) {
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
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
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