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