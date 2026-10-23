# Block 3: example prompts (answer key)
**13:45 - 15:15 · Two tracks**

**Coach only. Keep this closed on your own laptop.** Participants work from `block3-own-prompts.md`. When a pair has tried a step twice and is still stuck, show them just the one example prompt for that step, not the whole file. Looking is allowed, as long as they write down in their prompt log what the example does that theirs didn't.

These are examples, not the answer. Whatever you change, keep the habits: the source pasted in, the actual response read by hand, nothing accepted before it was reproduced.

---

Final pair rotation. This block goes deeper than anything you've done today. The goal is not
to finish everything, it's to experience what these tools actually feel like and where they
genuinely change the way you work.

Your coach will tell you which track your pair is on: **Automation** (the MCP server deep
dive) or **Manual & Exploratory** (the structured release review). If your group has two pairs
on the same track, work in parallel and compare notes at the showcase.

---

# Automation Track

## MCP server: grounded AI for test standards

You are going to connect to a hosted MCP server and use it to give your AI tool real,
grounded context from StackCraft's internal QA standards. This is the foundation of how
serious AI-assisted testing should work, not relying on the model's training data about
"typical" testing best practice, but feeding it the actual source of truth for this client.

---

### What is MCP and why does it matter?

MCP (Model Context Protocol) is a standard that lets AI tools connect to external data sources
and tools as first-class capabilities. Instead of copy-pasting context into a chat window,
you give the AI a live connection to the information it needs. See
`ai-cheat-sheets/what-is-mcp.md` if you want the fuller explanation before starting.

Today you'll connect to a hosted MCP server that serves StackCraft's actual QA standards
document, then use it to audit your team's test suite and bug reports. When you ask a
question, the AI can call that server to read the real document and ground its answer in it,
rather than making things up from training data, generic "testing best practices" advice from
a model's training data will not match a specific client's actual bug report format, coverage
thresholds, or flaky-test policy.

---

### Setup (do this before prompting)

No local install either way, this is a hosted server, not a local one. Pick whichever matches
what you actually have.

**If you have an IDE with Copilot (VS Code, IntelliJ, Visual Studio):** open `.vscode/mcp.json`
in your project (VS Code: Command Palette → **MCP: Add Server**; IntelliJ: Settings → Tools →
GitHub Copilot → Model Context Protocol (MCP) → Configure; Visual Studio: add an `mcp.json` to
the solution) and add:
```json
{
  "servers": {
    "ai-track-standards": {
      "type": "http",
      "url": "<hosted MCP server URL, ask your coach>"
    }
  }
}
```
Verify: open Copilot Chat in agent mode. You should see `ai-track-standards` listed as an
available tool, exposing `list_documents` and `read_document`.

**If you don't have an IDE at all:** use a free Claude.ai account instead, this needs nothing
installed, just a browser.
1. Create a free account at [claude.ai](https://claude.ai) if you don't have one.
2. Go to **Customize → Connectors → Add custom connector**.
3. Paste in the hosted MCP server URL (ask your coach) and click **Add**. No authentication step
   needed, the server is open, nothing to log into.
4. In a new chat, click **+** → **Connectors**, and toggle `ai-track-standards` on for that
   conversation.

Free accounts are limited to one custom connector, that's exactly what this needs, so no paid
plan required. Once connected, the same prompts below work identically; wherever a prompt says
"call read_document," Claude will use the connector the same way Copilot would use the MCP tool.

---

### Prompt 1 - Verify the connection

```
Using the ai-track-standards MCP server, call read_document with id "stackcraft-qa" to read
the StackCraft QA standards document, and give me a summary of the five rules most likely to
be violated by a test suite written under time pressure.
```

**What to do with the output:**
- If your AI tool reads from the actual file: the MCP connection works. The answer will
  reference specific rules from the StackCraft QA standards.
- If it produces generic testing advice instead: the MCP connection is not working. Check
  the setup steps before continuing.

The difference matters. This is the difference between grounded AI and hallucinated AI.

---

### Prompt 2 - Audit the test suite against the standards

```
Using the ai-track-standards MCP server's read_document tool (id "stackcraft-qa") to read
the StackCraft QA standards, audit our test suite for violations.

[paste your full test suite]

For each violation:
- Name the file and test method
- Quote the relevant standard that is violated
- Explain why it's a violation
- Suggest the correct approach

Sort violations by severity: Critical → Major → Minor.
```

**What to do with the output:**
- Check: are the quoted standards actually in the StackCraft QA standards document, or did
  the AI invent them? This is easy to verify, open the document and check.
- This is a live demonstration of grounded vs. ungrounded output. Note down any invented
  standard references for Retro 3.

---

### Prompt 3 - Fix the critical violations

```
From the audit, take all Critical violations and fix them in the test suite now.
For each fix:
- Apply the change
- Add a comment explaining what was wrong and what was fixed
- If the violation reveals an actual application bug (not just a test quality issue),
  log it in bugs.md as well
```

---

### Prompt 4 - Expand the use of MCP

Now that you've seen MCP working with a hosted server, think about what else this could
connect to in a real QA workflow. This is a discussion + research prompt:

```
What other MCP servers are publicly available that would be useful for a test engineer
working on a Spring Boot API project at a client site? Think about test management tools,
bug trackers, and observability/monitoring platforms.

Search for and list 5 real, available MCP servers (not hypothetical ones) with:
- What they connect to
- What you could do with them in a testing workflow
- Any security considerations for using them at a client
```

**What to do with the output:**
- Verify the listed servers actually exist, don't take the AI's word for it
- Note: this is a good test of hallucination risk. MCP servers are new enough that the
  model's training data may be incomplete or outdated.
- Write your findings in `decisions.md` under an "MCP exploration" section

---

## End of Block 3 checklist (Automation Track)

- [ ] MCP server is running and verified (Prompt 1 output references real file content)
- [ ] Audit completed and Critical violations fixed
- [ ] `decisions.md` updated with MCP exploration findings
- [ ] You can explain the difference between grounded and ungrounded AI output

---

# Manual & Exploratory Track

## AI-assisted structured review: the release quality gate

Your job: use AI to produce a structured pre-release review of RetroFlow, the kind of review
a senior test engineer would present before signing off on a release to 12 teams. This teaches
you to use AI as a reviewer, not just a generator, and it needs nothing but the artifacts your
team has already built today: no code required.

The interesting challenge: AI will find real issues AND invent fake ones. Your job is to tell
the difference.

---

### A short grounding demonstration first

Before the review itself, see what "grounded" AI actually means, since the Automation track is
experiencing the same idea through a more technical setup.

If you have a free Claude.ai account with the workshop's connector added (see
the setup steps in `block3-own-prompts.md` if you haven't set this up), try:

```
Without looking anything up, what does a typical QA standards document usually say about
tautological assertions like "assertThat(result).isNotNull()"?
```

Note the answer. Now enable the connector for the conversation and ask:

```
Using the connected document, what does StackCraft's actual QA standard say about
tautological assertions? Quote it directly.
```

**What to do with this:** compare the two answers. The first is a plausible-sounding guess.
The second should quote the real document (section 2.1) word for word. That gap, between a
confident guess and a grounded answer, is exactly what you're checking for throughout the
review below, just done by reading, not by MCP.

---

### The review mandate

You are acting as StackCraft's senior test engineer. Before RetroFlow ships to all 12 teams,
you need to produce:

1. A **security review**, what could go wrong if this API had a wider audience than expected?
2. An **API contract review**, is the API well-designed and does it behave as documented?
3. A **business rule coverage review**, is every rule from the brief actually verified,
   whether by an executable test or by something your team manually confirmed?
4. A **test quality review**, do the checks that exist, automated or manual, actually test
   the right things?

---

### Prompt 1 - Security review

```
Perform a security review of this Spring Boot REST API, from a tester's perspective, focused
on what you can verify by sending requests, not static code analysis.

[paste the API contract / endpoint list, and any responses you've collected]

Check for:
1. Input validation gaps, what happens if a malicious or malformed request is sent to each
   endpoint?
2. Information disclosure, do any error responses leak stack traces, internal class names,
   or database error text?
3. Any endpoint that returns more data than a caller should reasonably need
4. Any other security concern testable from the outside, without reading the source code

For each finding:
- Severity: Critical / High / Medium / Low
- The exact request that demonstrates it
- The actual response received
- Recommended fix
```

**What to do with the output:**
- The brief explicitly says no auth is required for this internal tool, so "missing
  authentication" is not a valid finding for today's scope. If AI flags it anyway, note this:
  AI reviewers don't always respect stated scope.
- For each Critical or High finding: reproduce it yourself with an actual request in Postman
  before accepting it into your report.

---

### Prompt 2 - API contract review

```
Review this REST API for contract correctness and consistency, based on the actual responses
you've collected during testing today.

[paste endpoint list and sample responses]

Evaluate:
1. Are the HTTP methods correct for each operation? (GET should not modify state, etc.)
2. Are the status codes correct and consistent? (201 for creation, 404 for not found, etc.)
3. Are error responses consistent in structure across all endpoints?
4. Is there anything that would surprise a consumer of this API relying on documented behaviour?

Produce a review in this format:
- PASS / FAIL / CONCERN for each category
- For each FAIL or CONCERN: specific finding + recommended fix, backed by an actual response
  you observed, not a hypothetical
```

**What to do with the output:**
- Cross-check every FAIL against an actual response you captured, don't accept a finding
  you haven't personally reproduced
- Pay attention to the retrospective-creation status code, what does your evidence say it
  actually returns?

---

### Prompt 3 - Business rule coverage review

```
Here are the business rules from the RetroFlow brief:

[paste the business rules section]

Here is everything my team has produced testing it today, our test case catalogue, session
notes from exploratory testing, and our bug list:

[paste test-plan.md, your test case catalogue, session-notes.md, and bugs.md]

For each business rule:
1. State whether it is covered, whether by an executable test, a manually-executed test case,
   or documented exploratory testing
2. If yes: quote or reference exactly which artifact covers it, and state whether the actual
   recorded result confirms the rule holds
3. If no: state that it is missing and what would happen if this shipped without coverage
4. If partially: describe what's missing

Be exhaustive. Check every single rule, including all three operations of the
closed-retrospective immutability rule.
```

**What to do with the output:**
- This is the most important review of the day. Cross-reference every finding against your
  actual recorded results, a test case "existing" is not the same as a test case having
  actually been run and passed.
- If AI says a rule is covered by something you know wasn't actually confirmed: flag that
  explicitly in your report as a known gap, not a covered rule.

---

### Prompt 4 - Test quality review

```
Here is everything we've produced testing this today, automated or manual:

[paste your test case catalogue, session-notes.md, bugs.md, and, if your team has any, the
automation track's test code]

Review this for:
1. Checks that pass but don't actually test the right thing (shallow assertions, or manual
   checks that only glanced at a status code)
2. Business rules that have no real coverage at all
3. Checks that are too broad or too narrow
4. Anything that could give a false sense of security

For each finding, show me the specific test case or note and explain the problem. If there
are checks with weak evidence, show me what stronger evidence would look like.
```

**What to do with the output:**
- This is adversarial by design. AI is reviewing work that may itself have involved AI. Can
  it catch its own mistakes, or your team's?
- Note: a manual test case that just says "checked, looks fine" with no recorded actual
  response is the manual equivalent of `assertThat(result).isNotNull()`, a very common
  weak-evidence pattern. Look for it in your own notes.
- Strengthen any weak evidence it identifies, these are real gaps.

---

### Prompt 5 - Compile the review document

```
Based on the four reviews we've done (security, API contract, business rule coverage, test
quality), plus the bug list we've accumulated all day, produce a single structured
pre-release quality report I could realistically hand to the StackCraft CTO.

Format:
# RetroFlow Pre-Release Quality Report
## Executive Summary (3-4 sentences)
## Bugs Found (severity, reproduction steps, business impact, pull from bugs.md)
## Critical Issues (must fix before release)
## Major Issues (should fix before release)
## Minor Issues / Recommendations (fix when time allows)
## What's tested and trustworthy (at least 3 things)
## What we did NOT have time to verify
## Release recommendation: BLOCKED / CONDITIONAL / APPROVED

Be honest. If there are real problems, flag them as blocking. If there are things you
genuinely didn't get to, say so explicitly rather than implying full coverage.
```

**What to do with the output:**
- Save this as `pre-release-quality-report.md`
- This is your showcase artifact for Retro 3
- Note the release recommendation, and whether you agree with it

---

## End of Block 3 checklist (Manual & Exploratory Track)

- [ ] All four reviews completed
- [ ] `pre-release-quality-report.md` exists and is saved
- [ ] You can name at least one finding that was a false positive and one that was real
- [ ] Release recommendation determined and you can defend it
- [ ] You can explain, in your own words, the difference between a guessed answer and a
      grounded one, from the demonstration at the start of this block

---

## Both tracks together

- [ ] `bugs.md` reflects every genuine defect found across the whole day
- [ ] You each have one concrete thing to share at Retro 3
