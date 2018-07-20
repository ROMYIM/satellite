package com.iptv.satellite.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * BeanUtil
 */
public class BeanUtil {

    public static DefaultListableBeanFactory beanFactory;

    public static void initBeanFactory(ApplicationContext applicationContext) {
        beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }

    public static boolean createBean(String className, String beanName, Map<String, Object> propertyMap, Map<String, String> referenceMap, 
            List<Object> propertyConstructList, List<String> referenceConstructList, String destoryMethod) {
        if (beanFactory.containsBeanDefinition(className)) {
            return false;
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(className);
        beanDefinitionBuilder.getBeanDefinition().setAttribute("id", beanName);

        // 设置bean的参数属性
        if (propertyMap != null && !propertyMap.isEmpty()) {
            Iterator<String> propertyKeys = propertyMap.keySet().iterator();
            while (propertyKeys.hasNext()) {
                String keyString = propertyKeys.next();
                beanDefinitionBuilder.addPropertyValue(keyString, propertyMap.get(keyString));
            }
        }

        // 设置bean的引用属性
        if (referenceMap != null && !referenceMap.isEmpty()) {
            Iterator<String> referenceKeys = referenceMap.keySet().iterator();
            while (referenceKeys.hasNext()) {
                String keyString = referenceKeys.next();
                beanDefinitionBuilder.addPropertyReference(keyString, referenceMap.get(keyString));
            }
        }

        // 设置bean的构造参数属性
        if (propertyConstructList != null && propertyConstructList.size() > 0) {
            for (int i = 0; i < propertyConstructList.size(); i++) {
                beanDefinitionBuilder.addConstructorArgValue(propertyConstructList.get(i));
            }
        }

        // 设置bean的构造参数引用
        if (referenceConstructList != null && referenceConstructList.size() > 0) {
            for (int i = 0; i < referenceConstructList.size(); i++) {
                beanDefinitionBuilder.addConstructorArgReference(referenceConstructList.get(i));
            }
        }

        if (destoryMethod != null && !destoryMethod.isEmpty()) {
            beanDefinitionBuilder.setDestroyMethodName(destoryMethod);
        }
        beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return true;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public static <T> T getBean(String beanName, Class<T> requiredType) {
        if (beanName == null || beanName.length() <= 0) {
            return null;
        }
        return beanFactory.getBean(beanName, requiredType);
    }

    public static void removeBean(String beanName) {
        beanFactory.removeBeanDefinition(beanName);
    }
}