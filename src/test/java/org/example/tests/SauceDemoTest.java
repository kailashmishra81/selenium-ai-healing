package org.example.tests;

import org.example.Base.BaseTest;
import org.example.util.SelfHealingHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

public class SauceDemoTest extends BaseTest {
    private SelfHealingHelper healer;

    @BeforeMethod
    public void setUp() {
        initDriver();
        driver.get("https://www.saucedemo.com/");
        healer = new SelfHealingHelper(driver, "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");
    }

    @Test(priority=2)
    public void loginWithoutAILocatorFail() {
        try {
            WebElement user = driver.findElement(By.id("user-name"));
            user.sendKeys("standard_user");
            WebElement pwd = driver.findElement(By.id("password"));
            pwd.sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000);
            // 2) Add "Sauce Labs Bike Light" to cart
            WebElement bikeLightAdd = driver.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button"));
            bikeLightAdd.click();
            Thread.sleep(1000);
            // 3) Checkout
            driver.findElement(By.className("shopping_cart_link")).click();
            driver.findElement(By.id("checkout")).click();
            driver.findElement(By.xpath("//div[first123-name]")).sendKeys("John"); // ❌ wrong locator
            driver.findElement(By.name("last")).sendKeys("Doe");

        } catch (Exception e) {
            System.out.println("❌ Locator failed without AI: " + e.getMessage());
            Assert.fail("Test failed due to invalid locator without AI healing");
        }
    }

    @Test(priority=1)
    public void loginWithoutAIHappypath() {
        try {
            WebElement user = driver.findElement(By.id("user-name"));
            user.sendKeys("standard_user");
            WebElement pwd = driver.findElement(By.id("password"));
            pwd.sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000);
            // 2) Add "Sauce Labs Bike Light" to cart
            WebElement bikeLightAdd = driver.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button"));
            bikeLightAdd.click();
            Thread.sleep(1000);
            // 3) Checkout
            driver.findElement(By.className("shopping_cart_link")).click();
            driver.findElement(By.id("checkout")).click();
            driver.findElement(By.id("first-name")).sendKeys("John"); // ❌ wrong locator
            Thread.sleep(1000);
            driver.findElement(By.name("lastName")).sendKeys("Doe");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("❌ Locator failed without AI: " + e.getMessage());
            Assert.fail("Test failed due to invalid locator without AI healing");
        }
    }

        @Test(priority=3)
    public void loginwithAILocatorFail() throws InterruptedException {
        // 1. Login
        healer.findElement(By.id("user---name")).sendKeys("standard_user"); // ❌ wrong locator
        healer.findElement(By.id("password")).sendKeys("secret_sauce");
        healer.findElement(By.id("login-button")).click();
        Thread.sleep(2000);
        WebElement bikeLightAdd = healer.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button22"));// ❌ wrong locator
        bikeLightAdd.click();
        Thread.sleep(2000);
        healer.findElement(By.className("shopping_cart_link")).click();
        healer.findElement(By.id("checkout")).click();
        healer.findElement(By.xpath("//div[first123-name]")).sendKeys("John");// ❌ wrong locator
        healer.findElement(By.name("last")).sendKeys("Doe");// ❌ wrong locator
        healer.findElement(By.id("postal456-code")).sendKeys("12345");// ❌ wrong locator
        healer.findElement(By.id("continue44")).click();// ❌ wrong locator
        healer.findElement(By.id("finish")).click();
        healer.findElement(By.id("back-to-products")).click();
        healer.findElement(By.id("react-burger-menu-btn")).click();
        healer.findElement(By.id("logout_sidebar_link")).click();

        System.out.println("🎯 Total healed locators used: " + healer.getHealedLocatorCount());
       // test.printHealingReport();
        System.out.println("✅ Test Completed Successfully!");
    }

    @AfterMethod
    public void tearDown() {
        quitDriver();
    }
}
