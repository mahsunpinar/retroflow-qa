## RetroFlow API tests: starter project

This is an empty project for writing API tests against your group's RetroFlow instance. It only exists so you have somewhere to work: a folder, Node.js and Playwright, ready to go. There is no RetroFlow code in here, because the application runs on its own server.

You need this in Block 2 if you are an **Automation** pair, Role 1 or Role 2 (the agent exercise) and you don't have an editor with an agent on your laptop. Everybody else can use the tool they already know, on their own laptop.

### Getting started in a Codespace

* Open this repo on GitHub, click **Code** → **Codespaces** → **Create codespace on main**. The first start takes a minute or two, because it installs everything.
* Copy `.env.example` to `.env` and put your group's URL in it. Your coach hands out the URLs.
* Open the Copilot chat panel and switch the chat from **Ask** to **Agent**.
* Run `npm test` in the terminal to check that everything works. The example test creates a team and fetches it again. If it passes, you're ready.

> You can't push to this repo, and you don't have to. Your work stays in your Codespace. Copy `bugs.md`, `decisions.md` and `prompt-log.md` to your team's shared place before you close it.

### What's in here

* `tests/`: your tests live here. There's one example to show the shape.
* `playwright.config.ts`: reads your group's URL from `RETROFLOW_URL`. You normally don't touch this.
* `.devcontainer/`: tells the Codespace to install Node.js, the Copilot extensions and the packages.

### Good to know

* We use Playwright's API testing here. That means the tests send HTTP requests directly. No browser is opened or needed.
* The endpoints are in `retroflow-api-reference.md`, which you got from your coach.
* Using Cypress or Newman instead? That's fine on your own laptop. In the Codespace, Playwright is the one we know works.
* Your Codespace stops by itself after 30 minutes of inactivity. Your files are still there when you start it again.
