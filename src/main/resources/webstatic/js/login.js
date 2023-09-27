function login(event) {
    event.preventDefault();
    
    let login = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    if (login == "" || password == "") {
        showErrorMessage("Provide username and password");
        return;
    }
    
    fetch("/app/login", {
        method: "POST",
        body: JSON.stringify({
            userLogin: login,
            userPassword: password
        }),
        headers: {"Content-type": "application/json"}
    })
    .then((response) => {  
        if (response.status == 200) {
            sessionStorage.setItem('Authorization', response.headers.get('Authorization'));
            window.location.href = "/";
        } else {
            response.text().then((text) => {
                showErrorMessage(new Error(text));
            });
        }
    }).catch((error) => {
        showErrorMessage(error.message);
    });
}

function showErrorMessage(message){
    document.getElementById("loginErrorDiv").style = 'display: block;';
    document.getElementById("loginErrorMessage").innerHTML = message;
}
