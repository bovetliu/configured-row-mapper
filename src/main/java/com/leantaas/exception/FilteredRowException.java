package com.leantaas.exception;

/**
 * This exception is used to mark whether one row should be marked as filtered or not.
 * Throwing one runtime exception can quickly stop output-computing recursion of
 * {@link com.leantaas.graph_logic.MappingStep}.
 */
public final class FilteredRowException extends RuntimeException {

    public FilteredRowException(String message) {
        super(message);
    }

    public FilteredRowException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilteredRowException(Throwable cause) {
        super(cause);
    }
}
