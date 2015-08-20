/**
 * ProductB.java
 */
package com.github.jieshaocd.example.xml;

/**
 * @author jieshao
 * @date Aug 14, 2015
 */
public class ProductB extends ProductA {

    private Property property;

    public String getId() {
        return "ProductB";
    }

    @Override
    public String getPropertyValue() {
        return getProperty().getPropertyName();
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

}
