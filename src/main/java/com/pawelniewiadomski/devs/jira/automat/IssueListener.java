package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class IssueListener implements InitializingBean, DisposableBean {
	private final EventPublisher eventPublisher;

	public IssueListener(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@EventListener
	public void onIssueEvent(IssueEvent issueEvent) {

	}

	@Override
	public void destroy() throws Exception {
		eventPublisher.unregister(this);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		eventPublisher.register(this);
	}
}
