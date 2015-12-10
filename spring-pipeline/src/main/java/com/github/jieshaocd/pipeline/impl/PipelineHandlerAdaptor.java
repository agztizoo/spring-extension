/**
 * PipelineHandlerAdaptor.java
 */
package com.github.jieshaocd.pipeline.impl;

import java.util.Map;

import com.github.jieshaocd.pipeline.PipelineHandler;

/**
 * @author jieshao
 * @date Sep 6, 2015
 */
public class PipelineHandlerAdaptor implements PipelineHandler {

    public static final int STOP_AND_COMMIT = 0;

    public static final int STOP_AND_ERROR  = -1;

    public static final int SUCCESS         = 1;

    @Override
    public int execute(Map<String, Object> params) {
        return STOP_AND_COMMIT;
    }

}
