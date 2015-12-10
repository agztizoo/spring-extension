/**
 * ChainDefinitionParser.java
 */
package com.github.jieshaocd.pipeline.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static final String X_ID                 = "id";
    private static final String X_CLASS              = "class";
    private static final String X_HEADER             = "header";
    private static final String X_RETURN_VALUE       = "returnvalue";
    private static final String X_NEXT_HANDLER       = "handler";
    private static final String X_BEAN               = "bean";

    private static final String CONTEXT_HANDLER      = "handler";
    private static final String CONTEXT_HANDLER_ID   = "handlerId";
    private static final String CONTEXT_NEXT_HANDLER = "nextHandlers";

    private static final String CHAIN_CLASS =
            "com.github.jieshaocd.pipeline.impl.DefaultPipelineChain";


    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String chainId = element.getAttribute(X_ID);
        AbstractBeanDefinition chainDefinition = parseChain(element, parserContext);


        List<Element> childElts = DomUtils.getChildElements(element);
        Set<String> usedNemes = getAllHandlerNames(childElts, parserContext, chainId);

        for (Element elt : childElts) {
            String hid = elt.getAttribute(X_ID);
            AbstractBeanDefinition bd = parseHandler(elt, parserContext);
            ManagedMap<Object, Object> nextMap = parseNextHandlers(elt, parserContext, chainId, usedNemes);
            PropertyValue pv = new PropertyValue(CONTEXT_NEXT_HANDLER, nextMap);
            bd.getPropertyValues().addPropertyValue(pv);
            parserContext.getRegistry().registerBeanDefinition(generateHandlerId(chainId, hid), bd);

        }
        parserContext.getRegistry().registerBeanDefinition(chainId, chainDefinition);
        return null;
    }

    /**
     * 解析pipeline:next标签
     * 
     * @param source
     * @param parserContext
     * @param chainId
     * @param usedNemes
     * @return
     */
    protected ManagedMap<Object, Object> parseNextHandlers(Element source, ParserContext parserContext, String chainId,
            Set<String> usedNemes) {
        List<Element> nextList = DomUtils.getChildElements(source);
        if (CollectionUtils.isEmpty(nextList)) {
            return null;
        }
        ManagedMap<Object, Object> nextMap = new ManagedMap<Object, Object>();
        for (Element element : nextList) {
            String nextKey = element.getAttribute(X_RETURN_VALUE);
            String nextRef = element.getAttribute(X_NEXT_HANDLER);
            if (!usedNemes.contains(nextRef)) {
                parserContext.getReaderContext().error("next handler not defined: " + nextRef + ", chain: " + chainId,
                        element);
            }
            RuntimeBeanReference ref = new RuntimeBeanReference(generateHandlerId(chainId, nextRef));
            nextMap.put(nextKey, ref);
        }
        return nextMap;
    }

    /**
     * 解析pipeline:chain标签
     * 
     * @param element
     * @param parserContext
     * @return
     */
    protected AbstractBeanDefinition parseChain(Element element, ParserContext parserContext) {
        String header = element.getAttribute(X_HEADER);
        String clazz = element.getAttribute(X_CLASS);
        String chainId = element.getAttribute(X_ID);
        if (!StringUtils.hasText(clazz)) {
            clazz = CHAIN_CLASS;
        }
        AbstractBeanDefinition bd = null;
        try {
            bd =
                    BeanDefinitionReaderUtils.createBeanDefinition(null, clazz, parserContext
                            .getReaderContext().getBeanClassLoader());
        } catch (ClassNotFoundException e) {
            parserContext.getReaderContext().error("pipeline chain class not found: " + clazz, element);
        }
        RuntimeBeanReference ref = new RuntimeBeanReference(generateHandlerId(chainId, header));
        PropertyValue pv = new PropertyValue(X_HEADER, ref);
        bd.getPropertyValues().addPropertyValue(pv);
        bd.setSource(element);
        return bd;
    }

    /**
     * 解析pipeline:handler标签
     * 
     * @param element
     * @param parserContext
     * @return
     */
    protected AbstractBeanDefinition parseHandler(Element element, ParserContext parserContext) {
        RootBeanDefinition bd = new RootBeanDefinition(PipelineHandlerContext.class);
        String refName = element.getAttribute(X_BEAN);
        RuntimeBeanReference ref = new RuntimeBeanReference(refName);
        PropertyValue hValue = new PropertyValue(CONTEXT_HANDLER, ref);
        bd.getPropertyValues().addPropertyValue(hValue);
        PropertyValue idValue = new PropertyValue(CONTEXT_HANDLER_ID, refName);
        bd.getPropertyValues().addPropertyValue(idValue);
        bd.setSource(element);
        return bd;
    }

    private Set<String> getAllHandlerNames(List<Element> elements, ParserContext parserContext, String chainId) {
        Set<String> usedHandlerNames = new HashSet<String>();
        for (Element elt : elements) {
            String hid = elt.getAttribute(X_ID);
            if (!usedHandlerNames.add(hid)) {
                parserContext.getReaderContext().error("handler already exists: " + hid + ", in chain: " + chainId,
                        elt);
            }
        }
        return usedHandlerNames;
    }

    private String generateHandlerId(String chainId, String handlerId) {
        return handlerId + "@" + chainId;
    }

}
