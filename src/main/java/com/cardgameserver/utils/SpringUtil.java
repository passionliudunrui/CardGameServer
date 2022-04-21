package com.cardgameserver.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 通过实现ApplicationContextAware接口中的setApplicationContext方法，我们可以获取到spring操作上线文applicationContext变量，
     　　　* 然后把它复制给静态变量applicationContext，这样我们就可以通过MyApplicationContext.applicationContext.getBean()的方式取spring管理的bean。
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    // ps: 获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    // ps: 通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    // ps: 通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    // ps:通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}