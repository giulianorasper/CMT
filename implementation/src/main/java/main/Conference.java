package main;

import agenda.Agenda;
import document.Document;
import request.Request;
import user.User;
import voting.Voting;

import java.sql.Time;
import java.util.List;

public class Conference {

    private String name;
    private String organizer;

    private Time startsAt;
    private Time endsAt;

    private Agenda agenda;
    private List<Voting> votings;
    private List<Document> documents;
    private List<User> users;
    private List<Request> requests;
}
