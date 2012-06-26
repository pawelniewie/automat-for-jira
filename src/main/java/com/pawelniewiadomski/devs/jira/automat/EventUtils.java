package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.jira.event.type.EventType;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class EventUtils {

    @Nonnull
    public static Map<Long, String> getExecutableNames(Set<EventType> events) {
        final Map<Long, String> result = Maps.newHashMapWithExpectedSize(events.size());
        for(EventType event : events) {
            result.put(event.getId(), getExecutableName(event));
        }
        return result;
    }

    static String getExecutableName(EventType event) {
        return StringUtils.uncapitalize(StringUtils.remove(StringEscapeUtils.escapeJava(event.getName()) + (SystemUtils.IS_OS_UNIX ? ".sh" : ".bat"), " "));
    }
}
