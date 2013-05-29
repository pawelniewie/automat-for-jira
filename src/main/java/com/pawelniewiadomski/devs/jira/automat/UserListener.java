package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.event.user.UserEvent;
import com.atlassian.jira.event.user.UserEventListener;
import com.atlassian.sal.api.ApplicationProperties;
import com.google.common.collect.Iterables;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class UserListener implements UserEventListener {
	public final static String NAME = "Automat User Listener";
    private final static Logger log = Logger.getLogger(UserListener.class);
    private AutomatLicense automatLicense;
    private ApplicationProperties applicationProperties;

    @Override
    public void userSignup(UserEvent event) {
        processEvent(event);
    }

    private void processEvent(UserEvent event) {
		final String baseUrl = applicationProperties.getBaseUrl();
        log.debug(event.toString());
		if (automatLicense.isValidLicense()) {
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
        
        automatLicense = Iterables.<AutomatLicense>getFirst(SpringContext.getApplicationContext().getBeansOfType(AutomatLicense.class).values(), null);
        applicationProperties = Iterables.<ApplicationProperties>getFirst(SpringContext.getApplicationContext().getBeansOfType(ApplicationProperties.class).values(), null);
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
}
