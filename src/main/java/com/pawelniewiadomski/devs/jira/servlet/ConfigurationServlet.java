package com.pawelniewiadomski.devs.jira.servlet;

import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.event.type.EventTypeManager;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;
import com.pawelniewiadomski.devs.jira.automat.EventUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.pawelniewiadomski.devs.jira.servlet.ServletUtils.hasAdminPermission;
import static com.pawelniewiadomski.devs.jira.servlet.ServletUtils.isValidLicense;
import static com.pawelniewiadomski.devs.jira.servlet.ServletUtils.redirectToLogin;

public class ConfigurationServlet extends HttpServlet
{
    private static final String TEMPLATE = "configuration-admin.vm";
    public static final String SERVLET_PATH = "/plugins/servlet/com.pawelniewiadomski.devs.jira.automat/configuration";

    private final ThirdPartyPluginLicenseStorageManager licenseManager;
    private final ApplicationProperties applicationProperties;
    private final TemplateRenderer renderer;
    private final LoginUriProvider loginUriProvider;
    private final UserManager userManager;
    private final I18nResolver i18nResolver;
    private final EventTypeManager eventTypeManager;
    private final UserUtil userUtil;

    public ConfigurationServlet(ThirdPartyPluginLicenseStorageManager licenseManager,
                                ApplicationProperties applicationProperties,
                                TemplateRenderer renderer,
                                LoginUriProvider loginUriProvider,
                                UserManager userManager,
                                I18nResolver i18nResolver,
                                EventTypeManager eventTypeManager,
                                UserUtil userUtil)
    {
        this.licenseManager = licenseManager;
        this.applicationProperties = applicationProperties;
        this.renderer = renderer;
        this.loginUriProvider = loginUriProvider;
        this.userManager = userManager;
        this.i18nResolver = i18nResolver;
        this.eventTypeManager = eventTypeManager;
        this.userUtil = userUtil;
    }

    private boolean canAccess(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (userManager.getRemoteUsername() == null)
        {
            redirectToLogin(loginUriProvider, req, resp);
            return false;
        }
        else if (!hasAdminPermission(userManager))
        {
            handleUnpermittedUser(req, resp);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        if (!canAccess(req, resp)) {
            return;
        }

        final Map<String, Object> context = initVelocityContext(resp);
        renderer.render(TEMPLATE, context, resp.getWriter());
    }

    private Map<String, Object> initVelocityContext(HttpServletResponse resp)
    {
        resp.setContentType("text/html;charset=utf-8");
        URI servletUri = URI.create(applicationProperties.getBaseUrl() + SERVLET_PATH);

        final Map<String, Object> context = new HashMap<String, Object>();
        context.put("servletUri", servletUri);
        context.put("displayAdminUi", true);
        context.put("currentUser", userUtil.getUserObject(userManager.getRemoteUsername()));

        try
        {
            if (!isValidLicense(licenseManager)) {
                context.put("errorMessage", i18nResolver.getText("plugin.configuration.admin.invalid.license",
                        URI.create(applicationProperties.getBaseUrl() + LicenseServlet.SERVLET_PATH)));
            }

            final Set<EventType> supportedEvents = EventUtils.getSupportedEvents(eventTypeManager);
            context.put("supportedEvents", supportedEvents);
            context.put("issueCreatedEvent", eventTypeManager.getEventType(EventType.ISSUE_CREATED_ID));
            context.put("eventExecutables", EventUtils.getExecutableNames(supportedEvents));
            context.put("executablesDir", EventUtils.getExecutablesDir(applicationProperties));
            context.put("baseUrl", applicationProperties.getBaseUrl());
        }
        catch (PluginLicenseStoragePluginUnresolvedException e)
        {
            context.put("errorMessage", i18nResolver.getText("plugin.license.storage.admin.plugin.unavailable"));
            context.put("displayAdminUi", false);
        }

        return context;
    }

    private void handleUnpermittedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put("errorMessage", i18nResolver.getText("plugin.license.storage.admin.unpermitted"));
        context.put("displayAdminUi", false);
        renderer.render(TEMPLATE, context, resp.getWriter());
    }
}
