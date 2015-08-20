/**
 * CustomizedFileSystemXmlApplicationContext.java
 */
package com.github.jieshaocd.context;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.github.jieshaocd.beans.factory.xml.CustomizedXmlBeanDefinitionReader;

/**
 * @author jieshao
 * @date Aug 6, 2015
 */
public class CustomizedFileSystemXmlApplicationContext extends FileSystemXmlApplicationContext {

    public CustomizedFileSystemXmlApplicationContext() {
        super();
    }

    public CustomizedFileSystemXmlApplicationContext(ApplicationContext parent) {
        super(parent);
    }

    public CustomizedFileSystemXmlApplicationContext(String configLocation) throws BeansException {
        super(new String[] {configLocation}, true, null);
    }

    public CustomizedFileSystemXmlApplicationContext(String... configLocations)
            throws BeansException {
        super(configLocations, true, null);
    }

    public CustomizedFileSystemXmlApplicationContext(String[] configLocations,
            ApplicationContext parent) throws BeansException {
        super(configLocations, true, parent);
    }

    public CustomizedFileSystemXmlApplicationContext(String[] configLocations, boolean refresh)
            throws BeansException {
        super(configLocations, refresh, null);
    }

    public CustomizedFileSystemXmlApplicationContext(String[] configLocations, boolean refresh,
            ApplicationContext parent) throws BeansException {
        super(configLocations, refresh, parent);
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
            throws BeansException, IOException {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        XmlBeanDefinitionReader beanDefinitionReader =
                new CustomizedXmlBeanDefinitionReader(beanFactory);

        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

        // Allow a subclass to provide custom initialization of the reader,
        // then proceed with actually loading the bean definitions.
        initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }

}
