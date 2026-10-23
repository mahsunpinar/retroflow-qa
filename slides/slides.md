---
theme: default
title: Test Engineer Day Theory
info: |
  AI Track, Module 2, Test Engineer Day.
  The short theory parts for the kick-off, Block 1 to 3 and the wrap-up.
layout: cover
fonts:
  sans: Public Sans
  serif: Rubik
  mono: Fira Code
transition: fade
mdc: true
---

<div class="eyebrow">AI Track · Module 2 · Test Engineer Day</div>

# Working with AI,<br>carefully and efficiently

A bit of theory for every block of today. Short, so you can get back to work.

<!--
We use this deck in five short parts: the kick-off (slides 2 to 5, about 10 minutes), 15 minutes at the start of Block 1, 2 and 3, and the last four slides during the wrap-up. There's no theory in Block 4, it has to stay a surprise. Keep every part short. People learn during the exercises, the theory only gives them the words for what they're about to run into. If you run over, it comes out of the exercise time, so cut a slide instead.
-->

---

<div class="eyebrow">Today</div>

# What are we doing today?

<div class="grid grid-cols-3 gap-5 mt-6">
<div class="card">
<div class="label">BLOCK 1 · MORNING</div>
<h3>Chat</h3>
<p>Prompt well, check by hand, build a test strategy and your first bug list</p>
</div>
<div class="card">
<div class="label">BLOCK 2 · BEFORE LUNCH</div>
<h3>Go deeper</h3>
<p>Executable tests built with an agent, or exploratory testing by hand</p>
</div>
<div class="card">
<div class="label">BLOCK 3 · AFTERNOON</div>
<h3>Grounding</h3>
<p>AI that reads StackCraft's real QA standards, and a review you can defend</p>
</div>
</div>

<div class="footer">Kick-off · Automated or manual: both count today, and both are judged on the same thing. Is your evidence convincing?</div>

<!--
Explain the shape of the day in about a minute. Not everybody here writes automated tests every day, and that's fine. Block 1 is the same for everyone and needs no code. From Block 2 on, pairs go on the Automation track or the Manual and Exploratory track, and you will tell them which one at the start of the block. Say that the pairs rotate and that you announce each new pairing at the start of the block. Don't say anything about the end of the day.
-->

---

<div class="eyebrow">How AI works with context</div>

# What does an AI actually know about RetroFlow?

<div class="grid grid-cols-2 gap-5 mt-6">
<div class="card grey">
<h3>Training</h3>
<p>Billions of public texts and repositories. What a typical test suite and a typical bug report look like. Nothing about RetroFlow, and nothing about StackCraft.</p>
</div>
<div class="card accent">
<h3>The context window</h3>
<p>Your prompt, the chat so far, the files and responses you give it. This is the only place RetroFlow exists for the AI.</p>
</div>
</div>

<div class="footer">Kick-off · If it's not in the context, the AI fills it in with what usually happens</div>

<!--
A quick recap of Module 1, but practical. The AI predicts the most likely next tokens, based on what it was trained on and what's in its context window. Everything about this client only exists in the context. If it's not in there, the AI falls back on the patterns it saw most often. For a tester that has a nasty side effect: ask for the expected response of an endpoint it has never seen, and you get what such an endpoint usually returns, not what this one returns. That is why we keep putting the real brief and the real responses in front of it today.
-->

---

<div class="eyebrow">How AI works with context</div>

# What happens when the brief doesn't say?

<div class="grid grid-cols-2 gap-4 mt-5">
<div class="card"><h3>It fills gaps</h3><p>with what's plausible, not with what's true</p></div>
<div class="card"><h3>It predicts the response</h3><p>"this should return 201" is a guess until you've sent the request</p></div>
<div class="card"><h3>It says "all covered"</h3><p>with total confidence, even if nothing was ever run</p></div>
<div class="card"><h3>Long chats drift</h3><p>old context crowds out what matters now</p></div>
</div>

<div class="footer">Kick-off · The RetroFlow brief has gaps, and that's on purpose</div>

<!--
This slide sets up the whole day, so be careful not to give anything away. Just say the brief has gaps on purpose, and that the app has been built by someone else under time pressure. Don't mention seeded bugs. The question to keep in mind all day: does the AI tell you it is making an assumption, or does it just decide? The last box is a practical one: when a chat gets long and messy, start a new one and only bring what you need. We come back to that in the wrap-up.
-->

---
layout: center
class: statement
---

<div class="eyebrow">The one rule of the day</div>

# Every decision today has a prompt behind it,<br>and you check what comes back

A passing test is not the same as a test that proves something.

<!--
This is the rule from the facilitator guide. Say it in your own words: use AI for test plans, test cases and test evidence, as much as you can. The second half matters just as much. If you skip the check, you're not testing the application anymore, you're trusting an AI. The CTO will review your findings at the end of the day, not your code. Then go straight into Block 1.
-->

---
layout: section
---

<div class="eyebrow">Block 1 · 09:30</div>

# Prompting well

How do you give AI the right context, and how do you make it show its work?

<!--
The Block 1 theory takes about 15 minutes, this slide and the next three. After that, pairs AB and CD start on their prompt ladders. Everything this morning is chat, with Postman or a browser next to it. Nobody writes code.
-->

---

<div class="eyebrow">Prompting well</div>

# What goes into a good prompt?

| Part | In today's case |
|------|-----------------|
| **Role** | You're a test engineer taking over QA for a backend nobody has tested yet |
| **Context** | The brief, plus the endpoint list and the responses you've seen |
| **Task** | Write a risk-based test plan for RetroFlow |
| **Format** | A table: ID, steps, expected result, the business rule it verifies |
| **Constraints** | Only use the brief. Flag everything it doesn't say. Explain the priority order |

<div class="footer">Block 1 · Most weak prompts only contain the task</div>

<!--
Don't turn this into a lecture about prompt frameworks. The point is that most weak prompts only have the task in them. Walk through the table using Prompt 2 of the AB ladder, which they'll send in a few minutes anyway. The constraints row is where most of the quality is decided. Another habit worth naming: ask for the reasoning, not just the result. A risk ranking without a reason is just a list.
-->

---

<div class="eyebrow">Prompting well</div>

# Give it the evidence, not your memory of it

<div class="grid grid-cols-2 gap-5 mt-5">
<div class="card grey">
<div class="label">WEAK</div>
<p>"The create call for retrospectives works fine, I think. Write me the test cases."</p>
<p class="text-sm mt-3">That's your summary, with your assumptions already in it. There's nothing to check it against.</p>
</div>
<div class="card accent">
<div class="label">STRONG</div>
<p>"[the brief] and [the actual response I got: status code and body]. Which rule does this verify, and does the response match the brief?"</p>
<p class="text-sm mt-3">The AI works from the real thing, and every claim points at something you can look up.</p>
</div>
</div>

<div class="footer">Block 1 · Paste the real response, not "it returned an error"</div>

<!--
Three habits on one slide. First: give it the source and the real response, not a summary. Second: ask it to point at the part of the brief or the response behind every claim, so checking becomes a search and not a judgement call. Third: ask it to say what it assumed, otherwise a guess looks exactly like a fact. A status code in a chat message is a claim. A status code in a Postman screenshot is evidence.
-->

---

<div class="eyebrow">Prompting well</div>

# You can't check everything, so what do you check?

<div class="grid grid-cols-3 gap-5 mt-5">
<div class="card">
<div class="big-number">1</div>
<h3>Run it</h3>
<p>The cheapest check there is. Send the request. What did the app really do?</p>
</div>
<div class="card">
<div class="big-number">2</div>
<h3>Risk-check</h3>
<p>Look hardest at what hurts most when wrong: the business rules, the status codes, what the error responses show</p>
</div>
<div class="card">
<div class="big-number">?</div>
<h3>Ask it back</h3>
<p>"What would this test catch? What would it miss?"</p>
</div>
</div>

<div class="footer">Block 1 · Every prompt in your ladder tells you what to check. Don't skip those.</div>

<!--
Nobody can check everything, and that's not what we're asking. We want a habit: let the application tell you what it does (send the request), read carefully what's risky, and ask the AI where its own output is weak. The third card is Prompt 5 of the AB ladder, the best prompt of the block. Asking the AI to review its own output is a real technique, but it only works if you know enough to judge the answer.
-->

---
layout: section
---

<div class="eyebrow">Block 2 · 11:15</div>

# Going deeper

Executable tests built with an agent, or exploratory testing by hand. And from now on you write the prompts.

<!--
The Block 2 theory takes about 15 minutes. Tell each pair which track and role they have, and why: you looked at who was driving the keyboard this morning. Then say it out loud: from now on they write the prompts. We tell them the goal, they decide the words. Remind them to start a prompt log.
-->

---

<div class="eyebrow">Going deeper</div>

# A failing test: is it the app, or is it the test?

<div class="grid grid-cols-3 gap-5 mt-5">
<div class="card">
<h3>Bug in the app</h3>
<p>The response doesn't match the rule. Log it in <code>bugs.md</code>, with the request and the response.</p>
</div>
<div class="card">
<h3>Bug in the test</h3>
<p>You read the contract wrong. Fix the test, and write down what you misread.</p>
</div>
<div class="card">
<h3>The rule is unclear</h3>
<p>Both readings are defensible. Somebody has to decide. Not you, not the AI.</p>
</div>
</div>

<p class="font-semibold mt-6" style="color: var(--navy)">The AI can help you reason about it. But reasoning from the real response, not from the code it can't see.</p>

<div class="footer">Block 2 · Read the response body yourself before you accept a verdict</div>

<!--
This is the judgement call of the whole day, and it applies to both tracks: the manual tester asks the same question when a response looks odd. A confident AI will happily say "your test is wrong" or "the app is wrong", and be right about half the time. The way to settle it is to look at the actual response next to the rule in the brief. Don't give them answers when they ask. Ask: "what would settle this for you?"
-->

---

<div class="eyebrow">Going deeper</div>

# Green doesn't mean good

<div class="grid grid-cols-2 gap-8 mt-4">
<div>

```java
@Test
void closeRetrospective() {
    var response = api.close(retroId);
    assertThat(response.getBody()).isNotNull();
}
```

</div>
<div>
<p>This test is green when the retrospective is closed.</p>
<p class="mt-2">It's also green when it isn't, when the call fails halfway, or when the status is wrong. It checks nothing.</p>
<p class="mt-4 text-sm">The manual version: a test case that says "checked, looks fine", without the response you saw.</p>
</div>
</div>

<div class="footer">Block 2 · Ask of every test: what would make it fail?</div>

<!--
AI is very good at producing tests that pass, and not always good at producing tests that prove something. Here the vendor's suite probably has a few of these already. The manual equivalent is the note without evidence. Both give a false sense of security, which is the thing the CTO is most worried about. Keep it short: they will meet it again in their own work and in Block 3.
-->

---

<div class="eyebrow">Going deeper</div>

# The same five gaps, by code or by hand

| Gap | The agent (Automation) | You (Manual) |
|-----|------------------------|--------------|
| **Shallow checks** | Asserts on body content, not just status | Reads the body, not only the status |
| **No boundaries** | Empty, null, very long, enum edges | The same, in Postman |
| **No adversarial input** | Malformed JSON, wrong content type, extra fields | The same, in Postman |
| **No cross-entity checks** | Wrong team, wrong retrospective | The same, by hand |
| **Dependent tests / no repeatability** | Run twice, compare | Send it twice, compare |

<div class="footer">Block 2 · Two ways to hunt for the same kind of bug</div>

<!--
This slide connects the two tracks, so nobody feels they got the lesser one. The Automation pairs use an agent to generate tests for these gaps. The Manual Role 2 pair finds them by hand. The manual tester often finds things a hastily generated suite would miss, because they read the responses. Say that out loud. And the agent can only test the gaps you tell it about, which is why they need to read the plan.
-->

---

<div class="eyebrow">Going deeper</div>

# Chat or agent? Plan, act, check

<div class="flex gap-3 mt-6">
<div class="card accent flex-1">
<h3 style="color: var(--blue)">1 · Plan</h3>
<p>"Look at the suite. Don't write anything yet. Tell me what's missing and why."</p>
<p class="font-semibold mt-2" style="color: var(--navy)">You: agree or correct</p>
</div>
<div class="arrow">→</div>
<div class="card accent flex-1">
<h3 style="color: var(--blue)">2 · Act</h3>
<p>"Do it one part at a time. Stop after each part and list what you added."</p>
<p class="font-semibold mt-2" style="color: var(--navy)">You: watch, stop it early</p>
</div>
<div class="arrow">→</div>
<div class="card accent flex-1">
<h3 style="color: var(--blue)">3 · Check</h3>
<p>"Run it twice. Explain five tests in plain words." Then read the diff yourself.</p>
<p class="font-semibold mt-2" style="color: var(--navy)">You: verify, not trust</p>
</div>
</div>

<div class="footer">Block 2 · An agent reads and edits several files by itself. A chat gives you text. Today's rule for the agent: no hand-written tests.</div>

<!--
Both Automation roles work with an agent this block (Role 1 builds the business rule tests with it, Role 2 expands the suite), and the Manual track benefits from the structure, also in a chat: ask for the plan first, then act, then check. The plan is the cheapest place to catch a mistake. Explain why we don't allow hand-written tests today: fixing things through the agent is how you find out where agents go wrong. On a real project you'd obviously fix the small stuff yourself. Tell the pair to open the Source Control panel and keep it open. It lists every file the agent touched, and clicking one shows exactly what changed.
-->

---
layout: section
---

<div class="eyebrow">Block 3 · 13:45</div>

# Grounding & reviewing

We connect AI to StackCraft's real QA standards, and we review RetroFlow with it.

<!--
The Block 3 theory takes about 15 minutes. Both tracks do step 1 with the connector. The Automation pair keeps using it for the audit, the Manual pair uses the Claude.ai connector for the first step and then moves on to the release review. The setup is at the top of their file, there is nothing to install. Say it again: they write the prompts, with even fewer hints than this morning.
-->

---

<div class="eyebrow">Grounding</div>

# Trained on it, or reading it right now?

<div class="grid grid-cols-2 gap-5 mt-6">
<div class="card grey">
<div class="label">UNGROUNDED</div>
<h3>Answers from training</h3>
<p>What a typical QA standard says. Plausible, generic, confident. It can cite a "rule" that sounds exactly right and doesn't exist.</p>
</div>
<div class="card accent">
<div class="label">GROUNDED</div>
<h3>Answers from the document</h3>
<p>Reads StackCraft's real text at the moment you ask, and quotes the section. You can look it up.</p>
</div>
</div>

<div class="footer">Block 3 · In step 1 you ask the same question twice: without and with the connection</div>

<!--
Grounding just means you give the AI the source it should rely on at the moment it answers, instead of letting it answer from memory. They've been doing a small version of this all day by pasting the brief and the responses. Fetching the right text and putting it in the context is called RAG, retrieval-augmented generation. MCP, on the next slide, is a standard way to hook a tool up to a source. Don't tell them what the right answer to the step 1 question is. That's the whole point of the exercise.
-->

---

<div class="eyebrow">Grounding</div>

# So what is MCP?

<div class="flex gap-3 mt-6">
<div class="card flex-1"><h3>Your AI tool</h3><p>Claude.ai, or Copilot in your IDE</p></div>
<div class="arrow">→</div>
<div class="card accent flex-1"><h3>MCP server</h3><p>Offers tools: list_documents, read_document</p></div>
<div class="arrow">→</div>
<div class="card flex-1"><h3>The source</h3><p>StackCraft's QA standards</p></div>
</div>

The AI calls a tool, the server sends back the real text, and the AI answers from that text. At a client, the source could just as well be a test management tool, the bug tracker or a monitoring platform.

<p class="font-semibold mt-3" style="color: var(--navy)">Before you connect a tool at a client: who runs the server, what can it read and write, and what leaves the machine?</p>

<div class="footer">Block 3 · Cheat sheet: ai-cheat-sheets/what-is-mcp.md</div>

<!--
MCP stands for Model Context Protocol. It's an open standard, so one server works with lots of AI tools, a bit like USB for AI. Our server is tiny on purpose: two tools, read-only. Tell them what to look for in the chat: the AI says it's calling read_document. That visible tool call is how you know the answer is based on the document. The bold line is the reflex to build before connecting anything at a client, because the output of an MCP server goes straight into the model's context.
-->

---

<div class="eyebrow">Reviewing with AI</div>

# When AI reviews, ask for proof

<div class="grid grid-cols-2 gap-5 mt-5">
<div class="card grey">
<div class="label">OPINION</div>
<p>"The API might leak internal details in its error responses."</p>
<p class="text-sm mt-3">Sounds serious. Can you act on it? Is it even real?</p>
</div>
<div class="card accent">
<div class="label">PROOF</div>
<p>"This request gives this response, and the body contains a stack trace with class names."</p>
<p class="text-sm mt-3">You can reproduce it in a minute, or throw it out.</p>
</div>
</div>

<p class="mt-5">Expect false positives: findings outside the scope you gave it, and standards that were never written down. Reproduce the Critical ones yourself.</p>

<div class="footer">Block 3 · The Automation pair checks every quote against the real document. The Manual pair reproduces every finding.</div>

<!--
Both tracks get the same lesson from a different angle. The Automation pair gets quotes from the standards and has to find the invented ones. The Manual pair gets review findings and has to find the false ones. In both cases, the answer that sounds the most authoritative is the one you check first. A good example for the Manual pair: the brief says no authentication is needed. If the AI flags missing authentication anyway, it ignored your scope.
-->

---

<div class="eyebrow">Reviewing with AI</div>

# "A test exists" is not "the rule is verified"

| Claim | What it really tells you |
|-------|--------------------------|
| "There's a test case for this rule" | Somebody wrote it down |
| "The test case was run" | Somebody ran it. What was the actual response? |
| "The test passed" | Does it assert something that could fail? |
| "The rule is covered" | Yes, if you can point at the response that proves it |

<div class="footer">Block 3 · In the business rule review: every "covered" needs an artifact and a recorded result</div>

<!--
This is the most important review of the Manual track, and it applies to the Automation track too. AI will happily say a rule is covered because a test with a matching name exists. Your job is to follow the claim to the evidence. If there is no recorded response, it's a known gap, and you put it in the report as a known gap. A report that says honestly what you didn't verify is more useful to the CTO than a clean one.
-->

---
layout: section
---

<div class="eyebrow">Wrap-up</div>

# What do we take with us?

<!--
There's no theory in Block 4, so these four slides are for the wrap-up at 16:45, after the debrief. They are the three messages from the facilitator guide in slide form.
-->

---

<div class="eyebrow">Wrap-up · Working efficiently</div>

# A few habits to take home

<div class="grid grid-cols-2 gap-4 mt-4">
<div class="card"><h3>New topic, new chat</h3><p>Bring only the source and the current state.</p></div>
<div class="card"><h3>Responses, not summaries</h3><p>Paste the real response. Your summary holds your assumptions.</p></div>
<div class="card"><h3>Ask for the format you need</h3><p>Tables, bug reports, test cases with expected results.</p></div>
<div class="card"><h3>Store what works</h3><p>Good result: save the prompt. Bad result: write down why.</p></div>
<div class="card"><h3>Check two things, always</h3><p>Run it yourself, and look at the riskiest claim.</p></div>
<div class="card"><h3>Check what it added</h3><p>New tests, new dependencies, new "facts". Does each one exist?</p></div>
</div>

<div class="footer">Wrap-up · Your prompt log is the start of your team's prompt library</div>

<!--
The first four are the practical habits. The last box ties back to Block 4 and the prompt logs: start a library from the prompts that actually found real bugs today, not from the prompts you think you'll need. The fifth habit, check what it adds, is a good place to refer back to the last block of the day.
-->

---

<div class="eyebrow">Wrap-up</div>

# How much should you check? That depends on the cost of being wrong

| Task | Cost of being wrong |
|------|---------------------|
| Drafting test case names and descriptions | Low |
| Generating boundary and negative tests | Low to medium (run them, and read five) |
| Writing a bug report for the team | Medium (reproduce it first) |
| A release recommendation | High |
| Anything that says "covered" or "no issues found" | High |
| **Testing code that handles personal or payment data** | **Very high** |

<div class="footer">Wrap-up · Where did the CTO's urgent request sit on this table?</div>

<!--
Working efficiently doesn't mean checking less everywhere. It means you spend your checking where being wrong costs the most. If the room just had the debrief, ask where the integration they planned tests for at 16:00 sits on this table, and how much checking it got.
-->

---
layout: center
class: statement
---

<div class="eyebrow">Monday</div>

# Try one thing on Monday

Take your prompt log with you. Pick one thing from today and do it differently on your project this week. Come back with a result.

<!--
End the day here. This is the third message from the facilitator guide: ask every group to name their one thing out loud before they leave.
-->
