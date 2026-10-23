# Block 2: example prompts (answer key)
**11:15 - 12:45 · Two tracks, two roles each**

**Coach only. Keep this closed on your own laptop.** Participants work from `block2-own-prompts.md`. When a pair has tried a step twice and is still stuck, show them just the one example prompt for that step, not the whole file. Looking is allowed, as long as they write down in their prompt log what the example does that theirs didn't.

These are examples, not the answer. Whatever you change, keep the habits: the source pasted in, the actual response read by hand, nothing accepted before it was reproduced.

---

Pairs have rotated. You are now working with someone you haven't paired with today yet. Your
coach will tell you which track your pair is on: **Automation** (an agent writes and runs executable
tests) or **Manual & Exploratory** (testing entirely by hand, no code). If your group has two
pairs on the same track, one takes Role 1 and the other takes Role 2.

Before you write a single prompt: spend 5 minutes reading what your teammates produced in
Block 1, `test-plan.md`, the test case catalogue, `session-notes.md`, and `bugs.md`. You will
be building on it, and possibly finding more in what it revealed.

---

# Automation Track

## Role 1 - Executable business-rule tests & bug diagnosis

Your job: let an agent turn the core business rules into executable, automated tests that run
against the live API, and correctly diagnose every failure you get, is it your test, or is it the app?

**Both Automation roles work with an agent.** The prompts below are written so you can paste them
into Agent mode. Add "Don't write or change any file yet, give me a plan first" to Prompt 1, 3
and 4, and tell the agent in Prompt 2 not to touch the test before it has given a verdict. Watch
for an agent that "fixes" a failing test by loosening the assertion. Same no-hand-edits rule as
Role 2: only `prompt-log.md`, `bugs.md`, `decisions.md` and `.env` are edited by hand.

---

### Before you start - read what Block 1 produced

Open the test case catalogue and recorded results from Block 1. Do not write new code yet.
Just read it, and run the app once to see current state.

---

### Prompt 1 - Implement executable business-rule tests

```
I have a running Spring Boot REST API for RetroFlow at <your group's URL>. Here are the
four core business rules I need automated, executable tests for, running against the live
HTTP API (not unit tests on internal methods):

1. A team can only have one OPEN retrospective at a time, attempting to create a second
   should fail
2. A retrospective can be closed but never reopened
3. Feedback items cannot be added to, modified in, or deleted from a CLOSED retrospective
4. A completed action item cannot be uncompleted

Here is the API contract: [paste your endpoint list from Block 1]

Write these as API tests (in the tool you use: Playwright, Cypress, Postman/Newman...) that actually call the
running API and assert on the real HTTP response, status code and body.
```

**What to do with the output:**
- Run every test against the live application right now
- Go rule by rule: does each test actually pass? If a test fails, do NOT assume your test is
  wrong, that is exactly the judgement call this block is testing
- Check rule 3 specifically, "cannot be added to, modified in, or deleted from." That's
  three operations. Do you have three separate tests?

---

### Prompt 2 - Diagnose the failures

Some of your tests from Prompt 1 will fail. For each failure:

```
I have a test that is failing. Here is the test code, the actual HTTP response I got back,
and the business rule it's meant to verify.

[paste test code]
[paste actual HTTP response, status code and body]
[paste the business rule from the brief]

Based on the actual response, is this a bug in the application, a bug in my test, or a
genuine ambiguity in the business rule that needs a decision? Explain your reasoning using
only what's in the response, don't guess at implementation details you can't see.
```

**What to do with the output:**
- Do not accept the AI's diagnosis without checking it yourself against the actual response
  body. Read the response yourself before agreeing.
- For every genuine bug: log it in `bugs.md` with severity, reproduction steps (exact request
  and response), and business impact, not just "test failed"
- For every test-was-wrong case: fix the test and note in `decisions.md` what you learned
  about reading the API contract more carefully

---

### Prompt 3 - Expand to edge cases and concurrency

```
The core business rule tests exist. Now help me test the edge cases that are easy to miss.

For each of the following, write an executable test against the live API:

1. Two requests to create a retrospective for the same team, fired as close together as
   possible (simulate a race condition), does the system still enforce "only one OPEN
   at a time"?
2. Creating a team with an empty members list, what status code comes back?
3. Filtering action items by both priority AND completed status together, does the result
   actually satisfy both conditions, or just one of them?
4. Requesting a retrospective by an ID that does not exist, what does the error response
   actually contain? Does it leak any internal implementation detail (stack trace, class
   names, database error text)?

For the concurrency test specifically, think about how to actually fire two requests close
enough together to expose a race condition, a naive sequential test won't catch this.
```

**What to do with the output:**
- Run all four. Read the actual responses carefully, this is where several real bugs live
- The concurrency test is the hardest to get right technically. If your first attempt doesn't
  actually race the requests, ask AI to help you fix the test itself, not just the assertion
- Log everything you find in `bugs.md`, whether or not it's a "designed" trap, a real bug
  is a real bug regardless of why it's there

---

### Prompt 4 - Test the delete and modify paths on closed retrospectives

```
I need executable tests specifically for the closed-retrospective immutability rule.

Business rule: feedback items cannot be added to, modified in, or deleted from a CLOSED
retrospective, that's three separate operations.

1. Write a test: attempt to add a feedback item to a closed retrospective, verify it's rejected
2. Write a test: attempt to modify an existing feedback item in a closed retrospective, verify
   it's rejected
3. Write a test: attempt to delete a feedback item from a closed retrospective, verify it's rejected

Run all three against the live API and tell me which ones actually pass versus fail.
```

**What to do with the output:**
- This is deliberately the most likely place to find a real gap, read the actual results
  carefully, do not assume all three behave the same way just because they're related
- Log any gap found as a bug with full reproduction steps

---

## Role 2 - Agent-mode test suite generation: adversarial expansion

Your job: use AI agent mode to generate a broad set of negative, boundary, and adversarial
tests across the whole API in a single coordinated operation. You may not hand-write
individual test cases, everything goes through the agent.

This block will teach you more about AI agents than any tutorial.

---

### Before you start - understand what you're doing

Agent mode (available in Copilot Chat via "Agent" mode, or equivalent in your AI tool) can
read and edit multiple files in a single operation. It is not magic, it operates on the
files and the running application you give it access to, makes a plan, and executes changes
step by step.

> **No editor with an agent?** The application is hosted, so you only need somewhere to keep
> your test project, in an editor that has an agent (VS Code with Copilot in Agent mode, for
> example). Put the folder in a git repository, because that is what makes the Source Control
> panel work. Still nothing? Ask your coach: a free **GitHub Codespace** with GitHub Copilot
> Free is the fallback.

Your job: give it a good enough instruction that the test suite it produces is trustworthy,
not just large. Your constraint: **do not hand-write any test case yourself**. If the agent
produces a weak test, fix it with another agent prompt, not by editing directly. This is how
you learn where agents fail at generating test coverage.

---

### The expansion brief

The RetroFlow test suite so far (from Block 1 and Role 1's work) has gaps that are common
in AI-assisted test generation:

1. **Shallow assertions**, tests that check status code only, not response body content
2. **No boundary testing**, nothing testing empty strings, very long strings, null vs.
   missing fields, or boundary values for enums
3. **No adversarial input**, nothing testing malformed JSON, wrong content types, or
   unexpected extra fields
4. **No cross-entity consistency checks**, nothing verifying that a retrospective really belongs to the team in the path, or that a
   retrospective's feedback items are actually
   scoped to the right retrospective
5. **Test independence**, some tests may depend on execution order or shared state

---

### Prompt 1 - Plan before acting

Before letting the agent generate any test files:

```
I have a test suite for a Spring Boot REST API (RetroFlow) that needs adversarial expansion.
Before writing any tests, analyse the current suite and the API surface and produce a plan.

For each of these gaps, list every endpoint or scenario that needs a new test:
1. Shallow assertions (status-code-only checks that should also verify the response body)
2. Missing boundary tests (empty/null/very long input, enum edge values)
3. Missing adversarial input tests (malformed JSON, wrong content type, unexpected fields)
4. Missing cross-entity consistency checks
5. Tests that appear order-dependent or share mutable state

Do not write any tests yet. Just list what's needed and why.
```

**What to do with the output:**
- Read the plan carefully. Does it match what you can see in the current suite?
- Are there things it missed? Things it flagged that aren't actually gaps?
- Only proceed to Prompt 2 once you agree with the plan.

---

### Prompt 2 - Execute the expansion

```
Now execute the test expansion plan. Add all five categories of new tests to the suite.

Constraints:
- Every new test must assert on something specific and meaningful, no test that would pass
  regardless of what the API actually returns
- Do not modify existing passing tests unless they are provably wrong
- Do not add new dependencies to the build file without flagging it first
- After each category, pause and list what you added before moving to the next

Start with cross-entity consistency, then boundary tests, then adversarial input, then
assertion depth on existing tests, then test independence fixes.
```

**What to do with the output:**
- Watch what the agent does between categories, this is the observable "reasoning" of an agent
- If it adds a test with a weak or tautological assertion: stop it and tell it why, ask for a
  rewrite
- Keep a running note: what did the agent generate that surprised you, good or bad?

---

### Prompt 3 - Verify the expansion actually works

```
The test expansion is complete. Now verify:

1. Do all the new tests actually run, or are there compilation or setup errors?
2. Run the full suite twice in a row, do results change between runs (a sign of flakiness
   or shared state)?
3. For 5 tests at random, explain in plain language what each one is actually verifying,
   does the explanation match what you'd expect from its name?
4. Are there any new tests that pass no matter what the API returns (tautological assertions)?

List every issue you find. Do not fix anything yet, just list.
```

Then, after reviewing the list:

```
Fix all the issues you identified. Apply the fixes now.
```

---

### Prompt 4 - Reflect on the agent

This is not an AI prompt. This is a discussion prompt for your pair.

Talk through these questions and write your answers in `decisions.md`:

1. What did the agent generate that surprised you?
2. At what point, if any, did you not trust what it produced?
3. If you were doing this on a production test suite at a client, what guardrails would you want?
4. What would have happened if you had let it run without checking the plan first, and just
   merged whatever it produced because the suite was green?

---

# Manual & Exploratory Track

## Role 1 - Deeper exploratory testing & risk-based bug hunting

Your job: go further and harder at the app than Block 1's exploratory session, specifically
targeting the highest-risk business rules, using nothing but Postman, a browser, and your AI
chat tool as a thinking partner. No code, on purpose.

---

### Before you start - read what Block 1 produced

Open `test-plan.md`, `session-notes.md`, and `bugs.md`. You're building on this, not starting
over.

---

### Prompt 1 - Target the highest-risk rules

```
Here is our risk-based test plan and what we've found so far exploring RetroFlow:

[paste test-plan.md, session-notes.md, and bugs.md]

Based on this, design a focused, 30-minute exploratory session specifically targeting the
business rules we've tested the least so far. For each rule, suggest 2-3 specific things to
try by hand, including at least one deliberately adversarial attempt to break it, not just
the expected path.
```

**What to do with the output:**
- Check: does this actually point at gaps, or does it repeat what Block 1 already covered?
  Push back and ask for something sharper if it's too similar.

---

### Prompt 2 - Manually race the concurrency rule

This is the hardest business rule to test by hand, and worth doing properly.

```
I need to manually test whether RetroFlow actually enforces "a team can have multiple
retrospectives, but only one can be OPEN at a time" when two requests happen almost
simultaneously, without writing any code. Give me a step-by-step manual procedure using
two Postman tabs (or a partner on a second machine) to fire two create-retrospective requests
for the same team as close together in time as realistically possible by hand, and tell me
what result would prove the rule holds versus prove it doesn't.
```

**What to do with the output:**
- Actually do this with your partner: one of you has a request ready to send in each of two
  Postman tabs (or two machines), and you both hit send within a second of each other
- Run it 3-4 times. A race condition doesn't always show up on the first attempt
- Record every attempt and its result in `session-notes.md`, including the ones where nothing
  went wrong, that's useful evidence too

---

### Prompt 3 - Run the session

Set a timer for 30 minutes. Work through Prompt 1's targeted list plus the concurrency probe
from Prompt 2. Keep notes in `session-notes.md` using the same format as Block 1:

```
What I tried:
What I expected:
What actually happened:
Worth a closer look? Y/N
```

**What to do during the session:**
- Follow anything that surprises you, don't feel bound to the list if something more
  interesting comes up
- If you find something that looks like a real bug, try to reproduce it a second time before
  moving on, a bug you can't reproduce twice is much weaker evidence

---

### Prompt 4 - Write up what you found

```
Here are my session notes: [paste session-notes.md]. Write these up as clear bug reports, one
per genuine issue: title, severity (Critical / High / Medium / Low), steps to reproduce,
expected result, actual result, business impact. Keep it factual, don't guess at the root
cause, and don't invent an issue that isn't in my notes.
```

**What to do with the output:**
- Merge into the shared `bugs.md` from Block 1, one list for the whole team
- Check each report against your own notes for accuracy before adding it

---

## Role 2 - Manual adversarial & boundary testing

Your job: the manual equivalent of what the Automation track's agent-mode role is doing in
code, systematically hunting for the same categories of gaps, just executed by hand.

---

### Before you start

Agent mode (used by the other track today) generates code. You're going to generate the same
kind of thinking, a systematic list of adversarial and boundary scenarios, and then execute
it yourself, by hand, against the running application.

---

### The expansion brief

The same five categories of gap apply whether you're writing code or testing by hand:

1. **Shallow checks**, only glancing at the status code, not what the response body actually
   contains
2. **No boundary testing**, nothing tried with empty strings, very long strings, missing
   fields, or edge values for categories like priority
3. **No adversarial input**, nothing tried with malformed JSON, wrong content types, or
   unexpected extra fields
4. **No cross-entity consistency checks**, nothing verifying that a retrospective really belongs to the team in the path, or that a
   retrospective's feedback items are actually
   scoped to the right retrospective
5. **No repeatability check**, has anyone actually tried the same request twice to see if the
   result stays consistent?

---

### Prompt 1 - Generate the scenario list

```
I'm manually testing a Spring Boot REST API for RetroFlow, a retrospective-tracking tool.
Generate a checklist of adversarial and boundary-value test scenarios I can execute by hand
in Postman, covering:

1. Boundary and empty/missing input values for each entity (team, retrospective, feedback
   item, action item)
2. Malformed requests (wrong content type, invalid JSON, unexpected extra fields)
3. Cross-entity consistency (does deleting or closing one thing affect related things
   correctly?)
4. Anything where the same request sent twice should, or should not, produce the same result

For each scenario: the exact request to send, and what a correct response should look like.
```

**What to do with the output:**
- Review the list before executing anything. Does it actually cover the app's real entities
  and rules, or does it look generic? Push back if it's generic.

---

### Prompt 2 - Execute the checklist

Work through the checklist in Postman, one scenario at a time. Record every result, including
the ones that behave correctly, in `session-notes.md`.

**What to do during the session:**
- Don't skip a scenario because you expect it to pass. Several real bugs in this app only show
  up on the "boring" cases.
- For anything that looks wrong, try to explain in plain language why, before asking AI, then
  compare your explanation to what AI says when you ask it to help you interpret the response.

---

### Prompt 3 - Interrogate the results

```
Here's what I found manually testing these adversarial and boundary scenarios: [paste
session-notes.md]. For each result marked "worth a closer look," tell me whether this looks
like a genuine defect, expected behaviour that just seemed odd, or something you'd need more
information to judge. Also tell me: based on everything I tried, are there any of the five
categories (shallow checks, boundary values, adversarial input, cross-entity consistency,
repeatability) where I clearly didn't try enough?
```

**What to do with the output:**
- If it flags a category as under-tested, go back and try 2-3 more scenarios there before
  moving on
- Log every genuine defect in `bugs.md`

---

### Prompt 4 - Reflect

This is not an AI prompt. Discuss with your pair and write your answers in `decisions.md`:

1. What did you find manually that you think a hastily-generated automated test would have
   missed, and why?
2. At what point, if any, did you stop trusting your own first read of a response and dig
   deeper?
3. If you were handing this off to someone writing automated tests from your findings, what
   would you tell them to make sure they don't lose?

---

## End of Block 2 checklist

Before lunch at 12:45, your team should be able to answer yes to:

**Automation Track, Role 1:**
- [ ] All four core business rules have executable tests running against the live API
- [ ] `bugs.md` reflects every genuine defect found, with severity and reproduction steps
- [ ] The concurrency test actually races two requests, not just runs them sequentially
- [ ] All three operations on the closed-retrospective rule (add/modify/delete) are tested
      individually

**Automation Track, Role 2:**
- [ ] Agent expansion completed, all five categories addressed
- [ ] No tautological or shallow assertions remain in the new tests
- [ ] `decisions.md` updated with agent reflection answers

**Manual & Exploratory Track, Role 1:**
- [ ] The concurrency rule was tested manually, multiple times, with results recorded
- [ ] `bugs.md` reflects every genuine defect found, with severity and reproduction steps
- [ ] `session-notes.md` shows a focused, targeted session, not a repeat of Block 1

**Manual & Exploratory Track, Role 2:**
- [ ] A full adversarial and boundary checklist was executed by hand, all five categories
- [ ] `decisions.md` updated with reflection answers
- [ ] Every genuine defect found is logged in `bugs.md`

**Everyone together:**
- [ ] `bugs.md` is one shared, merged list, not separate files per pair
- [ ] You each have something to say at Retro 2 about what surprised you, whether that came
      from an agent or from your own hands-on testing
