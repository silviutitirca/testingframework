package api_testing;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Product;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    String bearerToken = "ff2643e4513928acdfe73efa0e9d0ac18572c50926396643a887c4cc6685a21d";
    String username;

    /*@Test
    public void getCategories(){
        String endpoint = "http://localhost:82/api_testing/category/read.php";
        given().when().get(endpoint).
                then().
                    log().
                        headers().
                    assertThat().
                        statusCode(200).
                        body("records.size()", greaterThan(0)).
                        body("records.id", everyItem(notNullValue())).
                        body("records.name", everyItem(notNullValue())).
                        body("records.description", everyItem(notNullValue())).
                        //body("records.price", everyItem(notNullValue())).
                        //body("records.category_id", everyItem(notNullValue())).
                        //body("records.category_name", everyItem(notNullValue()));
                        body("records.id[0]", equalTo("1"));
    }

    @Test
    public void getProduct(){
        String endpoint = "http://localhost:82/api_testing/product/read_one.php";
        given().queryParam("id", 17).
        when().get(endpoint).
        then().
        assertThat().
                statusCode(200).
                body("id", equalTo("17")).
                body("name", equalTo("Magnesium 250 mg (100 tablets)")).
                body("description", equalTo("Magnesium is critical to many bodily processes, and supports nerve, muscle, and heart function.")).
                body("price", equalTo("12.00")).
                body("category_id", equalTo("4"));
    }

    @Test
    public void createProduct() {
        String endpoint = "http://localhost:82/api_testing/product/create.php";
        String body = """
                {
                "name": "Sweatband",
                "description": "White sweatband for gym",
                "price": 6,
                "category_id": 3
                }
                """;
        var response = given().body(body).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void updateProduct(){
        String endpoint = "http://localhost:82/api_testing/product/update.php";
        String body = """
                {
                "id": 1006,
                "name": "Sweatband",
                "description": "White sweatband for gym",
                "price": 5,
                "category_id": 3
                }
                """;
        var response = given().body(body).when().put(endpoint).then();
        response.log().body();
        //Assertions.assertTrue(response.log().body().toString().contains("Product was created."));
    }

    @Test
    public void deleteProduct(){
        String endpoint = "http://localhost:82/api_testing/product/delete.php";
        String body = """
                {
                "id": 1006
                }
                """;
        var response = given().body(body).when().delete(endpoint).then();
        response.log().body();
        //Assertions.assertTrue(response.log().body().toString().contains("Product was created."));
    }

    @Test
    public void createSerializedProducts(){
        String endpoint = "http://localhost:82/api_testing/product/create.php";
        Product product = new Product("Water bottle", "green bottle color", 11, 3);
        var response = given().body(product).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void getDeserializedProduct(){
        String endpoint = "http://localhost:82/api_testing/product/read_one.php";
        Product expectedProduct = new Product(2,"Cross-Back Training Tank",
                "The most awesome phone of 2013!", 299.00, 2,"Active Wear - Women");
        Product actualProduct = given().
                queryParam("id", "2").
                when().get(endpoint).as(Product.class);

        assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
    }*/


    @Test
    public void createUser(){
        String endpoint = "https://gorest.co.in/public/v1/users";
        username = RandomStringUtils.random(10, true, true);
        String rawBody = """
                {
                "name":"${Username}",
                "email":"${Username}@email.com",
                "gender":"male",
                "status":"active"
                }
                """;
        String body = rawBody.replace("${Username}", username);
        var response =
           given().headers("Authorization","Bearer " + bearerToken,"Content-Type",ContentType.JSON,"Accept",ContentType.JSON)
                  .body(body)
           .when()
                   .post(endpoint)
           .then();
        response.log().body();

        given().when().get("https://gorest.co.in/public/v1/users?name=" + username)
                .then().log().headers()
                .assertThat().statusCode(200)
                        .log().body()
                .assertThat()
                    .body("data.name[0]", equalTo(username));
    }

    @Test
    public void getSomeUsers(){
        String endpoint = "https://gorest.co.in/public/v1/users";
        given().when().get(endpoint).
                then().
                log().
                headers().
                assertThat().
                statusCode(200).
                body("data.size()", greaterThan(0)).
                body("data.id", everyItem(notNullValue())).
                body("data.name", everyItem(notNullValue())).
                body("data.id", everyItem(greaterThan(0)))
                .log().body();
    }

    @Test
    public void updateUser(){
        String endpoint = "https://gorest.co.in/public/v1/users/2126";
        username = RandomStringUtils.random(10, true, true);
        String rawBody = """
                {
                "name":"${Username}",
                "email":"${Username}@email.com",
                "status":"active"
                }
                """;
        String body = rawBody.replace("${Username}", username);
        var response =
                given().headers("Authorization","Bearer " + bearerToken,"Content-Type",ContentType.JSON,"Accept",ContentType.JSON)
                        .body(body)
                .when()
                        .patch(endpoint)
                .then();
        response.log().body();

        given().when().get("https://gorest.co.in/public/v1/users/2126")
                .then().log().headers()
                .assertThat().statusCode(200)
                .log().body()
                .assertThat()
                .body("data.name", equalTo(username));
    }

    @Test
    public void deleteUser(){
        baseURI = "https://gorest.co.in/public/v1/users";
        RequestSpecification httpRequest = given();
        JsonPath jsonPathEvaluator = httpRequest.get("").jsonPath();
        List<Integer> allUsersIds = jsonPathEvaluator.getList("data.id");

        var firstId = allUsersIds.get(0);

        System.out.println("First Id extracted is: " + firstId);

        var response =  given().headers("Authorization","Bearer " + bearerToken,"Content-Type",ContentType.JSON,"Accept",ContentType.JSON)
                                        .when().delete(baseURI + "/" + firstId)
                                        .then();
//        response.log().body();
//        response.log().headers();
//        Assertions.assertTrue(response.log().headers().toString().contains("No Content"));
    }






}
