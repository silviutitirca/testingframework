package appium_testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;


public class AppiumChromeTests {
    //private static final String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk";
    private static final String APPIUM = "http://localhost:4723/wd/hub";

    private RemoteWebDriver driver;


    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("appium:platformName", "Android");
        caps.setCapability("appium:platformVersion", "9");
        caps.setCapability("appium:deviceName", "Android Emulator");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("chromedriver_autodownload", true);
        driver = new RemoteWebDriver(new URL(APPIUM), caps);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testWebsiteLoadsAMenu() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://appiumpro.com");
        WebElement toggleMenu = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toggleMenu___NWpP_")));
        toggleMenu.click();
        driver.findElement(By.xpath("//div[@id=\"__next\"]/div/div[2]/div/ul/li[4]/a")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id=\"__next\"]/div/div[4]")));
    }

    @Test
    public void testContactFormPage(){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://appiumpro.com");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toggleMenu"))).click();
        driver.findElement(By.linkText("Contact")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#contactEmail"))).sendKeys("foo@bar.com");
        driver.findElement(By.cssSelector("#contactText")).sendKeys("hello");
        driver.findElement(By.xpath("//input[@value='Send']")).click();
        String response = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".contactResponse"))).getText();
        assert(response.contains("Captcha"));
    }


}
