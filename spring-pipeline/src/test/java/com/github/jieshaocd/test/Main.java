/**
 * Main.java
 */
package com.github.jieshaocd.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.github.jieshaocd.pipeline.PipelineChain;

/**
 * @author jieshao
 * @date Sep 6, 2015
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext context =
                new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        PipelineChain chain = (PipelineChain) context.getBean("checkPipeline");
        Map<String, Object> params = new HashMap<>();
        params.put("message", "test message");
        chain.runPipeline(params);
    }

}
