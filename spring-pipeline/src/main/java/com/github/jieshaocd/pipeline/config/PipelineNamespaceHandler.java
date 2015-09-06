/**
 * PipelineNamespaceHandler.java
 */
package com.github.jieshaocd.pipeline.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author jieshao
 * @date Sep 1, 2015
 */
public class PipelineNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("chain", new PipelineChainDefinitionParser());
    }

}
