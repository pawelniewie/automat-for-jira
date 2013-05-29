package com.pawelniewiadomski.devs.jira.automat;

import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;
import com.google.common.collect.Iterables;
import com.pawelniewiadomski.devs.jira.servlet.ServletUtils;

/**
 *
 */
public class AutomatLicense {

    public boolean isValidLicense() {
        try {
            try {
                final ThirdPartyPluginLicenseStorageManager licenseManager = Iterables.<ThirdPartyPluginLicenseStorageManager>getFirst(SpringContext.getApplicationContext().getBeansOfType(AutomatLicense.class).values(), null);
                return ServletUtils.isValidLicense(licenseManager);
            } catch (PluginLicenseStoragePluginUnresolvedException e) {
                return false;
            }
        } catch (ClassCastException e) {
            return false;
        }
    }

}
