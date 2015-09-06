/**
 * PostCheckHandler.java
 */
package com.github.jieshaocd.handler;

import java.util.Map;

import com.github.jieshaocd.pipeline.impl.PipelineHandlerAdaptor;

/**
 * example
 * 
 * @author jieshao
 * @date Sep 2, 2015
 */
public class PostCheckHandler extends PipelineHandlerAdaptor {

    @Override
    public int execute(Map<String, Object> params) {
        Object message = params.get("message");
        System.out.println("PostCheckHandler: " + (String) message);
        return 1;
    }

}
