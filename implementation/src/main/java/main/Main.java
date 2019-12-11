package main;


import communication.CommunicationManager;
import communication.CommunicationManagerFactory;
import user.Admin;
import voting.NamedVotingOption;
import voting.Voting;
import voting.VotingOption;

import javax.naming.Name;
import java.io.IOException;
import java.util.LinkedList;

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
        LinkedList<VotingOption> votingOptions = new LinkedList<>();
        NamedVotingOption o1 = new NamedVotingOption();
        NamedVotingOption o2 = new NamedVotingOption();
        NamedVotingOption o3 = new NamedVotingOption();
        NamedVotingOption o4 = new NamedVotingOption();
        votingOptions.add(o1);
        votingOptions.add(o2);
        votingOptions.add(o3);
        votingOptions.add(o4);
        Voting voting = new Voting(votingOptions, "Who am I?", true);
        o1.setParent(voting);
        o2.setParent(voting);
        o3.setParent(voting);
        o4.setParent(voting);
        o1.changeName("I");
        o2.changeName("am");
        o3.changeName("the");
        o4.changeName("Lorax");
        voting.startVote();
        conf.addVoting(voting);
        conf.update(voting);
       // conf.addAdmin(new Admin("test", "test", "test", "test", "test", "test", 0), "test");
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
