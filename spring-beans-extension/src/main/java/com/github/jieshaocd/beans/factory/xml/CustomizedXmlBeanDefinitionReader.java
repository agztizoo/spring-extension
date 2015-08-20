/**
 * CustomizedXmlBeanDefinitionReader.java
 */
package com.github.jieshaocd.beans.factory.xml;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author jieshao
 * @date Aug 6, 2015
 */
public class CustomizedXmlBeanDefinitionReader extends XmlBeanDefinitionReader {

    /**
     * @param registry
     */
    public CustomizedXmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
        setDocumentReaderClass(CustomizedBeanDefinitionDocumentReader.class);
    }

}
