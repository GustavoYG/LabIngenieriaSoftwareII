package guru.springframework.functional;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@Ignore("Selenium tests disabled - Chrome/WebDriver setup issues")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FunctionalSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Before
    public void setup() {
    // Force WebDriverManager to download a matching chromedriver
    WebDriverManager.chromedriver().forceDownload().setup();
        ChromeOptions options = new ChromeOptions();
        // run headless so CI / local machines without GUI work
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @Test
    public void indexPageLoads() {
        String url = "http://localhost:" + port + "/";
        driver.get(url);
        String title = driver.getTitle();
        // basic assertion: page title not empty
        assertTrue("Title should not be empty", title != null && !title.isEmpty());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
