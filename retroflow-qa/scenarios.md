# RetroFlow manual API test scenarios

This risk-based Postman checklist starts with input validation and parent/child integrity, then exercises lifecycle rules and repeat requests. It prioritizes data that must remain trustworthy after a retrospective is closed. Use the documented API paths and verify each response and subsequent GET; the response schema and most error status codes are not specified, so assert the stated status class and characteristics rather than undocumented message text.

## Postman setup and execution notes

- Set `{{baseUrl}}` to the group's hosted URL, without a trailing slash. Use `Accept: application/json` on all requests and `Content-Type: application/json` on requests with JSON bodies.
- Create fixture teams with a unique name containing your initials. Use team/retrospective/item IDs returned by successful requests; save them as Postman variables such as `{{teamAId}}`, `{{retroAId}}`, `{{feedbackAId}}`, and `{{actionAId}}`. Use distinct teams and retrospectives where a scenario says so.
- The documented retrospective creation route is `/api/teams/{teamId}/retrospectives`. The bugs list reports attempts against `/api/teams/{id}/retros`, which returned 404. Exercise the documented route exactly; do not silently substitute the reported alternate path.
- For invalid-input scenarios, a safe response is a 4xx with a useful JSON error where the API returns JSON, and no partial resource/state change. Do not require a particular error-message string unless the API contract defines one.
- A `404 Not Found` is the appropriate result for a missing referenced resource. The bugs list reports `400 Bad Request` for some unknown resources; record that as a discrepancy rather than treating it as the desired result. For a state conflict, `409 Conflict` is clearest; another documented/consistent 4xx is acceptable if the resource remains unchanged.
- Each checklist item is one scenario; where a scenario has numbered requests, execute them in order and retain all response IDs.

## 1. Boundary and missing-input scenarios

### Teams

- [ ] **TEAM-BND-01 — Empty members list**
  - **Category:** Boundary / required collection
  - **Purpose:** Reject a team with no members.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":"RF-{{initials}} empty-members","members":[]}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); response indicates validation failure; no team is created.
  - **Validates:** Every team must have at least one member. The current bugs list reports this request was accepted with `201`; confirm and record if reproduced.

- [ ] **TEAM-BND-02 — Missing members field**
  - **Category:** Boundary / missing input
  - **Purpose:** Verify members is required rather than silently defaulted.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":"RF-{{initials}} missing-members"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no team is created.
  - **Validates:** Team creation requires a non-empty members list.

- [ ] **TEAM-BND-03 — Missing team name**
  - **Category:** Boundary / missing input
  - **Purpose:** Reject a team with no name property.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"members":["RF-{{initials}} Member"]}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no team is created.
  - **Validates:** A team name is required.

- [ ] **TEAM-BND-04 — Empty team name**
  - **Category:** Boundary / empty input
  - **Purpose:** Check that an empty string is not treated as a valid team name.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":"","members":["RF-{{initials}} Member"]}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no team is created.
  - **Validates:** Team names must contain a value. The bugs list marks blank-name validation as passing; recheck it.

- [ ] **TEAM-BND-05 — Very long team name**
  - **Category:** Boundary / length
  - **Purpose:** Check length limits and ensure oversized values fail cleanly rather than causing a server error or truncation.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":"RF-{{initials}}-<paste a 10000-character name>","members":["RF-{{initials}} Member"]}`.
  - **Expected response:** Prefer `400 Bad Request` (or another clear 4xx) if over the supported limit; alternatively, if accepted, the returned and retrieved name must be intact and not silently corrupted. Never return 5xx.
  - **Validates:** Boundary handling and persistence safety for team names; the maximum length is not specified in the API reference.

- [ ] **TEAM-BND-06 — Duplicate member names**
  - **Category:** Boundary / duplicate values
  - **Purpose:** Observe whether duplicate member names are allowed or normalized, without losing the team.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":"RF-{{initials}} duplicate-members","members":["RF-{{initials}} Member","RF-{{initials}} Member"]}`.
  - **Expected response:** If duplicates are rejected, return a 4xx and create nothing; if accepted, return a valid team and persist a consistent list. No 5xx or unrelated member changes.
  - **Validates:** Duplicate member handling; the brief does not define uniqueness for member names.

- [ ] **TEAM-BND-07 — Null name**
  - **Category:** Boundary / null value
  - **Purpose:** Ensure JSON `null` is rejected as a required name.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":null,"members":["RF-{{initials}} Member"]}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no team is created. The bugs list reports null-name validation passed; recheck it.
  - **Validates:** Required team-name null validation.

- [ ] **TEAM-BND-08 — Null members**
  - **Category:** Boundary / null collection
  - **Purpose:** Ensure JSON `null` is rejected for members.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":"RF-{{initials}} null-members","members":null}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no team is created. The current bugs list reports this was accepted with `201`; confirm and record if reproduced.
  - **Validates:** Teams require a non-null, non-empty member collection.

### Retrospectives

- [ ] **RETRO-BND-01 — Missing title**
  - **Category:** Boundary / missing input
  - **Purpose:** Reject a retrospective without a title.
  - **Preconditions:** Create a valid team and save `{{teamAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no retrospective appears in the team's list.
  - **Validates:** A retrospective title is required.

- [ ] **RETRO-BND-02 — Empty title**
  - **Category:** Boundary / empty input
  - **Purpose:** Reject a retrospective with an empty title.
  - **Preconditions:** Create a valid team and save `{{teamAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"title":""}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no retrospective is created.
  - **Validates:** Retrospective titles must contain a value.

- [ ] **RETRO-BND-03 — Very long title**
  - **Category:** Boundary / length
  - **Purpose:** Check a title at an extreme length.
  - **Preconditions:** Create a valid team and save `{{teamAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"title":"RF-{{initials}}-<paste a 10000-character title>"}`.
  - **Expected response:** Prefer a clear 4xx if the limit is exceeded; if accepted, the title must be intact in the response and subsequent GET. Never return 5xx or silently corrupt it.
  - **Validates:** Length-boundary handling; no maximum is specified.

- [ ] **RETRO-BND-04 — Create multiple retrospectives for one team**
  - **Category:** Boundary / business constraint
  - **Purpose:** Confirm multiple retrospectives are allowed over time but not concurrently OPEN.
  - **Preconditions:** Create a team and save `{{teamAId}}`; ensure it has no OPEN retrospective.
  - **Exact HTTP request:** Send `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives` twice in sequence with headers `Accept: application/json`, `Content-Type: application/json`; bodies `{"title":"RF-{{initials}} multi-retro-1"}` then `{"title":"RF-{{initials}} multi-retro-2"}`. Close the first with `PUT {{baseUrl}}/api/retrospectives/{{retroAId}}/close` (headers `Accept: application/json`; no body), then retry the second POST.
  - **Expected response:** First POST succeeds and creates an OPEN retrospective. Second POST while it is OPEN is rejected with a conflict/validation 4xx and creates nothing. After closing the first, the POST succeeds with a distinct ID. `GET /api/teams/{{teamAId}}/retrospectives` shows both records and only one OPEN.
  - **Validates:** Multiple retrospectives are permitted historically; only one per team may be OPEN at a time.

- [ ] **RETRO-BND-05 — Create retrospective for a non-existing team**
  - **Category:** Boundary / missing parent
  - **Purpose:** Reject creation under an unknown team ID.
  - **Preconditions:** Choose a positive numeric team ID not returned by the API, e.g. `{{missingTeamId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams/{{missingTeamId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"title":"RF-{{initials}} orphan-retro"}`.
  - **Expected response:** `404 Not Found` (preferred) or another consistent 4xx; no retrospective is created. Never return 5xx.
  - **Validates:** Parent existence is checked before creating a retrospective.

### Feedback items

- [ ] **FB-BND-01 — Missing content**
  - **Category:** Boundary / missing input
  - **Purpose:** Reject feedback without content.
  - **Preconditions:** Create a team and an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"type":"WENT_WELL","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no item appears in the retrospective's feedback list.
  - **Validates:** Feedback content is required.

- [ ] **FB-BND-02 — Empty content**
  - **Category:** Boundary / empty input
  - **Purpose:** Reject an empty feedback statement.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"","type":"WENT_WELL","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no item is created.
  - **Validates:** Feedback content must not be empty.

- [ ] **FB-BND-03 — Missing submittedBy**
  - **Category:** Boundary / missing input
  - **Purpose:** Reject feedback without the submitting member name.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} feedback","type":"WENT_WELL"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no item is created.
  - **Validates:** Feedback captures its submitter as required by the entity definition.

- [ ] **FB-BND-04 — Invalid feedback type**
  - **Category:** Boundary / enum
  - **Purpose:** Reject a value outside the allowed feedback types.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} feedback","type":"PRAISE","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no item is created.
  - **Validates:** Type is restricted to `WENT_WELL`, `NEEDS_IMPROVEMENT`, or `ACTION_ITEM`.

- [ ] **FB-BND-05 — Very long content**
  - **Category:** Boundary / length
  - **Purpose:** Check oversized feedback content.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"<paste a 10000-character feedback string>","type":"WENT_WELL","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** Clear 4xx if over the supported limit; if accepted, content remains intact in response and GET. Never return 5xx or silently corrupt content.
  - **Validates:** Feedback length handling; maximum length is unspecified.

- [ ] **FB-BND-06 — Add feedback to non-existing retrospective**
  - **Category:** Boundary / missing parent
  - **Purpose:** Reject an item whose parent retrospective does not exist.
  - **Preconditions:** Choose a positive numeric retrospective ID not returned by the API.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{missingRetroId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} orphan feedback","type":"WENT_WELL","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `404 Not Found` (preferred) or another consistent 4xx; no feedback item is created.
  - **Validates:** Feedback creation requires an existing parent retrospective.

- [ ] **FB-BND-07 — List feedback for non-existing retrospective**
  - **Category:** Boundary / missing parent
  - **Purpose:** Ensure a missing parent is not mistaken for an empty feedback list.
  - **Preconditions:** Choose a positive numeric retrospective ID not returned by the API.
  - **Exact HTTP request:** `GET {{baseUrl}}/api/retrospectives/{{missingRetroId}}/feedback-items`; headers `Accept: application/json`; no body.
  - **Expected response:** `404 Not Found` (preferred) or another consistent 4xx; do not return a success-shaped empty list for a nonexistent retrospective.
  - **Validates:** Parent existence and resource-not-found behavior.

### Action items

- [ ] **ACTION-BND-01 — Missing priority**
  - **Category:** Boundary / missing input
  - **Purpose:** Reject an action item with no priority.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/action-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} action","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no action item is created.
  - **Validates:** Action items require one of the defined priorities.

- [ ] **ACTION-BND-02 — Invalid priority**
  - **Category:** Boundary / enum
  - **Purpose:** Reject priority outside the allowed set.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/action-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} action","priority":"URGENT","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no item is created.
  - **Validates:** Priority must be `LOW`, `MEDIUM`, or `HIGH`.

- [ ] **ACTION-BND-03 — Missing submittedBy**
  - **Category:** Boundary / missing input
  - **Purpose:** Reject an action item without a submitter.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/action-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} action","priority":"MEDIUM"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no item is created.
  - **Validates:** Action items retain submitter information.

- [ ] **ACTION-BND-04 — Empty content**
  - **Category:** Boundary / empty input
  - **Purpose:** Reject an action item with no actionable content.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/action-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"","priority":"MEDIUM","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no item is created.
  - **Validates:** Action content is required and non-empty.

- [ ] **ACTION-BND-05 — Very long content**
  - **Category:** Boundary / length
  - **Purpose:** Check oversized action-item content.
  - **Preconditions:** Create an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/action-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"<paste a 10000-character action description>","priority":"HIGH","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** Clear 4xx if over the supported limit; if accepted, content remains intact in response and retrieval. Never return 5xx or silently corrupt it.
  - **Validates:** Action content length handling; maximum length is unspecified.

- [ ] **ACTION-BND-06 — Invalid completion payloads**
  - **Category:** Boundary / completion validation
  - **Purpose:** Reject missing, null, and non-boolean completed values.
  - **Preconditions:** Create a valid action item and save `{{actionAId}}`; record its initial completion state.
  - **Exact HTTP request:** Send each request to `PATCH {{baseUrl}}/api/action-items/{{actionAId}}/complete`, headers `Accept: application/json`, `Content-Type: application/json`, with bodies `{}`, `{"completed":null}`, `{"completed":"true"}`, and `{"completed":1}`.
  - **Expected response:** Each request returns `400 Bad Request` (or consistent validation 4xx); the item remains in its initial state.
  - **Validates:** Completion requires a boolean value and malformed updates do not alter state.

- [ ] **ACTION-BND-07 — Create action item under non-existing retrospective**
  - **Category:** Boundary / missing parent
  - **Purpose:** Reject a child item with a nonexistent parent.
  - **Preconditions:** Choose a positive numeric retrospective ID not returned by the API.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{missingRetroId}}/action-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} orphan action","priority":"LOW","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `404 Not Found` (preferred) or another consistent 4xx; no action item is created.
  - **Validates:** Action-item creation requires an existing retrospective.

## 2. Malformed and adversarial requests

- [ ] **API-ADV-01 — Malformed JSON**
  - **Category:** Malformed request
  - **Purpose:** Ensure invalid JSON is rejected without creating a partial team.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; raw body `{"name":"RF-{{initials}} malformed","members":["A",]}`.
  - **Expected response:** `400 Bad Request` (or parser 4xx); no team is created; no stack trace, SQL details, or internal exception is exposed.
  - **Validates:** JSON parsing and safe error handling.

- [ ] **API-ADV-02 — Wrong Content-Type**
  - **Category:** Malformed request
  - **Purpose:** Ensure the server does not process a JSON body mislabeled as plain text.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: text/plain`; body `{"name":"RF-{{initials}} wrong-type","members":["RF-{{initials}} Member"]}`.
  - **Expected response:** `415 Unsupported Media Type` (preferred) or another clear 4xx; no team is created and no 5xx is returned.
  - **Validates:** Request media-type enforcement.

- [ ] **API-ADV-03 — Unexpected extra fields**
  - **Category:** Malformed / unexpected input
  - **Purpose:** Verify unknown properties cannot unexpectedly change team data.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":"RF-{{initials}} extra-field","members":["RF-{{initials}} Member"],"status":"ADMIN","teamId":999999}`.
  - **Expected response:** Either reject with 4xx or ignore unknown fields while creating only the requested team fields; response/retrieval must not reflect an altered ID or injected status. No 5xx.
  - **Validates:** Input binding is constrained to supported fields.

- [ ] **API-ADV-04 — Wrong enum values and casing**
  - **Category:** Malformed / enum
  - **Purpose:** Verify enum parsing is strict and documented enum values are honored.
  - **Preconditions:** Create an OPEN retrospective and save `{{retroAId}}`.
  - **Exact HTTP request:** Send two POST requests to `{{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items`, headers `Accept: application/json`, `Content-Type: application/json`; bodies `{"content":"RF-{{initials}} enum","type":"UNKNOWN","submittedBy":"RF-{{initials}} Member"}` and `{"content":"RF-{{initials}} enum-lower","type":"went_well","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** Each invalid enum is rejected with 4xx, creates no feedback, and produces no 5xx.
  - **Validates:** Only the exact documented feedback enum values are accepted.

- [ ] **API-ADV-05 — Incorrect JSON data types**
  - **Category:** Malformed / type validation
  - **Purpose:** Ensure fields are not coerced into unrelated types.
  - **Preconditions:** Create an OPEN retrospective and save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/action-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":42,"priority":true,"submittedBy":["RF-{{initials}} Member"]}`.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no action item is created; no 5xx or partial coercion.
  - **Validates:** Typed request fields are validated.

- [ ] **API-ADV-06 — Missing request body**
  - **Category:** Malformed request
  - **Purpose:** Reject bodyless create requests cleanly.
  - **Preconditions:** None.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; no body.
  - **Expected response:** `400 Bad Request` (or consistent validation 4xx); no team is created; no 5xx.
  - **Validates:** Create endpoint requires a request representation.

- [ ] **API-ADV-07 — Non-numeric ID in path**
  - **Category:** Malformed path
  - **Purpose:** Ensure invalid path identifiers are rejected safely.
  - **Preconditions:** None.
  - **Exact HTTP request:** `GET {{baseUrl}}/api/teams/not-a-number`; headers `Accept: application/json`; no body.
  - **Expected response:** `400 Bad Request` or `404 Not Found`; never `500`; error response must not expose a stack trace.
  - **Validates:** Path parameter parsing and safe error handling.

- [ ] **API-ADV-08 — Negative ID**
  - **Category:** Malformed path / boundary
  - **Purpose:** Verify negative identifiers do not resolve to an unrelated record.
  - **Preconditions:** None.
  - **Exact HTTP request:** `GET {{baseUrl}}/api/teams/-1`; headers `Accept: application/json`; no body.
  - **Expected response:** `400 Bad Request` or `404 Not Found`; no team data is returned and no 5xx occurs.
  - **Validates:** IDs are positive resource identifiers.

- [ ] **API-ADV-09 — Extremely large ID**
  - **Category:** Malformed path / boundary
  - **Purpose:** Check overflow handling in numeric ID conversion.
  - **Preconditions:** None.
  - **Exact HTTP request:** `GET {{baseUrl}}/api/teams/999999999999999999999999999999999999`; headers `Accept: application/json`; no body.
  - **Expected response:** `400 Bad Request` or `404 Not Found`; never `500`, and no incorrect resource is returned.
  - **Validates:** Numeric overflow is handled as an invalid/nonexistent identifier.

- [ ] **API-ADV-10 — Unknown team retrieval**
  - **Category:** Malformed / missing resource
  - **Purpose:** Check not-found behavior for a well-formed but unknown ID.
  - **Preconditions:** Choose a positive numeric team ID not returned by the API.
  - **Exact HTTP request:** `GET {{baseUrl}}/api/teams/{{missingTeamId}}`; headers `Accept: application/json`; no body.
  - **Expected response:** `404 Not Found`; no team body is returned. The current bugs list reports `400`; record that as a defect against the expected not-found behavior.
  - **Validates:** Correct distinction between malformed ID and nonexistent resource.

## 3. Cross-entity consistency checks

- [ ] **REL-01 — Retrospective is listed only under its owning team**
  - **Category:** Cross-entity consistency
  - **Purpose:** Detect retrospective leakage across teams.
  - **Preconditions:** Create teams `{{teamAId}}` and `{{teamBId}}`; create an OPEN retrospective under team A and save `{{retroAId}}`.
  - **Exact HTTP request:** `GET {{baseUrl}}/api/teams/{{teamAId}}/retrospectives` and `GET {{baseUrl}}/api/teams/{{teamBId}}/retrospectives`; headers `Accept: application/json`; no body.
  - **Expected response:** Both requests succeed; the first contains `{{retroAId}}`, the second does not. No retrospective belonging to one team appears in another team's results.
  - **Validates:** Retrospective-to-team ownership and list scoping.

- [ ] **REL-02 — OPEN-retrospective limit is scoped to the owning team**
  - **Category:** Cross-entity consistency
  - **Purpose:** Ensure one team's OPEN retrospective does not block or alter another team's lifecycle.
  - **Preconditions:** Create teams A and B; ensure neither has an OPEN retrospective.
  - **Exact HTTP request:** Create one retrospective under each using `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives` with `{"title":"RF-{{initials}} team-A-open"}`, then under team B with `{"title":"RF-{{initials}} team-B-open"}`; both use headers `Accept: application/json`, `Content-Type: application/json`.
  - **Expected response:** Both create requests succeed with distinct IDs and OPEN status; neither request changes the other team's status or list.
  - **Validates:** The single-OPEN rule is per team, not global.

- [ ] **REL-03 — Feedback is listed only under its owning retrospective**
  - **Category:** Cross-entity consistency
  - **Purpose:** Detect feedback leakage between retrospectives.
  - **Preconditions:** Create two teams and one OPEN retrospective per team; add a uniquely worded `WENT_WELL` feedback item to the first retrospective, saving its item ID.
  - **Exact HTTP request:** `GET {{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items` and `GET {{baseUrl}}/api/retrospectives/{{retroBId}}/feedback-items`; headers `Accept: application/json`; no body.
  - **Expected response:** The first list contains the new item; the second does not. Existing feedback in either retrospective remains unchanged.
  - **Validates:** Feedback-to-retrospective association and list scoping.

- [ ] **REL-04 — Action items are listed only under their owning retrospective**
  - **Category:** Cross-entity consistency
  - **Purpose:** Detect action-item association leakage.
  - **Preconditions:** Create two OPEN retrospectives belonging to different teams; create one uniquely worded action item under the first and save its ID.
  - **Exact HTTP request:** `GET {{baseUrl}}/api/action-items`; headers `Accept: application/json`; no body.
  - **Expected response:** The result includes the action item once with its owning retrospective association (if returned by the API); it must not be duplicated or associated with the second retrospective.
  - **Validates:** Action-item persistence and parent association. The API reference provides no per-retrospective action-item list endpoint.

- [ ] **REL-05 — Use a valid retrospective ID where a team ID is expected**
  - **Category:** Cross-entity consistency / wrong parent type
  - **Purpose:** Ensure an existing ID of the wrong entity type cannot create a child under an unrelated entity.
  - **Preconditions:** Create a team and retrospective; save IDs. Choose a retrospective ID known not to equal an existing team ID (create fixtures until distinct).
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams/{{retroAId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"title":"RF-{{initials}} wrong-parent-type"}`.
  - **Expected response:** `404 Not Found` or another safe 4xx; no retrospective is created under any team. A valid ID for a different entity must not be treated as a team.
  - **Validates:** Parent resource type is checked, not merely numeric ID existence.

- [ ] **REL-06 — Use a valid feedback-item ID where a retrospective ID is expected**
  - **Category:** Cross-entity consistency / wrong parent type
  - **Purpose:** Ensure an existing feedback ID cannot be mistaken for a retrospective.
  - **Preconditions:** Create a team, OPEN retrospective, and feedback item; ensure feedback ID differs from the retrospective ID.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{feedbackAId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} wrong-parent","type":"WENT_WELL","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** `404 Not Found` or another safe 4xx; no item is added to the feedback item's record or another retrospective.
  - **Validates:** The path ID must identify a retrospective of the correct resource type.

- [ ] **REL-07 — Feedback update uses only the feedback item's own ID**
  - **Category:** Cross-entity consistency / child ID isolation
  - **Purpose:** Verify updating one feedback item cannot alter another retrospective's feedback.
  - **Preconditions:** Create two OPEN retrospectives; add distinct feedback to each and save both IDs and original content.
  - **Exact HTTP request:** `PUT {{baseUrl}}/api/feedback-items/{{feedbackAId}}`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} updated-A-only"}`. Then GET each retrospective's feedback list.
  - **Expected response:** Update succeeds for item A only; item B's ID, content, parent, and list membership remain unchanged.
  - **Validates:** Child-ID update isolation and correct feedback ownership.

- [ ] **REL-08 — Action completion uses only the action item's own ID**
  - **Category:** Cross-entity consistency / child ID isolation
  - **Purpose:** Ensure completing one action item does not affect a different retrospective's item.
  - **Preconditions:** Create two OPEN retrospectives and one action item under each; ensure item IDs differ and both are initially incomplete.
  - **Exact HTTP request:** `PATCH {{baseUrl}}/api/action-items/{{actionAId}}/complete`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"completed":true}`. Then `GET {{baseUrl}}/api/action-items?completed=false` and `GET {{baseUrl}}/api/action-items?completed=true`.
  - **Expected response:** Only action A becomes completed; action B remains incomplete, and filters place each item in the correct result.
  - **Validates:** Action state updates and filter results are scoped to the specified action ID.

- [ ] **REL-09 — Nonexistent parent cannot be used for child creation**
  - **Category:** Cross-entity consistency / missing reference
  - **Purpose:** Check both child types reject dangling parent references.
  - **Preconditions:** Choose a positive nonexistent retrospective ID.
  - **Exact HTTP request:** Send `POST {{baseUrl}}/api/retrospectives/{{missingRetroId}}/feedback-items` with `{"content":"RF-{{initials}} orphan","type":"WENT_WELL","submittedBy":"RF-{{initials}} Member"}`, then `POST {{baseUrl}}/api/retrospectives/{{missingRetroId}}/action-items` with `{"content":"RF-{{initials}} orphan","priority":"LOW","submittedBy":"RF-{{initials}} Member"}`. Both use `Accept: application/json` and `Content-Type: application/json`.
  - **Expected response:** Both requests return `404 Not Found` (preferred) or consistent 4xx; neither item is created and no 5xx is returned.
  - **Validates:** Referential integrity for feedback and action items.

## 4. Repeatability and idempotency checks

- [ ] **REPEAT-01 — Create the same team twice**
  - **Category:** Repeatability / create
  - **Purpose:** Determine whether identical team-create requests are treated as distinct creations.
  - **Preconditions:** None.
  - **Exact HTTP request:** Send twice: `POST {{baseUrl}}/api/teams`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"name":"RF-{{initials}} repeat-team","members":["RF-{{initials}} Member"]}`.
  - **Expected response:** With no team-name uniqueness or idempotency rule documented, both requests should succeed as separate creations with distinct IDs; the second should create a duplicate-valued team, not silently return the first ID. If the API enforces uniqueness, it should explicitly reject the second and leave the first unchanged.
  - **Validates:** Create semantics, duplicate handling, and absence of accidental overwrite.

- [ ] **REPEAT-02 — Create the same retrospective twice while OPEN**
  - **Category:** Repeatability / create / business constraint
  - **Purpose:** Confirm a retry cannot create a second OPEN retrospective for one team.
  - **Preconditions:** Create a team with no OPEN retrospective.
  - **Exact HTTP request:** Send twice in sequence: `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"title":"RF-{{initials}} repeat-retro"}`.
  - **Expected response:** First request succeeds. The second must be rejected with conflict/validation 4xx because the first remains OPEN; it must not return the same result as a false success or create a duplicate OPEN retrospective.
  - **Validates:** One OPEN retrospective per team under repeated submission.

- [ ] **REPEAT-03 — Create another retrospective after closing the first**
  - **Category:** Repeatability / lifecycle
  - **Purpose:** Confirm a closed retrospective does not prevent a later retrospective.
  - **Preconditions:** Create a team and an OPEN retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `PUT {{baseUrl}}/api/retrospectives/{{retroAId}}/close` (headers `Accept: application/json`, no body), then twice send `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives` with headers `Accept: application/json`, `Content-Type: application/json` and body `{"title":"RF-{{initials}} after-close"}`.
  - **Expected response:** First subsequent create succeeds. Its repeated create is rejected while that new retrospective is OPEN. Team history retains the closed first retrospective and one new OPEN retrospective.
  - **Validates:** Repeated creates honor the one-OPEN-at-a-time rule while allowing retrospective history.

- [ ] **REPEAT-04 — Close a retrospective twice**
  - **Category:** Repeatability / lifecycle
  - **Purpose:** Check repeat closure is safe and does not change the final state.
  - **Preconditions:** Create an OPEN retrospective and save `{{retroAId}}`.
  - **Exact HTTP request:** Send twice: `PUT {{baseUrl}}/api/retrospectives/{{retroAId}}/close`; headers `Accept: application/json`; no body.
  - **Expected response:** The second request should succeed again as an idempotent close or return a clear conflict/validation 4xx; either way, the retrospective remains `CLOSED`, its ID and data remain unchanged, and it is never reopened or duplicated.
  - **Validates:** Safe repeat-close behavior and immutable closed state.

- [ ] **REPEAT-05 — Complete an action item twice**
  - **Category:** Repeatability / action state
  - **Purpose:** Check repeating the same completion command is idempotent.
  - **Preconditions:** Create a valid action item; save `{{actionAId}}`, initially incomplete.
  - **Exact HTTP request:** Send twice: `PATCH {{baseUrl}}/api/action-items/{{actionAId}}/complete`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"completed":true}`.
  - **Expected response:** Both requests may succeed and return the same completed state (or the second may be a clear no-op response); the item remains completed and is not duplicated.
  - **Validates:** Repeating completion does not create extra records or reverse the state.

- [ ] **REPEAT-06 — Attempt to un-complete a completed action item**
  - **Category:** Repeatability / protected state
  - **Purpose:** Verify completed actions cannot be reopened by a repeated update.
  - **Preconditions:** Create an action item, mark it complete, and confirm completed state.
  - **Exact HTTP request:** `PATCH {{baseUrl}}/api/action-items/{{actionAId}}/complete`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"completed":false}`.
  - **Expected response:** Reject with conflict/validation 4xx; item remains completed. The bugs list reports this currently returns `200` and succeeds; reproduce and capture the resulting state as a defect if still present.
  - **Validates:** Completed action items cannot be uncompleted.

- [ ] **REPEAT-07 — Add identical feedback twice**
  - **Category:** Repeatability / create
  - **Purpose:** Determine whether identical feedback submissions are stored as distinct records.
  - **Preconditions:** Create an OPEN retrospective and save `{{retroAId}}`.
  - **Exact HTTP request:** Send twice: `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} repeated feedback","type":"WENT_WELL","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** With no feedback uniqueness or idempotency rule documented, both should succeed as separate items with distinct IDs; the second should create a duplicate-valued feedback item rather than overwrite or return the first ID.
  - **Validates:** Feedback create behavior and duplicate-item persistence.

- [ ] **REPEAT-08 — Repeat feedback update**
  - **Category:** Repeatability / update
  - **Purpose:** Ensure repeating an identical edit does not produce additional records or unintended changes.
  - **Preconditions:** Create an OPEN retrospective and feedback item; save `{{feedbackAId}}`.
  - **Exact HTTP request:** Send twice: `PUT {{baseUrl}}/api/feedback-items/{{feedbackAId}}`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} repeated edit"}`.
  - **Expected response:** Both requests succeed or the second is a successful no-op; the same item ID has the requested content and remains a single item in the list.
  - **Validates:** Idempotent update and stable item identity.

## 5. Core business-rule attack scenarios

### A team can have only one OPEN retrospective at a time

- [ ] **RULE-OPEN-01 — Create a second OPEN retrospective for the same team**
  - **Category:** Core business-rule attack
  - **Purpose:** Attempt the primary one-OPEN rule violation through ordinary creation.
  - **Preconditions:** Create team A and one OPEN retrospective; save `{{teamAId}}` and `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"title":"RF-{{initials}} forbidden-second-open"}`. Then GET `{{baseUrl}}/api/teams/{{teamAId}}/retrospectives`.
  - **Expected response:** POST is rejected with conflict/validation 4xx; list still contains exactly one OPEN retrospective and no resource from the rejected attempt.
  - **Validates:** At most one OPEN retrospective per team.

- [ ] **RULE-OPEN-02 — Repeat the conflicting create with a different title**
  - **Category:** Core business-rule attack
  - **Purpose:** Ensure the rule is based on status/team ownership, not duplicate-title detection.
  - **Preconditions:** Team A already has one OPEN retrospective.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams/{{teamAId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"title":"RF-{{initials}} different-title-still-conflicts"}`. Then GET the team's retrospective list.
  - **Expected response:** Reject with conflict/validation 4xx; existing OPEN retrospective remains unchanged and is the only OPEN record.
  - **Validates:** The one-OPEN rule cannot be bypassed by changing the title.

- [ ] **RULE-OPEN-03 — Ensure a different team can create its own OPEN retrospective**
  - **Category:** Core business-rule attack / isolation
  - **Purpose:** Check the one-OPEN limit does not block unrelated teams.
  - **Preconditions:** Team A has an OPEN retrospective; team B has none.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/teams/{{teamBId}}/retrospectives`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"title":"RF-{{initials}} team-B-open"}`.
  - **Expected response:** Create succeeds; team A's OPEN record and team B's new OPEN record remain separate.
  - **Validates:** The OPEN limit is scoped to a team.

### A CLOSED retrospective cannot be reopened

- [ ] **RULE-CLOSED-01 — Attempt to close again and transition back**
  - **Category:** Core business-rule attack
  - **Purpose:** Verify no supported close operation can reopen a retrospective.
  - **Preconditions:** Create and close a retrospective; save `{{retroAId}}`; confirm it is `CLOSED`.
  - **Exact HTTP request:** Send `PUT {{baseUrl}}/api/retrospectives/{{retroAId}}/close` with headers `Accept: application/json` and no body. Then `GET {{baseUrl}}/api/retrospectives/{{retroAId}}` with `Accept: application/json`.
  - **Expected response:** Repeat close may succeed as a no-op or return a 4xx; GET must still show `CLOSED`. There is no documented reopen endpoint; the API must not transition it to OPEN.
  - **Validates:** Closed status is terminal.

- [ ] **RULE-CLOSED-02 — Attempt guessed reopen route**
  - **Category:** Core business-rule attack / unsupported operation
  - **Purpose:** Check that a guessed state-changing route cannot reopen a closed record.
  - **Preconditions:** Create and close a retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `PUT {{baseUrl}}/api/retrospectives/{{retroAId}}/reopen`; headers `Accept: application/json`; no body.
  - **Expected response:** `404 Not Found` or `405 Method Not Allowed`; no success response and subsequent GET remains `CLOSED`.
  - **Validates:** No undocumented reopen operation bypasses the terminal-state rule.

### Feedback in a CLOSED retrospective cannot be added, modified, or deleted

- [ ] **RULE-FEEDBACK-01 — Add feedback after closure**
  - **Category:** Core business-rule attack
  - **Purpose:** Attempt to append feedback to finalized history.
  - **Preconditions:** Create and close a retrospective; save `{{retroAId}}`.
  - **Exact HTTP request:** `POST {{baseUrl}}/api/retrospectives/{{retroAId}}/feedback-items`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} must-not-add","type":"WENT_WELL","submittedBy":"RF-{{initials}} Member"}`.
  - **Expected response:** Reject with conflict/validation 4xx; GET feedback list confirms no new item. No 5xx.
  - **Validates:** Closed retrospectives are immutable for feedback creation.

- [ ] **RULE-FEEDBACK-02 — Modify feedback after closure**
  - **Category:** Core business-rule attack
  - **Purpose:** Attempt to change an existing item in finalized history.
  - **Preconditions:** Create an OPEN retrospective, add feedback and save `{{feedbackAId}}`, then close the retrospective; record original content.
  - **Exact HTTP request:** `PUT {{baseUrl}}/api/feedback-items/{{feedbackAId}}`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"content":"RF-{{initials}} must-not-update"}`. Then GET feedback list.
  - **Expected response:** Reject with conflict/validation 4xx; original item remains present with original content.
  - **Validates:** Feedback in a CLOSED retrospective cannot be modified.

- [ ] **RULE-FEEDBACK-03 — Delete feedback after closure**
  - **Category:** Core business-rule attack
  - **Purpose:** Attempt to remove finalized feedback.
  - **Preconditions:** Create an OPEN retrospective, add feedback and save `{{feedbackAId}}`, then close the retrospective.
  - **Exact HTTP request:** `DELETE {{baseUrl}}/api/feedback-items/{{feedbackAId}}`; headers `Accept: application/json`; no body. Then GET the retrospective's feedback list.
  - **Expected response:** Reject with conflict/validation 4xx; the item remains in the list. The bugs list reports deletion after closure succeeded; reproduce and record if still present.
  - **Validates:** Feedback in a CLOSED retrospective cannot be deleted.

### Completed action items cannot be uncompleted

- [ ] **RULE-ACTION-01 — Set completed false after completion**
  - **Category:** Core business-rule attack
  - **Purpose:** Attempt the known state-reversal defect.
  - **Preconditions:** Create an action item and save `{{actionAId}}`; mark it completed with `PATCH /api/action-items/{{actionAId}}/complete` body `{"completed":true}`; confirm it is completed.
  - **Exact HTTP request:** `PATCH {{baseUrl}}/api/action-items/{{actionAId}}/complete`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"completed":false}`. Then GET `{{baseUrl}}/api/action-items?completed=true`.
  - **Expected response:** Reject with conflict/validation 4xx; item remains completed and appears in the completed filter. The bugs list reports HTTP 200 and successful un-completion; capture if reproduced.
  - **Validates:** Completed action items cannot be uncompleted.

- [ ] **RULE-ACTION-02 — Repeat un-completion attempt**
  - **Category:** Core business-rule attack
  - **Purpose:** Check repeated attempts cannot eventually reverse a completed action.
  - **Preconditions:** Create and complete an action item; save `{{actionAId}}`.
  - **Exact HTTP request:** Send twice to `PATCH {{baseUrl}}/api/action-items/{{actionAId}}/complete`; headers `Accept: application/json`, `Content-Type: application/json`; body `{"completed":false}`. Then GET `/api/action-items?completed=true` and `/api/action-items?completed=false`.
  - **Expected response:** Both attempts are rejected; the item remains completed, occurs only in the completed results, and retains the same ID and content.
  - **Validates:** Terminal completion state is enforced consistently across retries.
