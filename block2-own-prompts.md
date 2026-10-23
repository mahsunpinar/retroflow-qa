# Block 2: Writing the prompts yourself
New pairs! You're now working with someone you haven't paired with yet today.

Your coach will tell you which track your pair is on: **Automation** (an agent writes and runs executable tests for you) or **Manual & Exploratory** (testing by hand, no code). If your group has two pairs on the same track, one pair takes Role 1 and the other takes Role 2.

Before you write a single prompt, take 5 minutes to read what your teammates made in Block 1: `test-plan.md`, the test case catalogue, `session-notes.md` and `bugs.md`. We're building on top of that, so don't start from scratch. It will probably also show you a few things you missed this morning.

**This block, you write the prompts yourself.** This morning you got them ready-made. Now we only tell you the goal, what a good prompt for that goal needs to contain, and what to check afterwards. How you word it is up to you.

> Keep a **prompt log** in `prompt-log.md`, in the same folder as your `bugs.md`. Every time you write a prompt, paste it in and add one line: did it work, or did you have to fix it, and what did you change? This is the start of your team's prompt library, and you'll use it again in Block 3.

> Stuck after two tries? Ask your coach for the example prompt for that step. Looking is allowed, just write down what the example does that yours didn't.

> **Watch your tokens.** Every message in a long chat gets read again by the AI, and an agent reads your files again on every step. So start a **new chat** for each step, and only paste what that step needs (your session notes, not the whole brief again). Your tool says you're out of requests? Don't wait for it to reset: work on your partner's machine or switch tools, and tell your coach.

---

# Automation track

In this track, both roles work with an **agent**. This is an AI course, so the agent writes and runs the tests and you steer it. Role 1 builds the business rule tests and diagnoses what fails. Role 2 attacks the API with negative, boundary and adversarial tests.

An agent is an AI that reads and edits several files by itself and can run commands. In Copilot you switch the chat from **Ask** to **Agent**. It makes a plan, and then works through it step by step. That changes your job quite a bit: you're no longer writing prompts that produce code, you're giving instructions, reviewing a plan and checking what it changed.

> **Where do you work?** An agent needs a folder with your test project in an editor that has an agent, like VS Code with GitHub Copilot in **Agent** mode. Your tests call your group's URL, so you don't need Java or the code of the app. Put the folder in a git repository (`git init`), because that is what makes the Source Control panel work.

> No editor with an agent on your laptop? Ask your coach for the link to the **RetroFlow tests starter**. Open it as a free **GitHub Codespace** (Code → Codespaces → Create codespace on main). It comes with Node.js and Playwright's API testing ready, and its README tells you the rest. Enable **GitHub Copilot Free** (no credit card) for agent mode. It's capped at 50 requests a month, which is enough for one block as long as you watch your tokens. Skip setup chatter in your prompts: the agent can run `npm test` itself.

> Your own editor works too, and then you choose the tool: Playwright, Cypress or Postman/Newman. Tell the agent which one in your first prompt. No idea? Playwright works for everyone, and the starter has it ready.

> There's one rule for this block: you don't edit any test by hand. The only exceptions are `prompt-log.md`, `bugs.md`, `decisions.md` and the `.env` file with your group's URL. The `.env` file is setup, not a test, so you make that one yourself. If the agent produces a weak test, you fix it by giving it another instruction. It will feel slower at times, but this is exactly how you find out where agents go wrong.

> Two pairs of your group share one app. Put your own initials in the names of the teams you create, so you don't trip over each other's data.

Open the **Source Control** panel on the left (or run `git diff` in a terminal). Every file the agent touched is listed there, and if you click one, you see exactly what changed. You'll use this all block long. It's how you check what an agent really did.

**The habit of this block:** never let an agent start changing files before you've seen its plan. Every step below starts with "plan first".

---

## Role 1: executable business rule tests and bug diagnosis

Your job: let the agent turn the core business rules into automated tests that run against the live API, and work out for every failure whether the app is wrong, or the test is.

Before you start, send one request to the app yourself so you know what the current state is. Don't write any code yet.

### Step 1: plan, then write the tests

**Goal:** executable tests for the four core business rules, running against the live API, which is your group's URL. Not unit tests on internal methods, real HTTP calls. First a plan, then the tests.

The four rules:
1. A team can only have one OPEN retrospective at a time
2. A retrospective can be closed, but never reopened
3. Feedback items cannot be added to, modified in or deleted from a CLOSED retrospective
4. A completed action item cannot be uncompleted

A good prompt for this:
* Tells the agent what the application is and where it runs
* Lists the four rules, in the words of the brief
* Gives it your endpoint list from Block 1, so it doesn't have to guess the paths
* Says in so many words that the tests have to call the running API over HTTP
* Wants the status code AND the body checked
* Says that nothing may be written yet: you want a plan first
* Names the tool you want to use, if you work in your own editor

Then check it:
* Read the plan. One test per rule, or more? Does it cover the failure case of every rule, or only the happy path? Tell the agent what to change before you let it continue.
* Then tell it to write the tests and run them against the live application.
* Go rule by rule. Does each test pass? If one fails, do NOT assume your test is wrong. Deciding that is exactly what this block is about.
* Look at rule 3. "Added to, modified in or deleted from" is three operations. Do you have three separate tests?

### Step 2: diagnose the failures

Some of your tests from step 1 will fail. Take them one at a time.

**Goal:** for every failing test, a verdict: a bug in the application, a bug in your test, or a rule that isn't clear enough and needs someone to decide.

A good prompt for this:
* Points the agent to the failing test, and asks it to show the actual response (status code and body) next to the business rule from the brief
* Asks for one of those three verdicts, and the reasoning behind it
* Tells it to reason from the response only, and not to guess at code it can't see
* Says it may not change the test or anything else yet. Only a verdict

Then check it:
* Don't accept the verdict before you've read the response body yourself.
* An agent likes a green suite. Watch for the moment it "fixes" a test by loosening the assertion. That hides a bug.
* Every genuine bug goes in `bugs.md`, with severity, exact reproduction steps (the request and the response) and business impact. "Test failed" is not a bug report.
* Every test that was wrong: tell the agent to fix it, and write a line in `decisions.md` about what you learned about reading the API contract.

### Step 3: edge cases and concurrency

**Goal:** an executable test, against the live API, for each of these:
1. Two requests to create a retrospective for the same team, fired at the same moment. Is "one OPEN at a time" still enforced?
2. Creating a team with an empty members list. What comes back?
3. Filtering action items on priority AND completed status together. Does the result satisfy both conditions, or just one?
4. Requesting a retrospective with an ID that doesn't exist. What's in the error response? Does it leak anything about the implementation (stack trace, class names, database error text)?

A good prompt for this:
* Describes each case, and what you expect to see
* For the race condition: says the requests really have to go out at the same time. A loop that sends one after the other proves nothing
* Asks what result would prove the rule holds, and what result would prove it doesn't
* For the error response: says what you count as a leak
* Asks for a plan first, and for the agent to stop after each case

Then check it:
* Run all four and read the actual responses. This is where several real bugs live.
* The race condition is the hardest one. Open the test in the Source Control panel and check that the requests are really sent at the same moment. If they aren't, tell the agent to fix the test itself, not just the assertion.
* Log everything you find in `bugs.md`. A real bug is a real bug, whether it was put there on purpose or not.

### Step 4: one operation at a time

**Goal:** three separate tests for the closed retrospective rule: add a feedback item, modify one and delete one, each on a CLOSED retrospective. You get a pass or fail for each of them, not one answer for all three.

A good prompt for this:
* Names the three operations, one test each
* Says what a correct response looks like (the request has to be rejected)
* Tells the agent to run them against the live API and report per test: pass or fail

Then check it:
* This is the most likely place to find a real gap. Read the results carefully, and don't assume the three behave the same just because they're related.
* Log every gap with full reproduction steps.
* Open the diff once more. Did the agent change something you didn't ask for?

---

## Role 2: let an agent expand the test suite

Your job: use the agent to add negative, boundary and adversarial tests across the whole API, in one coordinated operation. Everything goes through the agent, you don't write test cases by hand.

> Start from the tests the Role 1 pair built. Your group has no Role 1 pair? Then your first prompt asks the agent to create a small baseline suite for the four core rules (your Block 1 test case catalogue is good input), in the tool you know.

The test suite so far has the gaps that are common in AI-assisted testing:
1. **Shallow assertions.** Tests that only check the status code and not what's in the body
2. **No boundary tests.** Nothing with empty strings, very long strings, null versus a missing field, or edge values of an enum
3. **No adversarial input.** Nothing with malformed JSON, a wrong content type or unexpected extra fields
4. **No cross-entity checks.** Nothing that verifies that a retrospective really belongs to the team in the path (try another team's ID), or that feedback items belong to the right retrospective
5. **Tests that depend on each other.** Some tests may only pass in a certain order, or share state

### Step 1: plan before you act

Never let an agent start changing files before you've seen its plan. That's the habit of this block.

**Goal:** a plan, and no changed files yet. For each of the five gaps: which endpoints or scenarios need a new test, and why.

A good prompt for this:
* Tells the agent what the project is and which tests to look at
* Says in so many words that nothing may be written or changed yet
* Names the five gaps
* Asks for a plan per gap, not one long list

Then check it:
* Read the plan. Does it match what you can see in the current suite?
* Did it miss things? Did it flag things that aren't really gaps?
* Only continue when you agree with it. If you don't, tell the agent what to change in the plan.

### Step 2: let it execute

**Goal:** new tests for all five gaps, written by the agent, with you in control.

A good prompt for this:
* Points to the plan, so you don't have to repeat it
* Gives an order of work, and says the agent has to stop after each gap and list what it added
* Says every new test has to assert something specific. Nothing that passes whatever the API returns
* Sets limits: existing passing tests stay as they are unless they are provably wrong, and no new dependencies without asking you first

Then check it:
* Watch what the agent does between the gaps. This is the closest you'll get to seeing how an agent "thinks".
* Did it add a test with a weak or tautological assertion? Stop it, tell it why, and ask for a rewrite.
* Keep a running note of everything the agent did that surprised you, good or bad.

### Step 3: verify what it built

**Goal:** a list of everything that's wrong with the new tests, and then those things fixed. Two prompts, in this order.

A good prompt for the first one:
* Asks whether the new tests actually compile and run
* Asks to run the whole suite twice in a row and compare the results (a sign of flakiness or shared state)
* Asks to explain five random tests in plain language: what does each one really verify?
* Asks to find the tests that would pass whatever the API returns
* Says: list the issues, don't fix anything yet

And then a second prompt that tells the agent to fix what it listed.

Then check it:
* Compare the five explanations with the names of the tests. Do they match what you'd expect?
* Open the Source Control panel after the fixes. Did the agent change something you didn't ask for?

### Step 4: reflect

This one isn't a prompt, it's a conversation with your partner. Talk it through, and write your answers in `decisions.md`:
1. What did the agent generate that surprised you?
2. At what point, if any, did you stop trusting what it produced?
3. If you did this on a production test suite at a client, what guardrails would you want?
4. What would have happened if you hadn't looked at the plan first, and had merged everything just because the suite was green?

---

# Manual & Exploratory track

## Role 1: deeper exploratory testing

Your job: go further and harder at the app than you did in Block 1, and aim at the business rules that carry the most risk. You only need Postman (or a browser) and your AI chat. No code, on purpose.

### Step 1: plan a focused session

**Goal:** a plan for a 30-minute exploratory session, aimed at the rules you've tested the least so far.

A good prompt for this:
* Gives the AI your `test-plan.md`, `session-notes.md` and `bugs.md`
* Asks for a session of 30 minutes, aimed at the least-tested rules
* Asks for 2 or 3 things to try by hand per rule
* Says at least one of them has to be a deliberate attempt to break the rule, not just the expected path

Then check it:
* Does this really point at gaps, or does it repeat what you did in Block 1? If it's too similar, push back and ask for something sharper.

### Step 2: race the concurrency rule by hand

This is the hardest rule to test by hand, so it's worth doing properly.

**Goal:** a step-by-step procedure, for two people with Postman, to create two retrospectives for the same team as close together as you can manage, and a clear idea of what result proves the rule holds and what result proves it doesn't.

A good prompt for this:
* Names the rule you want to test, in the words of the brief
* Says you can't write code, so it all has to work with two Postman tabs (or a partner on a second machine)
* Asks for the exact steps, and for what each outcome means

Then actually do it with your partner:
* Each of you has the request ready in Postman, and you both hit send within a second of each other.
* Run it 3 or 4 times. A race condition doesn't always show up on the first attempt.
* Write down every attempt in `session-notes.md`, including the ones where nothing went wrong. That's useful evidence too.

### Step 3: run the session

This one isn't a prompt. Set a timer for 30 minutes and work through the list from step 1, plus the race from step 2. Keep your notes in `session-notes.md`, in the same format as this morning:

```
What I tried:
What I expected:
What actually happened:
Worth a closer look? Y/N
```

* Follow anything that surprises you. You're not bound to the list if something more interesting shows up.
* Found something that looks like a real bug? Reproduce it a second time before you move on. A bug you can't reproduce twice is weak evidence.

### Step 4: write up what you found

**Goal:** a clear bug report for every genuine issue in your notes, in the format your team uses in `bugs.md`.

A good prompt for this:
* Gives the AI your session notes
* Asks for one report per genuine issue, with title, severity (Critical / High / Medium / Low), steps to reproduce, expected result, actual result and business impact
* Says to stay factual: no guessing at the root cause, and no inventing an issue that isn't in your notes

Then check it:
* Compare every report with your own notes before it goes into the shared `bugs.md`. Did it stay factual?
* Merge into the same `bugs.md` as Block 1. One list for the whole team.

---

## Role 2: manual adversarial and boundary testing

Your job: the manual version of what the Automation track's agent is doing in code. You hunt for the same kinds of gaps, but you do it by hand.

The agent in the other track generates tests. You're going to generate the same kind of thinking, a systematic list of adversarial and boundary scenarios, and then run it yourself against the app. These are the five kinds of gaps to go after:
1. **Shallow checks.** Only glancing at the status code, not at what's in the body
2. **No boundary tests.** Nothing tried with empty strings, very long strings, missing fields or edge values like priority
3. **No adversarial input.** Nothing tried with malformed JSON, a wrong content type or unexpected extra fields
4. **No cross-entity checks.** Nothing that verifies that a retrospective really belongs to the team in the path (try another team's ID), or that feedback items belong to the right retrospective
5. **No repeatability check.** Has anyone sent the same request twice to see if the result stays the same?

### Step 1: generate the scenario list

**Goal:** a checklist of scenarios you can run by hand in Postman. For each scenario: the exact request, and what a correct response looks like.

A good prompt for this:
* Tells the AI what the application is, and that you test by hand in Postman
* Names the four kinds of scenarios: boundary and missing input per entity (team, retrospective, feedback item, action item), malformed requests, cross-entity consistency, and requests where sending the same thing twice should (or shouldn't) give the same result
* Asks for the exact request and the expected response for every scenario

Then check it:
* Read the list before you run anything. Does it cover the real entities and rules of this app, or does it look generic? If it's generic, push back.

### Step 2: run the checklist

This one isn't a prompt. Work through the checklist in Postman, one scenario at a time, and write down every result in `session-notes.md`. Also the ones that behave correctly.

* Don't skip a scenario because you expect it to pass. Several real bugs in this app only show up on the boring cases.
* Something looks wrong? Try to explain in plain language why, before you ask the AI. Then ask the AI to help you read the response, and compare its explanation with yours.

### Step 3: interrogate the results

**Goal:** for every result you marked "worth a closer look": is it a defect, expected behaviour that just looked odd, or something you can't judge without more information? Plus an honest answer to the question: which of the five kinds of gaps did I clearly not try enough?

A good prompt for this:
* Gives the AI your `session-notes.md`
* Asks for one of those three verdicts per result
* Names the five kinds of gaps and asks which ones are under-tested

Then check it:
* The AI says a category is under-tested? Go back and try 2 or 3 more scenarios there before you move on.
* Log every genuine defect in `bugs.md`.

### Step 4: reflect

This one isn't a prompt either. Talk it through with your partner, and write your answers in `decisions.md`:
1. What did you find by hand that a quickly generated automated test would have missed, and why?
2. At what point, if any, did you stop trusting your own first read of a response and dig deeper?
3. If you handed this to someone who writes automated tests from your findings, what would you tell them so they don't lose anything?

---

## Done?

Before lunch at 12:45, you should be able to say yes to these.

Automation, Role 1:
* The agent wrote the tests from a plan you approved, and you wrote none by hand
* All four core rules have executable tests running against the live API
* `bugs.md` has every genuine defect, with severity and reproduction steps
* Your concurrency test really races two requests. It doesn't just run them one after the other
* Add, modify and delete on a closed retrospective are tested separately
* No test was made green by loosening an assertion

Automation, Role 2:
* You approved a plan before the agent changed any file
* The agent expanded the suite for all five gaps
* No tautological or shallow assertions are left in the new tests
* `decisions.md` has your answers about the agent

Manual & Exploratory, Role 1:
* You tested the concurrency rule by hand, more than once, and wrote down every attempt
* `bugs.md` has every genuine defect, with severity and reproduction steps
* `session-notes.md` shows a focused session, not a repeat of Block 1

Manual & Exploratory, Role 2:
* You ran a full adversarial and boundary checklist by hand, covering all five kinds of gaps
* `decisions.md` has your answers
* Every genuine defect you found is in `bugs.md`

Everyone together:
* `bugs.md` is one shared list, not a separate file per pair
* Your prompt log has every prompt you wrote, each with a one-line verdict
* Pick your **best** and your **worst** prompt from the log, you'll be asked for them in Retro 2
* You each have something to tell at Retro 2 about what surprised you, whether it came from an agent or from your own hands

What you learned in this block:
* How to decide whether a failing test means a bug in the app or a bug in the test
* Why you let an agent make a plan before it starts, and why you check what it changed by reading the diff
* That a green test suite, or a clean manual pass, only proves that what you checked works
* What a good prompt needs to contain, so you don't get tests that can't fail
* How to find gaps in your own testing, by hand or with an agent
