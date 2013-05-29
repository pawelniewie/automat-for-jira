package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.event.type.EventTypeManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.pawelniewiadomski.devs.jira.servlet.ServletUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class AutomatListener implements InitializingBean, DisposableBean {
    private final static Logger log = Logger.getLogger(AutomatListener.class);
    private final EventPublisher eventPublisher;
    private final EventTypeManager eventTypeManager;
    private final ApplicationProperties applicationProperties;
    private final ThirdPartyPluginLicenseStorageManager licenseManager;

    public AutomatListener(EventPublisher eventPublisher, EventTypeManager eventTypeManager, ApplicationProperties applicationProperties, ThirdPartyPluginLicenseStorageManager licenseManager) {
        this.eventPublisher = eventPublisher;
        this.eventTypeManager = eventTypeManager;
        this.applicationProperties = applicationProperties;
        this.licenseManager = licenseManager;

//        log.setLevel(Level.ALL);
    }

    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }

    @SuppressWarnings("unused")
    @EventListener
    public void onIssueEvent(IssueEvent issueEvent) {
        final Long eventTypeId = issueEvent.getEventTypeId();
        final EventType eventType = eventTypeManager.getEventType(eventTypeId);
        final String baseUrl = applicationProperties.getBaseUrl();
        final Issue issue = issueEvent.getIssue();
        final Set<Long> supportedEvents = ImmutableSet.copyOf(Iterables.transform(EventUtils.getSupportedEvents(eventTypeManager), new Function<EventType, Long>() {
            @Override
            public Long apply(@Nullable EventType eventType) {
                return eventType.getId();
            }
        }));

        if (supportedEvents.contains(eventTypeId) && isValidLicense()) {
            executeCommand(EventUtils.getExecutableName(eventType), baseUrl, issue.getKey(), issueEvent.getUser() != null ? issueEvent.getUser().getName() : "");
        }
    }

    private void executeCommand(String executableName, String baseUrl, String issueKey, String user) {
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
                    commandPath.toString(), baseUrl, issueKey, user
            });
        } catch (IOException e) {
            log.error("Unable to execute command " + commandPath, e);
        }
    }

    private boolean isValidLicense() {
        try {
            return ServletUtils.isValidLicense(licenseManager);
        } catch (PluginLicenseStoragePluginUnresolvedException e) {
            return false;
        }
    }

//	@EventListener
//	public void onUserEvent(UserEvent userEvent) {
//		int i = 2;
//	}
//
//	@EventListener
//	public void onPropertyChangeEvent(ApplicationPropertyChangeEvent changeEvent) {
//		int i = 3;
//	}

//	@EventListener
//	public void onVersionArchiveEvent(VersionArchiveEvent event) {
//
//	}
//
//	@EventListener
//	public void onVersionCreateEvent(VersionCreateEvent event) {
//
//	}
//
//	@EventListener
//	public void onVersionDeleteEvent(VersionDeleteEvent event) {
//
//	}
//
//	@EventListener
//	public void onVersionMergeEvent(VirtualMachineError event) {
//
//	}
//
//	@EventListener
//	public void onVersionMoveEvent(VirtualMachineError event) {
//
//	}
//
//	@EventListener
//	public void onVersionReleaseEvent(VersionReleaseEvent event) {
//
//	}
//
//	@EventListener
//	public void onVersionUnarchiveEvent() {
//
//	}
//
//	@EventListener
//	public void onVersionUnreleaseEvent() {
//
//	}

}
