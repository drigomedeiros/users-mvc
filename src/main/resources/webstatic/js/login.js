function login(event) {
    event.preventDefault();
    
    let login = $("#username").val();
    let password = $("#password").val();
    
    if (login == "" || password == "") {
        showErrorMessage("Provide username and password");
        return;
    }
    
    jQuery.ajax({
        url: "/app/login",
        type: "POST",
        data: {
            username: login,
            password: password
        },  
        success: function (data, message, xhr) {
            sessionStorage.setItem('Authorization', xhr.getResponseHeader('Authorization'));
            window.location.href = "/";
        },
        error: function (data) {
            showErrorMessage(data.responseText);
        }
    });
}

function showErrorMessage(message){
    $("#loginErrorDiv").attr('style', 'display: block;');
    $("#loginErrorMessage").html(message);
}

function userAlreadyLoggedIn() {
    jQuery.ajax({
        url: "/app/home",
        type: "GET",
        headers: {
            'Authorization': sessionStorage.getItem('Authorization')
        },  
        success: function (data, message, xhr) {
            initializeUserSession(data, xhr);
        },
        error: function (data) {
            $("#loginErrorDiv").attr('style', 'display: block;');
            $("#loginErrorMessage").html(data.responseText);
        }
    });
}
