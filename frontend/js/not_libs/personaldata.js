import CommunicationManager from "../../communication/CommunicationManager.js";
import PersonalDataRequestPacket from "../../communication/packets/PersonalDataRequestPacket.js";

/**
 * The furpose of the ready function is to assign id's to variables and to get packet from the server.
 * Then printPersonalData function will be called to pass packet data.
 * @param packet( with attendee details) 
 * @param profileName
 * @param profileEmail
 * @param profileGroup
 * @param profileFunction
 * @param profileResidence
 */



$(document).ready( function(){
	
	// All input values are assigned to variables.

    var profileName = $('#personalDataID');
    var profileEmail = $('#profileEmail');
    var profileGroup = $('#profileGroup');
    var profileFunction = $('#profileFunction');
    var profileResidence = $('#profileResidence');

    function success(packet){
        if(packet.result === "Valid"){
            printPersonalData(packet.attendee, profileName, profileEmail, profileGroup, profileFunction, profileResidence);
        }
    }

    function fail(){
        console.log("Something went wrong during Get Personal Data Request");
    }

    const requestPacket = new PersonalDataRequestPacket();

    CommunicationManager.send(requestPacket, success, fail)
});

/**
 * printPersonalData will print/display packet data.
 * It requires the following arguments
 * @param packet( with attendee details) 
 * @param profileName
 * @param profileEmail
 * @param profileGroup
 * @param profileFunction
 * @param profileResidence
 */


function printPersonalData(attendee, $name, $email, $group, $function, $residence){
    //append all data to respective place
    var insertName = $('<h2 class="contact-title">').appendTo($name);
    insertName.text(attendee.name);
    $('</h2>').appendTo(insertName);

    var insertEmail = $('<div style="font-size: large">').appendTo($email);
    insertEmail.text(attendee.email);

    var insertGroup = $('<div style="font-size: large">').appendTo($group);
    insertGroup.text(attendee.group);

    var insertFunction = $('<div style="font-size: large">').appendTo($function);
    insertFunction.text(attendee.function);

    var insertResidence = $('<div style="font-size: large">').appendTo($residence);
    insertResidence.text(attendee.residence);

}