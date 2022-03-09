package tests;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DemowebshopTests {
    @Test
    void addToWishList() {
        Integer wishList = 25;
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=6e0ebc9d-8452-4d2b-bc33-fff42f2bcfd1; ARRAffinity=a1e87db3fa424e3b31370c78def315779c40ca259e77568bef2bb9544f63134e; __utma=78382081.1719492091.1646497672.1646497672.1646497672.1; __utmc=78382081; __utmz=78382081.1646497672.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmt=1; _atshc={\"http://demowebshop.tricentis.com/141-inch-laptop\":74,\"http://demowebshop.tricentis.com/simple-computer\":9}; nop.CompareProducts=CompareProductIds=75&CompareProductIds=79; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=14&RecentlyViewedProductIds=75&RecentlyViewedProductIds=31&RecentlyViewedProductIds=20; __atuvc=9%7C9; __atuvs=6223912dab85ecf7008; __utmb=78382081.27.10.1646497672; ARRAffinity=a1e87db3fa424e3b31370c78def315779c40ca259e77568bef2bb9544f63134e; Nop.customer=040e7d09-cc58-477d-ac74-c91b16388543")
                .body("addtocart_14.EnteredQuantity=1")
                .when()
                .log().all()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/14/2")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "\u003ca href=\"/wishlist\"\u003ewishlist\u003c/a\u003e"))
                .body("updatetopwishlistsectionhtml", is("(" + wishList + ")"));
    }

    @Test
    void addToWishListWithoutCookie() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("addtocart_14.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/53/2")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is("(1)"));
    }
}
