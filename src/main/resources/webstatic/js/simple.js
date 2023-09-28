class SimpleApp {

    constructor(mustache) {
        this._APPLICATION_PATH = "/";
        this._AUTHORIZATION_PATH = "/login.html";
        this._secured = false;
        this._mustache = mustache;
    }

    async run(secured, applicationPath, loginPath = "/login.html") {
        
        this._APPLICATION_PATH = applicationPath;
        this._AUTHORIZATION_PATH = loginPath;
        this._secured = secured;

        if((this._secured && this.isUserAlreadyLoggedIn()) || !this._secured){
            this.getContent(this._APPLICATION_PATH, document.body, false);
            return "Simple App is started";
        } else {
            window.location.href = "login.html";
            return "Simple App is secured and needs authentication";
        }

        
    }

    getContent(url, htmlElement, addLoading = true) {
        return new Promise((resolve, reject) => {
            if(document.getElementById("error.mustache") == undefined || document.getElementById("loading.mustache") == undefined){
                alert("the initial static html must have template scripts in head. Refer to documentation to get this done or use the default index.html file");
                reject("");
                return;
            }
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
                let serverContent = new DOMParser().parseFromString(data, "text/html");
                
                self.isServerContentValid(htmlElement, serverContent);
                 
                self.appendChildren(serverContent, htmlElement);
                self.executeScripts(htmlElement);
                self.addNavigationHooks(htmlElement);
                
                resolve(htmlElement.innerHTML);
                
            }).catch((error) => {
                if(error.code == 400){
                    alert(error.message);
                    htmlElement.innerHTML = "";
                } else {
                    htmlElement.innerHTML = self.getTemplateContent("error.mustache", {errorCode: "500", errorMessage: "Ooops! Service might be unavailable. Trying to fix it"});
                }
                
                reject(htmlElement.innerHTML);
            });
        });
    }

    isServerContentValid(htmlElement, serverContent){
        if(htmlElement instanceof HTMLBodyElement && serverContent.getElementById("contentRoot") == undefined) {
            throw {code: 400, message: "the initial content retrieved from server must have a html component with id contentRoot"};
        } else if(htmlElement instanceof HTMLBodyElement && serverContent.getElementsByClassName("simpleNavigation").length == 0) {
            throw {code: 400, message: "the initial content retrieved from server must have the application links defined with class simpleNavigation and simple-target attribute pointing to a server route"};
        } else if(htmlElement instanceof HTMLBodyElement && !this.allSimpleNavigationHaveSimpleTargetAttribute(serverContent.getElementsByClassName("simpleNavigation"))) {
            throw {code: 400, message: "the simpleNavigation links must have simple-target attribute pointing to a server route"};
        }
    }

    allSimpleNavigationHaveSimpleTargetAttribute(htmlElements) {
        let allHave = true;
        Array.from(htmlElements).forEach(element => {
            if(element.getAttribute("simple-target") == undefined){
                allHave = false;
                return;
            }
        });
        return allHave;
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
    