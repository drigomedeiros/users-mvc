class SimpleApp {

    constructor(mustache) {
        this._APPLICATION_PATH = "";
        this._AUTHORIZATION_PATH = "";
        this._secured = false;
        this._mustache = mustache;
    }

    run(secured, applicationPath, loginPath) {
        
        this._APPLICATION_PATH = applicationPath;
        this._AUTHORIZATION_PATH = loginPath;
        this._secured = secured;

        if((this._secured && this.isUserAlreadyLoggedIn()) || !this._secured){
            this.getContent(this._APPLICATION_PATH, document.body, false);
        } else {
            window.location.href = "login.html";
        }

    }

    getContent(url, htmlElement, addLoading = true) {
        if(addLoading) {
            htmlElement.innerHTML = this.getTemplateContent("loading.mustache", {});
        }
        let self = this;
        fetch(url, {
            headers: {"Authorization": sessionStorage.getItem("Authorization")},
            type: "GET"
        })
        .then((response) => response.text())
        .then((data) => {
            htmlElement.innerHTML = "";
            let parsedElement = new DOMParser().parseFromString(data, "text/html");
            self.appendChildren(parsedElement, htmlElement);
            self.executeScripts(htmlElement);
            self.addNavigationHooks(htmlElement);
        }).catch(() => {
            htmlElement.innerHTML = self.getTemplateContent("error.mustache", {errorCode: "500", errorMessage: "Ooops! Service might be unavailable. Trying to fix it"});
        });
    }

    appendChildren(parsedElement, htmlElement) {
        while(parsedElement.body.children.length > 0) {
            htmlElement.appendChild(parsedElement.body.children[0]);
        }
    }

    executeScripts(htmlElement) {
        Array.from(htmlElement.querySelectorAll("script")).forEach( oldScriptEl => {
            const newScriptEl = document.createElement("script");  
            Array.from(oldScriptEl.attributes).forEach( attr => {
                newScriptEl.setAttribute(attr.name, attr.value) 
            });
            const scriptText = document.createTextNode(oldScriptEl.innerHTML);
            newScriptEl.appendChild(scriptText);
            oldScriptEl.parentNode.replaceChild(newScriptEl, oldScriptEl);
        });
    }

    addNavigationHooks(htmlElement) {
        let self = this;
        let navigationLinks = htmlElement.getElementsByClassName("simpleNavigation");
        for(let i = 0; i < navigationLinks.length; i++){
            let element = navigationLinks[i];
            element.addEventListener("click", (event) => {
                event.preventDefault();
                self.getContent(event.target.getAttribute("simple-target"), self.findHtmlElement("contentRoot"));
            });
        }
    }

    getTemplateContent(template, params){
        let html = document.getElementById(template).innerHTML;
        return this._mustache.render(html, params);
    }

    findHtmlElement(selector){
        return document.getElementById(selector);
    }
    
    isUserAlreadyLoggedIn() {
        return sessionStorage.getItem('Authorization') != null;
    }

}

if(typeof require === "function") {
    module.exports = SimpleApp;
}
    