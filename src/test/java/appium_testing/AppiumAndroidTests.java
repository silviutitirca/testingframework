package appium_testing;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Interaction;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;

public class AppiumAndroidTests {
    private static final String APP = "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk";
    private static final String APPIUM = "http://localhost:4723/wd/hub";

    private AndroidDriver driver;


    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("appium:platformName", "Android");
        caps.setCapability("appium:platformVersion", "9");
        caps.setCapability("appium:deviceName", "Android Emulator");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:app", APP);
        driver = new AndroidDriver(new URL(APPIUM), caps);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLogin() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Login Screen")));
        screen.click();

        WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("username")));
        username.sendKeys("alice");

        WebElement password = driver.findElement(MobileBy.AccessibilityId("password"));
        password.sendKeys("mypassword");

        WebElement login = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
        login.click();

        WebElement loginText = wait.until(ExpectedConditions.presenceOfElementLocated(
                MobileBy.xpath("//android.widget.TextView[contains(@text, 'You are logged in')]")));

        assert(loginText.getText().contains("alice"));
    }

    @Test
    public void testPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Login Screen")));
        screen.click();

        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println(driver.getPageSource());
    }

    @Test
    public void testEchoMenu() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Echo Box"))).click();

        String myMessageInput = "Hello Appium World!";
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("messageInput"))).sendKeys(myMessageInput);

        driver.findElement(MobileBy.AccessibilityId("messageSaveBtn")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId(myMessageInput)));

    }

    @Test
    public void testTouchAction() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("List Demo")));
        screen.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Altocumulus")));

        swipe(1);

        driver.findElement(MobileBy.AccessibilityId("Stratus"));
    }

    private void swipe(Integer durationInSeconds){
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Interaction moveToStart = finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 520, 1530);
        Interaction pressDown = finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg());
        Interaction moveToEnd = finger.createPointerMove(Duration.ofSeconds(1), PointerInput.Origin.viewport(), 520, 490);
        Interaction pressUp = finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg());

        Sequence swipe = new Sequence(finger, 0);
        swipe.addAction(moveToStart);
        swipe.addAction(pressDown);
        swipe.addAction(moveToEnd);
        swipe.addAction(pressUp);

        driver.perform(Arrays.asList(swipe));
    }

}
