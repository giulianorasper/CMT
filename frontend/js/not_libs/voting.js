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
	const packet = new StartVotingRequestPacket(voteId);

    function success(packet){
    	console.log(packet)
    	if(packet.result === "Valid"){
            renderVotings();

        }
    }

     function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    } 

    CommunicationManager.send(packet, success, fail);
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
                                            "<button style=\"margin-right: 20px\" class=\"button button-contactForm boxed-btn\" onclick=\"saveVoting('"+vote.ID+"')\">Save Changes</button>"+
                                            "<button style=\"margin-right: 20px\" class=\"button button-contactForm boxed-btn\" onclick=\"deleteVote('"+vote.ID+"')\">Delete</button>"+
                                            "</div>"+
                                        "</div>"+
                                    "</td>"+
                                    "</tr>"

		).appendTo(createdVotesContainer);

	const optionsField = $("#votingOptions"+vote.ID);
	for(option of vote.options){
		$("<div class = \"row\"><div><input style=\"font-size:20px; margin-top: 5px;\" class=\"form-control\" "+
		"type=\"text\" value=\""+option.name+"\"></div>").appendTo(optionsField);
	}

	 

}


/*function displayActiveVote(packet){
	// packet.exists,
	// packet.voting.id
	var dateObject = packet.voting.openUntil;
	
	if(packet.exists){

		var optionList = packet.voting.options;
		console.log(optionList[0]);
		
		var voteID = packet.voting.ID;
		
		//$("#voteQuestion").html('<h2 class="contact-title pull-left">'+ packet.voting.question + '</h2>')
		$("#voteQuestion").html('<div class="row"><div class="col-lg-2" style="float:left;"></div><div class="col-lg-10" style="float:left; padding-top: 50px;" id="'+packet.voting.ID+'s"><h2 class="contact-title pull-left">'+packet.voting.question+'</h2></div></div>');
		 
		for(var i in packet.voting.options){
		
		var questionOptions = '<div class="row"><div class="col-lg-2"></div><div class="custom-control custom-radio col-lg-10"><div class="form-group"><input type="radio" class="custom-control-input" id="'+packet.voting.options[i].optionID+'" checked name="radio" style="background:#2E004B;"><label class="custom-control-label" for="'+packet.voting.options[i].optionID+'">'+packet.voting.options[i].name+'</label></div></div></div>';
		$('#options').append(questionOptions);
		
		}	 
               		
	}
	else {
		
		$("#submit-message").empty();
		$("#submit-message").addClass("row").addClass("contact-title");
		$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Currently no active vote!</h2>");
	}
	
}


        //$("#form-submit").submit(function (e) {
        $("#submitButton").on("click", function () {
			
                //e.preventDefault();
				
				
				const selectedOptionId = $('input[name="radio"]:checked').attr('id');
				
				//const voteId = $('#voteQuestion').attr('id');
				//const voteId = $('#voteQuestion').find(":nth-child(2)"); 
				
				//console.log(selectedOptionId);
				//console.log(questionID);
				//console.log(voteId);
				//console.log(optionList[selectedOptionId].optionID);
				
				var voteDate = new Date(dateObject);
				var currentDateOnly = new Date();
				
				console.log(voteDate.toUTCString());
				console.log(currentDateOnly.toUTCString());
				
							//	if(voteDate.toUTCString() <= currentDateOnly.toUTCString())
								
				console.log(voteDate.toUTCString() <= currentDateOnly.toUTCString());

				    function success(packet){
						if(packet.result === "Valid" && voteDate.toUTCString() < currentDateOnly.toUTCString()){
							
							$("#submit-message").empty();
							$("#submit-message").addClass("row").addClass("contact-title");
							$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Vote Submitted!</h2>");
						}
						else if(packet.result === "Valid" && voteDate.toUTCString() >= currentDateOnly.toUTCString()) {
							$("#failure").html("<h4 style='float: right; margin-top:30px;'>Vote has been expired!</h4>");
							
						}
						else{
							$("#failure").html("<h4 style='float: right; margin-top:30px;'>You have already submitted vote!</h4>");	
						}
					}

					function fail() {
						console.log("sorry! your vote is not sumbiited");
					}
				
				const sendVote = new AddVoteRequestPacket(voteID, selectedOptionId);

				CommunicationManager.send(sendVote, success, fail);
				
				

        });*/
		