package jp.vmi.selenium.selenese.command;

import java.io.File;
import java.io.IOException;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxBinary;

import com.thoughtworks.selenium.SeleniumException;

import jp.vmi.selenium.selenese.Runner;
import jp.vmi.selenium.selenese.TestCase;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.webdriver.WebDriverManager;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Test for {@link BuiltInCommand}.
 */
public class BuiltInCommandTest {

    /**
     * Check Firefox installation.
     */
    @Before
    public void assumeInstalledFirefox() {
        try {
            new FirefoxBinary();
        } catch (SeleniumException e) {
            Assume.assumeNoException(e);
        } catch (WebDriverException e) {
            Assume.assumeNoException(e);
        }
    }

    /**
     * Check Firefox connected.
     */
    @Before
    public void assumeConnectFirefox() {
        try {
            WebDriverManager wdm = WebDriverManager.getInstance();
            wdm.setWebDriverFactory(WebDriverManager.FIREFOX);
            WebDriverManager.getInstance().get();
        } catch (WebDriverException e) {
            if (e.getMessage().contains("no display specified"))
                Assume.assumeNoException(e);
        }
    }

    /**
     * Test of user friendly error message.
     *
     * @throws IOException exception.
     */
    @Test
    public void userFriendlyErrorMessage() throws IOException {
        Command click = new BuiltInCommand(1, "click", new String[] { "link=linktext" }, "click", false);

        File selenesefile = File.createTempFile("selenese", ".html");

        TestCase testcase = new TestCase();
        WebDriverManager wdm = WebDriverManager.getInstance();
        Runner runner = new Runner();
        runner.setDriver(wdm.get());
        testcase.initialize(selenesefile, "test", runner, "");

        Result result = click.doCommand(testcase);

        assertThat(result.getMessage(), is("failed command:Command#1: click(\"link=linktext\") result:Element link=linktext not found"));
    }
}
