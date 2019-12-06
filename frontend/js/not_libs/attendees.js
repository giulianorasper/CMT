import GetAllAttendeesRequestPacket from "../../communication/packets/admin/GetAllAttendeesRequestPacket.js";
import CommunicationManager from "../../communication/CommunicationManager.js";

$(document).ready( function() {

    function success(packet){
        console.log(packet)
        if(packet.result === "Valid"){
            printAttendeeList(packet.attendees);
        }
    }

    function fail() {
        console.log("Something went wrong during Get All Attendees Request.");
    }

    const requestPacket = new GetAllAttendeesRequestPacket();

    CommunicationManager.send(requestPacket, success, fail);

});

function printAttendeeList(attendeeList){

    console.log(attendeeList);

    var attendeeContainer = $('#attendeeList');

     for (var currAttendee of attendeeList){
        generateAttendee(currAttendee).appendTo(attendeeContainer);
     }


    function generateAttendee(attendee){
        return $('<tr data-toggle="collapse" data-target="#accordion" class="clickable">'+
                                            '<td>'+attendee.name+'</td>'+
                                            '<td>'+attendeeList.group+'</td>'+
                                            '<td>'+attendee.function+'</td>'+
                                            '<td>'+attendee.present+'</td>'+
                                        '</tr>'+
                                        '<tr>'+
                                            '<td colspan="4">'+
                                                '<div id="accordion" class="collapse">'+
                                                    '<h4 style="color:grey;">Residence: '+attendee.residence+'</h4>'+
                                                    '<h4 style="color:grey;">Email: '+attendee.email+'</h4>'+
                                                '</div>'+
                                            '</td>'+
                                        '</tr>');
    }

}


