/**
 * ProductA.java
 */
package com.github.jieshaocd.example.xml;

/**
 * @author jieshao
 * @date Aug 14, 2015
 */
public class ProductA implements Product {

    public String getId() {
        return "ProductA";
    }

    @Override
    public String getPropertyValue() {
        return "No property";
    }

}
