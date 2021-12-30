let stompClient = null;
let userName = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#name").prop("disabled", connected)
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        $("#sendingMessage").show();
    }
    else {
        $("#conversation").hide();
        $("#sendingMessage").hide();
    }
    $("#messages").html("");
}

function connect() {
    let name = $("#name").val();
    console.log(name);
    if (name === "") {
        return;
    }
    setName(name)
    let socket = new SockJS('/messager');

    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages',
                greeting => showMessages(JSON.parse(greeting.body).userName + "> " + JSON.parse(greeting.body).content));
    });
    sayHello();
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function setName(name) {
    userName = name;
    console.log("userName = " + userName);
}

async function sayHello() {
    await new Promise(resolve => setTimeout(resolve, 1000))
    stompClient.send('/app/hello', {}, JSON.stringify(
        {'userName': userName}));
}

function showMessages(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>");
}

function sendMessage() {
    let messageInput = $("#message");
    if (messageInput.val() === '') {
        return;
    }
    stompClient.send("/app/message", {}, JSON.stringify(
        {'userName': userName, 'content': messageInput.val()}));
    messageInput.val('');
}

$(function () {
    $("form").on('submit', e => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#sendMessage" ).click(() => sendMessage());
});

