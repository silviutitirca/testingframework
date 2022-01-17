package selenium_testing.pageObjects;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LocatorPageObjectsTest {
    WebDriver driver;

    @BeforeEach
    public void setupData(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/simpletodolist/todolists.html");
    }

    @Test
    public void canCreateThenDeleteAList(){
        String listName = "my-new-list";
        TodoListsStructuralPage todolists = new TodoListsStructuralPage(driver);

        int defaultListCount = todolists.countLists();
        todolists.typeListName(listName);
        //todolists.loseNameFocus();
        todolists.submitListName();

        Assertions.assertEquals(
                defaultListCount + 1,
                todolists.countLists());

        todolists.clickDeleteButtonForListNamed(listName);
        todolists.confirmDeleteAlert();

        Assertions.assertEquals(
                defaultListCount,
                todolists.countLists());
    }

    @AfterEach
    public void closeBrowser(){driver.close();}

    private class TodoListsStructuralPage {
        private final WebDriver driver;
        final WebElement inputField;
        private final TodoListsPageLocators locators;

        public TodoListsStructuralPage(final WebDriver driver) {
            this.driver = driver;
            locators = new TodoListsPageLocators();
            inputField = driver.findElement(locators.getNewTodoListEntryField());
        }

        public void typeListName(final String listName) {
            inputField.sendKeys(listName);
        }

        public void loseNameFocus() {driver.findElement(locators.getPageHeading()).click();}

        public void submitListName() {inputField.sendKeys(Keys.ENTER);}

        public void clickDeleteButtonForListNamed(final String listName) {

            final WebElement todoListEntry =
                    new WebDriverWait(driver, 10).until(
                            ExpectedConditions.presenceOfElementLocated(
                                    locators.getListNamed(listName)
                            ));

            todoListEntry.findElement(locators.getDeleteButton()).click();
        }

        public void confirmDeleteAlert() {
            driver.switchTo().alert().accept();
        }

        public int countLists(){
            return driver.findElements(
                            locators.getAllListNameItems())
                    .size();
        }

    }

    private class TodoListsPageLocators {

        private final By newTodoListEntryField = By.cssSelector(".new-todo-list");
        private final By pageHeading = By.tagName("h1");
        private final By aDeleteButton = By.cssSelector("button.destroy");
        private final By allListNameItems = By.cssSelector("ul.todo-list-list li");

        public By getNewTodoListEntryField() {return newTodoListEntryField;}

        public By getPageHeading() {return pageHeading;}

        public By getDeleteButton() {return aDeleteButton;}

        public By getAllListNameItems() {return allListNameItems;}

        public By getListNamed(String listName) { return By.cssSelector("li[data-id='" + listName + "']");}
    }
}
