package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.jira.event.user.UserEvent;
import com.atlassian.jira.event.user.UserEventListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Map;

public class UserListener implements UserEventListener {
    private final static Logger log = Logger.getLogger(AutomatListener.class);

    @Override
    public void userSignup(UserEvent event) {
        processEvent(event);
    }

    private void processEvent(UserEvent event) {
        log.debug(event.toString());
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
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
