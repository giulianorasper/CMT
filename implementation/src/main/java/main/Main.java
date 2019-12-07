package main;


import communication.CommunicationManager;
import communication.CommunicationManagerFactory;
import user.Admin;

import java.io.IOException;

public class Main {

    static Conference conf;

    public static void main(String[] args) {
        parseArguments(args);
    }

    private static void parseArguments(String[] args){
        if(args.length != 2){
            printUsage();
        }
        else{
            if(!args[0].toLowerCase().equals("test")){
                printUsage();
            }
            else{
                switch (args[1]){
                    case "normal": startNormalConference();
                }
            }
        }
    }

    /** Starts a conference with a single admin (username and password 'test') **/
    private static void startNormalConference(){
        conf = new Conference();
        conf.addAdmin(new Admin("test", "test", "test", "test", "test", "test", 0), "test");
        CommunicationManager w = new CommunicationManagerFactory(conf).enableDebugging().create();
        w.start();
        System.out.println("Press 'q' to close the server");
        try{
            char c = (char)System.in.read();
            while (c != 'q'){
                c = (char)System.in.read();
            }
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsage(){
        System.out.println("Usage : 'test' <testID>");
        System.exit(1);
    }

}
