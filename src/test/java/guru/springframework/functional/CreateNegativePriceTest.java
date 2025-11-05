package guru.springframework.functional;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CreateNegativePriceTest {

    public static void main(String[] args) {
    // Force WebDriverManager to download a matching chromedriver
    WebDriverManager.chromedriver().forceDownload().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1200,800");
        
        WebDriver driver = new ChromeDriver(options);

        try {
            String baseUrl = "http://localhost:8083";
            
            System.out.println("=== Caso 3: Precio negativo ===");
            System.out.println("Navegando a " + baseUrl + "/product/new");
            driver.get(baseUrl + "/product/new");

            driver.findElement(By.id("productId")).sendKeys("PROD-NEG");
            driver.findElement(By.id("description")).sendKeys("Precio negativo");
            driver.findElement(By.id("price")).sendKeys("-5.00");
            driver.findElement(By.id("imageUrl")).sendKeys("http://example.com/test.jpg");

            System.out.println("Enviando formulario con precio negativo...");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            Thread.sleep(2000);

            System.out.println("URL actual: " + driver.getCurrentUrl());
            
            // Verificación: debe quedarse en formulario
            if (driver.getCurrentUrl().contains("/product/new") || 
                driver.getCurrentUrl().contains("/product?")) {
                System.out.println("✓ PASS: No se creó producto con precio negativo");
            } else {
                System.out.println("✗ FAIL: Se creó producto cuando no debía");
            }

        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
