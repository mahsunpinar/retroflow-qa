# Postman's Built-In AI (Postbot)

Postman is already part of most test engineers' daily routine. What many people miss is that
Postman has AI features built directly into it, which can save real time on writing requests
and test scripts. This isn't part of today's core exercises, but it's directly applicable to
your everyday work, so it's worth learning.

## What Postbot is

Postbot is Postman's built-in AI assistant. It lives inside the Postman app itself (look for a
Postbot icon, usually a small sparkle or bot icon near the request or test script area) and can
generate requests, generate test scripts, explain a response, and suggest example values,
without you leaving Postman or copying anything into a separate chat tool.

## Getting started

1. Open Postman and open (or create) a request.
2. Look for the Postbot icon, typically near the top of the request builder or in the "Tests"
   tab.
3. Click it and describe what you want in plain language.

## What it's useful for

**Generating test scripts.** Instead of hand-writing Postman test assertions, describe what you
want checked:

> "Write a test that checks the response status is 200, the body contains a valid ISO 8601
> date in the `resolvedAt` field, and the `status` field equals 'resolved'."

Postbot will generate the JavaScript test assertions for you, which you can then review and
run.

**Generating example requests.** If you're building out a new request against an API you have
documentation for, you can ask Postbot to draft the request body from a description or from a
pasted API spec, rather than typing the JSON by hand.

**Explaining a response.** Paste a response body (or let it read the last response) and ask
"what's unusual about this response?" or "does this match what I'd expect for a successful
request?" Useful when debugging an unfamiliar API.

## Prompting it well

Same principle as any AI tool: specificity gets you a usable result faster.

> "Write a test."

versus

> "Write a test that verifies the extension request is rejected with a 403 if it's submitted
> more than 24 hours after the original complaint was logged, per our business rules."

The second version tells it the actual rule to test against, rather than letting it invent a
plausible-sounding but possibly wrong assertion.

## What to check before trusting it

- **Verify against the real business rule, not just against what looks reasonable.** A
  generated assertion that checks "status is 200" is trivially correct but often not the point;
  check that it's testing the behavior that actually matters, per
  `stackcraft-qa-standards.md`.
- **Check edge cases yourself.** Postbot is good at generating an obvious first test, less
  reliable at spotting the edge case a client's business rules actually turn on (timeouts,
  boundary values, contradictory requirements). Use it as a starting point, not the finish line.
- **Don't paste real customer data or credentials** into a prompt, including inside Postbot.

## Why this is worth learning now

Unlike most of today's AI tools, which are demonstrated specifically for the workshop, Postbot
is something you can start using in your actual Postman collections tomorrow, with zero new
setup, since it's already built into the tool you use daily.
