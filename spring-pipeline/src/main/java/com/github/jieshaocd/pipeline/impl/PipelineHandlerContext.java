/**
 * ChainHandlerContext.java
 */
package com.github.jieshaocd.pipeline.impl;

import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.github.jieshaocd.exception.PipelineException;
import com.github.jieshaocd.pipeline.PipelineHandler;

/**
 * @author jieshao
 * @date Sep 1, 2015
 */
public class PipelineHandlerContext {

    private String handlerId;

    private PipelineHandler handler;

    private Map<String, PipelineHandlerContext> nextHandlers;

    public PipelineHandlerContext() {}

    public PipelineHandlerContext(PipelineHandler handler) {
        this.handler = handler;
    }

    public void execute(Map<String, Object> params) {
        int result = handler.execute(params);
        if (CollectionUtils.isEmpty(nextHandlers)) {
            return;
        }
        PipelineHandlerContext nextHandler = nextHandlers.get(String.valueOf(result));
        if (nextHandler == null) {
            throw new PipelineException("Next handler not found by: " + result + ", in: "
                    + handlerId);
        }
        nextHandler.execute(params);
    }

    public PipelineHandler getHandler() {
        return handler;
    }

    public void setHandler(PipelineHandler handler) {
        this.handler = handler;
    }

    public Map<String, PipelineHandlerContext> getNextHandlers() {
        return nextHandlers;
    }

    public void setNextHandlers(Map<String, PipelineHandlerContext> nextHandlers) {
        this.nextHandlers = nextHandlers;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

}
