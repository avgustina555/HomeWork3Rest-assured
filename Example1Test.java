package HomeWork3;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Example1Test<getApiKey, id> extends AbstractTest {
    @Test
    void getExampleTest() {
                given()
                .when()
                .request(Method.GET,getBaseUrl()+"recipes/715538/information?" +
                        "includeIngredients={Ingredients}&apiKey={apiKey}", false, getApiKey())
                .then()
                .statusCode(200);

        given()
                .when()
                .request(Method.GET,getBase1Url()+"recipes/716429/information?" +
                        "includeNutrition={Nutrition}&apiKey={apiKey}", false, getApiKey())
                .then()
                .statusCode(200);
    }


    @Test
    void getSpecifyingRequestDataTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeIngredients", "false")
                .pathParam("id", 715538)
                .when()
                .get(getBaseUrl()+"recipes/{id}/information")
                .then()
                .statusCode(200);


        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title","Pork roast with green beans")
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .then()
                .statusCode(200);


        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title","Sesame flank steak salad")
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .then()
                .statusCode(200);

        Cookie someCookie = new Cookie
                .Builder("some_cookie", "some_value")
                .setSecured(true)
                .setComment("some comment")
                .build();


        given().cookie("username","max")
                .cookie(someCookie)
                .when()
                .get(getBaseUrl()+"recipes/715538/information?" +
                        "includeNutrition=false&apiKey=" +getApiKey())
                .then()
                .statusCode(200);

            }


    @Test
    void getResponseData(){
        Response response = given()
                .when()
                .get(getBaseUrl()+"recipes/715538/information?" +
                        "includeNutrition=false&apiKey=" +getApiKey());


        System.out.println("Content-Encoding: " + response.getHeader("Content-Encoding"));

        Map<String, String> allCookies = response.getCookies();


        System.out.println("StatusLine: " + response.getStatusLine());


        String cuisine = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .path("cuisine");

        System.out.println("cuisine: " + cuisine);

        response = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .then().extract().response();


    }

    @Test
    void getVerifyingResponseData(){

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/715538/information")
                .body()
                .jsonPath();
        assertThat(response.get("vegetarian"), is(false));
        assertThat(response.get("license"), equalTo("CC BY-SA 3.0"));
        assertThat(response.get("pricePerServing"), equalTo(163.15F));
        assertThat(response.get("extendedIngredients[0].aisle"), equalTo("Milk, Eggs, Other Dairy"));


        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/715538/information")
                .then()
                .assertThat()
                //.cookie("cookieName", "cookieValue")
                .statusCode(200)
                .statusLine(containsString("OK"))
                .header("Connection", "keep-alive")
                .header("Content-Length", Integer::parseInt, lessThan(3000))
                .time(lessThan(600L));


    }

           given()
                .queryParam("hash", "0ecb5e7c58a240c8b47b23dc593a75cf")
                .queryParam("apiKey", getApiKey())
                .delete("https://api.spoonacular.com/" + id)
                .then()
                .statusCode(200);
    }




}
