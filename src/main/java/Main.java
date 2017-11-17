import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import spark.Spark;

import java.util.ArrayList;

import static spark.Spark.*;
import static spark.route.HttpMethod.get;



public class Main {

    private static ArrayList<Player> players = new ArrayList<Player>(0);

    private static ArrayList<Game> gameList = new ArrayList<Game>(0);

    ///////////// clear this away

    private static Map m; //declared outside to be global?

    private static ArrayList chat = new ArrayList();

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
<<<<<<< HEAD
            logger.info("GET request to /chat/createMessage");
=======
            logger.info("POST request to /chat/createMessage");
>>>>>>> Beta
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

        post("/signup", (req, res) -> {
            String usr = req.queryParams("username");
            String pwd = req.queryParams("password");

            System.out.println(usr + ", " + pwd);

            // needs if condition here - should reject weird or existing usernames/passwords

            System.out.println("valid!");

            Player p = new Player(usr, pwd);
            players.add(p);

            res.cookie("HASH", "0");    // places a cookie!
            res.redirect("/Player.html");
            return "";
        });

        post("/login", (req, res) -> {
            String usr = req.queryParams("username");
            String pwd = req.queryParams("password");

            System.out.println(usr + ", " + pwd);

            boolean valid = false;

            for(int i = 0; i < players.size(); i++)
            {
                if(usr.equals(players.get(i).getUsername())) {
                    if(pwd.equals(players.get(i).getPassword())) {
                        valid = true;
                    }
                }
            }

            if(valid) {
                System.out.println("valid!");
                res.cookie("HASH", "0");    // places a cookie!
                res.redirect("/Player.html");    // should eventually be User's page
            } else {
                System.out.println("invalid!");
                res.redirect("/Login.html");
            }
            return "";  // doesn't override the redirect above, but is this necessary/dangerous?
        });

        post("/logout", (req, res) -> {
            res.removeCookie("HASH");
            res.redirect("/");
            return "";
        });

        Spark.get("/map/getBoard", (req, res) -> {
            logger.info("GET request to /map/getBoard");
            int[][] map;
<<<<<<< HEAD
            //    if(m == null) { //if map has not been initialized, this initializes the map
            m = new Map(0, 0); //map initialized here if not initialized previously
            //    }
            map = m.getMainMap();
=======
        //    if(m == null) { //if map has not been initialized, this initializes the map
                m = new Map(0, 0); //map initialized here if not initialized previously
        //    }
            map = m.getMap();
>>>>>>> Beta

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