package com.pawelniewiadomski.devs.jira.automat;

/**
 * TODO: Document this class / interface here
 *
 * @since v6.0
 */

import com.atlassian.sal.api.ApplicationProperties;
import com.google.common.collect.Iterables;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContext implements ApplicationContextAware {
	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	public static ApplicationContext getApplicationContext() {
		return context;
	}

    public static AutomatLicense getAutomatLicense() {
        ApplicationContext applicationContext = SpringContext.getApplicationContext();
        return applicationContext != null ? Iterables.<AutomatLicense>getFirst(applicationContext.getBeansOfType(AutomatLicense.class).values(), null) : null;
    }
}