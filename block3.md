# Block 3: Grounding, reviewing and skills
Last rotation of the day! New pairs again.

**Choose your track again (2 minutes, with your pair).** Same two tracks as in Block 2, and you may pick a different one than before. **Automation** is an MCP server that helps you audit your test suite against StackCraft's QA standards. **Manual & Exploratory** is a structured review of RetroFlow before release, no code. Not sure? Same rule as in Block 2: if at least one of you is comfortable reading test code, Automation is an option. Neither of you? Take Manual & Exploratory. Tell your coach which one you picked. If your group has two pairs on the same track, work in parallel and compare notes at the showcase.

Both tracks end the block the same way: you turn what worked into a skill, a set of instructions the AI only loads when it actually needs them.

What we want you to notice in this block is the difference between an AI that answers from what it was trained on ("this is what good test practice usually looks like") and an AI that reads StackCraft's actual QA standards and quotes them back to you. Both tracks get to see that difference, just in a different way.

**This block, you write all the prompts yourself.** No hints about wording this time. For every task we only tell you what the result has to look like, and how you check it. You've had a whole day of practice, so now it's up to you.

> Keep adding to your **prompt log** (`prompt-log.md`): the prompt, and one line on whether it worked or what you had to fix. Your best prompts are the raw material for the skill you build at the end of this block.

> Stuck after two tries? Ask your coach for the example prompt for that step. Looking is allowed, just write down what the example does that yours didn't.

> **Watch your tokens.** Start a **new chat** for each step, and only paste what that step needs. Your tool says you're out of requests? Don't wait for it to reset: work on your partner's machine or switch tools, and tell your coach.

**Where things live.** You work in the same group folder as before, `retroflow-qa`, with everything from Blocks 1 and 2 in it. The standards document is `stackcraft-qa-standards.md` in the course files. New this block: `audit-findings.md` (Automation), `review-notes.md` and `pre-release-quality-report.md` (Manual), and your skill.

**How to read this block.** Every step starts with a **Read:** line (what it works with, these exist already) and a **Create:** line (the file you make, and where it lives). "The result is good when" is your *done when*. Not sure what to do now? Look at those lines first, then ask your coach.

**Your block at a glance.** This is a rough guide, not a rule.

| Part | Automation | Manual & Exploratory |
|---|---|---|
| Choosing your track and setting up | 10 min | 5 min |
| Step 1: feel the difference | 10 min | 10 min |
| The audit, or the four reviews | 15 min (audit) | 25 min (four reviews, about 6 minutes each. Short on time? Take two reviews each and merge) |
| Fixing, or the report | 15 min (fix) | 10 min (report) |
| Build a skill and swap | The last 25 min. Don't skip the drafting, cut the swap if you must | The last 25 min. Don't skip the drafting, cut the swap if you must |

---

## Setting up (both tracks, about 10 minutes)

* **Read:** nothing yet, the standards come in step 1
* **Create:** the connector in Claude.ai (Option 1), or `.vscode/mcp.json` (Option 2)
* **Done when:** your tool can use `ai-track-standards` (in Copilot, the tools list shows `list_documents` and `read_document`)

We serve StackCraft's QA standards through an MCP server that we host ourselves. You don't have to install anything. Pick the option that fits what you have:

**Option 1: Claude.ai in your browser (a free account is fine).** Nothing to install.
* Sign in at [claude.ai](https://claude.ai).
* Go to **Customize** → **Connectors** → **Add custom connector**. Call it `ai-track-standards` and paste this URL: `https://ai-track-ps-production.up.railway.app/mcp`. There's no login.
* Start a new chat, click **+** → **Connectors** and switch `ai-track-standards` on for that chat.
* For the skill at the end of the block, go to **Settings** → **Capabilities** and switch on **Code execution and file creation**. Skills don't work without it.

**Option 2: GitHub Copilot in an IDE or a Codespace.** Create a file called `.vscode/mcp.json` with the following content:

```json
{
  "servers": {
    "ai-track-standards": {
      "type": "http",
      "url": "https://ai-track-ps-production.up.railway.app/mcp"
    }
  }
}
```

A small **Start** link appears above the server name in that file, click it. Using IntelliJ? Go to Settings → Tools → GitHub Copilot → Model Context Protocol (MCP) → **Configure**, and add the same block there. Then open the chat in Agent mode and take a look at the tools list. You should see `list_documents` and `read_document` in there.

> Curious about what you're actually connecting to? `ai-cheat-sheets/what-is-mcp.md` explains it.

**What you need to know to talk to the server:** it's called `ai-track-standards`, it has two tools (`list_documents` and `read_document`), and the document we need has the id `stackcraft-qa`. An AI only reads that document when you tell it to, it won't do it by itself.

The Manual track needs this for step 1 and to test the skill at the end. Option 1 is the easiest for you.

---

## Both tracks, step 1: feel the difference

* **Read:** the standards document (through the server), `stackcraft-qa-standards.md` to check it, and your own `bugs.md`
* **Create:** nothing. Note what you saw in `prompt-log.md`
* **Done when:** you can say what is different between the answer without and the answer with the connector

You're going to ask the same question twice: first in a chat without the connector, then in one with the connector. For this one, we give you the question, so both runs are comparable:

```
What does StackCraft's QA standard say about tautological assertions like
assertThat(result).isNotNull()? How severe is a violation, and what does a bug report
have to contain?
```

For the second run, you write the extra sentence that makes the AI use the server and quote the document. Add it at the end of the question.

* Without the connector, the AI has never seen StackCraft's standards. Did it tell you that, or did it come up with something that sounds plausible?
* With the connector, open `stackcraft-qa-standards.md` and look up what it quoted (section 2.1 and section 4.1). Does it exist? Is it quoted word for word?
* Now look at your own `bugs.md` from this morning. Does it match the format the standards ask for?

---

# Automation track

## Auditing your test suite against the standards

**Your mandate:** find out where your test suite breaks StackCraft's QA standards, and fix the worst of it.

### Step 2: the audit

* **Read:** the inherited `RetroflowApiTest.java`, the tests you and the other pair made today, and the standards document through the MCP server
* **Create:** `audit-findings.md` (new file). No changes to the tests yet

Paste your full test suite into the chat or, if you work in an agent, point it at the test folder. The suite you audit is the inherited one, plus everything you and the other pair added today.

The result is good when:
* Every violation names the file and the test method
* Every violation quotes the section of the standards it breaks
* Every violation explains why it's a violation, and what the correct approach is
* The violations are sorted by severity, Critical first, using the severity in the standards document

Then check it:
* Take three violations and look up the quoted section in the document. Can't find it? Then the AI made it up. Write it down, that's great material for Retro 3.
* Take two violations and open the test it names. Is the problem really in there?
* Look at what the audit found, and then at what it didn't. Can a test suite also break the standards by what's missing, not just by what's wrong?

### Step 3: fix the Critical violations

* **Read:** `audit-findings.md`
* **Create:** the fixes in your own tests, one per Critical violation. Mark each finding as fixed in `audit-findings.md`. A violation that is a real bug goes in `bugs.md` as well

**Your mandate:** fix every Critical violation, one at a time.

The result is good when:
* Every fix is a separate step, and you run the tests after each one
* Every fix has a short comment: what was wrong, and which section of the standards it's based on
* A violation that turns out to be a real bug in the application doesn't just get fixed in the test: it's also in `bugs.md`

Then check it:
* Open the Source Control panel (or run `git diff`). Did the AI change more than you asked?
* Run the whole suite yourself. Is it still green, and did it get better, or only greener?

Done? Go to the skill at the bottom of this file: "Both tracks, the last 25 minutes: build a skill".

---

# Manual & Exploratory track

## The release review

**Your mandate:** produce the review a senior test engineer would present before RetroFlow goes out to 12 teams. This time you use AI as a reviewer and not just as a generator. You don't need any code for this, only what your team made today.

The interesting part: the AI will find real issues AND invent fake ones. It's up to you to tell them apart.

You do four reviews, and then you bring them together in one report:
1. **Security.** What could go wrong if this API had a wider audience than expected?
2. **API contract.** Is the API well designed, and does it behave the way it's documented?
3. **Business rule coverage.** Is every rule in the brief verified, whether by an executable test or by something your team confirmed by hand?
4. **Test quality.** Do the checks that exist, automated or manual, actually test the right things?

### Step 2: the security review

* **Read:** your `bugs.md`, `session-notes.md`, the responses you captured today and the brief
* **Create:** a section "Security" in `review-notes.md` (new file)

Remember: you can only check what you can see by sending requests. This is not a review of the source code.

The result is good when:
* It covers input validation, information disclosure, endpoints that return more data than a caller needs, and anything else you can test from the outside
* Every finding has a severity (Critical / High / Medium / Low), the exact request that shows it, the actual response and a recommended fix

Then check it:
* The brief says no authentication is required for this internal tool. If the AI flags missing authentication anyway, note it: AI reviewers don't always respect the scope you gave them.
* Take every Critical or High finding and reproduce it yourself in Postman before it goes into your report.

### Step 3: the API contract review

* **Read:** the responses you captured today, `retroflow-api-reference.md` and the brief
* **Create:** a section "API contract" in `review-notes.md`

The result is good when:
* It looks at the HTTP methods (does a GET ever change something?), the status codes, whether the error responses have the same structure everywhere, and anything that would surprise someone who uses the API
* It gives PASS, FAIL or CONCERN for each of those
* Every FAIL or CONCERN comes with a finding and a fix, based on an actual response you collected today. Not on something hypothetical

Then check it:
* Cross-check every FAIL against a response you captured. A finding you haven't reproduced doesn't go in.
* Look at what the create-retrospective call returns. What does your evidence say about the status code?

### Step 4: the business rule coverage review

* **Read:** the brief, `test-cases.md`, `session-notes.md` and `bugs.md`
* **Create:** a section "Business rule coverage" in `review-notes.md`

The result is good when:
* Every rule from the brief is in there, and the closed retrospective rule is checked as three separate operations
* For every rule it says: covered, missing, or partly covered
* For a covered rule it points to the exact artifact (test case, session note or bug) and says whether the recorded result confirms that the rule holds
* For a missing rule it says what happens if this ships without coverage

Then check it:
* This is the most important review of the day. Check every finding against your recorded results. A test case that exists is not the same as a test case that was run and passed.
* The AI says a rule is covered, and you know it was never really confirmed? Put that in your report as a known gap, and not as a covered rule.

### Step 5: the test quality review

* **Read:** `test-cases.md`, `session-notes.md`, and `RetroflowApiTest.java` (the inherited suite)
* **Create:** a section "Test quality" in `review-notes.md`, and stronger evidence for what you find weak

The result is good when:
* It finds checks that pass but don't test the right thing, rules without real coverage, checks that are too broad or too narrow, and anything that gives a false sense of security
* Every finding names the specific test case or note, and explains the problem
* Where evidence is weak, it shows what stronger evidence would look like

Then check it:
* This one is adversarial on purpose. The AI is reviewing work that may have been made with AI too. Does it catch its own mistakes, or your team's?
* A manual test case that says "checked, looks fine" with no recorded response is the manual version of `assertThat(result).isNotNull()`. Look for it in your own notes.
* Strengthen the weak evidence it found. Those are real gaps.

### Step 6: the report

* **Read:** `review-notes.md` and `bugs.md`
* **Create:** `pre-release-quality-report.md` in the working folder (new file)

**Your mandate:** one document, built from the four reviews and your bug list from today, that you could realistically hand to the StackCraft CTO. Save it as `pre-release-quality-report.md`.

The result is good when it has:
* An executive summary of 3 or 4 sentences
* The bugs, with severity, reproduction steps and business impact (from `bugs.md`)
* The critical issues (must fix before release), the major ones (should fix) and the minor ones (fix when time allows)
* What's tested and trustworthy (at least 3 things)
* What you did NOT have time to verify
* A release recommendation: BLOCKED / CONDITIONAL / APPROVED

Then check it:
* Does the report say honestly what you didn't get to? Or does it make it sound like everything was covered?
* Do you agree with the recommendation? You'll have to defend it at the showcase.

Done? Go to the skill at the bottom of this file: "Both tracks, the last 25 minutes: build a skill".

---

## Both tracks, the last 25 minutes: build a skill

You've written a lot of instructions for the same kind of check by now. A skill saves you that work. It's a folder with a `SKILL.md` file in it, which tells the AI how to do one specific task. The AI only reads the short description of each skill it has, and loads the full instructions when your request matches that description.

> How is this different from an instructions file, a file the AI reads before every request? A skill is only read when it's relevant. `ai-cheat-sheets/working-efficiently.md` explains it in a bit more detail.

Which skill do you build? That depends on your track:

* **Automation** builds `stackcraft-test-audit`, a skill that audits any test suite against StackCraft's QA standards.
* **Manual & Exploratory** builds `stackcraft-release-review`, a skill that reviews your tests and your evidence before a release and writes the report the StackCraft way.
* **Both pairs of your group on the same track?** Then the second pair builds `stackcraft-bug-report` instead, a skill that turns raw notes or a failing test into a bug report that follows the standards.

### Last step: draft the skill

* **Read:** your `prompt-log.md` and the prompts that worked best in this block
* **Create:** a folder with the name of your skill, and in it `SKILL.md` (new). In Copilot: `.github/skills/<skill-name>/SKILL.md`. In Claude Code: `.claude/skills/<skill-name>/SKILL.md`
* **Done when:** the skill exists, it is under 40 lines, and its description says when to use it

A `SKILL.md` file always looks like this. That's the format, the AI doesn't know it by heart:

```
---
name: the-name-in-lowercase-with-hyphens
description: One or two sentences. What the skill does and when to use it.
---
The instructions: the steps to follow, what to check, what sources to use, the exact
output format, and what never to do.
```

**Your mandate:** have the AI write the `SKILL.md` for the task you just did, based on the prompts that worked best for you today (your prompt log is useful here).

The result is good when:
* It follows the format above
* It's short: under 40 lines
* The instructions are specific enough that someone else could follow them without asking you anything
* It tells the AI where the source of truth is, so it reads the QA standards through the MCP server (`ai-track-standards`, document `stackcraft-qa`) instead of using what it remembers

Then read what it wrote and edit it yourselves. Spend most of your time on the description: that's what the AI uses to decide whether to load your skill or not.

> **Level up:** a skill can be more than one file. Put a template next to `SKILL.md` (the exact layout of a bug report, for example) and refer to it from the instructions.

### Install it and swap

* **Read:** the skill of the other pair in your group
* **Create:** a short comparison at the bottom of `decisions.md`: what did their skill do with your material that you had not done yourself?
* **Done when:** you gave the other pair one piece of feedback on their skill

* Using Claude.ai? Put `SKILL.md` in a folder with the same name as the skill, zip that folder and upload it under **Customize** → **Skills** (the **+** button). Don't forget to switch it on, and switch the `ai-track-standards` connector on in the chat where you use the skill.
* Using GitHub Copilot in VS Code or a Codespace? Ask the agent to save it as `.github/skills/<skill-name>/SKILL.md`. Copilot picks it up by itself, and you can also start it with `/<skill-name>` in the chat. Not showing up? Check that agent skills are switched on in the VS Code settings, and tell your coach.
* Using Claude Code? Save it as `.claude/skills/<skill-name>/SKILL.md`.
* Now swap with the other pair, and run their skill in a new chat on your own material from today, without explaining what it does. For example: run `stackcraft-test-audit` on `RetroflowApiTest.java`, or `stackcraft-release-review` on your `bugs.md` and `test-cases.md`, or `stackcraft-bug-report` on your `session-notes.md`.
* Did the AI pick up the skill on its own? Did it read the standards through the server, or did it answer from memory? Did it do what the other pair had in mind? Give them one piece of feedback.

---

## Finished early? Automation: what else could we connect?

* **Read:** `ai-cheat-sheets/what-is-mcp.md`
* **Create:** a heading "MCP exploration" in `decisions.md`
* **Done when:** it has 5 real MCP servers on it, each one checked

You've now seen MCP work with one hosted server. Think about what else you'd want to connect to in a real QA workflow at a client: a test management tool, a bug tracker, a monitoring platform.

**Your mandate:** find out which MCP servers really exist for that.

The result is good when:
* You have a list of 5 real MCP servers, not made-up ones
* For each: what it connects to, what you could do with it in a testing workflow, and what you'd have to think about before using it at a client (security!)
* It's written down in `decisions.md`, under a heading "MCP exploration"

Then check it:
* Look up every server on the list. Does it exist? MCP is new enough that the AI's training data may be incomplete or outdated, so this is a good test of how much you can trust it.

---

## Finished early? Manual & Exploratory: strengthen your weakest evidence

* **Read:** your `review-notes.md`, section "Test quality"
* **Create:** the extra evidence in `session-notes.md`, and the changes in `pre-release-quality-report.md`
* **Done when:** at least one weak piece of evidence is replaced by a recorded response

**Your mandate:** take the weakest piece of evidence your test quality review found, a case that says "checked, looks fine" without a response. Redo it by hand in Postman and write down the real status code and body. Then update the report: did the result change what you said about that rule?

---

## Done?

This is what we look at in Retro 3 and in the showcase. Your coach asks you to show proof, not to tell a story: the quotes you checked, the finding you reproduced, the skill doing its work in a fresh chat.

Automation:
* You saw the difference between an answer with and without the connector
* You have an audit with quoted sections, and you checked at least three of those quotes
* The Critical violations are fixed, one by one
* Your skill exists, and the other pair tested it

Manual & Exploratory:
* You saw the difference between an answer with and without the connector
* All four reviews are done, and `pre-release-quality-report.md` is saved
* You can name one finding that turned out to be a false positive, and one that was real
* You have a release recommendation and you can defend it
* Your skill exists, and the other pair tested it

Both tracks:
* `bugs.md` has every genuine defect of the whole day
* Your prompt log is complete, and you know which of your prompts you'd reuse on a real project
* You each have one concrete thing to share at Retro 3

Finished early: Automation has an MCP exploration in `decisions.md` with verified servers. Manual & Exploratory replaced at least one weak piece of evidence with a recorded response.

What you learned in this block:
* What grounding is, and why an answer from the real document beats an answer from training data
* What MCP is and how it connects an AI tool to a source
* How to check whether a quoted standard really exists
* Why AI reviewers find real issues and invent fake ones, and why you reproduce the important ones yourself
* Why "a test exists" and "a test was run and passed" are two different things
* What a skill is, how it differs from instructions, and why the description matters so much
* How to brief an AI when all you have is the result you want
