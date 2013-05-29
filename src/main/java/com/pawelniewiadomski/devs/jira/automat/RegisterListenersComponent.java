package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.event.ListenerManager;
import com.atlassian.jira.event.user.UserEvent;
import com.atlassian.jira.event.user.UserEventListener;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;
import com.pawelniewiadomski.devs.jira.servlet.ServletUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class RegisterListenersComponent implements InitializingBean, DisposableBean {
    private final static Logger log = Logger.getLogger(RegisterListenersComponent.class);

	private final ListenerManager listenerManager;

    public RegisterListenersComponent(ListenerManager listenerManager) {
		this.listenerManager = listenerManager;
    }

	@Override
	public void destroy() throws Exception {
		listenerManager.deleteListener(UserListener.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		listenerManager.createListener(UserListener.NAME, UserListener.class);
	}
}
