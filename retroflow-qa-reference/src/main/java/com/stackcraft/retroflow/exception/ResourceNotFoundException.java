package com.stackcraft.retroflow.exception;

/**
 * Thrown when a requested resource (team, feedback item, action item)
 * does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
