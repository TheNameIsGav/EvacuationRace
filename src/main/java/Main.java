import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

import java.util.Arrays;
import java.util.ArrayList;

import static spark.Spark.*;
import static spark.route.HttpMethod.get;



public class Main {
    public static Map m; //declared outside to be global?

    private static ArrayList chat = new ArrayList();

    private static void adder(String user, String message) { //Method with User ID and Name
        String totalMessage = user + ": " + message;
        chat.add(totalMessage);
    }

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        Spark.staticFileLocation("/FrontEnd");

        post("/chat/createMessage", (request, response) -> {
            String[] parts = request.body().split("\"");
            String user = parts[3];
            String message = parts[7];
            adder(user, message);
            System.out.println(chat.get(chat.size() -1));
            return "";
        });

        post("/trade/createRequest", (request, response) -> {
            System.out.println(request.body());
            return "";
        });

        Spark.get("/map/getBoard", (req, res) -> {
            logger.info("GET request to /map/getBoard");
            int[][] map;
            if(m == null) { //if map has not been initialized, this initializes the map
                m = new Map(14, 10); //map initialized here if not initialized previously
                m.generateRandom();
            //    m.generateInlandOceans();
            //    m.generateIceWaterPoles();
            //    m.generatePeninsulas();
            //    m.generateDoubleHexes();
            //    m.removeClumps();
            //    m.evenOutPoles();
            }
            map = m.getMap();
            for (int[] i : map) {  //prints out the map array no matter what
                System.out.println(Arrays.toString(i));
            }
            return m;
        }, new JsonUtil());
    }
}
