/**
 * CustomizedBeanDefinitionDocumentReader.java
 */
package com.github.jieshaocd.beans.factory.xml;

import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Element;

/**
 * @author jieshao
 * @date Aug 6, 2015
 */
public class CustomizedBeanDefinitionDocumentReader extends DefaultBeanDefinitionDocumentReader {

    @Override
    protected BeanDefinitionParserDelegate createDelegate(XmlReaderContext readerContext,
            Element root, BeanDefinitionParserDelegate parentDelegate) {
        BeanDefinitionParserDelegate delegate =
                new CustomizedBeanDefinitionParserDelegate(readerContext);
        delegate.initDefaults(root, parentDelegate);
        return delegate;
    }

}
