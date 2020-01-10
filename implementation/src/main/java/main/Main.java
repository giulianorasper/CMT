package main;


import communication.CommunicationManager;
import communication.CommunicationManagerFactory;
import user.Admin;
import user.Attendee;
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

    /**
     * Parse Arguments from main method and create a clean or persistent Conference
     * @param args
     */
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
                    case "normal": startNormalConference(true);
                        break;
                    case "normal-persistent": startNormalConference(false);
                        break;
                }
            }
        }
    }

    /** Starts a conference with a single admin (username and password 'test') **/
    private static void startNormalConference(boolean clean){
        conf = new Conference(clean);
        LinkedList<VotingOption> votingOptions = new LinkedList<>();
        NamedVotingOption o1 = new NamedVotingOption(0, "Dieter");
        NamedVotingOption o2 = new NamedVotingOption(1, "Peter");
        NamedVotingOption o3 = new NamedVotingOption(2, "Sarah");
        NamedVotingOption o4 = new NamedVotingOption(3, "Lorax");
        votingOptions.add(o1);
        votingOptions.add(o2);
        votingOptions.add(o3);
        votingOptions.add(o4);
        Voting voting = new Voting(votingOptions, "Who am I?", true);
        o1.setParent(voting);
        o2.setParent(voting);
        o3.setParent(voting);
        o4.setParent(voting);
        voting.startVote();
        conf.addVoting(voting);
        conf.update(voting);
        if(conf.getAllAdmins().isEmpty()) {
            conf.addAdmin(new Admin("Johannes Dieters", "johannesdieters@web.de", "admin", "RCDS", "66111 Saarbrücken, Mainzerstaße 45", "Admin", 0), "admin");
            conf.addAdmin(new Admin("Franz Mayer", "franzl@gmx.de", "FrMa", "RCDS", "66111 Saarbrücken, Donaustaße 45", "Admin", 1), "franz");
            conf.addAttendee(new Attendee("Peter Schmidt",  "peterschidt@t-online.de", "user", "Unicorn", "66271 Kleinblittersdorf, am Tälchen 12", "User", 2), "user");
            conf.addAttendee(new Attendee("Sabine Hein", "sahe@stud.uni-saarland.de", "SaHe", "Flower", "66181 Ensheim, Hauptstraße 12", "User", 3), "user2");
        }
        CommunicationManager w = new CommunicationManagerFactory(conf).enableDebugging().create();
        w.start();
        System.out.println("Press 'q' to close the server");
        try{
            char c = (char)System.in.read();
            System.out.println(c);
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
        System.out.println("Valid test ids : 'normal', 'normal-persistent'");
        System.exit(0);
    }

}
