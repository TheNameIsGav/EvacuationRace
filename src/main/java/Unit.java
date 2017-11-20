public class Unit {
    int type;		// 0 is infantry, 1 is crawler, 2 is shuttle
    int systemLocator;	// 0 is solarMap, 1 is innermost planet, counts outward
    int planetLocator;	// 0 is main mapp, 1 is innermost moon, counts outward
    int x;
    int y;		// gives the x and y coordinates of the unit on the above map
    // (for solar map, x is radius and y is degrees)

    int playerNumber;	// determines unit color and nationality

    int hp;
    int offense;
    int deffense;
    int damage;
    int movement;
    int remainingMovement;

}
