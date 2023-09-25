((global, jQuery) => {
    class SimpleApp {

        constructor(secured, applicationPath, loginPath) {
            this._APPLICATION_PATH = applicationPath;
            this._AUTHORIZATION_PATH = loginPath;
            this._secured = secured;
        }
    
        run() {
            if((this._secured && this.isUserAlreadyLoggedIn()) || !this._secured){
                this.getContent(this._APPLICATION_PATH, "body", false);
            } else {
                global.location.href = "login.html";
            }
        }
    
        getContent(url, targetElement, addLoading = true) {
            if(addLoading) {
                $(targetElement).html('<div style="text-align: center; padding-top: 10px;"><div class="spinner-border text-secondary" style="text-align: center; vertical-align: middle;"></div></div>');
            }
            let self = this;
            $.ajax({
                url: url,
                headers: {"Authorization": sessionStorage.getItem("Authorization")},
                type: "GET",
                success: function (data) {
                    $(targetElement).html(data);
                    self.addNavigationHooks(targetElement);
                },
                error: function (data) {
                    $(targetElement).html("<h1>ERROR</h1>");
                }
            });
        }
    
        addNavigationHooks(targetElement) {
            let self = this;
            $(targetElement + " .simpleNavigation").each(function () {
                var navigationElement = this;
                navigationElement.addEventListener("click", (event) => {
                    event.preventDefault();
                    self.getContent(event.target.getAttribute("simple-target"), "#appRoot");
                });
            });
        };
        
        login(event) {
            event.preventDefault();
            $.ajax({
                url: this._AUTHORIZATION_PATH,
                type: "POST",
                data: {
                    username: $("#username").val(),
                    password: $("#password").val()
                },  
                success: function (data, message, xhr) {
                    sessionStorage.setItem('Authorization', xhr.getResponseHeader('Authorization'));
                    window.location.href = "index.html";
                },
                error: function (data) {
                    $("#loginErrorDiv").attr('style', 'display: block;');
                    $("#loginErrorMessage").html(data.responseText);
                }
            });
        }
    
        isUserAlreadyLoggedIn() {
            return sessionStorage.getItem('Authorization') != null;
        }
    
    }
    
    global.simpleApp = new SimpleApp(true, "/app/", "/app/login");

})(window, jQuery);