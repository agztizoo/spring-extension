/**
 * PipelineException.java
 */
package com.github.jieshaocd.exception;

/**
 * @author jieshao
 * @date Sep 6, 2015
 */
public class PipelineException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PipelineException() {
        super();
    }

    public PipelineException(String message) {
        super(message);
    }

    public PipelineException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipelineException(Throwable cause) {
        super(cause);
    }

}
