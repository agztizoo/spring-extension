/**
 * ChainDefinitionParser.java
 */
package com.github.jieshaocd.pipeline.config;

import java.util.List;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.github.jieshaocd.pipeline.impl.PipelineHandlerContext;

/**
 * @author jieshao
 * @date Sep 2, 2015
 */
public class PipelineChainDefinitionParser implements BeanDefinitionParser {

    private static final String ID = "id";
    private static final String CLASS = "class";
    private static final String HEADER = "header";
    private static final String HANDLER = "handler";
    private static final String HANDLER_ID = "handlerId";
    private static final String KEY = "key";
    private static final String REFID = "refid";
    private static final String REF = "ref";
    private static final String NEXT_HANDLER = "nextHandlers";

    private static final String CHAIN_CLASS =
            "com.github.jieshaocd.pipeline.impl.DefaultPipelineChain";


    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String chainId = element.getAttribute(ID);
        AbstractBeanDefinition chainDefinition = parseChain(element, parserContext);

        List<Element> childElts = DomUtils.getChildElements(element);
        for (Element elt : childElts) {
            String hid = elt.getAttribute(ID);
            AbstractBeanDefinition bd = parseHandler(elt, parserContext);
            ManagedMap<Object, Object> nextMap = parseNextHandlers(elt, parserContext);
            PropertyValue pv = new PropertyValue(NEXT_HANDLER, nextMap);
            bd.getPropertyValues().addPropertyValue(pv);
            parserContext.getRegistry().registerBeanDefinition(hid, bd);

        }
        parserContext.getRegistry().registerBeanDefinition(chainId, chainDefinition);
        return null;
    }

    protected ManagedMap<Object, Object> parseNextHandlers(Element source,
            ParserContext parserContext) {
        List<Element> nextList = DomUtils.getChildElements(source);
        if (CollectionUtils.isEmpty(nextList)) {
            return null;
        }
        ManagedMap<Object, Object> nextMap = new ManagedMap<Object, Object>();
        for (Element element : nextList) {
            String nextKey = element.getAttribute(KEY);
            String nextRef = element.getAttribute(REFID);
            RuntimeBeanReference ref = new RuntimeBeanReference(nextRef);
            nextMap.put(nextKey, ref);
        }
        return nextMap;
    }

    protected AbstractBeanDefinition parseChain(Element element, ParserContext parserContext) {
        String header = element.getAttribute(HEADER);
        String clazz = element.getAttribute(CLASS);
        if (!StringUtils.hasText(clazz)) {
            clazz = CHAIN_CLASS;
        }
        AbstractBeanDefinition bd = null;
        try {
            bd =
                    BeanDefinitionReaderUtils.createBeanDefinition(null, clazz, parserContext
                            .getReaderContext().getBeanClassLoader());
        } catch (ClassNotFoundException e) {
            parserContext.getReaderContext().error("pipeline chain class not found.", element);
        }
        RuntimeBeanReference ref = new RuntimeBeanReference(header);
        PropertyValue pv = new PropertyValue(HEADER, ref);
        bd.getPropertyValues().addPropertyValue(pv);
        bd.setSource(element);
        return bd;
    }

    protected AbstractBeanDefinition parseHandler(Element element, ParserContext parserContext) {
        RootBeanDefinition bd = new RootBeanDefinition(PipelineHandlerContext.class);
        String refName = element.getAttribute(REF);
        RuntimeBeanReference ref = new RuntimeBeanReference(refName);
        PropertyValue hValue = new PropertyValue(HANDLER, ref);
        bd.getPropertyValues().addPropertyValue(hValue);
        PropertyValue idValue = new PropertyValue(HANDLER_ID, refName);
        bd.getPropertyValues().addPropertyValue(idValue);
        bd.setSource(element);
        return bd;
    }

}
