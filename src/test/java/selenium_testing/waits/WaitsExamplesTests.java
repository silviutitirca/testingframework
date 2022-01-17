package selenium_testing.waits;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WaitsExamplesTests {

    private static WebDriver driver;

    @BeforeAll
    public static void createDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/supportclasses/");
    }

    @Test
    public void whyWaitsAreRequired(){
        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // the message is not immediately displayed, need to wait
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            final WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));
        });
    }

    @Test
    public void implicitWait(){
        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // implicit wait forces all findElement to poll until it passes or timesout end 5 seconds
        driver.manage().timeouts().
                implicitlyWait(5000, TimeUnit.MILLISECONDS);
        // the message is not immediately displayed so this line will fail without an implicit wait
        final WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));

        // slows down tests on failures
        // may cause some tests to pass which should not
        // only option is to increase global timeout when timing issues happen, which slows tests down further
        // hard to check for absence of something since it takes as long as the timeout

        // remember to set implicit waits off if you use them otherwise it will affect all findElement commands
        driver.manage().timeouts().
                implicitlyWait(0, TimeUnit.MILLISECONDS);
    }

    @Test
    public void explicitWait(){
        // explicit wait means only waiting at specific points
        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();
        // the message is not immediately displayed so I need wait for
        // visibility of Element to change
        new WebDriverWait(driver, 10).until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#single-list li.message")
                )
        );

        // need to wait for the element to be visible prior to trying to find it
        // and assert on the result
        final WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));
        Assertions.assertTrue(message.getText().startsWith("Received message:"));
    }

    @Test
    public void shareWaitAndUseOnReturn(){
        // often we share a wait e.g. set this up in @BeforeX methods
        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // normally we 'wait and return' rather than wait then repeat find
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#single-list li.message")));

        Assertions.assertTrue(message.getText().startsWith("Received message:"));
    }

    @Test
    public void explicitWaitUsingCustomExpectedCondition(){

        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        new WebDriverWait(driver, 10).until(
                historyMessagesIncreaseInNumber());

        WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));
        Assertions.assertTrue(message.getText().startsWith("Received message:"));
    }

    private ExpectedCondition<Boolean> historyMessagesIncreaseInNumber() {
        return new ExpectedCondition<Boolean>(){

            private int initialCount=driver.findElements(By.cssSelector("li.message")).size();

            @Override
            public Boolean apply(WebDriver webDriver) {
                int currentCount = driver.findElements(By.cssSelector("li.message")).size();
                return currentCount>initialCount;
            }
        };
    }

    @Test
    public void explicitWaitIsFluent(){

        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // WebDriverWait is built on FluentWait so we have a lot of control over the wait
        // todo: customise timeout message, poll every 50 milliseconds,
        //       and ignore StaleElementReferenceException.class
        final WebElement message = new WebDriverWait(driver, 5).
                withMessage("Could not find a Message").
                pollingEvery(Duration.ofMillis(50)).
                ignoring(StaleElementReferenceException.class).
                until(ExpectedConditions.
                        visibilityOfElementLocated(
                                By.cssSelector("#single-list li.message")));

        Assertions.assertTrue(message.getText().startsWith("Received message:"));

    }

    // using fluent wait to wait using WebElement rather than driver
    @Test
    public void usingFluentWait(){
        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        WebElement singleListParent = driver.findElement(By.id("single-list"));
        FluentWait wait = new FluentWait<WebElement>(singleListParent).
                withTimeout(Duration.ofSeconds(10)).
                pollingEvery(Duration.ofMillis(500)).
                withMessage("Could not find any new messages");

        wait.until(new WaitsExamplesTests.HistoryMessagesIncreaseInNumber(0));
        final WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));
        Assertions.assertTrue(message.getText().startsWith("Received message:"));

    }

    private class HistoryMessagesIncreaseInNumber implements Function<WebElement, Boolean> {
        private final int initialCount;

        public HistoryMessagesIncreaseInNumber(int initialCount) {
            this.initialCount = initialCount;
        }

        @Override
        public Boolean apply(final WebElement element) {
            int currentCount = element.findElements(By.cssSelector("li.message")).size();
            return currentCount>initialCount;
        }
    }

    @AfterAll
    public static void closeDriver() { driver.quit(); }

}
