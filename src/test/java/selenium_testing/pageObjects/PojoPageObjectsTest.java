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

import java.util.List;

public class PojoPageObjectsTest {
    WebDriver driver;

    @BeforeEach
    public void setupData(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/simpletodolist/todolists.html");

    }

    @Test
    public void canCreateAList(){

        String listName = "my-new-list";

        TodoListsPage todolists = new TodoListsPage(driver);

        todolists.enterTodoListName(listName);

        Assertions.assertEquals(listName,
                todolists.getDisplayedListTest(listName));
    }

    @Test
    public void canCreateTodos(){

        // click on the list name
        String listName = "my-new-list";

        TodoListsPage todolists = new TodoListsPage(driver);
        todolists.enterTodoListName(listName);
        todolists.clickOnList(listName);

        TodoListPage todolist = new TodoListPage(driver);

        String todoItemNamed = "Do this thing";
        todolist.enterTodo(todoItemNamed);

        Assertions.assertEquals(1, todolist.countTodos());

    }

    @AfterEach
    public void closeBrowser(){
        driver.close();
    }

    private class TodoListPage {
        private final WebDriver driver;

        public TodoListPage(WebDriver driver) {
            this.driver = driver;
        }

        public void enterTodo(String todoItemNamed) {
            final WebElement createTodo =
                    driver.findElement(By.cssSelector("input.new-todo"));
            createTodo.sendKeys(todoItemNamed + Keys.ENTER);
        }

        public int countTodos() {
            // check one item in list
            final List<WebElement> todoItems =
                    driver.findElements(
                            By.cssSelector("ul.todo-list li div.view"));

            return todoItems.size();
        }
    }

    private class TodoListsPage {
        private final WebDriver driver;

        public TodoListsPage(WebDriver driver) {
            this.driver = driver;
        }

        public void enterTodoListName(String listName) {
            final WebElement inputField =
                    driver.findElement(
                            By.cssSelector(".new-todo-list"));

            inputField.sendKeys(listName + Keys.ENTER);
        }

        public String getDisplayedListTest(String listName) {
            WebElement todoListEntry = driver.findElement(
                    By.cssSelector(
                            "li[data-id='" + listName + "']"));
            return
                    todoListEntry.findElement(By.tagName("label")).getText();
        }

        public void clickOnList(String listName) {
            WebElement todoListEntry = driver.findElement(
                    By.cssSelector(
                            "li[data-id='" + listName + "']"));

            todoListEntry.findElement(By.tagName("a")).click();
        }
    }
}
