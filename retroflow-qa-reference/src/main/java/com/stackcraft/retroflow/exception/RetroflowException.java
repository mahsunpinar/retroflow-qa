package com.stackcraft.retroflow.exception;

/**
 * Thrown when a request violates one of RetroFlow's business rules — e.g.
 * attempting to modify a closed retrospective, or creating a second OPEN
 * retrospective for a team.
 */
public class RetroflowException extends RuntimeException {

    public RetroflowException(String message) {
        super(message);
    }

}
