import CommunicationManager from "../../communication/CommunicationManager.js";
import GetPreviousVotingsRequestPacket from "../../communication/packets/GetPreviousVotingsRequestPacket.js";


export function getPreviousVote(){

	function success(packet){

        if(packet.result === "Valid"){
			
			displayPreviousVotes(packet);


		}

    }

    function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    }

    const previousVote = new GetPreviousVotingsRequestPacket();

    CommunicationManager.send(previousVote, success, fail); 
}

$(document).ready(getPreviousVote);
    
function displayPreviousVotes(packet){
    console.log(packet);
    

    for(var i in packet.votings){

    var voteQuestion = '<div class="card" style="margin-bottom:0px;">'+
                    '<div class="card-header" id="headingTwo" style="padding-top: 25px;">'+
                    '<h5 class="mb-0" style="margin-left: 30px;">'+
                        packet.votings[i].question +
                    '<button class="btn btn-link collapsed" data-toggle="collapse"'+
                    'data-target="#collapse'+packet.votings[i].ID+'" aria-expanded="false" aria-controls="collapse'+packet.votings[i].ID+'" style="word-break: break-word;">'+
                    '<i>see full details</i>'+
                    '</button>'+
                    '</h5>'+
                    '</div>'+
                    '<div id="collapse'+packet.votings[i].ID+'" class="collapse" aria-labelledby="headingTwo" data-parent="#accordion">' +
                    '<ul>' +
                    '<div class="card-body" id="'+packet.votings[i].ID+'prev">'+

                    '</div>'+
                    '</ul>' +
                    '</div>'+
                    '</div>';

        $('#accordion').append(voteQuestion);

        var totalAttendees = 0;

        for( var k in packet.votings[i].options){
            totalAttendees =  totalAttendees + packet.votings[i].options[k].publicVotes;
        }

        var voteOptions = '<pre>' +'Total Numbers of Voters:   ' + totalAttendees  + '</pre>';;
        $('#'+packet.votings[i].ID+'prev').append(voteOptions);

        //+ 'Vote Option ' + (packet.votings[i].options[j].optionID + 1) + ': '
        for(var j in packet.votings[i].options){
            voteOptions =  '<pre>' + 'Vote Option ' + (packet.votings[i].options[j].optionID + 1) + ': '
                                + packet.votings[i].options[j].name + '\nNumber of voters: '
                                + packet.votings[i].options[j].publicVotes + '\n'
                                + Math.round((packet.votings[i].options[j].publicVotes/totalAttendees)*100) + '% of the total voters'
                                + '\nName of Voters: ' + packet.votings[i].options[j].votersname
                                // + '<br>'
                                + '</pre>';
            // if(packet.votings[i].namedVote){
            //     for(var name in packet.votings[i].options[k].voters) {
            //         var namesOfAttendee = namesofAttendee + packet.votings[i].options[k].voters[name]  
            //     }
            // }

            $('#'+packet.votings[i].ID+'prev').append(voteOptions);
        }

    }

}