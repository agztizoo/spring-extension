/**
 * CustomizedBeanDefinitionParserDelegate.java
 */
package com.github.jieshaocd.beans.factory.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @author jieshao
 * @date Aug 6, 2015
 */
public class CustomizedBeanDefinitionParserDelegate extends BeanDefinitionParserDelegate {

    public static final String OVERRIDE_ORDER_ATTRIBUTE = "override-order";

    private final Set<String>  usedNames                = new HashSet<String>();

    /**
     * @param readerContext
     * @param environment
     */
    public CustomizedBeanDefinitionParserDelegate(XmlReaderContext readerContext, Environment environment) {
        super(readerContext, environment);
    }

    @Override
    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, BeanDefinition containingBean) {
        String id = ele.getAttribute(ID_ATTRIBUTE);
        String nameAttr = ele.getAttribute(NAME_ATTRIBUTE);

        List<String> aliases = new ArrayList<String>();
        if (StringUtils.hasLength(nameAttr)) {
            String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, MULTI_VALUE_ATTRIBUTE_DELIMITERS);
            aliases.addAll(Arrays.asList(nameArr));
        }

        String beanName = id;
        if (!StringUtils.hasText(beanName) && !aliases.isEmpty()) {
            beanName = aliases.remove(0);
            if (logger.isDebugEnabled()) {
                logger.debug("No XML 'id' specified - using '" + beanName + "' as bean name and "
                        + aliases + " as aliases");
            }
        }

        if (containingBean == null) {
            checkAliaseUniqueness(aliases, ele);
        }

        AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
        if (beanDefinition == null) {
            return null;
        }
        if (!StringUtils.hasText(beanName)) {
            try {
                if (containingBean != null) {
                    beanName =
                            BeanDefinitionReaderUtils.generateBeanName(beanDefinition,
                                    getReaderContext().getRegistry(), true);
                } else {
                    beanName = getReaderContext().generateBeanName(beanDefinition);
                    // Register an alias for the plain bean class name, if still possible,
                    // if the generator returned the class name plus a suffix.
                    // This is expected for Spring 1.2/2.0 backwards compatibility.
                    String beanClassName = beanDefinition.getBeanClassName();
                    if (beanClassName != null && beanName.startsWith(beanClassName)
                            && beanName.length() > beanClassName.length()
                            && !getReaderContext().getRegistry().isBeanNameInUse(beanClassName)) {
                        aliases.add(beanClassName);
                    }
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Neither XML 'id' nor 'name' specified - "
                            + "using generated bean name [" + beanName + "]");
                }
            } catch (Exception ex) {
                error(ex.getMessage(), ele);
                return null;
            }
        }
        AbstractBeanDefinition oldDefinition = getRegisterdBeanDefinition(beanName);
        if (oldDefinition != null) {
            beanDefinition = mergeBeanDefinition(beanDefinition, oldDefinition, beanName, ele);
            if (beanDefinition == null) {
                return null;
            }
        }
        String[] aliasesArray = StringUtils.toStringArray(aliases);
        return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
    }

    protected void checkAliaseUniqueness(List<String> aliases, Element beanElement) {
        String foundName = null;
        if (foundName == null) {
            foundName = CollectionUtils.findFirstMatch(this.usedNames, aliases);
        }
        if (foundName != null) {
            error("Bean name '" + foundName + "' is already used in this <beans> element",
                    beanElement);
        }
        this.usedNames.addAll(aliases);
    }

    @Override
    public AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele, String beanName,
            BeanDefinition containingBean, AbstractBeanDefinition bd) {
        AbstractBeanDefinition sbd =
                super.parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
        String order = ele.getAttribute(OVERRIDE_ORDER_ATTRIBUTE);
        if (StringUtils.hasText(order)) {
            sbd.setAttribute(OVERRIDE_ORDER_ATTRIBUTE, Integer.parseInt(order));
        } else {
            sbd.setAttribute(OVERRIDE_ORDER_ATTRIBUTE, 0);
        }
        return sbd;
    }

    protected AbstractBeanDefinition getRegisterdBeanDefinition(String beanName) {
        try {
            AbstractBeanDefinition def =
                    (AbstractBeanDefinition) getReaderContext().getRegistry().getBeanDefinition(
                        beanName);
            return def;
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    protected AbstractBeanDefinition mergeBeanDefinition(AbstractBeanDefinition curBean,
            AbstractBeanDefinition oldBean, String beanName, Element ele) {
        int curOrder = getOverrideOrderFromBeanDefinition(curBean);
        int oldOrder = getOverrideOrderFromBeanDefinition(oldBean);
        if (curOrder == oldOrder) {
            //conflict
            error("Bean name '" + beanName + "' is already used in this <beans> element", ele);
        } else if (curOrder < oldOrder) {
            curBean.overrideFrom(oldBean);
            return curBean;
        } else {
            oldBean.overrideFrom(curBean);
            return oldBean;
        }
        return null;
    }

    protected int getOverrideOrderFromBeanDefinition(AbstractBeanDefinition beanDefinition) {
        Integer order = (Integer) beanDefinition.getAttribute(OVERRIDE_ORDER_ATTRIBUTE);
        if (order != null) {
            return order.intValue();
        }
        return 0;
    }

}
