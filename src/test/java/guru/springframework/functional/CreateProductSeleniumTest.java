package guru.springframework.functional;

import io.github.bonigarcia.wdm.WebDriverManager;
import guru.springframework.repositories.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@Ignore("Selenium tests disabled - Chrome/WebDriver setup issues")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateProductSeleniumTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    private WebDriver driver;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Before
    public void setup() {
    // Force WebDriverManager to download a matching chromedriver
    WebDriverManager.chromedriver().forceDownload().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // comenta si quieres ver el navegador
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1200,800");
        driver = new ChromeDriver(options);
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void createValidProduct() {
        long before = productRepository.count();

        driver.get(baseUrl() + "/product/new");

        // Los campos del formulario usan th:field, generan id/name automáticamente
        driver.findElement(By.id("productId")).sendKeys("TEST-001");
        driver.findElement(By.id("description")).sendKeys("Test Shirt Created by Selenium");
        driver.findElement(By.id("price")).sendKeys("19.99");
        driver.findElement(By.id("imageUrl")).sendKeys("http://example.com/shirt.jpg");

        // submit form
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // espera hasta 5s a que el repo aumente
        assertTrue("Product should be created", waitForRepoCountGreaterThan(before, 5000));
    }

    @Test
    public void createMissingProductIdShouldNotCreate() {
        long before = productRepository.count();

        driver.get(baseUrl() + "/product/new");

        // deja productId vacío
        driver.findElement(By.id("productId")).clear();
        driver.findElement(By.id("description")).sendKeys("No product ID");
        driver.findElement(By.id("price")).sendKeys("10.00");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // debe seguir igual (no creado) o validación del lado servidor
        // este test puede fallar si no hay validación backend - ajusta según tu lógica
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        // si la app permite crear sin productId, este assert fallará - ajusta la expectativa
        assertEquals("Product count should not change", before, productRepository.count());
    }

    @Test
    public void createMissingDescriptionShouldNotCreate() {
        long before = productRepository.count();

        driver.get(baseUrl() + "/product/new");

        driver.findElement(By.id("productId")).sendKeys("TEST-002");
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("price")).sendKeys("15.00");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        // ajusta la expectativa según validación backend
        assertEquals("Product count should not change without description", before, productRepository.count());
    }

    @Test
    public void createNegativePriceShouldNotCreate() {
        long before = productRepository.count();

        driver.get(baseUrl() + "/product/new");

        driver.findElement(By.id("productId")).sendKeys("TEST-003");
        driver.findElement(By.id("description")).sendKeys("Negative Price Test");
        driver.findElement(By.id("price")).sendKeys("-5.00");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        // ajusta según validación backend
        assertEquals("Product count should not change with negative price", before, productRepository.count());
    }

    // Helper: espera hasta timeout ms a que repo.count() > value
    private boolean waitForRepoCountGreaterThan(long value, long timeoutMs) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (productRepository.count() > value) {
                return true;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
                // ignore
            }
        }
        return false;
    }
}
