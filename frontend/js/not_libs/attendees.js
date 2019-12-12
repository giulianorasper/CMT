import GetAllAttendeesRequestPacket from "../../communication/packets/admin/GetAllAttendeesRequestPacket.js";
import CommunicationManager from "../../communication/CommunicationManager.js";
import RemoveAttendeeRequestPacket from "../../communication/packets/admin/RemoveAttendeeRequestPacket.js";
import EditUserRequestPacket from "../../communication/packets/admin/EditUserRequestPacket.js";
import AddAttendeeRequestPacket from "../../communication/packets/admin/AddAttendeeRequestPacket.js";
import LogoutAttendeeRequestPacket from "../../communication/packets/admin/LogoutAttendeeRequestPacket.js";
import GenerateNewAttendeePasswordRequestPacket
    from "../../communication/packets/admin/GenerateNewAttendeePasswordRequestPacket.js";
import { getSortedList } from "./attendeeSorting.js";

$(document).ready( function() {
    refresh();
});

//By default, always sort by attendee Name
var sortingRelation = 'attendeeName';


/**
 * Sends a request to the server to get the current information about each and every attendee in the database.
 *
 * @return a list of attendee objects
 */
function updateAttendeeList(){
    function success(packet){
        if(packet.result === "Valid"){
            console.log(packet.attendees);
            sortAttendeeList(packet.attendees);
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
        '<span style="display:inline-block; width: 30px;">' +
        '</span><span class="glyphicon glyphicon-pencil" <!--onclick="editAttendee(...)"--> ></span>'+
        '<span style="display:inline-block; width: 60px;">'+
        '</span><span class="glyphicon glyphicon-log-in" onclick = "getNewAttendeePassword('+ attendee.ID +')"></span>'+
        '<span style="display:inline-block; width: 30px;">' +
        '</span><span class="glyphicon glyphicon-log-out" onclick = "logoutAttendee('+ attendee.ID +')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-trash" onclick = "deleteAttendee('+ attendee.ID +')"></span>' +
        '<h4 style="color:grey;">Email: '+attendee.email+'</h4>'+
        '</div>'+
        '</td>'+
        '</tr>');
}


/**
 * Can be called by "onclick" param of glyphicon for each attendee, sends request to the server to delete the attendee
 * with corresponding ID. In case the deleting went on successfully, the attendee list will reload.
 *
 * @param attendeeID: ID of the attendee to be deleted.
 */
function deleteAttendee(attendeeID){
    const requestPacket = new RemoveAttendeeRequestPacket(attendeeID);

    function successDeleteAttendee(packet){
        if(packet.result === "Valid"){
            refresh();
        }
        else{
            console.log("Something went wrong while trying to delete attendee " + attendeeID);
        }
    }

    function failDeleteAttendee(){
        console.log("Something went wrong while trying to access the server.");
    }

    CommunicationManager.send(requestPacket, successDeleteAttendee, failDeleteAttendee);

}


/**
 * Sends an EditAttendeeRequest to the server. If the operation was successful, the attendee list will reload.
 *
 * @param attendeeID represents the ID of the attendee that is to edit
 * @param name represents the (new) name of the attendee
 * @param email represents the (new) email of the attendee
 * @param group represents the (new) group of the attendee
 * @param residence represents the (new) residence of the attendee
 * @param fnctn represents the (new) function of the attendee
 */

function editAttendee(attendeeID, name, email, group, residence, fnctn){
    const editRequestPacket = new EditUserRequestPacket(attendeeID, name, email, group, residence, fnctn);

    function successEditAttendee(packet){
        if(packet.result === "Valid"){
            refresh();
        }
        else{
            console.log("Something went wrong while trying to edit attendee " + attendeeID);
        }
    }

    function failEditAttendee(){
        console.log("Something went wrong while trying to access the server.")
    }

    CommunicationManager.send(editRequestPacket, successEditAttendee, failEditAttendee);
}


/**
 * Sends a request to create a new attendee using the data given below. When the operation was successful, the attendee list will
 * reload.
 *
 * @param name of the attendee
 * @param email of the attendee
 * @param group of the attendee
 * @param residence of the attendee
 * @param fnctn of the attendee
 */
function createAttendee(name, email, group, residence, fnctn){
    const createRequestPacket = new AddAttendeeRequestPacket(name, email, group, residence, fnctn);

    function successCreateAttendee(packet) {
        if (packet.result === "Valid"){
            refresh();
        }
        else{
            console.log("Something went wrong while trying to create attendee " + name);
        }
    }

    function failCreateAttendee(){
        console.log("Something went wrong while trying to access the server.");
    }

    CommunicationManager.send(createRequestPacket, successCreateAttendee, failCreateAttendee);
}

/**
 * Sends a request to logout an attendee, reloading the attendee list in case it was successful.
 *
 * @param attendeeID
 */
function logoutAttendee(attendeeID){
    const logoutRequestPacket = new LogoutAttendeeRequestPacket(attendeeID);

    function successLogoutAttendee(packet){
        if(packet.result === "Valid"){
            refresh();
        }
        else{
            console.log("Something went wrong while trying to logout attendee " + attendeeID)
        }
    }

    function failLogoutAttendee(){
        console.log("Something went wrong while trying to access the server.");
    }

    CommunicationManager.send(logoutRequestPacket, successLogoutAttendee, failLogoutAttendee);
}


/**
 * Gets called when a new password shall be generated for a certain attendee. First, the attendee shall be logged out
 * since his old password shall not work anymore. After that, a request is sent to the server that responds with a new
 * password.
 *
 * @param attendeeID of the attendee for which a new password shall be generated
 */
function getNewAttendeePassword(attendeeID){
    const newPasswordRequestPacket = new GenerateNewAttendeePasswordRequestPacket(attendeeID);
    const logoutAttendeeRequestPacket = new LogoutAttendeeRequestPacket(attendeeID);

    function successNewPassword(packet){
        if(packet.result === "Valid"){
            //TODO print out the new password for the attendee
        }
        else{
            console.log("Something went wrong while trying to generate a new password for attendee " + attendeeID);
        }
    }

    function successLogout(packet){
        if(! (packet.result === "Valid")){
            console.log("Generating new password failed because attendee couldn't be logged out");
        }
    }

    function failNewPassword(){
        console.log("Something went wrong while trying to access the server.");
    }

    CommunicationManager.send(logoutAttendeeRequestPacket, successLogout, failNewPassword);
    CommunicationManager.send(newPasswordRequestPacket, successNewPassword, failNewPassword);
}


/**
 * Gets called by updateAttendeeList to sort the entries before printing them. Calls getSortedList from attendeeSorting,
 * generates the attendee List afterwards. Uses the current sorting relation state.
 *
 * @param attendeeList that needs to be sorted.
 */
function sortAttendeeList(attendeeList){

    //Calls getSortedList from attendeeSorting.js
    const sortedList = getSortedList(attendeeList, sortingRelation);
    generateAttendeeList(sortedList);
}


/**
 * Gets called from "Sort by" dropdown options in user_management.html, updates sorting relation and parses attendee List
 * yet again.
 *
 * @param relation to be sorted by.
 */
function sort(relation){
    //Sets general sorting relation to the new one
    sortingRelation = relation;

    //Refreshes page so list gets rendered with new sorting relation
    refresh();
}




/**
 * Gets called whenever the attendee list needs to reload.
 */
function refresh(){
    updateAttendeeList();
}

