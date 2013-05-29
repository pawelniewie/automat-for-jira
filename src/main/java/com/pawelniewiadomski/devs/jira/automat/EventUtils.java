package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.event.type.EventTypeManager;
import com.atlassian.jira.event.user.UserEventType;
import com.atlassian.sal.api.ApplicationProperties;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Map;
import java.util.Set;

public class EventUtils {
	static final Map<Integer, String> userEvents = ImmutableMap.<Integer, String>builder()
			.put(UserEventType.USER_SIGNUP, "userSignUp")
			.put(UserEventType.USER_CREATED, "userCreated")
			.put(UserEventType.USER_FORGOTPASSWORD, "userForgotPassword")
			.put(UserEventType.USER_FORGOTUSERNAME, "userForgotUsername")
			.put(UserEventType.USER_CANNOTCHANGEPASSWORD, "cannotChangePassword")
			.put(UserEventType.USER_LOGIN, "userLogin")
			.put(UserEventType.USER_LOGOUT, "userLogout").build();

	@Nonnull
    public static Map<Long, String> getExecutableNames(Set<EventType> events) {
        final Map<Long, String> result = Maps.newHashMapWithExpectedSize(events.size());
        for (EventType event : events) {
            result.put(event.getId(), getExecutableName(event));
        }
        return result;
    }

	@Nonnull
	public static Map<Integer, String> getUserExecutableNames(Set<Integer> events) {
		final Map<Integer, String> result = Maps.newHashMapWithExpectedSize(events.size());
		for (Integer event : events) {
			result.put(event, getExecutableName(event));
		}
		return result;
	}

	static String getExecutableName(int eventType) {
		final String eventName = StringUtils.defaultString(userEvents.get(eventType), "unknownUserEvent");
		return StringUtils.uncapitalize(StringUtils.remove(StringEscapeUtils.escapeJava(eventName) + (SystemUtils.IS_OS_UNIX ? ".sh" : ".bat"), " "));
	}

	static String getExecutableName(EventType event) {
        return StringUtils.uncapitalize(StringUtils.remove(StringEscapeUtils.escapeJava(event.getName()) + (SystemUtils.IS_OS_UNIX ? ".sh" : ".bat"), " "));
    }

	@Nonnull
	public static Set<Integer> getSupportedUserEvents() {
		return userEvents.keySet();
	}

    @Nonnull
    public static Set<EventType> getSupportedEvents(EventTypeManager eventTypeManager) {
        return Sets.newLinkedHashSet(eventTypeManager.getEventTypes());
//        return ImmutableSet.copyOf(Iterables.filter(eventTypeManager.getEventTypes(), new Predicate<EventType>() {
//            Set<Long> handled = ImmutableSet.of(EventType.ISSUE_CREATED_ID, EventType.ISSUE_CLOSED_ID,
//                    EventType.ISSUE_DELETED_ID, EventType.ISSUE_RESOLVED_ID);
//
//            @Override
//            public boolean apply(@Nullable EventType o) {
//                return handled.contains(o.getId());
//            }
//        }));
    }

    public static File getExecutablesDir(ApplicationProperties applicationProperties) {
        return new File(applicationProperties.getHomeDirectory(), "automat");
    }

	public static Map<Integer, String> getUserEventNames() {
		final Map <Integer, String> userEvents = ImmutableMap.<Integer, String>builder()
				.put(UserEventType.USER_SIGNUP, "User Signed Up")
				.put(UserEventType.USER_CREATED, "User Created")
				.put(UserEventType.USER_FORGOTPASSWORD, "User Forgot Password")
				.put(UserEventType.USER_FORGOTUSERNAME, "User Forgot Username")
				.put(UserEventType.USER_CANNOTCHANGEPASSWORD, "Cannot Change Password")
				.put(UserEventType.USER_LOGIN, "User Login")
				.put(UserEventType.USER_LOGOUT, "User Logout").build();

		return userEvents;
	}
}
