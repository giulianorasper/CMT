import GetAllAttendeesRequestPacket from "../../communication/packets/admin/GetAllAttendeesRequestPacket.js";
import CommunicationManager from "../../communication/CommunicationManager.js";

$(document).ready( function() {

    var attendeeContainer =  $('#attendeeListID');

    function success(packet){
        if(packet.result === "Valid"){
            printAttendeeList(packet.attendees, attendeeContainer);
        }
    }

    function fail() {
        console.log("Something went wrong during Get All Attendees Request.");
    }

    const requestPacket = new GetAllAttendeesRequestPacket();

    CommunicationManager.send(requestPacket, success, fail);

});

function printAttendeeList(attendeeList, $listID){

    //List header
    $('<thead>\n' +
        '        <th>Name</th>\n' +
        '        <th>Group</th>\n' +
        '        <th>Function</th>\n' +
        '        <th>Present</th>\n' +
        ' </thead>\n' +
        '<tbody>').appendTo($listID);

    //Iterate over all given attendees
    for(var i = 0; i < attendeeList.length; i++){
        //Head of new entry
        var currAttendee = attendeeList[i];

        $('<tr data-toggle="collapse" data-target="#accordion" class="clickable">\n').appendTo($listID);

        //Add data related to list header
        $('<td>' + currAttendee.name + '</td>').appendTo($listID);

        $('<td>' + currAttendee.group + '</td>').appendTo($listID);

        $('<td>' + currAttendee.function + '</td>').appendTo($listID);

        $('<td>' + currAttendee.present + '</td>').appendTo($listID);

        //Add rest of the data to expandable accordion part
        $('</tr>\n' +
            '<tr>\n' +
            '<td colspan="4">\n' +
            '<div id="accordion" class="collapse">').appendTo($listID);

        $('<h4 style="color:grey;">Residence: ' + currAttendee.residence + '</h4>').appendTo($listID);
        $('<h4 style="color:grey;">Email:  ' + currAttendee.email + '</h4>').appendTo($listID);

        //Close blocks
        $('</div>\n' +
            '</td>\n' +
            '</tr>').appendTo($listID);
    }

    $('</tbody>').appendTo($listID);

}
