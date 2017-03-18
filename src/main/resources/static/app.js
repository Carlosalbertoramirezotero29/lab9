var stompClient = null;

function connect() {
    var socket = new SockJS('/stompendpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/newpoint', function (data) {
          var theObject=JSON.parse(data.body);
          alert("valor x: "+theObject['x'] + " valor y: "+theObject['y']);

        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendPoint(){
    var xval = document.getElementById("X").value;
    var yval = document.getElementById("Y").value;
    stompClient.send("/topic/newpoint", {}, JSON.stringify({'x':xval,'y':yval}));

}


$(document).ready(
        function () {
            connect();
            console.info('connecting to websockets');

        }
);
