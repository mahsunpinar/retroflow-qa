# AI for Manual and Exploratory Testing

Not every test engineer writes automated tests, and most of today's exercises assume you do.
This page is specifically for manual and exploratory testing work, no code, no automation
required.

## Why this matters

The AI tools in this workshop are mostly demonstrated through code (Copilot generating JUnit
tests, agent mode fixing a failing suite). If your day-to-day work is manual or exploratory
testing, that can feel like none of it applies to you. It does, just applied differently: the
same AI chat tools (Copilot Chat, Claude.ai) are just as useful for planning what to test and
documenting what you found as they are for writing code.

## Drafting an exploratory testing charter

An exploratory testing charter is a short, focused brief for a testing session: what area
you're exploring, what you're specifically looking for, and any known risks. AI is good at
turning a feature description into a first-draft charter you can then adjust based on your own
judgment.

Example prompt:

> "Here's a description of a new complaint extension approval feature: [paste description].
> Generate an exploratory testing charter covering the main happy path, likely edge cases
> (timing boundaries, permission checks, concurrent requests), and anything that looks
> underspecified in this description."

That last part matters: asking it to flag what's underspecified often surfaces exactly the kind
of ambiguity worth testing deliberately.

## Generating test case checklists

For more structured manual testing, ask for a checklist rather than a narrative:

> "Generate a manual test case checklist for the complaint acknowledgement flow. Include the
> happy path, the 2-working-day deadline boundary (day 1, exactly day 2, day 3), and what
> should happen if the customer's contact details are invalid."

Use this as a starting checklist, not a finished one. AI is reliably good at generating the
obvious cases and inconsistently good at generating the specific, business-critical edge case
that only shows up if it actually knows your rules, which is exactly what Block 3's MCP
grounding is about: an AI reading the real standards document produces a meaningfully better
checklist than one guessing from general testing knowledge.

## Drafting bug reports

A clear bug report takes time to write well, especially under pressure. AI is well suited to
turning rough notes into something readable:

> "Here are my raw notes from testing: [paste notes]. Write this up as a clear bug report with
> a title, steps to reproduce, expected result, actual result, and severity. Keep it factual,
> don't guess at the root cause."

That last instruction matters: it's easy for AI to speculate about why something is broken and
present that speculation as if it were established fact. Ask it to stick to what you observed.

## What to be careful about

- **AI doesn't know your product's history.** It won't remember that a similar bug was
  introduced by a specific past change, or that a certain edge case is a known recurring issue.
  Your domain knowledge still matters more than anything AI generates.
- **A checklist isn't a substitute for actually exploring.** Use AI-generated checklists as a
  starting point for a session, not as the entire test plan. The most valuable bugs are often
  found by deviating from the checklist, not following it.
- **Verify claims about expected behavior** against the actual standard or requirement, the
  same habit that applies everywhere else in this workshop. If you're unsure whether AI's
  "expected result" is actually correct, check the source document rather than trusting the
  confident phrasing.

## If you get stuck

See `../../ai-tooling-fallback.md` for what to do if your AI chat tool isn't accessible, or ask
your coach.
