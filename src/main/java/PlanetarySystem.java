import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


public class PlanetarySystem {
    private static int numOrbits;
    private static Map mainMap;
    private static ArrayList<Map> moons = new ArrayList<Map>(0);

    public PlanetarySystem(int type, int subtype){
        mainMap = new Map(type, subtype);
        numOrbits = 1;

        if(type == 0 || type == 5 || type == 6){    //set number of orbits
            numOrbits = 2;
        }

        if(type == 0){              //if planet is earth type
            Map moon = new Map(1,1);        //creates a plain, mercury type moon
            moons.add(moon);
        }
    }
}
