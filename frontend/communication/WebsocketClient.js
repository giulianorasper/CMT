document.getElementById("demo").innerHTML = 5 + 6;

var connection = new WebSocket('ws://127.0.0.1:17699');

connection.onopen = function () {
    console.log('Connected!');
    connection.send('{"username":"theAnswer","password":"42","packetType":"LOGIN_REQUEST_PACKET"}');
};

// Log errors
connection.onerror = function (error) {
    console.log('WebSocket Error ' + error);
};

// Log messages from the server
connection.onmessage = function (e) {
    console.log('Server: ' + e.data);
};