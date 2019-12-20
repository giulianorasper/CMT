import GetAllAttendeesRequestPacket from "../../communication/packets/admin/GetAllAttendeesRequestPacket.js";
import CommunicationManager from "../../communication/CommunicationManager.js";
import RemoveAttendeeRequestPacket from "../../communication/packets/admin/RemoveAttendeeRequestPacket.js";
import EditUserRequestPacket from "../../communication/packets/admin/EditUserRequestPacket.js";
import AddAttendeeRequestPacket from "../../communication/packets/admin/AddAttendeeRequestPacket.js";
import LogoutAttendeeRequestPacket from "../../communication/packets/admin/LogoutAttendeeRequestPacket.js";
import GenerateNewAttendeePasswordRequestPacket
    from "../../communication/packets/admin/GenerateNewAttendeePasswordRequestPacket.js";
import { getSortedList } from "./attendeeSorting.js";
import GenerateNewAttendeeTokenRequestPacket
    from "../../communication/packets/admin/GenerateNewAttendeeTokenRequestPacket.js";
import GetAttendeePasswordRequestPacket from "../../communication/packets/admin/GetAttendeePasswordRequestPacket.js";

$(document).ready( function() {
    //Move functions to global scope so onclick parameters can call them
    window.deleteAttendeeGlobal = deleteAttendee;
    window.editAttendeeGlobal = editAttendee;
    window.getNewAttendeePasswordGlobal = getNewAttendeePassword;
    window.logoutAttendeeGlobal = logoutAttendee;

    //Click functions in global scope
    window.clickCreateGlobal = clickCreate;
    window.clickEditGlobal = clickEdit;

    //Add listeners to buttons/dropdown menus that don't need to be generated dynamically
    document.getElementById("sortingOptions").addEventListener("change", changeSort, false);

    refresh();
});

//By default, always sort by attendee Name
var sortingRelation = 'attendeeName';


//----------------------------------- SENDING REQUEST AND SORTING METHODS ----------------------------------------------


/**
 * Sends a request to the server to get the current information about each and every attendee in the database.
 *
 * @return a list of attendee objects
 */
function updateAttendeeList(){
    function success(packet){
        if(packet.result === "Valid"){
            console.log(packet);
            sortAttendeeList(packet.attendees);
        }
        else{
            console.log(packet.details);
        }
    }

    function fail() {
        console.log("Something went wrong while trying to access the server.");
    }

    const requestPacket = new GetAllAttendeesRequestPacket();

    CommunicationManager.send(requestPacket, success, fail);
}

/**
 * Gets called by updateAttendeeList to sort the entries before printing them. Calls getSortedList from attendeeSorting,
 * uses the current sorting relation state. Calls generateAttendeeList after sorting.
 *
 * @param attendeeList that needs to be sorted.
 */
function sortAttendeeList(attendeeList){

    //Calls getSortedList from attendeeSorting.js
    const sortedList = getSortedList(attendeeList, sortingRelation);
    console.log(sortedList);
    generateAttendeeList(sortedList);
}


/**
 * Gets called from changeSort hook when dropdown option in user_management.html gets changed (Listener for that dropdown
 * menu got added in ready method of the document)
 *
 * @param relation to be sorted by.
 */
function sort(relation){
    //Sets general sorting relation to the new one
    sortingRelation = relation;

    //Refreshes page so list gets rendered with new sorting relation
    refresh();
}


//---------------------------------- RENDERING ATTENDEE LIST METHODS ---------------------------------------------------

/**
 * Gets called whenever the attendee list needs to reload.
 */
function refresh(){
    updateAttendeeList();
}


function generateAttendeeList(attendeeList){

    //console.log(attendeeList);

    const attendeeContainer = $('#attendeeList');

    //Replaces old list content with empty HTML
    $('#attendeeList').empty();

    //Generates new list content
    for (var currAttendee of attendeeList){
        generateAttendee(currAttendee).appendTo(attendeeContainer);
    }

}


function generateAttendee(attendee){
    return  $('<tr data-toggle="collapse" data-target="#user_accordion'+ attendee.ID +'" class="clickable">'+
        '<td>'+attendee.name+'</td>'+
        '<td>'+attendee.group+'</td>'+
        '<td>'+attendee.function+'</td>'+
        '<td>'+attendee.present+'</td>'+
        '</tr>'+
        '<tr>'+
        '<td colspan="4">'+
        '<div id="user_accordion'+ attendee.ID +'"  class="collapse">'+
        '<h4 style="color:grey;">Username: '+attendee.userName +'</h4>'+
        '<h4 style="color:grey;">Email: '+attendee.email+'</h4>'+
        '<h4 style="color:grey;">Residence: '+attendee.residence+'</h4>'+
        '<span style="display:inline-block; width: 30px;">' +
        '</span><span class="glyphicon glyphicon-pencil" onclick="clickEditGlobal(event, '+ attendee.ID + ')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-log-in" onclick="getNewAttendeePasswordGlobal(' + attendee.ID +')"></span>'+
        '<span style="display:inline-block; width: 30px;">' +
        '</span><span class="glyphicon glyphicon-log-out" onclick="logoutAttendeeGlobal(' + attendee.ID +')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-trash" onclick="deleteAttendeeGlobal(' + attendee.ID +')"></span>' +
        '</div>'+
        '</td>'+
        '</tr>');
}

/*
function addIconListeners(attendee) {
    document.getElementById("newPassword" + attendee.ID).addEventListener("click", clickNewPassword, false);
    document.getElementById("logout" + attendee.ID).addEventListener("click", clickLogout, false);
    document.getElementById("delete" + attendee.ID).addEventListener("click", clickDelete, false);
} */



//-------------------------------- FUNCTIONS TO BE CALLED BY GLYPHICONS ------------------------------------------------


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
            console.log(packet.details);
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
            console.log(packet.details);
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
    console.log("Data: " + name + " " + email + " " + group + " " + residence + " " + fnctn);

    function successCreateAttendee(packet) {
        if (packet.result === "Valid"){
            refresh();
            //console.log("the d")
        }
        else{
            console.log(packet.details);
        }
    }

    function failCreateAttendee(){
        console.log("Something went wrong while trying to access the server.");
    }

    CommunicationManager.send(createRequestPacket, successCreateAttendee, failCreateAttendee);
}

/**
 * Sends a request to logout an attendee. Note that after destroying the old token, a new one has to be generated. To
 * prevent as much concurrency as possible between sent requests, "Successful" message only gets sent after successfully
 * generating a new token.
 *
 * @param attendeeID
 */
function logoutAttendee(attendeeID){
    const logoutRequestPacket = new LogoutAttendeeRequestPacket(attendeeID);
    const newTokenRequestPacket = new GenerateNewAttendeeTokenRequestPacket(attendeeID);

    function successNewToken(packet){
        if(packet.result === "Valid"){
            alert("Attendee has successfully been logged out!");
        } else{
            console.log(packet.details);
        }
    }

    function successLogoutAttendee(packet){
        if(packet.result === "Valid"){
            CommunicationManager.send(newTokenRequestPacket, successNewToken, failLogoutAttendee);
        }
        else{
            console.log(packet.details);
        }
    }

    function failLogoutAttendee(){
        console.log("Something went wrong while trying to access the server.");
    }

    CommunicationManager.send(logoutRequestPacket, successLogoutAttendee, failLogoutAttendee);
}


/**
 * Gets called when a new password shall be generated for a certain attendee. Note that after generating a new password,
 * a separate request to get the password has to be sent.
 *
 * @param attendeeID of the attendee for which a new password shall be generated
 */
function getNewAttendeePassword(attendeeID){
    const getPasswordRequestPacket = new GetAttendeePasswordRequestPacket(attendeeID);
    const newPasswordRequestPacket = new GenerateNewAttendeePasswordRequestPacket(attendeeID);


    function successGetPassword(packet){
        if(packet.result === "Valid"){
            alert("New Attendee Password: " + packet.password);
        } else{
            console.log(packet.details);
        }
    }

    function successNewPassword(packet){
        if(packet.result === "Valid"){
            CommunicationManager.send(getPasswordRequestPacket, successGetPassword, failNewPassword);
        }
        else{
            console.log(packet.details);
        }
    }

    function failNewPassword(){
        console.log("Something went wrong while trying to access the server.");
    }

    CommunicationManager.send(newPasswordRequestPacket, successNewPassword, failNewPassword);
}



//----------------------------------------- HOOKS FOR ONCLICK EVENTS ---------------------------------------------------

/**
 * Hook that gets called by EventListener on Dropdown menu
 */
function changeSort(){
    const selectedOption = this.options[this.selectedIndex].value;

    sort(selectedOption);
}


function clickCreate(event){
    //TODO make this prettier than just using five prompts

    //Reloading gets prevented
    event.preventDefault();

    const name = prompt("Enter Name of the Attendee:");
    if(name === null){ return; }

    const email = prompt("Enter Email of the Attendee:");
    if(email === null){ return; }

    const group = prompt("Enter Group of the Attendee:");
    if(group === null){ return; }

    const residence = prompt("Enter Residence of the Attendee:");
    if(residence === null){ return; }

    const fnctn = prompt("Enter Function of the Attendee:");
    if(fnctn === null){ return; }

    //console.log("Data: " + name + " " + email + " " + group + " " + residence + " " + fnctn);

    createAttendee(name, email, group, residence, fnctn);
}

function clickEdit(event, attendeeID){
    //TODO make this prettier than just using five prompts

    //Reloading gets prevented
    event.preventDefault();

    const name = prompt("Enter new Name of the Attendee:");
    if(name === null){ return; }

    const email = prompt("Enter new Email of the Attendee:");
    if(email === null){ return; }

    const group = prompt("Enter new Group of the Attendee:");
    if(group === null){ return; }

    const residence = prompt("Enter new Residence of the Attendee:");
    if(residence === null){ return; }

    const fnctn = prompt("Enter new Function of the Attendee:");
    if(fnctn === null){ return; }

    editAttendee(attendeeID, name, email, group, residence, fnctn);Did 
}

