# Test-plan.md
**Version: 1.2**  
**Status:** Draft  
**Project:** RetroFlow  
**Source Document:** retroflow-qa-brief.md  
**Owner:** QA Team

---

# 1. Change Log

## Version 1.2
Changes:
- Removed Automation Strategy section.
- Removed Existing Test Audit Strategy section.
- Added Business Rule Decomposition.
- Added Validation Risk Coverage.
- Added Data Integrity Risk Analysis.
- Added Defect Classification Model.
- Added Measurable Release Decision Matrix.
- Strengthened traceability to business requirements.

## Version 1.1
Changes:
- Added requirement traceability.
- Added source classification.
- Added requirement identifiers.
- Added test coverage matrix.
- Added version control process.

## Version 1.0
Changes:
- Initial Risk-Based Test Plan.

---

# 2. Objective

The objective of this QA effort is to determine whether RetroFlow is ready for its first release by validating the core business rules, identifying defects that threaten data integrity, and providing a justified release recommendation.

The primary focus is not CRUD validation but verification of the business rules that protect retrospective history and action tracking.

---

# 3. Scope

## In Scope

### Teams
- Team creation
- Team retrieval
- Team membership validation

### Retrospectives
- Retrospective creation
- Retrospective closure
- Retrospective status management

### Feedback Items
- Feedback creation
- Feedback updates
- Feedback deletion
- Feedback lifecycle rules

### Action Items
- Action item creation
- Action item completion
- Action item filtering

### General
- Business rule enforcement
- Validation rules
- Error handling
- API responses

---

## Out of Scope

- Front-end testing
- Performance testing beyond basic verification
- Security and penetration testing
- User Directory integration testing

---

# 4. Source Classification

## Derived from retroflow-qa-brief.md

- Domain model
- Teams
- Retrospectives
- Feedback Items
- Action Items
- Business rules
- Scope limitations

## QA Analysis Added by This Test Plan

- Risk ratings
- Test priorities
- Test levels
- Entry criteria
- Exit criteria
- Defect severity model
- Validation strategy
- Release recommendation criteria

---

# 5. Requirement Traceability

## REQ-001
Only one OPEN retrospective may exist for a team at any given time.

## REQ-002
A CLOSED retrospective cannot be reopened.

## REQ-003
Feedback items may only be modified while a retrospective is OPEN.
Feedback items belonging to CLOSED retrospectives cannot be modified or deleted.

## REQ-004
Completed action items cannot be marked incomplete again.

---

# 6. Business Rule Decomposition

## REQ-001
- REQ-001A Create first OPEN retrospective.
- REQ-001B Attempt to create second OPEN retrospective for same team. Expected: reject.

## REQ-002
- REQ-002A Close retrospective.
- REQ-002B Attempt to reopen retrospective. Expected: reject.

## REQ-003
- REQ-003A Modify feedback while OPEN. Expected: success.
- REQ-003B Modify feedback while CLOSED. Expected: reject.
- REQ-003C Delete feedback while CLOSED. Expected: reject.

## REQ-004
- REQ-004A Mark action item complete. Expected: success.
- REQ-004B Attempt to uncomplete action item. Expected: reject.

---

# 7. Test Levels

## API Testing
Primary level for validating business rules and API behavior.

## Integration Testing
Validates interactions between controllers, services, persistence layer and entity relationships.

## Exploratory Testing
Targets validation gaps, edge cases and unexpected workflows.

---

# 8. Entry Criteria

- Application builds successfully.
- Application starts successfully.
- API endpoints are accessible.
- Test environment is available.
- Test data can be created.
- Endpoint documentation is available.

---

# 9. Exit Criteria

- All critical business rules are tested.
- All critical and high defects are documented.
- Risk assessment is completed.
- No unresolved blocker defects remain.
- Release recommendation is approved by QA.

---

# 10. Risk Matrix

| Requirement | Likelihood | Impact | Score | Priority |
|---|---|---|---|---|
| REQ-001 | High | High | 9 | P1 |
| REQ-002 | Medium | High | 6 | P2 |
| REQ-003 | High | High | 9 | P1 |
| REQ-004 | Medium | Medium | 4 | P3 |

---

# 11. Testing Priority Order

1. REQ-003
2. REQ-001
3. REQ-002
4. REQ-004

---

# 12. Validation Risk Coverage

- Team with zero members
- Empty/null team name
- Missing retrospective title
- Invalid team reference
- Invalid status value
- Empty/null feedback content
- Invalid feedback type
- Invalid priority
- Missing priority
- Non-existing IDs
- Invalid payloads
- Malformed JSON

---

# 13. Data Integrity Risk Analysis

## Historical Integrity
Protected by REQ-002 and REQ-003.

## State Integrity
Protected by REQ-001.

## Progress Integrity
Protected by REQ-004.

---

# 14. Environment Requirements

- Dedicated QA environment
- Spring Boot deployment
- H2 database
- Environment reset capability

---

# 15. Test Data Requirements

- Team A
- Team B
- OPEN retrospective
- CLOSED retrospective
- Historical retrospectives
- WENT_WELL
- NEEDS_IMPROVEMENT
- ACTION_ITEM
- LOW/MEDIUM/HIGH priorities

---

# 16. Defect Classification

## Critical
Business rule violation.

## High
Major workflow failure.

## Medium
Validation or processing issue.

## Low
Minor issue with limited business impact.

---

# 17. Release Decision Matrix

## BLOCKED
- Any P1 rule violated.
- Data integrity compromised.

## CONDITIONAL
- Critical rules pass.
- Only accepted Medium/Low defects remain.

## APPROVED
- Critical rules pass.
- No Critical or High defects remain.

---

# 18. Test Coverage Matrix

- TC-001 Create first OPEN retrospective
- TC-002 Attempt second OPEN retrospective
- TC-003 Close retrospective
- TC-004 Attempt to reopen retrospective
- TC-005 Modify feedback while OPEN
- TC-006 Modify feedback while CLOSED
- TC-007 Delete feedback while CLOSED
- TC-008 Complete action item
- TC-009 Attempt to uncomplete action item
