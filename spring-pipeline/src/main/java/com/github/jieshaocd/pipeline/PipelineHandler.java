/**
 * PipelineChain.java
 */
package com.github.jieshaocd.pipeline;

import java.util.Map;

/**
 * @author jieshao
 * @date Sep 1, 2015
 */
public interface PipelineHandler {

    public int execute(Map<String, Object> params);

}
