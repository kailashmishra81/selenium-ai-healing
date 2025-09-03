package org.example.tests;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.util.HealCounter;
import org.example.util.SelfHealingHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class SauceDemoTest extends BaseTest {
    private SelfHealingHelper healer;
    public static List<String> failedLocators = new ArrayList<>();
    public static List<String> healedLocators = new ArrayList<>();
    public static List<String> PageSource = new ArrayList<>();
    @BeforeMethod
    public void setUp() {
        Dotenv dotenv = Dotenv.load();
        String apikey = dotenv.get("OPENAI_API_KEY");
        initDriver();
        driver.get("https://www.saucedemo.com/");
        healer = new SelfHealingHelper(driver, apikey);
    }

  //  @Test(priority=2)
    public void loginWithoutAILocatorFail() {
        try {
            WebElement user = driver.findElement(By.id("user-name"));
            user.sendKeys("standard_user");
            WebElement pwd = driver.findElement(By.id("password"));
            pwd.sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000);
            WebElement bikeLightAdd = driver.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button"));
            bikeLightAdd.click();
            Thread.sleep(1000);
            driver.findElement(By.className("shopping_cart_link")).click();
            driver.findElement(By.id("checkout")).click();
            driver.findElement(By.xpath("//div[first123-name]")).sendKeys("John"); // ❌ wrong locator
            driver.findElement(By.name("last")).sendKeys("Doe");
        } catch (Exception e) {
            System.out.println("❌ Locator failed : " + e.getMessage());
            Assert.fail("Test failed due to invalid locator without AI healing");
        }
    }

//    @Test(priority=1)
    public void Happypath_NoLocatorsFailed() {
        try {
            WebElement user = driver.findElement(By.id("user-name"));
            user.sendKeys("standard_user");
            WebElement pwd = driver.findElement(By.id("password"));
            pwd.sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000);
            WebElement bikeLightAdd = driver.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button"));
            bikeLightAdd.click();
            Thread.sleep(1000);
            driver.findElement(By.className("shopping_cart_link")).click();
            driver.findElement(By.id("checkout")).click();
            driver.findElement(By.id("first-name")).sendKeys("John");
            Thread.sleep(1000);
            driver.findElement(By.name("lastName")).sendKeys("Doe");
            Thread.sleep(1000);
          } catch (Exception e) {
            System.out.println("❌ Locator failed: " + e.getMessage());
            Assert.fail("Test failed due to invalid locator without healing");
        }
    }

        @Test(priority=3)
        public void LocatorSelfHealing() throws InterruptedException {
        healer.findElement(By.id("user---name")).sendKeys("standard_user");  // ❌ Inserting wrong locator for username field to simulate failure and healing
        healer.findElement(By.id("password")).sendKeys("secret_sauce");
        healer.findElement(By.id("login-button")).click();
        Thread.sleep(2000);
        WebElement bikeLightAdd = healer.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button22"));  // ❌ Inserting wrong locator for add to cart button to simulate failure and healing
        bikeLightAdd.click();
        Thread.sleep(2000);
        healer.findElement(By.className("shopping_cart_link")).click();
        healer.findElement(By.id("checkout")).click();
        healer.findElement(By.xpath("//div[first123-name]")).sendKeys("John"); // ❌ Inserting  wrong locator for first name to simulate failure and healing
        healer.findElement(By.name("last")).sendKeys("Doe"); // ❌ Inserting  wrong locator last name to simulate failure and healing
        healer.findElement(By.id("postal456-code")).sendKeys("12345"); // ❌ Inserting  wrong locator zipcode fields to simulate failure and healing
        healer.findElement(By.id("continue44")).click(); // ❌ Inserting  wrong locator for continue button to simulate failure and healing
        healer.findElement(By.id("finish")).click();
        healer.findElement(By.id("back-to-products")).click();
        healer.findElement(By.id("react-burger-menu-btn")).click();
        healer.findElement(By.id("logout_sidebar_link")).click();
        System.out.println("🎯 Total healed locators used: " + HealCounter.getHealedLocators());
       // test.printHealingReport();
        System.out.println("✅ Test Completed Successfully!");
    }

    @AfterMethod
    public void tearDown() {
        quitDriver();
    }
}
