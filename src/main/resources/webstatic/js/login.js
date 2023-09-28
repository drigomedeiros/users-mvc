function login(event) {
    return new Promise((resolve, reject) => {
        event.preventDefault();
    
        let login = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        if (login == "" || password == "") {
            showErrorMessage("Provide username and password");
            resolve("Provide username and password");
        } else {
            fetch(document.getElementById("formLogin").action, {
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
                    resolve("OK");
                } else {
                    response.text().then((text) => {
                        showErrorMessage(text);
                        resolve(text);
                    });
                }
            }).catch((error) => {
                showErrorMessage(error.message);
                resolve(error.message);
            });
        }
    });
}

function showErrorMessage(message){
    document.getElementById("loginErrorDiv").style = 'display: block;';
    document.getElementById("loginErrorMessage").innerHTML = message;
}

if(typeof require === "function") {
    module.exports = login;
}
