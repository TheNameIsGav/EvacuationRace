import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

import java.util.Arrays;
import java.util.ArrayList;

import static spark.Spark.*;
import static spark.route.HttpMethod.get;



public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        Spark.staticFileLocation("/FrontEnd");
        post("/chat/createMessage", (request, response) -> {
            System.out.println(request.body());
            String message= request.body();
            message = message.substring(29, message.length() - 2);
            System.out.println(message);
            response.body(message);
            request.queryMap("message").toMap();
            return response.body();
        });


       /* Spark.get("/chat/createMessage", (req, res) ->{


        });*/

        post("/trade/createRequest", (request, response) -> {
            System.out.println(request.body());
            return "";
        });

        Spark.get("/map/getBoard", (req, res) -> {
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


    public class chatBox{ /*Addition of chat adder and object for use in chat BOX */
        public chatBox() { //Constructor
            ArrayList chat = new ArrayList();
        }
        public static void adder(String user, String message) { //Method with User ID and Name
            ArrayList chat = new ArrayList();
            String totalMessage = user + ": " + message;
            chat.add(" "  + totalMessage + " ");
            System.out.println(chat.get(0));
        }
    }

}
