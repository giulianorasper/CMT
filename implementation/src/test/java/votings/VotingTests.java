package votings;

import main.Conference;
import org.junit.Before;
import org.junit.Test;
import voting.AnonymousVotingOption;
import voting.NamedVotingOption;
import voting.Voting;
import voting.VotingOption;
import voting.VotingStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class VotingTests {

    Conference conf;
    List<VotingOption> standardAnonymousOptions;
    List<VotingOption> standardNamedOptions;

    @Before
    public void createConference(){
        conf = new Conference(true);
        standardAnonymousOptions = new ArrayList<>();
        standardAnonymousOptions.add(new AnonymousVotingOption(0, "yes"));
        standardAnonymousOptions.add(new AnonymousVotingOption(0, "no"));

        standardNamedOptions = new ArrayList<>();
        standardNamedOptions.add(new NamedVotingOption(0, "yes"));
        standardNamedOptions.add(new NamedVotingOption(0, "no"));
    }

    @Test
    public void addMultipleVotings(){

        Voting annon = new Voting(standardAnonymousOptions, "Is this the real life?", false, 10);
        Voting named = new Voting(standardNamedOptions, "Is this the real life?", true, 10);

        conf.addVoting(annon);
        conf.addVoting(named);

        if(!conf.getVotings().contains(annon)){
            fail("A voting did not get registered");
        }
        if(!conf.getVotings().contains(named)){
            fail("A voting did not get registered");
        }

        if(annon.getStatus() != VotingStatus.Created){
            fail("wrong voting status");
        }

        if(!conf.startVoting(annon)){
            fail("Vote should have been started");
        }
        if(conf.startVoting(named)){
            fail("Started two votes at once");
        }

        if(named.getStatus() != VotingStatus.Created){
            fail("wrong voting status");
        }

        if(annon.getStatus() != VotingStatus.Running){
            fail("wrong voting status");
        }


        try {
            Thread.sleep(10 * 1000);
        }
        catch (InterruptedException e){
            fail("broken test");
        }

        if(annon.getStatus() != VotingStatus.Closed){
            fail("vote did not get closed");
        }



    }
}
