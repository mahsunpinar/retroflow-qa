# Session notes

## P1 - Happy-Path Workflow (Baseline Release Confidence)

### Create team
#### What I tried: Create a team
#### What I expected: New team
#### What happened: New team created
#### Worth a closer look? N

### Create a retrospective
#### What I tried: Create retrospective
#### What I expected: New retrospective
#### What happened: New retro created
#### Worth a closer look? N

### Add feedback
#### What I tried: Add feedback
#### What I expected: Feedback on the retrospective
#### What happened: Feedback has been added to the retrospective submitted by a specific user
#### Worth a closer look? N

### Add action item
#### What I tried: Added an action item
#### What I expected: Action item on the retrospective
#### What happened: Action item added on the retro
#### Worth a closer look? N

### Complete action item
#### What I tried: Complete an action item
#### What I expected: Action item completed
#### What happened: Action item completed
#### Worth a closer look? N

### Close retrospective
#### What I tried: Close retrospective 1
#### What I expected: Retro 1 closed
#### What happened: Retro 1 closed
#### Worth a closer look? N

## Retrospective Lifecycle Rule enforcement

### Create second open retrospective for the same team
#### What I tried: Create a second open retro for the same team
#### What I expected: Rejected
#### What happened: Reject with status 409: Team 4 has already an open retrospective
#### Worth a closer look? N

### Create second retro after the first one is closed
#### What I tried: Create second after closed
#### What I expected: Success
#### What happened: Success
#### Worth a closer look? N

### Close already closed retrospective
#### What I tried: Close already closed
#### What I expected: Rejection
#### What happened: 200 Ok 
#### Worth a closer look? Y

### Attempt reopening through available endpoints
#### What I tried:
- Add feedback item to closed retro
- Add action item to closed retro
#### What I expected:
- Can't add feedback item to closed retro
- Can't action item to closed retro
#### What happened:
- No feedback added item to closed retro
- Action added item to closed retro
#### Worth a closer look? Y

### Create retrospective using non-existent team-ID
#### What I tried: Using an non existing team-ID (5) in post request
#### What I expected: Fail
#### What happened: Fail, 404 Team not found
#### Worth a closer look? N

## Closed Retrospective Protection

### Modify feedback after closure
#### What I tried: Update feedback
#### What I expected: Fail
#### What happened: Fail 409 Feedback item 1: cannot modify feedback in a closed retrospective
#### Worth a closer look? N

### Delete feedback after closure
#### What I tried: Delete feedback request
#### What I expected: Fail
#### What happened: Success
#### Worth a closer look? Y

### Retrieve feedback after closure
#### What I tried: Get feedback
#### What I expected: Success
#### What happened: Success
#### Worth a closer look? Y

## Action Item Lifecycle Enforcement

### Complete action item
#### What I tried: Success
#### What I expected: Success
#### What happened: Success
#### Worth a closer look? N

### Complete already completed item
#### What I tried: Complete item
#### What I expected: Fail
#### What happened: Success 200
#### Worth a closer look? Y

### Set completed=false after completion
#### What I tried: Set completed false
#### What I expected: Fail
#### What happened: Success 200
#### Worth a closer look? Y

### Filter completed=true
#### What I tried: Filter completed
#### What I expected: List of completed action-items
#### What happened: List of completed
#### Worth a closer look? N

### Filter completed=false after completion
#### What I tried: Filter incomplete
#### What I expected: Incomplete list of action-items
#### What happened: Incomplete list retrieved
#### Worth a closer look? N

# Concurrency and Race Conditions

### Two users create OPEN retrospectives simultaneously
#### What I tried: Create with two testers at the same time a retro
#### What I expected: Fastest one succeed
#### What happened: MP succeed
#### Worth a closer look? N

### One user closes retrospective while another adds feedback
#### What I tried: ""
#### What I expected: One consistent outcome enforced
#### What happened: Retro closed and feedback is added
#### Worth a closer look? Y

### Two users close same retrospective simultaneously
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 

### 
#### What I tried:
#### What I expected:
#### What happened:
#### Worth a closer look? 