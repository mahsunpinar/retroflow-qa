# Block 1 Prompt Ladders
**Pair AB and Pair CD work in parallel**

These are structured prompts, not free-form suggestions. Work through them in order.
The goal of this block is not to find every bug, it is to build the testing and prompting
habits that will make the rest of the day faster and safer.

**Nobody writes or edits code in this block, on purpose.** Whether you automate tests day to
day or work entirely manually, everything below uses Postman (or a browser) and your AI chat
tool. From Block 2 onward, your coach will route pairs toward an Automation track or a
Manual & Exploratory track based on what actually shows up in the room today, but Block 1 is
the same for everyone, so there's no wrong group to be in right now.

After each prompt: read the output critically before using it. If something feels off, it
probably is.

**No Java or Maven installed, and not planning to write code today?** You still need the
application running so you have something to test. Open the RetroFlow QA reference repo in a
free **GitHub Codespace** (Code → Codespaces → Create codespace, on the repo's GitHub page). It
starts the application for you with one pre-set command, no local install, nothing to
understand about the code. Point Postman at the Codespace's forwarded port instead of
`localhost`, ask your coach if you're not sure which URL to use, and work exactly as described
below from there.

---

## Pair AB - Risk-based test plan & test case catalogue

You are building the test strategy for RetroFlow: what to test, in what order, and why.

---

### Prompt 1 - Understand before you test

Open your AI chat tool. Paste the full contents of the RetroFlow QA brief and send this prompt:

```
I'm a test engineer taking over QA for a Spring Boot backend called RetroFlow, ahead of its
first release. Here is the handover brief:

[paste brief here]

Explain the domain and the business rules to me as if I'm a new tester joining the team.
Then identify which of these business rules carries the highest risk if it fails silently
in production, and why. Be specific about the business consequence, not just "this could
be a bug."
```

**What to do with the output:**
- Discuss with your pair: do you agree with the risk ranking? What would you rank differently?
- Write down your agreed risk priorities in a file called `test-plan.md` in the root of your
  working folder. You'll refer back to this all day.

> **Watch for:** AI may rank risks based on generic API-design intuition rather than what the
> brief actually says matters to StackCraft. Cross-check against the CTO's quote in the brief.

---

### Prompt 2 - Risk-based test plan

```
Based on this domain brief, propose a risk-based test plan for RetroFlow covering:
- Test levels to use (API-level, integration, exploratory) and why
- Entry and exit criteria for this QA pass
- A risk matrix: for each business rule, rate likelihood of a defect and impact if it ships,
  and use that to set testing priority order
- Environments and test data needed

Explain your reasoning for the priority order, not just the ranking itself.
```

**What to do with the output:**
- Compare the proposed plan to your Prompt 1 risk discussion, does it match?
- Check the priority order: does concurrency (two people acting on the same retrospective at
  once) rank high enough given it's a multi-user collaborative tool by design?
- Do not copy-paste blindly. Adapt the plan to reflect your team's actual judgement.

---

### Prompt 3 - Test case catalogue for core flows

```
Write a structured test case catalogue for the following RetroFlow flows. For each test case,
include: ID, preconditions, steps, expected result, and which business rule it verifies.

Flows to cover:
- Creating a team (including the "at least one member" rule)
- Creating a retrospective (including the "only one OPEN at a time" rule)
- Closing a retrospective (including the "cannot be reopened" rule)
- Adding a feedback item to an OPEN retrospective
- Adding, modifying, or deleting a feedback item in a CLOSED retrospective

Include both positive and negative cases for each flow.
```

**What to do with the output:**
- Check: does the catalogue cover all three operations for the closed-retrospective rule,
  add, modify, AND delete? That's three separate test cases, not one.
- Manually execute at least 3 of these test cases against the running application right now,
  using Postman or your browser. Do they behave as expected?

---

### Prompt 4 - Manually exercise the API

No test code today, just Postman's UI (or a browser, or curl if you're comfortable with it)
and your own hands.

```
Here is my test case catalogue: [paste it]. For each test case, tell me exactly what request
to send (method, URL, headers if any, and body if any) so I can enter it into Postman by hand.
Present it as a simple table: test case ID, request, and what response you'd expect.
```

**What to do with the output:**
- Build each request in Postman one at a time (or reuse one from a partner who's already set
  one up) and actually send it against the running application
- Record the real status code and body next to your prediction in the test case catalogue,
  don't just trust that it matches, look at the actual response
- Note any endpoint that returns a raw stack trace or an unexpected status code, do not
  fix or explain it away yet, just log it in a new file called `bugs.md`
- Are the status codes actually correct per REST conventions (201 for creation, 404 for not
  found)? Check a few by hand against the actual HTTP spec, don't just trust AI's claim.

---

### Prompt 5 - Interrogate the output

This is the most important prompt of the block. Do not skip it.

```
Review the test case catalogue and the actual results I recorded against these specific
business rules from the brief. For each rule, tell me:
1. Whether it currently has explicit test coverage in the catalogue
2. Where exactly (which test case)
3. What kind of defect this test case would and would NOT catch

Business rules to check:
- A team can only have one OPEN retrospective at a time
- A closed retrospective cannot be reopened
- Feedback items in a closed retrospective cannot be modified or deleted
- Completed action items cannot be uncompleted
```

**What to do with the output:**
- This is your quality gate. Anything with "no coverage" needs a test case added before the
  block ends.
- Note especially the phrase "what this test would NOT catch", a test case that checks the
  happy path of a rule but not a concurrent or adversarial attempt to break it is incomplete,
  even if it exists.

---

## Pair CD - Exploratory testing & bug reporting

You are setting up the exploratory side of QA: a testing charter, a hands-on session against
the running application, an honest read of what you inherited, and bug reports that would
survive scrutiny.

---

### Prompt 1 - Build an exploratory testing charter

```
I'm a test engineer preparing an exploratory testing session for RetroFlow, a Spring Boot
backend for running team retrospectives. Here is the handover brief:

[paste brief here]

Generate an exploratory testing charter covering:
- The main happy-path flows to explore first
- Likely edge cases given the business rules (timing boundaries, permission-adjacent
  behaviour, concurrent actions, invalid or missing input)
- Anything in this brief that looks underspecified or worth probing deliberately

Keep it as a focused, time-boxed session plan, not an exhaustive checklist.
```

**What to do with the output:**
- Discuss with your pair: does the charter actually point you at the highest-risk areas, or
  just the obvious ones? Adjust it if not.
- The "underspecified" section matters most, that's usually where a real bug is hiding.

---

### Prompt 2 - Run the session

Set a timer for 25 minutes. Using Postman (point-and-click, no scripting) or a browser, work
through your charter against the running application. Keep running notes as you go, in a file
called `session-notes.md`, using this format for anything notable:

```
What I tried:
What I expected:
What actually happened:
Worth a closer look? Y/N
```

**What to do during the session:**
- Deliberately try to break the "only one OPEN retrospective at a time" rule: create a
  retrospective, then, working with your partner, both try to create a second one for the
  same team within a second or two of each other, one of you sending the request right as the
  other does. This is a manual approximation of a race condition, and it's a real testing
  technique, not a compromise for not writing code.
- Try the three separate operations on a closed retrospective (add, modify, delete a feedback
  item) individually. Don't assume they all behave the same way because they're related.
- Follow anything that surprises you, that's the point of exploratory testing.

---

### Prompt 3 - Get a plain-language read on the inherited test suite

You do not need to read Java to do this. Let AI translate it for you, then you make the call.

```
Here is an existing automated test suite for a Spring Boot project:

[paste the existing test file(s) from the repo, or ask a teammate on the automation side of
the room to share theirs]

For each test, explain in plain English, no code, what it actually checks, and rate whether
that check would catch a real regression or would pass no matter what the API returned. Flag
any test that only checks "not null" or a status code when the endpoint's whole purpose is to
return specific data.
```

**What to do with the output:**
- You're judging the plain-language explanation, not the code, does each described check
  sound like it would actually catch something going wrong?
- Cross-check a couple of the "weak" flags against what you observed yourself in Prompt 2, do
  they line up?
- Note this for Retro 1: "the inherited test suite passes, but does it prove anything?"

---

### Prompt 4 - Write up what you found

```
Here are my raw session notes from exploratory testing: [paste session-notes.md]. Write these
up as clear bug reports, one per genuine issue, with a title, severity (Critical / High /
Medium / Low), steps to reproduce, expected result, actual result, and business impact. Keep it
factual, don't guess at the root cause, and don't invent an issue that isn't in my notes.
```

**What to do with the output:**
- Save this as `bugs.md`. This is the same file the automation-focused pair is also building,
  merge into one list rather than keeping two.
- Check each report against your own notes, did it stay factual, or did it speculate?

---

### Prompt 5 - Interrogate the output

This is the most important prompt of the block. Do not skip it.

```
Based on the RetroFlow brief's business rules, what have I not tried yet in my exploratory
session that a thorough tester would? Be specific about which business rule each gap relates
to, not just "test more edge cases."

Business rules to check against:
- A team can only have one OPEN retrospective at a time
- A closed retrospective cannot be reopened
- Feedback items in a closed retrospective cannot be modified or deleted
- Completed action items cannot be uncompleted
```

**What to do with the output:**
- This is your quality gate. Anything flagged as untried, try it now if time allows, or
  carry it forward explicitly into Block 2.

---

## End of Block 1 checklist

Before Retro 1, your team should be able to answer yes to:

**Pair AB:**
- [ ] `test-plan.md` exists with an agreed risk priority order
- [ ] A test case catalogue exists covering all four core business rules, including all three
      operations of the closed-retrospective rule
- [ ] At least 3 test cases have actually been run against the live app, with real results
      recorded
- [ ] You've run Prompt 5 and know which business rules currently have zero coverage

**Pair CD:**
- [ ] A testing charter exists and was actually followed, not just written and ignored
- [ ] `session-notes.md` exists with real findings from at least 25 minutes of hands-on testing
- [ ] The inherited test suite's weak assertions and gaps are documented in plain language
- [ ] You've run Prompt 5 and know which business rules you haven't tried to break yet

**Both pairs together:**
- [ ] `bugs.md` exists as one shared list, merged from both pairs' findings
- [ ] Everyone knows the actual current behaviour of the app on at least a handful of
      endpoints, not just what the brief says it should do
