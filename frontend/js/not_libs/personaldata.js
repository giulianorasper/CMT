import CommunicationManager from "../../communication/CommunicationManager.js";
import PersonalDataRequestPacket from "../../communication/packets/PersonalDataRequestPacket";

$(document).ready( function(){

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

function printPersonalData(attendee, $name, $email, $group, $function, $residence){
    //append all data to respective place
    $name.append(attendee.name);
    $email.append(attendee.email);
    $group.append(attendee.group);
    $function.append(attendee.function);
    $residence.append(attendee.residence);

}