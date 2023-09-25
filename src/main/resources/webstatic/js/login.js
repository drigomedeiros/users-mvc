function initializeUserSession(data, xhr){
    
}

function login(event) {
    event.preventDefault();
    jQuery.ajax({
        url: "/app/login",
        type: "POST",
        data: {
            username: $("#username").val(),
            password: $("#password").val()
        },  
        success: function (data, message, xhr) {
            sessionStorage.setItem('Authorization', xhr.getResponseHeader('Authorization'));
            window.location.href = "/";
        },
        error: function (data) {
            $("#loginErrorDiv").attr('style', 'display: block;');
            $("#loginErrorMessage").html(data.responseText);
        }
    });
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

