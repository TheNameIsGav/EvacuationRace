import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import spark.Spark;

import java.util.Arrays;
import java.util.ArrayList;

import static spark.Spark.*;
import static spark.route.HttpMethod.get;



public class Main {

    private static ArrayList<Player> players;

    private static ArrayList<Game> gameList = new ArrayList<Game>(0);

    ///////////// clear this away

    private static Map m; //declared outside to be global?

    private static ArrayList chat = new ArrayList();
    private static ArrayList tradeRequest = new ArrayList();

    /////////////////////////////

    private static void adder(String user, String message) { //Method with User ID and Name
        String totalMessage = user + ": " + message;
        chat.add(totalMessage);
    }

    /////////////////////////////

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        Spark.staticFileLocation("/FrontEnd");

        //////////////// get and post handlers below ///////////////////

        Spark.get("/chat/loadChat", (request, response) -> {
            logger.info("GET request to update chat");
            return chat.toString();
        });

        post("/chat/createMessage", (request, response) -> {
            logger.info("POST request to /chat/createMessage");
            String[] parts = request.body().split("\"");
            String user = parts[3];
            String message = parts[7];
            adder(user, message);
            System.out.println(chat.get(chat.size() - 1));
            String convertedString = chat.toString();
            return convertedString;
        });



        post("/trade/createRequest", (request, response) -> {
            System.out.println(request.body());
            return "";
        });

        post("/login", (req, res) -> {
            String usr = req.queryParams("username");
            String pwd = req.queryParams("password");

            System.out.println(usr + ", " + pwd);

            boolean valid = false;

            if(usr.equals("username")) {
                if(pwd.equals("password")) {
                    valid = true;
                }
            }

            if(valid) {
                res.cookie("HASH", "0");    // places a cookie!
                res.redirect("/Board.html");    // should eventually be User's page
            } else {
                res.redirect("/Login.html");
            }
            return "";  // doesn't override the redirect above, but is this necessary/dangerous?
        });

        post("/signup", (req, res) -> {

            return "";
        });

        post("/logout", (req, res) -> {
            res.removeCookie("HASH");
            res.redirect("/");
            return "";
        });

        Spark.get("/map/getBoard", (req, res) -> {
            logger.info("GET request to /map/getBoard");
            int[][] map;
        //    if(m == null) { //if map has not been initialized, this initializes the map
                m = new Map(0, 0); //map initialized here if not initialized previously
        //    }
            map = m.getMap();

            for (int i = 0; i < map[0].length; i++) {   //prints out map values
                for (int j = 0; j < map.length; j++) {
                    System.out.print(map[j][i] + "\t");
                }
                System.out.println("");
            }

            return m;
        }, new JsonUtil());
    }
}
