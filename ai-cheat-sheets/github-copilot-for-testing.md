# GitHub Copilot for Test Engineers

GitHub Copilot is the primary AI tool used throughout today's workshop. If you've never used it,
or only ever accepted the odd autocomplete suggestion, read this first.

## The three things Copilot actually does

**Inline suggestions.** As you type, Copilot suggests code, usually grayed out ahead of your
cursor. Tab accepts, Esc dismisses. Useful, but not the focus of today's exercises.

**Copilot Chat.** A conversation panel next to your code. You ask questions, describe what you
want, paste in a stack trace, or ask it to generate tests for a piece of code. It proposes
changes, which you review before accepting. This is where most of today's work happens.

**Agent mode.** A more autonomous version of Chat: it can read multiple files, make a plan,
edit several files, run commands (like your test suite), check the results, and keep going
until the task is done or it needs input. You review and approve as it goes. This is what
Block 2 is about.

## Getting started with Copilot Chat for testing

1. Open the Chat panel in your IDE.
2. Select the code you want to test, then ask directly, or use the built-in shortcut:

   - `/tests`: generates tests for the selected code
   - `/explain`: explains what a piece of code (or a failing test) is doing
   - `/fix`: proposes a fix for a selected error

3. Review the proposed test before accepting it. A generated test that compiles and passes is
   not automatically a test that checks the right thing, more on this below.

## Prompting for better test output

Generic prompts get generic tests. Compare:

> "Write tests for this."

versus

> "Write JUnit tests for this `ComplaintService.resolve()` method. Cover: a complaint resolved
> within the 5-day internal target, one exceeding the 8-week regulatory limit, and one where an
> extension has been approved. Use the business rules in `stackcraft-qa-standards.md` as the
> source of truth for expected behavior, not assumptions."

The second version tells Copilot exactly which scenarios matter and, critically, points it at
the actual standard rather than letting it guess what "correct" behavior looks like.

## Using agent mode for a test suite

Agent mode is useful when you need to get a whole suite passing, not just write one test:

1. Describe the goal: "Get all tests in `RetroflowBusinessRulesTest` passing. Some are failing
   because the implementation doesn't match the business rules in the standards doc, not
   because the tests are wrong. Check the standards doc before deciding which side to fix."
2. Watch it run the test suite, read the failures, and propose changes.
3. Review each proposed fix. Specifically check whether it fixed the actual code to match the
   rule, or just changed the test to match whatever the code currently does; the second one
   makes a real bug pass silently.

## What to be careful about

- A passing test isn't a correct test. Verify the assertions actually reflect the real business
  rule, not just "the code as written."
- Copilot doesn't know your specific standards unless it's grounded against them (see
  `what-is-mcp.md`) or you paste them in directly.
- Don't paste real customer data or credentials into a prompt, use the workshop's sample data.

## If you get stuck

See `../../ai-tooling-fallback.md` for what to do if Copilot itself isn't working (license
issues, rate limits, or access problems), or ask your coach.
