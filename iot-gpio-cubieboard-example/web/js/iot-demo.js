$(document).ready(function(){
	$("#connect_clientId").val("example-"+(Math.floor(Math.random() * 100000)));
	if( !window.WebSocket) {
		$("#connect").html("\
			<h1>Get a new Web Browser!</h1>\
			<p>\
			Your browser does not support WebSockets. This example will not work properly.<br>\
			Please use a Web Browser with WebSockets support (WebKit or Google Chrome).\
			</p>\
		");
	} else {
		var client, bellDestination, doorDestination;
		$('#connect_form').submit(function() {
			var host = $("#connect_host").val();
			var port = $("#connect_port").val();
			var clientId = $("#connect_clientId").val();
			var user = $("#connect_user").val();
			var password = $("#connect_password").val();
			bellDestination = "IotDemoDoorBell";
			doorDestination = "IotDemoDoorLock";

			client = new Messaging.Client(host, Number(port), clientId);
			client.onConnect = onConnect;
			client.onMessageArrived = onMessageArrived;
			client.onConnectionLost = onConnectionLost;
			client.connect({
				userName:user,
				password:password,
				onSuccess:onConnect,
				onFailure:onFailure
			});
			return false;
		});

		function updateTemp(){
			$.ajax({
				// The REST interface providing the current temperature value (e.q. from DS18B20 temperature sensor)
				url: "http:/127.0.0.1:8080/rest/temperature",
				type: "GET",
					success : function(response) {
					$('#temperature').html(response);
				}
			});
			return false;
		}

		// the client is notified when it is connected to the server.
		var onConnect = function(frame) {
			debug("connected to MQTT");
			updateTemp();
			var timer = $.timer(function() {
				updateTemp();
			});
			timer.set({ time : 1000, autostart : true });

			$('#connect').fadeOut({ duration: 'fast' });
			$('#connected').fadeIn();
			client.subscribe(bellDestination);
		};

		// this allows to display debug logs directly on the web page
		var debug = function(str) {
			$("#debug").append(document.createTextNode(str + "\n"));
		};

		$('#disconnect').click(function() {
			client.disconnect();
			$('#connected').fadeOut({ duration: 'fast' });
			$('#connect').fadeIn();
			$("#messages").html("")
			return false;
		});

		function clearPictureNotification(){
			$("#iot-picture").css("border", "5px solid white");
		}

		$('#send_form').submit(function() {
			message = new Messaging.Message("");
			message.destinationName = "IotDemoDoorLock";
			debug("Sending MQTT message [" + message.payloadString + "] to {" + message.destinationName+"}");
			client.send(message);
			clearPictureNotification();
			return false;
		});

		$('#ignore').click(clearPictureNotification);

		function onFailure(failure) {
			debug("failure");
			debug(failure.errorMessage);
		}

		function onMessageArrived(message) {
			debug("Message arrived from {" + message.destinationName + "}: [" + message.payloadString + "]");
			var p = document.createElement("p");
			var t = document.createTextNode("Latest Bell Event Occured: " + new Date());
			p.appendChild(t);
			$("#messages").html(p);
			$("#iot-picture").attr("src", message.payloadString).css("border", "5px solid red");
		}

		function onConnectionLost(responseObject) {
			if (responseObject.errorCode !== 0) {
				debug(client.clientId + ": " + responseObject.errorCode + "\n");
			}
		}
	}
});
