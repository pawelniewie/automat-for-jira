package it;

import com.atlassian.jira.webtest.webdriver.tests.common.BaseJiraWebTest;
import org.junit.Test;

public class TestRestoreBlankJira extends BaseJiraWebTest
{
    @Test
    public void restore()
    {
        backdoor.restoreBlankInstance();
    }
}
