package selenium_testing.locators;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.Colors;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class LocatorExamplesTests {

    private WebDriver driver;

    @BeforeEach
    public void createDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/supportclasses/");
    }

    // The select class makes it easy to work with Select options
    // rather than finding the select menu and then all the options
    // below it - this is the only Element Abstraction in the
    // support classes
    @Test
    public void canSelectAnOptionUsingSelect(){
        final WebElement selectMenu = driver.findElement(By.id("select-menu"));
        final Select select = new Select(selectMenu);
        select.selectByVisibleText("Option 3");
        Assertions.assertEquals("3",
                select.getFirstSelectedOption().
                        getAttribute("value"));
    }

    @Test
    public void findInstructionsByIdOrName(){
        // findElement returns the element with the id if it exists, and if not searches for it via the name
        final WebElement instructionsPara = driver.findElement(
                new ByIdOrName("instruction-text"));
        final List<WebElement> instructionsParaAgain = driver.findElements(
                new ByIdOrName("instructions"));

        Assertions.assertEquals(instructionsPara.getText(),
                instructionsParaAgain.get(0).getText());
    }

    @Test
    public void quotesEscapingToCreateXPath(){
        Assertions.assertEquals("\"literal\"",
                Quotes.escape("literal"));
        Assertions.assertEquals("\"'single-quoted'\"",
                Quotes.escape("'single-quoted'"));
        Assertions.assertEquals("'\"double-quoted\"'",
                Quotes.escape("\"double-quoted\""));
        Assertions.assertEquals("concat(\"\", '\"', \"quot'end\", '\"')",
                Quotes.escape("\"quot'end\""));
        Assertions.assertEquals("concat(\"'quo\", '\"', \"ted'\")",
                Quotes.escape("'quo\"ted'"));
    }

    @Test
    public void checkColors(){
        final WebElement title = driver.findElement(By.id("instruction-title"));
        // Colors is an enum of named Color objects
        final Color blackValue = Colors.BLACK.getColorValue();

        // Color has methods to help convert between RBG, HEX
        Assertions.assertEquals("#000000",blackValue.asHex());
        Assertions.assertEquals("rgba(0, 0, 0, 1)",blackValue.asRgba());
        Assertions.assertEquals("rgb(0, 0, 0)",blackValue.asRgb());

        // color values returned by WebElement's getCSSValue are always
        // RGBA format, not the HTML source HEX or RGB

        Assertions.assertEquals(title.getCssValue("background-color"),
                blackValue.asRgba());

        // can create custom colors using the RGB input constructor
        // if the Colors enum does not have what we need

        final Color redValue = new Color(255,0,0, 1);
        Assertions.assertEquals(title.getCssValue("color"), redValue.asRgba());

    }

    @Test
    public void waitForMessage() {
        final WebElement selectMenu = driver.findElement(By.id("select-menu"));
        final Select select = new Select(selectMenu);
        select.selectByVisibleText("Option 2");

        // We are so used to using WebDriverWait and the ExpectedConditions class
        // that we might not have realised these are part of the support packages

        new WebDriverWait(driver, 10).until(
                ExpectedConditions.textToBe(By.id("message"), "Received message: selected 2"));
    }

    @Test
    public void canGetInfoAboutSelect(){
        final WebElement selectMenu = driver.findElement(By.id("select-menu"));
        final Select select = new Select(selectMenu);
        // the isMultiple method should be false for the select-menu item

        final WebElement multiSelectMenu = driver.findElement(By.id("select-multi"));
        final Select multiSelect = new Select(multiSelectMenu);

        // the isMultiple method should be true for multi select
        Assertions.assertFalse(select.isMultiple());
        Assertions.assertTrue(multiSelect.isMultiple());
    }

    @Test
    public void canGetAllOptionsFromSelect(){
        final WebElement selectMenu = driver.findElement(By.id("select-menu"));
        final Select select = new Select(selectMenu);

        // getOptions will return a List of WebElement
        // and allow me to access the options using
        // simple List methods
        List<WebElement> options = select.getOptions();
        Assertions.assertEquals(4,options.size());
        Assertions.assertEquals("Option 1", options.get(0).getText());
    }

    @Test
    public void canSelectSingleOptions(){
        // demo test to show single-select capabilities
        final WebElement selectMenu = driver.findElement(By.id("select-menu"));
        final Select select = new Select(selectMenu);

        // select will do nothing because this option is selected by default
        select.selectByIndex(0);
        Assertions.assertEquals("Option 1", select.getFirstSelectedOption().getText());

        // I can select the second item by Index 1 to chooose Option 2
        select.selectByIndex(1);
        Assertions.assertEquals("Option 2", select.getFirstSelectedOption().getText());

        // I can select the first by using the value "1"
        select.selectByValue("1");
        Assertions.assertEquals("Option 1", select.getFirstSelectedOption().getText());

        // and I can select using the text in the option
        select.selectByVisibleText("Option 3");
        Assertions.assertEquals("3", select.getFirstSelectedOption().getAttribute("value"));
    }


    @Test
    public void canSelectAndDeselectMultiOptions(){
        // demo test to show multi-select capabilities
        final WebElement selectMenu = driver.findElement(By.id("select-multi"));
        final Select select = new Select(selectMenu);

        // make sure nothing is selected with deselectAll
        select.deselectAll();
        // A normal select by index to get the First item
        select.selectByIndex(0);
        Assertions.assertEquals("First", select.getFirstSelectedOption().getText());

        // if I select I can deselect - by index, text or value
        select.deselectByIndex(0);
        // when nothing is selected a NoSuchElementException is thrown
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            select.getFirstSelectedOption(); });

        // select two items - values 20 and 30
        select.selectByValue("20");
        select.selectByValue("30");

        // use getAllSelectedOptions, there should be 2 in the list
        final List<WebElement> selected = select.getAllSelectedOptions();
        Assertions.assertEquals(2, selected.size());

        // assert on the getText for the list entries
        Assertions.assertEquals("Second", selected.get(0).getText());
        Assertions.assertEquals("Third", selected.get(1).getText());

        // deselect the first one - assert visible text "Second"
        select.deselectByVisibleText("Second");

        // and assert that the first selected option text is "Third"
        Assertions.assertEquals("Third", select.getFirstSelectedOption().getText());

        // deselect them all to finish
        select.deselectAll();
        Assertions.assertEquals(0, select.getAllSelectedOptions().size());

        Assertions.assertEquals(selectMenu, select.getWrappedElement());
    }

    //@Disabled("To be enabled if run independently")
    @Test
    public void canClickAButton(){
        final WebElement buttonElement = driver.findElement(By.id("resend-select"));
        Button button = new Button(buttonElement);
        // rather than click on a button element,
        // could we click on a Button?
        Assertions.assertEquals("Resend Single Option Message",
                button.getText());
        button.click();
        new WebDriverWait(driver, 10).
                until(ExpectedConditions.textToBe(By.id("message"),
                        "Received message: selected 1"));
    }

    @Test
    public void byIdOrName(){

        WebElement idButton = driver.findElement(By.id("resend-select"));
        Assertions.assertEquals("Resend Single Option Message",
                idButton.getText());

        WebElement namedButton = driver.findElement(By.name("resend-select"));
        Assertions.assertEquals("Resend Multi Option Message",
                namedButton.getText());

        // ByIdOrName can match by id, and if that doesn't match treat it as a name
        // use ByIdOrName to find a button element "resend-select"
        // and the assertions should pass
        WebElement button = driver.findElement(new ByIdOrName("resend-select"));
        Assertions.assertEquals(idButton, button);
        Assertions.assertNotEquals(namedButton, button);


        //ByIdOrName findElements returns all id and name matches
        //findElements for "resend-select" should find 2 buttons
        List<WebElement> buttons = driver.findElements(new ByIdOrName("resend-select"));
        Assertions.assertEquals(2, buttons.size());

        // the elements identified should be the same as we found initially
        Assertions.assertTrue(buttons.contains(idButton));
        Assertions.assertTrue(buttons.contains(namedButton));
    }

    @Test
    public void byAll(){
        // we could use ByAll to find by id or by name
        // by all is a collator, so given a number of locators, find all items that match
        final List<WebElement> buttons = driver.findElements(
                new ByAll(By.id("resend-select"),
                        By.name("resend-select")));
        Assertions.assertEquals(2, buttons.size());
        Assertions.assertTrue(buttons.contains(driver.findElement(By.id("resend-select"))));
        Assertions.assertTrue(buttons.contains(driver.findElement(By.name("resend-select"))));
    }

    @Test
    public void byChained(){
        final WebElement resendSingle = driver.findElement(By.id("resend-select"));
        resendSingle.click();
        resendSingle.click();
        resendSingle.click();
        resendSingle.click();

        final WebElement resend = driver.findElement(By.id("resend-multi"));
        resend.click();
        resend.click();

        // TODO: make this more specific to only find messages under a 'list'
        final List<WebElement> allMessages = driver.findElements(
                new ByChained(By.name("list"),
                        By.className("message")));
        Assertions.assertEquals(6, allMessages.size());

        // then just the #single list .message
        final List<WebElement> singleMessages = driver.findElements(
                new ByChained(By.id("single"),By.name("list"),
                        By.className("message")));
        Assertions.assertEquals(4, singleMessages.size());
        // then the #multi list .message
        final List<WebElement> multiMessages = driver.findElements(
                new ByChained(By.id("multi"),By.name("list"),
                        By.className("message")));
        Assertions.assertEquals(2, multiMessages.size());
    }

    @Test
    public void loggingFindingElements() {
        final By resend = By.id("resend-select");
        final By noSuchElement = By.id("no-such-element");

        EventFiringWebDriver events = new EventFiringWebDriver(driver);
        events.register(new LocalEventFiringListener());

        WebElement resendElem = events.findElement(resend);
        Assertions.assertNotNull(resendElem);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            events.findElement(noSuchElement);
        });

    }

    private class LocalEventFiringListener extends AbstractWebDriverEventListener {

        @Override
        public void beforeFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println("Looking For " + by.toString());
        }

        @Override
        public void afterFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println("Finished looking for " + by.toString());
        }
    }


    @AfterEach
    public void closeDriver() { driver.quit(); }

    private class Button implements WrapsElement {
        private final WebElement button;

        public Button(WebElement buttonElement) {
            this.button = buttonElement;
        }

        @Override
        public WebElement getWrappedElement() {
            return button;
        }

        public String getText() {
            return button.getText();
        }

        public void click() {
            button.click();
        }
    }


}
