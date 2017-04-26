var stompClient = null;
var client = 0;
var canvas = null;
var colaborador = null;

function connect() {
    var socket = new SockJS('/stompendpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/newpoint.' + client, function (data) {
            var theObject = JSON.parse(data.body);
            var c = document.getElementById("myCanvas");
            var ctx = c.getContext("2d");
            ctx.beginPath();
            ctx.arc(theObject['x'], theObject['y'], 1, 0, 2 * Math.PI);
            ctx.stroke();

        });

        stompClient.subscribe('/topic/newpolygon.' + client, function (data) {
            var canvas = document.getElementById('myCanvas');
            var puntos = JSON.parse(data.body);
            var c2 = canvas.getContext('2d');
            c2.fillStyle = '#f00';
            c2.beginPath();
            c2.moveTo(puntos[0]['x'], puntos[0]['y']);
            for (var i = 1; i < puntos.length; i++) {
                c2.lineTo(puntos[i]['x'], puntos[i]['y']);
            }
            c2.moveTo(puntos[0]['x'], puntos[0]['y']);
            c2.closePath();
            c2.fill();

        });


        stompClient.subscribe('/topic/newpoint', function (data) {
            var theObject = JSON.parse(data.body);

            var c = document.getElementById("myCanvas");
            var ctx = c.getContext("2d");
            ctx.beginPath();
            ctx.arc(theObject['x'], theObject['y'], 1, 0, 2 * Math.PI);
            ctx.stroke();

        });

        stompClient.subscribe('/topic/newpolygon', function (data) {
          var canvas = document.getElementById('myCanvas');
          var puntos = JSON.parse(data.body);
          var c2 = canvas.getContext('2d');
          c2.fillStyle = '#f00';
          c2.beginPath();
          c2.moveTo(puntos[0]['x'], puntos[0]['y']);
          for (var i = 1; i < puntos.length; i++) {
              c2.lineTo(puntos[i]['x'], puntos[i]['y']);
          }
          c2.moveTo(puntos[0]['x'], puntos[0]['y']);
          c2.closePath();
          c2.fill();

        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function changeTopic() {
    client = document.getElementById("IdTopic").value;
    colaborador = document.getElementById("Nombre").value;
    canvas.width = canvas.width;
    disconnect();
    doPost();
    connect();
}

doPost = function(){
  var ob = {"sala":client, "nombre":colaborador};
  var postPromise=$.ajax({
      url: "/dibujos/"+client+"/colaboradores",
      type: 'POST',
      data: JSON.stringify(ob),
      contentType: "application/json",
  });
  postPromise.then(
    function(){
      console.info("OK");
    },
    function(){
      console.info("ERROR");
    }
  );
  return postPromise;
}

$(document).ready(
        function () {
            connect();
            console.info('connecting to websockets');

            canvas = document.getElementById('myCanvas');
            var context = canvas.getContext('2d');

            function getMousePos(canvas, evt) {
                var rect = canvas.getBoundingClientRect();
                return {
                    x: evt.clientX - rect.left,
                    y: evt.clientY - rect.top
                };
            }

            canvas.addEventListener('mousedown', function (evt) {
                var mousePos = getMousePos(canvas, evt);
                var punto = {'x': mousePos.x, 'y': mousePos.y};
                stompClient.send("/app/newpolygon." + client, {}, JSON.stringify({'x': mousePos.x, 'y': mousePos.y}));
            }, false);

        }
);
