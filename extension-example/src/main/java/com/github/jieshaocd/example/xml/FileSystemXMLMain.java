/**
 * Main.java
 */
package com.github.jieshaocd.example.xml;

import org.springframework.context.ApplicationContext;

import com.github.jieshaocd.context.CustomizedFileSystemXmlApplicationContext;

/**
 * @author jieshao
 * @date Aug 14, 2015
 */
public class FileSystemXMLMain {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext contenxt =
                new CustomizedFileSystemXmlApplicationContext("classpath:applicationContext.xml");
        Product product = (Product) contenxt.getBean("product");
        System.out.println(product.getId());
        System.out.println(product.getPropertyValue());
    }

}
