/**
 * PreCheckHandler.java
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
public class PreCheckHandler extends PipelineHandlerAdaptor {

    @Override
    public int execute(Map<String, Object> params) {
        Object message = params.get("message");
        System.out.println("PreCheckHandler: " + (String) message);
        return 1;
    }

}
