package selenium_testing.pageObjects;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class DataObjectTest {

    WebDriver driver;

    @BeforeEach
    public void setupData(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/simpletodolist/todolists.html");
    }

    @Test
    public void canCreateAList(){

        TodoListName listName = new TodoListName();

        DataObjectsTodoListsPage todolists = new DataObjectsTodoListsPage(driver);

        todolists.enterTodoListName(listName.getName());

//        Assertions.assertEquals(
//                listName,
//                todolists.getDisplayedListText(listName));
        for(int x=0;x<15;x++){
            todolists.enterTodoListName(new TodoListName().getName());
        }
        System.out.println("Use the app now");
    }

    @AfterEach
    public void closeBrowser(){
        driver.close();
    }



    private class TodoListName {
        private final String name;

        public TodoListName(){
            this.name = new Faker().chuckNorris().fact().replace(" ","-");
            //this.name = new RandomString("-").generate();
            System.out.println("Create Todo List " + name);
        }

        public String getName() {
            return name;
        }
    }

    class DataObjectsTodoListsPage {
        private final WebDriver driver;

        public DataObjectsTodoListsPage(final WebDriver driver) {
            this.driver = driver;
        }

        public void enterTodoListName(final String listName) {
            final WebElement inputField =
                    driver.findElement(
                            By.cssSelector(".new-todo-list"));

            inputField.sendKeys(listName + Keys.ENTER);
        }

        public String getDisplayedListText(final String listName) {
            WebElement todoListEntry = driver.findElement(
                    By.cssSelector(
                            "li[data-id='" + listName + "']"));

            return todoListEntry.findElement(
                    By.tagName("label")).getText();
        }

        public void clickOnList(final String listName) {
            WebElement todoListEntry = driver.findElement(
                    By.cssSelector(
                            "li[data-id='" + listName + "']"));

            todoListEntry.findElement(By.tagName("a")).click();
        }
    }
}
