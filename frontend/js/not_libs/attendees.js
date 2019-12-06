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

    var i = 0;
     for (var currAttendee of attendeeList){
        generateAttendee(currAttendee, i++).appendTo(attendeeContainer);
     }


    function generateAttendee(attendee, id){
        return $('<tr data-toggle="collapse" data-target="#user_accordion'+i +'" class="clickable">'+
                                            '<td>'+attendee.name+'</td>'+
                                            '<td>'+attendee.group+'</td>'+
                                            '<td>'+attendee.function+'</td>'+
                                            '<td>'+attendee.present+'</td>'+
                                        '</tr>'+
                                        '<tr>'+
                                            '<td colspan="4">'+
                                                '<div id="user_accordion'+i +'"  class="collapse">'+
                                                    '<h4 style="color:grey;">Residence: '+attendee.residence+'</h4>'+
                                                    '<h4 style="color:grey;">Email: '+attendee.email+'</h4>'+
                                                '</div>'+
                                            '</td>'+
                                        '</tr>');
    }

}


