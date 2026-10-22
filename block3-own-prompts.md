# Block 3: Grounding and reviewing
Last rotation of the day! New pairs again, and a different track than this morning is perfectly possible. Your coach will tell you where you're at.

Two tracks again: **Automation** (an MCP server, to audit your test suite against StackCraft's QA standards) and **Manual & Exploratory** (a structured review of RetroFlow before release, no code). If your group has two pairs on the same track, work in parallel and compare notes at the showcase.

What we want you to notice in this block is the difference between an AI that answers from what it was trained on ("this is what good test practice usually looks like") and an AI that reads StackCraft's actual QA standards and quotes them back to you. Both tracks get to see that difference, just in a different way.

**This block, you write all the prompts yourself.** No hints about wording this time. For every task we only tell you what the result has to look like, and how you check it. You've had a whole day of practice, so now it's up to you.

> Keep adding to your **prompt log** (`prompt-log.md`): the prompt, and one line on whether it worked or what you had to fix.

> Stuck after two tries? Ask your coach for the example prompt for that step. Looking is allowed, just write down what the example does that yours didn't.

> **Watch your tokens.** Start a **new chat** for each step, and only paste what that step needs. Your tool says you're out of requests? Don't wait for it to reset: work on your partner's machine or switch tools, and tell your coach.

---

## Setting up (both tracks, about 10 minutes)

We serve StackCraft's QA standards through an MCP server that we host ourselves. You don't have to install anything. Pick the option that fits what you have:

**Option 1: Claude.ai in your browser (a free account is fine).** Nothing to install.
* Sign in at [claude.ai](https://claude.ai).
* Go to **Customize** → **Connectors** → **Add custom connector**. Call it `ai-track-standards` and paste this URL: `https://ai-track-ps-production.up.railway.app/mcp`. There's no login.
* Start a new chat, click **+** → **Connectors** and switch `ai-track-standards` on for that chat.

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

The Manual track only needs this for step 1. Option 1 is the easiest for you.

---

## Both tracks, step 1: feel the difference

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

**Your mandate:** fix every Critical violation, one at a time.

The result is good when:
* Every fix is a separate step, and you run the tests after each one
* Every fix has a short comment: what was wrong, and which section of the standards it's based on
* A violation that turns out to be a real bug in the application doesn't just get fixed in the test: it's also in `bugs.md`

Then check it:
* Open the Source Control panel (or run `git diff`). Did the AI change more than you asked?
* Run the whole suite yourself. Is it still green, and did it get better, or only greener?

### Step 4: what else could we connect?

You've now seen MCP work with one hosted server. Think about what else you'd want to connect to in a real QA workflow at a client: a test management tool, a bug tracker, a monitoring platform.

**Your mandate:** find out which MCP servers really exist for that.

The result is good when:
* You have a list of 5 real MCP servers, not made-up ones
* For each: what it connects to, what you could do with it in a testing workflow, and what you'd have to think about before using it at a client (security!)
* It's written down in `decisions.md`, under a heading "MCP exploration"

Then check it:
* Look up every server on the list. Does it exist? MCP is new enough that the AI's training data may be incomplete or outdated, so this is a good test of how much you can trust it.

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

Remember: you can only check what you can see by sending requests. This is not a review of the source code.

The result is good when:
* It covers input validation, information disclosure, endpoints that return more data than a caller needs, and anything else you can test from the outside
* Every finding has a severity (Critical / High / Medium / Low), the exact request that shows it, the actual response and a recommended fix

Then check it:
* The brief says no authentication is required for this internal tool. If the AI flags missing authentication anyway, note it: AI reviewers don't always respect the scope you gave them.
* Take every Critical or High finding and reproduce it yourself in Postman before it goes into your report.

### Step 3: the API contract review

The result is good when:
* It looks at the HTTP methods (does a GET ever change something?), the status codes, whether the error responses have the same structure everywhere, and anything that would surprise someone who uses the API
* It gives PASS, FAIL or CONCERN for each of those
* Every FAIL or CONCERN comes with a finding and a fix, based on an actual response you collected today. Not on something hypothetical

Then check it:
* Cross-check every FAIL against a response you captured. A finding you haven't reproduced doesn't go in.
* Look at what the create-retrospective call returns. What does your evidence say about the status code?

### Step 4: the business rule coverage review

The result is good when:
* Every rule from the brief is in there, and the closed retrospective rule is checked as three separate operations
* For every rule it says: covered, missing, or partly covered
* For a covered rule it points to the exact artifact (test case, session note or bug) and says whether the recorded result confirms that the rule holds
* For a missing rule it says what happens if this ships without coverage

Then check it:
* This is the most important review of the day. Check every finding against your recorded results. A test case that exists is not the same as a test case that was run and passed.
* The AI says a rule is covered, and you know it was never really confirmed? Put that in your report as a known gap, and not as a covered rule.

### Step 5: the test quality review

The result is good when:
* It finds checks that pass but don't test the right thing, rules without real coverage, checks that are too broad or too narrow, and anything that gives a false sense of security
* Every finding names the specific test case or note, and explains the problem
* Where evidence is weak, it shows what stronger evidence would look like

Then check it:
* This one is adversarial on purpose. The AI is reviewing work that may have been made with AI too. Does it catch its own mistakes, or your team's?
* A manual test case that says "checked, looks fine" with no recorded response is the manual version of `assertThat(result).isNotNull()`. Look for it in your own notes.
* Strengthen the weak evidence it found. Those are real gaps.

### Step 6: the report

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

---

## Done?

Automation:
* You saw the difference between an answer with and without the connector
* You have an audit with quoted sections, and you checked at least three of those quotes
* The Critical violations are fixed, one by one
* `decisions.md` has your MCP exploration, and you verified that the servers exist

Manual & Exploratory:
* You saw the difference between an answer with and without the connector
* All four reviews are done, and `pre-release-quality-report.md` is saved
* You can name one finding that turned out to be a false positive, and one that was real
* You have a release recommendation and you can defend it

Both tracks:
* `bugs.md` has every genuine defect of the whole day
* Your prompt log is complete, and you know which of your prompts you'd reuse on a real project
* You each have one concrete thing to share at Retro 3

What you learned in this block:
* What grounding is, and why an answer from the real document beats an answer from training data
* What MCP is and how it connects an AI tool to a source
* How to check whether a quoted standard really exists
* Why AI reviewers find real issues and invent fake ones, and why you reproduce the important ones yourself
* Why "a test exists" and "a test was run and passed" are two different things
* How to brief an AI when all you have is the result you want
