import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class Player {

    private static String username;
    private static String password;

    public Player(String usr, String pwd){
        username = usr;
        password = pwd;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()   //really should not be like this
    {
        return password;
    }

    public void setUsername(String u)
    {
        username = u;
    }

    public void setPassword(String p)   //really should not be like this
    {
        password = p;
    }
}
