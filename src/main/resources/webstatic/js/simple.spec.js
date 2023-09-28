/**
 * @jest-environment jsdom
 */
fetch = jest.fn();
alert = jest.fn();

describe("SimpleApp SPA", () => {

    beforeEach(() => {
        let location = window.location;
        delete global.window.location;
        global.window.location = Object.assign({}, location);

        jest.clearAllMocks();
    })
      
    test('should redirect to login page if the app is secured and there is no token on sessionStorage', async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");
        
        let simpleApp = new SimpleApp(Mustache);
        simpleApp.getContent = jest.fn().mockImplementation(() => Promise.resolve("my content"));

        let applicationMessage = await simpleApp.run(true, "index.html");

        expect(window.location.href).toBe("login.html");
        expect(applicationMessage).toBe("Simple App is secured and needs authentication");
    });

    test("should start the app if it's not secured", async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");

        fetch.mockImplementation(() => {
            const fakeResponse = "<div>Single Page Application</div><div id='contentRoot'>Where content will be displayed</div>";
            return Promise.resolve({
                ok: true,
                text: () => Promise.resolve(fakeResponse)
            });
        });

        let simpleApp = new SimpleApp(Mustache);
        simpleApp.getContent = jest.fn().mockImplementation(() => Promise.resolve("my content"));

        let applicationMessage = await simpleApp.run(false, "index.html");
        
        expect(applicationMessage).toBe("Simple App is started");
    });

    test("should insert the initial content in the body", async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");

        document.head.innerHTML = `<script id="error.mustache" type="x-tmpl-mustache">Error</script>
                                    <script id="loading.mustache" type="x-tmpl-mustache">Loading</script>`;

        fetch.mockImplementation(() => {
            const fakeResponse = "<div>Single Page Application</div><div id='contentRoot'>Where content will be displayed</div><a href='#' class='simpleNavigation' simple-target='/initial'>Initial</a>";
            return Promise.resolve({
                ok: true,
                text: () => Promise.resolve(fakeResponse)
            });
        });

        let simpleApp = new SimpleApp(Mustache);
        await simpleApp.getContent("/initial",document.body);
        
        expect(document.body.innerHTML).toBe('<div>Single Page Application</div><div id="contentRoot">Where content will be displayed</div><a href="#" class="simpleNavigation" simple-target="/initial">Initial</a>');
    });

    test("should alert if server initial page doesn't have a component with id contentRoot", async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");

        document.head.innerHTML = `<script id="error.mustache" type="x-tmpl-mustache">Error</script>
                                    <script id="loading.mustache" type="x-tmpl-mustache">Loading</script>`;

        alert.mockImplementation(message => console.log(message));

        fetch.mockImplementation(() => {
            const fakeResponse = "<div>Single Page Application</div><div>Where content will be displayed</div>";
            return Promise.resolve({
                ok: true,
                text: () => Promise.resolve(fakeResponse)
            });
        });

        let simpleApp = new SimpleApp(Mustache);
        
        expect.assertions(2);

        try {
            await simpleApp.getContent("/initial", document.body);
        } catch (htmlContent) {
            console.log(htmlContent);
            expect(htmlContent).toBe("");
            expect(alert).toBeCalledWith("the initial content retrieved from server must have a html component with id contentRoot");
        }
    });

    test("should alert if server initial page doesn't have simpleNavigation components", async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");

        document.head.innerHTML = `<script id="error.mustache" type="x-tmpl-mustache">Error</script>
                                    <script id="loading.mustache" type="x-tmpl-mustache">Loading</script>`;

        alert.mockImplementation(message => console.log(message));

        fetch.mockImplementation(() => {
            const fakeResponse = "<div>Single Page Application</div><div id='contentRoot'>Where content will be displayed</div>";
            return Promise.resolve({
                ok: true,
                text: () => Promise.resolve(fakeResponse)
            });
        });

        let simpleApp = new SimpleApp(Mustache);
        
        expect.assertions(2);

        try {
            await simpleApp.getContent("/initial", document.body);
        } catch (htmlContent) {
            console.log(htmlContent);
            expect(htmlContent).toBe("");
            expect(alert).toBeCalledWith("the initial content retrieved from server must have the application links defined with class simpleNavigation and simple-target attribute pointing to a server route");
        }
    });

    test("should alert if server initial page simpleNavigation links don't have simple-target attribute", async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");

        document.head.innerHTML = `<script id="error.mustache" type="x-tmpl-mustache">Error</script>
                                    <script id="loading.mustache" type="x-tmpl-mustache">Loading</script>`;

        alert.mockImplementation(message => console.log(message));

        fetch.mockImplementation(() => {
            const fakeResponse = "<div>Single Page Application</div><div id='contentRoot'>Where content will be displayed</div><a 'href='#' class='simpleNavigation'>Initial</a>";
            return Promise.resolve({
                ok: true,
                text: () => Promise.resolve(fakeResponse)
            });
        });

        let simpleApp = new SimpleApp(Mustache);
        
        expect.assertions(2);

        try {
            await simpleApp.getContent("/initial", document.body);
        } catch (htmlContent) {
            console.log(htmlContent);
            expect(htmlContent).toBe("");
            expect(alert).toBeCalledWith("the simpleNavigation links must have simple-target attribute pointing to a server route");
        }
    });

    test("should alert if static index.html does not have templates defined", async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");

        document.head.innerHTML = "";

        alert.mockImplementation(message => console.log(message));

        fetch.mockImplementation(() => {
            const fakeResponse = "<div>Single Page Application</div><div>Where content will be displayed</div>";
            return Promise.resolve({
                ok: true,
                text: () => Promise.resolve(fakeResponse)
            });
        });

        let simpleApp = new SimpleApp(Mustache);
        
        expect.assertions(2);

        try {
            await simpleApp.getContent("/initial", document.body);
        } catch (htmlContent) {
            expect(htmlContent).toBe("");
            expect(alert).toBeCalledWith("the initial static html must have template scripts in head. Refer to documentation to get this done or use the default index.html file");
        }
    });

    test("should execute scripts placed in partial server contents", async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");

        document.head.innerHTML = `<script id="error.mustache" type="x-tmpl-mustache">Error</script>
                                    <script id="loading.mustache" type="x-tmpl-mustache">Loading</script>`;
        document.body.innerHTML = "<div>Single Page Application</div><div id='contentRoot'>Where content will be displayed</div><a href='#' class='simpleNavigation' simple-target='/initial'>Initial</a>";
        
        alert.mockImplementation(message => console.log(message));

        fetch.mockImplementation(() => {
            const fakeResponse = '<div>Partial content rendered by the server</div><script language="javascript">alert("executed")</script>';
            return Promise.resolve({
                ok: true,
                text: () => Promise.resolve(fakeResponse)
            });
        });

        let simpleApp = new SimpleApp(Mustache);
        await simpleApp.getContent("/initial", simpleApp.findHtmlElement('contentRoot'));
        
        expect(document.getElementById('contentRoot').innerHTML).toBe('<div>Partial content rendered by the server</div><script language="javascript">alert("executed")</script>');
        expect(alert).toBeCalledWith("executed");
    });

    test("should enable simpleNavigation links to call getContent after being inserted", async () => {
        const Mustache = require("mustache");
        const SimpleApp = require("./simple");

        document.head.innerHTML = `<script id="error.mustache" type="x-tmpl-mustache">Error</script>
                                    <script id="loading.mustache" type="x-tmpl-mustache">Loading</script>`;
        document.body.innerHTML = "<div>Single Page Application</div><div id='contentRoot'>Where content will be displayed</div><a href='#' class='simpleNavigation' simple-target='/initial'>Initial</a>";
        
        alert.mockImplementation(message => console.log(message));

        fetch.mockImplementation(() => {
            const fakeResponse = '<a id="navigationLink" href="#" class="simpleNavigation" simple-target="/get-content-on-server">Partial content rendered by the server</a><script language="javascript">alert("executed")</script>';
            return Promise.resolve({
                ok: true,
                text: () => Promise.resolve(fakeResponse)
            });
        });

        let simpleApp = new SimpleApp(Mustache);
        await simpleApp.getContent("/initial", simpleApp.findHtmlElement('contentRoot'));
        
        expect(document.getElementById('contentRoot').innerHTML).toBe('<a id="navigationLink" href="#" class="simpleNavigation" simple-target="/get-content-on-server">Partial content rendered by the server</a><script language="javascript">alert("executed")</script>');
        expect(alert).toBeCalledWith("executed");

        simpleApp.getContent = jest.fn().mockImplementation(() => Promise.resolve("my content"));

        document.getElementById('navigationLink').click();

        expect(simpleApp.getContent).toBeCalledWith("/get-content-on-server", document.getElementById('contentRoot'));

    });

});