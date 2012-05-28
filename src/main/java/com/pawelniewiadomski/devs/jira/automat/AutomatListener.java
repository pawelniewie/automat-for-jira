package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.config.ApplicationPropertyChangeEvent;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.user.UserEvent;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class AutomatListener implements InitializingBean, DisposableBean {
	private final EventPublisher eventPublisher;

	public AutomatListener(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void destroy() throws Exception {
		eventPublisher.unregister(this);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		eventPublisher.register(this);
	}

	@EventListener
	public void onIssueEvent(IssueEvent issueEvent) {
		int i = 1;
	}

	@EventListener
	public void onUserEvent(UserEvent userEvent) {
		int i = 2;
	}

	@EventListener
	public void onPropertyChangeEvent(ApplicationPropertyChangeEvent changeEvent) {
		int i = 3;
	}

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
