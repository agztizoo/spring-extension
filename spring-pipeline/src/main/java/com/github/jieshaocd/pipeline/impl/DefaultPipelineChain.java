/**
 * PipelineChainImpl.java
 */
package com.github.jieshaocd.pipeline.impl;

import java.util.Map;

import com.github.jieshaocd.pipeline.PipelineChain;

/**
 * @author jieshao
 * @date Sep 1, 2015
 */
public class DefaultPipelineChain implements PipelineChain {

    private PipelineHandlerContext header;

    @Override
    public void runPipeline(Map<String, Object> params) {
        header.execute(params);
    }

    public PipelineHandlerContext getHeader() {
        return header;
    }

    public void setHeader(PipelineHandlerContext header) {
        this.header = header;
    }

}
