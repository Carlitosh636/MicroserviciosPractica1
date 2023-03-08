let socket = new WebSocket("ws://"+window.location.host+"/tasks");
let currentID = 0;
let text = "";
socket.onopen = function (e) {
    console.log("WebSocket connection established");
};

socket.onmessage = function (event) {
    console.log(event.data);
};

socket.onerror = function (error) {
    console.log(`[error] ${error.message}`);
};

function createTask(){
    text = document.getElementById("message").value;
    console.log(text);
    fetch('http://localhost:8080/tasks/',{
        method: 'POST',
        body: JSON.stringify({
            id: currentID,
            content: text,
            progress: 0,
            completed: false
        }),
        headers: {
            "Content-type": "application/json"
        }
    })
    .then(response => response.json())
    .then(value => currentID = value)
    console.log(currentID)
}

function checkProgress(){
    socket.send(currentID);
}