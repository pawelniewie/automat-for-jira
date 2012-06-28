package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.event.type.EventTypeManager;
import com.atlassian.sal.api.ApplicationProperties;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;
import java.util.Set;

public class EventUtils {

    @Nonnull
    public static Map<Long, String> getExecutableNames(Set<EventType> events) {
        final Map<Long, String> result = Maps.newHashMapWithExpectedSize(events.size());
        for (EventType event : events) {
            result.put(event.getId(), getExecutableName(event));
        }
        return result;
    }

    static String getExecutableName(EventType event) {
        return StringUtils.uncapitalize(StringUtils.remove(StringEscapeUtils.escapeJava(event.getName()) + (SystemUtils.IS_OS_UNIX ? ".sh" : ".bat"), " "));
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
}
