package main;



public class Main {

    static Conference conf;

    public static void main(String[] args) {
        parseArguments(args);
    }

    private static void parseArguments(String[] args){
        if(args.length == 0){
            startConference();
        }
        else{
            printUsage();
        }
    }

    private static void startConference(){
        conf = new Conference();
    }

    private static void printUsage(){
        System.out.println("No arguments are currently supported");
        System.exit(1);
    }

}
