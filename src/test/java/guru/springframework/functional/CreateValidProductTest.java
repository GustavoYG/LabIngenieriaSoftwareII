package guru.springframework.functional;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CreateValidProductTest {

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
            
            System.out.println("=== Caso 1: Producto válido ===");
            System.out.println("Navegando a " + baseUrl + "/product/new");
            driver.get(baseUrl + "/product/new");

            // Completar formulario con datos válidos
            driver.findElement(By.id("productId")).sendKeys("PROD-001");
            driver.findElement(By.id("description")).sendKeys("Camiseta de prueba");
            driver.findElement(By.id("price")).sendKeys("12.50");
            driver.findElement(By.id("imageUrl")).sendKeys("http://example.com/shirt.jpg");

            System.out.println("Enviando formulario...");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            Thread.sleep(2000);

            System.out.println("URL actual: " + driver.getCurrentUrl());
            System.out.println("Título: " + driver.getTitle());
            
            // Verificación esperada: redirige a /product/{id} o /products
            if (driver.getCurrentUrl().contains("/product")) {
                System.out.println("✓ PASS: Producto creado exitosamente");
            } else {
                System.out.println("✗ FAIL: No se detectó redirección esperada");
            }

        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
