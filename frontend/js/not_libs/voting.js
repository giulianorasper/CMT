import CommunicationManager from "../../communication/CommunicationManager.js";
import GetActiveVotingRequestPacket from "../../communication/packets/GetActiveVotingRequestPacket.js";
import AddVoteRequestPacket from "../../communication/packets/AddVoteRequestPacket.js";
import GetVotingsRequestPacket from "../../communication/packets/admin/GetVotingsRequestPacket.js";
import AddVotingRequestPacket from "../../communication/packets/admin/AddVotingRequestPacket.js";
import EditVotingRequestPacket from "../../communication/packets/admin/EditVotingRequestPacket.js";
import RemoveVotingRequestPacket from "../../communication/packets/admin/RemoveVotingRequestPacket.js";
import StartVotingRequestPacket from "../../communication/packets/admin/StartVotingRequestPacket.js";

var createdVotesContainer = $("#createdVotesList");

var votings;

$(document).ready( function() {

   renderVotings();

    window.createNewVoting = create;
    window.addVotingOption = addOption;
    window.saveVoting = save;
    window.deleteVote = remove;
    window.startVote = start;
    window.deleteVotingOption = deleteOption;
	
	

});

function save(voteId){
	var vote;

	for(var _vote of votings){
		if(_vote.ID == voteId){
			vote = _vote;
			break;
		}
	}

	var options =[]

	for(var elem of $("#votingOptions"+voteId).children()){
		var option = $($($(elem).children()[0]).children()[0]).val();
		options.push(option)
	}

	const packet = new EditVotingRequestPacket(voteId, vote.question, options ,vote.named, vote.duration);

	function success(packet){
		console.log(packet);
	}

	 function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    }

    CommunicationManager.send(packet, success, fail);
}

function remove(voteId){
    const packet = new RemoveVotingRequestPacket(voteId);

    function success(packet){
    	if(packet.result === "Valid"){
			console.log(packet);
            renderVotings();

        }
    }

     function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    } 

    CommunicationManager.send(packet, success, fail);
}

function renderVotings(){
	 function success(packet){
        if(packet.result === "Valid"){
        	createdVotesContainer.html("");
        	votings = packet.votings;

            for(var voting of packet.votings){
            	if(voting.status === "Created"){

					renderCreatedVote(voting)
            	}
            }

        }
    }

    function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    }

    const packet = new GetVotingsRequestPacket();

    CommunicationManager.send(packet, success, fail);
}

function addOption(voteId){
	const optionsField = $("#votingOptions"+voteId);
	
	
	$("<div class = \"col-md-4\"><div><input style=\"font-size:20px; margin-top: 5px;\" class=\"form-control\" "+
		"type=\"text\" value=\"\" placeholder = \"Please provide the voting option\"></div></div>").appendTo(optionsField);
}

// function deleteOption will be called when you want to delete an option per Vote question.

function deleteOption(voteId) {
	
	$("#votingOptions"+voteId).children().last().remove();
}

function create(){
	var res = prompt("Please provide the voting question");
	if(!res){return;}

	const packet = new AddVotingRequestPacket(res, [], confirm("named vote?"), 1000*60*5);
	CommunicationManager.send(packet, success, fail);

	 function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    }

	function success(packet){
 		if(packet.result === "Valid"){
            renderVotings();

        }
	}
}

function start(voteId){
	
	console.log*(voteId);

    function success(packet){

    	if(packet.result === "Valid"){
			
			console.log("testing");
            // renderVotings();

        }
    }

     function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    } 

	const startVote = new StartVotingRequestPacket(voteId);
    CommunicationManager.send(startVote, success, fail);
}

function renderCreatedVote(vote){
	var durationAux = (vote.duration/1000).toFixed(0);
	var secondsAux = (durationAux % 60).toFixed(0);
	var seconds = (secondsAux < 10 ? "0"+secondsAux:secondsAux);
	var durationMinutes = (durationAux/60).toFixed(0)+":"+seconds;

	$( "<tr data-toggle=\"collapse\" data-target=\"#user_accordion"+vote.ID+"\" class=\"clickable\">"+
                                        "<td>"+vote.question+"</td>"+
                                        "<td>"+(vote.namedVote?"Named":"Anonymous")+"</td>"+
                                        "<td>"+durationMinutes+"</td>"+
                                    "</tr>"+
                                    "<tr>"+
                                    "<td colspan=\"3\">"+
                                        "<div id=\"user_accordion"+vote.ID+"\"  class=\"collapse\">"+
                                            "<div style='padding:20px;' class = \"row\" id = \"votingOptions"+vote.ID+"\">"+
                                            "</div>"+
                                            "<button style=\"margin-right: 20px\" class=\"button button-contactForm boxed-btn\" onclick=\"startVote('"+vote.ID+"')\">Start Vote</button>"+
                                            "<button style=\"margin-right: 20px\" class=\"button button-contactForm boxed-btn\" onclick=\"addVotingOption('"+vote.ID+"')\">Add Option</button>"+
                                            "<button style=\"margin-right: 20px\" class=\"button button-contactForm boxed-btn\" onclick=\"deleteVotingOption('"+vote.ID+"')\">Delete Option</button>"+
                                            "<button style=\"margin-right: 20px\" class=\"button button-contactForm boxed-btn\" onclick=\"saveVoting('"+vote.ID+"')\">Save Changes</button>"+
                                            "<button style=\"margin-right: 20px\" class=\"button button-contactForm boxed-btn\" onclick=\"deleteVote('"+vote.ID+"')\">Delete</button>"+
                                            "</div>"+
                                        "</div>"+
                                    "</td>"+
                                    "</tr>"

		).appendTo(createdVotesContainer);

	const optionsField = $("#votingOptions"+vote.ID);
    for (var i = 0; i < vote.options.length; i++) {
        var option = vote.options[i];
		$("<div class = \"row\"><div><input style=\"font-size:20px; margin-top: 5px;\" class=\"form-control\" "+
		"type=\"text\" value=\""+option.name+"\"></div>").appendTo(optionsField);
	}

	 

}

		
