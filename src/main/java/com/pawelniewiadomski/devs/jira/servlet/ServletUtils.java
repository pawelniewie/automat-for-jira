package com.pawelniewiadomski.devs.jira.servlet;

import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;

public class ServletUtils {

    static void redirectToLogin(LoginUriProvider loginUriProvider, HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        resp.sendRedirect(loginUriProvider.getLoginUri(URI.create(req.getRequestURL().toString())).toASCIIString());
    }

    static boolean hasAdminPermission(UserManager userManager)
    {
        String user = userManager.getRemoteUsername();
        try
        {
            return user != null && (userManager.isAdmin(user) || userManager.isSystemAdmin(user));
        }
        catch(NoSuchMethodError e)
        {
            // userManager.isAdmin(String) was not added until SAL 2.1.
            // We need this check to ensure backwards compatibility with older product versions.
            return user != null && userManager.isSystemAdmin(user);
        }
    }

    public static boolean isValidLicense(ThirdPartyPluginLicenseStorageManager licenseManager) throws PluginLicenseStoragePluginUnresolvedException {
        if (licenseManager.getLicense().isDefined())
        {
            for (PluginLicense pluginLicense : licenseManager.getLicense())
            {
                return !pluginLicense.getError().isDefined();
            }
        }
        return false;
    }
}
