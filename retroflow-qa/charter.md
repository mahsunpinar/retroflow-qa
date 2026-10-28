# RetroFlow Exploratory Testing Charter

## Objective

Support a release recommendation by rapidly increasing confidence in the highest-risk business rules rather than maximizing endpoint coverage. The QA brief emphasizes determining whether RetroFlow can safely be used by 12 teams and whether critical business rules fail silently. 【1-8dcd37】【2-a38fa0】【3-b2ef5f】【4-1bc609】

---

# Session Plan (25 Minutes)

| Priority | Exploration Area | Time Box |
|----------|------------------|-----------|
| P1 | Happy-path workflow end-to-end | 6 min |
| P1 | Retrospective lifecycle rule enforcement | 5 min |
| P1 | Closed retrospective protection | 4 min |
| P1 | Action item lifecycle enforcement | 3 min |
| P1 | Concurrency and race conditions | 5 min |
| P2 | Validation and negative testing | 2 min |

---

# P1 - Happy-Path Workflow (Baseline Release Confidence)

## Why Start Here?

The brief states that the happy path works in a demo but has not been properly tested. Before exploring edge cases, establish that the primary business workflow functions end-to-end. A release recommendation has little value if the core workflow itself is broken. 【1-8dcd37】【3-b2ef5f】

| Flow | Why Selected | Business Rule Validated | Why It Matters for Release Confidence |
|--------|--------|--------|--------|
| Create team | Entry point for all subsequent activity | Team must have at least one member | No retrospectives can exist without teams |
| Create retrospective | Core business capability | Retrospective belongs to team | Central business purpose |
| Add feedback | Primary user activity | Feedback can be added to OPEN retrospective | Retrospectives are useless without feedback |
| Add action item | Core value proposition | Action item creation | Tracks improvement actions |
| Complete action item | Action lifecycle | Action items can be completed | Demonstrates follow-through tracking |
| Close retrospective | Lifecycle completion | Retrospective closure | Historical integrity depends on closure |

---

## Happy Path Exploration

| Step | Endpoint | Exploratory Idea | Expected Business Result |
|--------|--------|--------|--------|
| Create Team | POST /api/teams | Create team with valid name and multiple members | Team created successfully |
| Create Retro | POST /api/teams/{teamId}/retrospectives | Create retrospective for team | OPEN retrospective created |
| Add Feedback | POST /api/retrospectives/{id}/feedback-items | Add WENT_WELL feedback | Feedback stored |
| Add Action Item | POST /api/retrospectives/{id}/action-items | Add HIGH priority action item | Action item stored |
| Complete Action Item | PATCH /api/action-items/{id}/complete | Mark completed=true | Action item marked completed |
| Close Retro | PUT /api/retrospectives/{id}/close | Close retrospective | Status becomes CLOSED |

### Expected Outcome

The complete retrospective lifecycle executes without data loss or invalid state transitions. 【1-8dcd37】【2-a38fa0】【3-b2ef5f】【4-1bc609】

---

# P1 - Retrospective Lifecycle Rule Enforcement

| Field | Details |
|---------|---------|
| Priority | P1 |
| Objective | Verify retrospective state restrictions |
| Business Risk | Teams could maintain conflicting active retrospectives or alter history |
| Related Business Rules | Only one OPEN retrospective per team; CLOSED retrospectives cannot be reopened |
| Relevant API Endpoints | POST /api/teams/{teamId}/retrospectives, PUT /api/retrospectives/{id}/close |
| Happy Path | Create and close retrospective |
| Expected Business Result | State transitions allowed only in valid direction |

### Why This Area Deserves Testing

The brief identifies retrospective state management as a core business constraint. Silent failure would compromise historical records and team process integrity. 【1-8dcd37】【3-b2ef5f】

### Exploratory Test Ideas

| Test Idea | Expected Result |
|------------|------------|
| Create second OPEN retrospective for same team | Rejected |
| Create second retrospective after first is CLOSED | Allowed |
| Close already CLOSED retrospective | Consistent rejection or idempotent handling |
| Attempt reopening through available endpoints | Rejected |
| Create retrospective using non-existent team ID | Not found |

### Likely Defect Hypotheses

| Hypothesis | Justification |
|------------|------------|
| Rule enforced only during normal workflow and not server-side | Brief warns validation may be incomplete |
| State transition validation missing or inconsistent | Explicit business rule with no implementation details provided |

---

# P1 - Closed Retrospective Protection

| Field | Details |
|---------|---------|
| Priority | P1 |
| Objective | Verify immutability after retrospective closure |
| Business Risk | Historical retrospective records become unreliable |
| Related Business Rules | Feedback in CLOSED retrospectives cannot be modified or deleted |
| Relevant API Endpoints | POST feedback-items, PUT feedback-items/{id}, DELETE feedback-items/{id}, PUT retrospectives/{id}/close |
| Happy Path | Add feedback before closure |
| Expected Business Result | Historical feedback remains protected after closure |

### Why This Area Deserves Testing

The brief treats closure as a permanent state. If feedback remains editable after closure, RetroFlow loses credibility as a historical record. 【1-8dcd37】【3-b2ef5f】

### Exploratory Test Ideas

| Test Idea | Expected Result |
|------------|------------|
| Add feedback after closure | Rejected |
| Modify feedback after closure | Rejected |
| Delete feedback after closure | Rejected |
| Retrieve feedback after closure | Allowed |

### Likely Defect Hypotheses

| Hypothesis | Justification |
|------------|------------|
| One operation blocked while another remains possible | Modify and delete are separately called out in the brief |
| Closure checked for create but not update/delete | Validation is explicitly described as potentially incomplete |

---

# P1 - Action Item Lifecycle Enforcement

| Field | Details |
|---------|---------|
| Priority | P1 |
| Objective | Verify completion lifecycle restrictions |
| Business Risk | Teams lose confidence in action tracking |
| Related Business Rules | Completed action items cannot be uncompleted |
| Relevant API Endpoints | POST action-items, PATCH action-items/{id}/complete, GET action-items |
| Happy Path | Create and complete action item |
| Expected Business Result | Completion is irreversible |

### Why This Area Deserves Testing

The brief defines action-item tracking as a core business outcome of the product. 【1-8dcd37】【3-b2ef5f】

### Exploratory Test Ideas

| Test Idea | Expected Result |
|------------|------------|
| Complete action item | Success |
| Complete already completed item | Safe handling |
| Set completed=false after completion | Rejected |
| Filter completed=true | Item returned |
| Filter completed=false after completion | Item excluded |

### Likely Defect Hypotheses

| Hypothesis | Justification |
|------------|------------|
| API allows completed=false despite business rule | Endpoint accepts completion state updates |
| Filtering does not reflect actual lifecycle state | Filtering is explicitly required |

---

# P1 - Concurrency and Race Conditions

| Field | Details |
|---------|---------|
| Priority | P1 |
| Objective | Verify business rules hold under simultaneous user actions |
| Business Risk | Collaborative usage bypasses business rules |
| Related Business Rules | One OPEN retrospective, closure rules, feedback protection, action item completion |
| Relevant API Endpoints | Retrospective, feedback and action-item endpoints |
| Expected Business Result | Consistent state regardless of timing |

### Why This Area Deserves Testing

RetroFlow is a collaborative system. The exercise instructions explicitly direct testers to attempt race-condition scenarios around retrospective creation. 【5-db0121】【6-a61a45】

### Concurrency Scenarios

| Scenario | Why Risky | Potential Defect Revealed | Expected Result |
|-----------|-----------|-----------|-----------|
| Two users create OPEN retrospectives simultaneously | Business rule may be bypassed | Multiple OPEN retrospectives | Only one succeeds |
| One user closes retrospective while another adds feedback | State transition conflict | Feedback accepted after closure | One consistent outcome enforced |
| Two users close same retrospective simultaneously | Duplicate state updates | Inconsistent state or errors | Single CLOSED state |
| Two users update same feedback simultaneously | Lost update risk | Silent overwrite | Consistent final state |
| Two users complete same action item simultaneously | Completion race | Invalid lifecycle state | Item remains completed |

### Additional Concurrency Explorations

| Scenario | Expected Result |
|-----------|-----------|
| Create retrospective while another user closes current OPEN retrospective | Rule consistently enforced |
| Delete feedback while another updates it | Consistent handling |
| Complete action item while another user queries filters | Data remains consistent |

---

# P2 - Validation and Negative Testing

| Field | Details |
|---------|---------|
| Priority | P2 |
| Objective | Verify robustness of documented validation |
| Business Risk | Invalid data corrupts retrospectives |
| Related Business Rules | Team creation, feedback creation, action item creation |
| Relevant API Endpoints | Team, retrospective, feedback and action-item creation endpoints |
| Expected Business Result | Invalid requests rejected cleanly |

### Why This Area Deserves Testing

The brief explicitly states that validation exists on most endpoints but should not be trusted. 【1-8dcd37】【3-b2ef5f】

### Exploratory Test Ideas

| Input Area | Exploratory Idea | Expected Result |
|------------|------------|------------|
| Missing required fields | Omit name, content or submittedBy | Validation failure |
| Empty strings | Empty title, content or member name | Validation failure or documented handling |
| Null values | Null content, members or priority | Validation failure |
| Invalid enum values | INVALID_TYPE, CRITICAL priority | Validation failure |
| Extremely long text | Large title or feedback content | Graceful handling |
| Non-existent IDs | Unknown team, retrospective, feedback or action item IDs | Not found |
| Duplicate data | Duplicate team names, members or feedback | Behaviour investigated |
| Invalid completion payload | Non-boolean completed value | Validation failure |

---

# Questions the Documentation Does Not Answer

| Question | Why It Deserves Investigation Before Release |
|------------|------------|
| Must submittedBy be a team member? | Membership validation behaviour is undefined |
| Are duplicate team names allowed? | Could create operational confusion |
| Are duplicate member names allowed? | May affect attribution |
| Is retrospective title required? | Creation rules unclear |
| Is retrospective title unique? | Duplicate retrospective identification unclear |
| How is retrospective date assigned? | Brief mentions dates but API request does not |
| Can feedback content be empty after update? | Update validation unspecified |
| Can action items be modified after completion? | Only uncompletion rule is defined |
| What happens when completed=true is sent repeatedly? | Idempotency behaviour undefined |
| Which status codes represent business-rule violations? | API reference intentionally omits responses |
| What is the error response structure? | Client integration impact |
| Can feedback be retrieved after closure? | Not explicitly defined |
| Can action items be added after closure? | Closure restrictions not specified |
| Is closure irreversible at persistence level or endpoint level only? | Historical integrity concern |
| Can retrospectives be closed with open action items? | Lifecycle behaviour unclear |

---

# Recommended Execution Order

| Order | Activity |
|---------|---------|
| 1 | Execute complete happy-path workflow |
| 2 | Attempt second OPEN retrospective |
| 3 | Close retrospective |
| 4 | Attempt add, modify and delete feedback after closure |
| 5 | Verify completed action item cannot be uncompleted |
| 6 | Execute concurrency scenarios |
| 7 | Run targeted validation checks |
| 8 | Record documentation ambiguities impacting release confidence |

---

# Release Recommendation Focus

Any failure of the following business rules should be treated as a potential release blocker because they directly violate documented business requirements:

1. Only one OPEN retrospective per team.
2. Closed retrospectives cannot be reopened.
3. Feedback in CLOSED retrospectives cannot be modified.
4. Feedback in CLOSED retrospectives cannot be deleted.
5. Completed action items cannot be uncompleted.

These rules should receive the highest testing priority because their failure would undermine the core purpose of RetroFlow as a reliable system of record for team retrospectives.