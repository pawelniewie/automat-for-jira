package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.jira.event.user.UserEvent;
import com.atlassian.jira.event.user.UserEventListener;

import java.util.Map;

/**
 * TODO: Document this class / interface here
 *
 * @since v5.0.1
 */
public class UserListener implements UserEventListener {
	@Override
	public void userSignup(UserEvent event) {
	}

	@Override
	public void userCreated(UserEvent event) {
	}

	@Override
	public void userForgotPassword(UserEvent event) {
	}

	@Override
	public void userForgotUsername(UserEvent event) {
	}

	@Override
	public void userCannotChangePassword(UserEvent event) {
	}

	@Override
	public void init(Map params) {
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
		return false;
	}

	@Override
	public String getDescription() {
		return null;
	}
}
