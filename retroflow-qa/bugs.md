# Bug reports

## 1. Action items can be added to a closed retrospective

**Severity:** Medium

**Steps to reproduce:**
1. Create a retrospective and close it.
2. Attempt to add an action item to that retrospective.

**Expected result:** The request is rejected and no action item is added, because the retrospective is closed.

**Actual result:** The session notes report that the action item was added.

**Business impact:** A closed retrospective can continue to be changed, so its recorded outcomes may not remain final.

## 2. Feedback can be deleted after its retrospective is closed

**Severity:** Medium

**Steps to reproduce:**
1. Create a retrospective and add a feedback item.
2. Close the retrospective.
3. Attempt to delete the feedback item.

**Expected result:** The deletion is rejected; feedback in a closed retrospective cannot be deleted.

**Actual result:** The session notes report that the deletion succeeded.

**Business impact:** Feedback can be removed from a finalized retrospective, reducing the reliability of its historical record.

## 3. A completed action item can be marked incomplete

**Severity:** High

**Steps to reproduce:**
1. Create an action item and mark it complete.
2. Send an update setting its completed state to false.

**Expected result:** The request is rejected and the action item remains completed.

**Actual result:** The session notes report that setting completed to false succeeded with HTTP 200.

**Business impact:** A completed commitment can be reopened, making completion records unreliable.
--------------
# RetroFlow Test Suite - Execution Results

## Executive Summary

| Metric | Count |
|----------|----------|
| Passed Scenarios | 9 |
| Failed Scenarios | 26 |
| Total Scenarios | 35 |
| Passed Assertions | 45 |
| Failed Assertions | 28 |
| Total Assertions | 73 |

---

# 1. Create Team

## ✅ Passed

| ID | Test Case |
|------|------|
| TC-TEAM-001 | Create team with 1 member |
| TC-TEAM-002 | Create team with multiple members |
| TC-TEAM-005 | Create team with blank name |
| TC-TEAM-006 | Create team with null name |
| TC-TEAM-007 | Retrieve team by ID |

## ❌ Failed

### TC-TEAM-003
**Scenario:** Create team with empty member list

**Expected**
- 400 Bad Request

**Actual**
- 201 Created

**Finding**
- API currently allows teams with no members.

---

### TC-TEAM-004
**Scenario:** Create team with null members

**Expected**
- 400 Bad Request

**Actual**
- 201 Created

**Finding**
- API currently allows null member collections.

---

### TC-TEAM-008
**Scenario:** Retrieve unknown team

**Expected**
- 404 Not Found

**Actual**
- 400 Bad Request

---

# 2. Create Retrospective

## ✅ Passed

| ID | Test Case |
|------|------|
| TC-RETRO-002 | Create retro for invalid team |

## ❌ Failed

| ID | Reason |
|------|------|
| TC-RETRO-001 | Endpoint not found |
| TC-RETRO-003 | Endpoint not found |
| TC-RETRO-004 | Endpoint not found |
| TC-RETRO-005 | Endpoint not found |
| TC-RETRO-006 | Endpoint not found |
| TC-RETRO-007 | Endpoint not found |

### Common Error

```
404 Not Found
/api/teams/{id}/retros
```

---

# 3. Close Retrospective

## ❌ Failed

| ID | Test Case | Reason |
|------|------|------|
| TC-RETRO-008 | Member closes retro | retroId not available |
| TC-RETRO-009 | Non-member closes retro | retroId not available |
| TC-RETRO-010 | Reopen closed retro | retroId not available |
| TC-RETRO-011 | Close already closed retro | retroId not available |

**Observation**

These failures are downstream consequences of retrospective creation failing.

---

# 4. Feedback on OPEN Retrospective

## ❌ Failed

| ID | Test Case |
|------|------|
| TC-FB-001 | Add WENT_WELL feedback |
| TC-FB-002 | Add NEEDS_IMPROVEMENT feedback |
| TC-FB-003 | Add ACTION_ITEM feedback |
| TC-FB-004 | Non-member submits feedback |
| TC-FB-005 | Empty feedback content |
| TC-FB-006 | Invalid feedback type |

### Common Cause

```
404 Not Found
retro2Id not set
```

No retrospective was successfully created.

---

# 5. Feedback on CLOSED Retrospective

## ❌ Failed

| ID | Test Case |
|------|------|
| TC-FB-007 | Add feedback to CLOSED retrospective |
| TC-FB-008 | Update feedback before closure |
| TC-FB-009 | Delete feedback before closure |
| TC-FB-010 | Update feedback after closure |
| TC-FB-011 | Delete feedback after closure |
| TC-FB-012 | Retrieve feedback list |

### Common Cause

```
404 Not Found
retroId not set
```

All feedback scenarios depend on a successfully created retrospective.

---

# 6. API Contract Validation

## ✅ Passed

| ID | Test Case |
|------|------|
| TC-API-001 | Verify 201 Created |
| TC-API-002 | Verify 200 OK |
| TC-API-003 | Verify 400 Bad Request |

## ❌ Failed

### TC-API-004

**Scenario**
- Verify unknown resource returns 404

**Expected**
- 404 Not Found

**Actual**
- 400 Bad Request

---

# Root Cause Analysis

## Primary Defect

The retrospective creation endpoint appears incorrect:

```
/api/teams/{id}/retros
```

The API responds with:

```
404 Not Found
```

Because this endpoint fails:

1. Retrospectives cannot be created.
2. No retroId is produced.
3. Close-retro tests cannot execute.
4. Feedback tests cannot execute.
5. Concurrency scenarios cannot execute.

This results in a large cascade of failures.

---

# Recommended Next Actions

## High Priority

### API Investigation
Confirm the correct retrospective endpoint:

Possible alternatives:

```
/api/retros
/api/retrospectives
/api/teams/{id}/retrospectives
```

### Team Validation Defects

Fix:

- Teams should not allow empty member lists.
- Teams should not allow null member lists.

### Error Handling Consistency

Review:

- Unknown Team → Should return 404
- Unknown Resource → Should return 404

Current implementation returns 400 in some cases.

---

# Overall Assessment

## Working Areas

✅ Team creation

✅ Team retrieval

✅ Basic REST status handling

✅ Request validation for names

## Blocking Issues

❌ Retrospective creation endpoint

❌ Retrospective lifecycle

❌ Feedback management

❌ Authorization verification

❌ Concurrency validation

**Conclusion:** RetroFlow currently has one major blocking defect in the retrospective API path that prevents validation of most business functionality. Resolving the retrospective endpoint issue should unlock approximately 20+ currently failing test scenarios and allow meaningful verification of the core application flows.