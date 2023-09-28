/**
 * @jest-environment jsdom
 */
fetch = jest.fn();
alert = jest.fn();
const mockedSessionStorage = {
    setItem: (key, value) => "OK"   
};
const submitEvent = {
    preventDefault: () => "OK"
};

const loginPage = new DOMParser().parseFromString(`
        <div id='loginPage'>
            <div id='loginErrorDiv' style='display: none;'>
            <p id='loginErrorMessage'></p>
            <form id='formLogin' action='/login'>
            <input id='username' type='text' value='user'/>
            <input id='password' type='password' value='password'/>
            <input id='loginButton' type='submit' value='Login'/>
            </form>
        </div>`, "text/html");

describe("Login script", () => {

    beforeEach(() => {
        let location = window.location;
        
        delete global.window.location;
        delete global.window.sessionStorage;
        
        global.window.location = Object.assign({}, location);
        global.window.sessionStorage = mockedSessionStorage;

        document.body.innerHTML = loginPage.body.innerHTML;

        jest.clearAllMocks();
    })
      
    test("should redirect to app index when login is successful", async () => {
        let login = require("./login");

        fetch.mockImplementation(() => {
            return Promise.resolve({
                ok: true,
                status: 200,
                headers: new Map(),
                json: () => Promise.resolve("{\"ok\": \"true\"}")
            });
        });

        let response = await login(submitEvent);

        expect(response).toBe("OK");
        expect(window.location.href).toBe("/");
    });

    test("should display server error message on #loginErrorMessage component", async () => {
        let login = require("./login");

        fetch.mockImplementation(() => {
            return Promise.resolve({
                ok: false,
                status: 401,
                headers: new Map(),
                text: () => Promise.resolve("login error")
            });
        });

        let response = await login(submitEvent);

        expect(response).toBe("login error");
        expect(document.getElementById('loginErrorDiv').style.display).toBe("block");
        expect(document.getElementById('loginErrorMessage').innerHTML).toBe("login error");
    });

    test("should show error when login server is unavailable", async () => {
        let login = require("./login");

        fetch.mockImplementation(() => {
            return Promise.reject({
                message: "service unavailable"
            });
        });

        let response = await login(submitEvent);

        expect(response).toBe("service unavailable");
        expect(document.getElementById('loginErrorDiv').style.display).toBe("block");
        expect(document.getElementById('loginErrorMessage').innerHTML).toBe("service unavailable");
    });

    test("should validate if username or password are not set", async () => {
        let login = require("./login");

        fetch.mockImplementation(() => {
            return Promise.resolve({
                ok: false,
                status: 401,
                headers: new Map(),
                text: () => Promise.resolve("login error")
            });
        });

        document.getElementById('username').value = "";
        document.getElementById('password').value = "";

        let response = await login(submitEvent);

        expect(response).toBe("Provide username and password");
        expect(document.getElementById('loginErrorDiv').style.display).toBe("block");
        expect(document.getElementById('loginErrorMessage').innerHTML).toBe("Provide username and password");
    });

});