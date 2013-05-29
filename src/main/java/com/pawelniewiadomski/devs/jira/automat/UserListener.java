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
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class UserListener implements UserEventListener, InitializingBean, DisposableBean {
	private final static String NAME = "Automat User Listener";
    private final static Logger log = Logger.getLogger(UserListener.class);

	@Autowired
	private ThirdPartyPluginLicenseStorageManager licenseManager;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ListenerManager listenerManager;


	private boolean isValidLicense() {
		try {
			return ServletUtils.isValidLicense(licenseManager);
		} catch (PluginLicenseStoragePluginUnresolvedException e) {
			return false;
		}
	}

    @Override
    public void userSignup(UserEvent event) {
        processEvent(event);
    }

    private void processEvent(UserEvent event) {
		final String baseUrl = applicationProperties.getBaseUrl();
        log.debug(event.toString());
		if (isValidLicense()) {
			executeCommand(EventUtils.getExecutableName(event.getEventType()), baseUrl, getUsername(
					event.getInitiatingUser()),
					getUsername(event.getUser()));
		}
    }

	@Nonnull
	private String getUsername(@Nullable User initiatingUser) {
		return initiatingUser != null ? initiatingUser.getName() : "";
	}

	private void executeCommand(String executableName, String baseUrl, String initiatingUser, String user) {
		final File commandPath = new File(EventUtils.getExecutablesDir(applicationProperties), executableName);
		if (!commandPath.exists()) {
			log.debug(String.format("%s doesn't exist", commandPath));
			return;
		}
		if (!commandPath.canExecute()) {
			log.warn(String.format("%s is not executable", commandPath));
			return;
		}
		try {
			Runtime.getRuntime().exec(new String[]{
					commandPath.toString(), baseUrl, initiatingUser, user
			});
		} catch (IOException e) {
			log.error("Unable to execute command " + commandPath, e);
		}
	}

    @Override
    public void userCreated(UserEvent event) {
        processEvent(event);
    }

    @Override
    public void userForgotPassword(UserEvent event) {
        processEvent(event);
    }

    @Override
    public void userForgotUsername(UserEvent event) {
        processEvent(event);
    }

    @Override
    public void userCannotChangePassword(UserEvent event) {
        processEvent(event);
    }

    @Override
    public void init(Map params) {
        log.setLevel(Level.ALL);
    }

    @Override
    public String[] getAcceptedParams() {
        return new String[0];
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getDescription() {
        return "This is automatically created Automat User Listener, don't remove.";
    }

	@Override
	public void destroy() throws Exception {
		listenerManager.deleteListener(getClass());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		listenerManager.createListener(NAME, getClass());
	}
}
