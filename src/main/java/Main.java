import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

import java.util.Arrays;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        Spark.staticFileLocation("/FrontEnd");
        post("/chat/createMessage", (req, res) -> {
            System.out.println(req.body());
            return "";
        });

        post("/trade/createRequest", (req, res) -> {
            System.out.println(req.body());
            return "";
        });

        get("/map/getBoard", (req, res) -> {
            logger.info("GET request to /map/getBoard");
            Map m = new Map(10, 14);
            m.generateInlandOceans();
            m.generateIceWaterPoles();
            m.generatePeninsulas();
            m.generateDoubleHexes();
            m.removeClumps();
            m.evenOutPoles();
            int[][] map = m.getMap();
            for (int[] i : map) {
                System.out.println(Arrays.toString(i));
            }
            return m;
        }, new JsonUtil());
    }

}
