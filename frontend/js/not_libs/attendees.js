import GetAllAttendeesRequestPacket from "../../communication/packets/admin/GetAllAttendeesRequestPacket.js";
import CommunicationManager from "../../communication/CommunicationManager.js";
import RemoveAttendeeRequestPacket from "../../communication/packets/admin/RemoveAttendeeRequestPacket.js";

$(document).ready( function() {
    const attendees = getAttendeeList();
    generateAttendeeList(attendees);
});

function generateAttendeeList(attendeeList){

    console.log(attendeeList);

    var attendeeContainer = $('#attendeeList');

    var i = 0;
     for (var currAttendee of attendeeList){
        generateAttendee(currAttendee, i).appendTo(attendeeContainer);
        i++;
     }

}

function generateAttendee(attendee, id){
    return $('<tr data-toggle="collapse" data-target="#user_accordion'+ id +'" class="clickable">'+
        '<td>'+attendee.name+'</td>'+
        '<td>'+attendee.group+'</td>'+
        '<td>'+attendee.function+'</td>'+
        '<td>'+attendee.present+'</td>'+
        '</tr>'+
        '<tr>'+
        '<td colspan="4">'+
        '<div id="user_accordion'+ id +'"  class="collapse">'+
        '<h4 style="color:grey;">Residence: '+attendee.residence+'</h4>'+
        '<h4 style="color:grey;">Email: '+attendee.email+'</h4>'+
        '</div>'+
        '</td>'+
        '</tr>');
}


/**
 * Can be called by "onclick" param of glyphicon for each attendee, sends request to the server to delete the attendee
 * with corresponding ID. In case the deleting went on successfully, the attendee list will reload itself.
 *
 * @param attendeeID: ID of the attendee to be deleted.
 */
function deleteAttendee(attendeeID){
    const requestPacket = new RemoveAttendeeRequestPacket(attendeeID);

    function successDeleteAttendee(packet){
        if(packet.result === "Valid"){
            const attendees = getAttendeeList();
            generateAttendeeList(attendees);
        }
        else{
            console.log("Something went wrong while trying to attendee " + attendeeID);
        }
    }

    function failDeleteAttendee(){
        console.log("Something went wrong when trying to access the server.");
    }

    CommunicationManager.send(requestPacket, successDeleteAttendee, failDeleteAttendee);

}

/**
 * Sends an EditAttendeeRequest to the server. If the operation was successful,
 *
 * @param attendeeID represents the ID of the attendee that is to edit
 * @param name represents the (new) name of the attendee
 * @param email represents the (new) email of the attendee
 * @param group represents the (new) group of the attendee
 * @param residence represents the (new) residence of the attendee
 * @param fnctn represents the (new) function of the attendee
 */

function editAttendee(attendeeID, name, email, group, residence, fnctn){

}


/**
 * Sends a request to the server to get the current information about each and every attendee in the database.
 *
 * @return a list of attendee objects
 */
function getAttendeeList(){
    function success(packet){
        if(packet.result === "Valid"){
            return packet.attendees;
        }
        else{
            console.log("You aren't authorized to get the attendee list.")
        }
    }

    function fail() {
        console.log("Something went wrong while trying to access the server.");
    }

    const requestPacket = new GetAllAttendeesRequestPacket();

    CommunicationManager.send(requestPacket, success, fail);
}


