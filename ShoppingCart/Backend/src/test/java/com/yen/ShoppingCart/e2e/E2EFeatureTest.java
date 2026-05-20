package com.yen.ShoppingCart.e2e;

import com.yen.ShoppingCart.model.Category;
import com.yen.ShoppingCart.model.dto.cart.AddToCartDto;
import com.yen.ShoppingCart.model.dto.product.ProductDto;
import com.yen.ShoppingCart.model.dto.user.SignInDto;
import com.yen.ShoppingCart.model.dto.user.SignInResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignupDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end feature tests covering every API surface of the ShoppingCart backend.
 *
 * Infrastructure: H2 in-memory DB (via application-test.properties) + in-memory
 * ConcurrentMapCacheManager (via TestCacheConfig). No MySQL or Redis required.
 *
 * Coverage:
 *  - User: signup, sign-in, profile, duplicate-email error, invalid-token error
 *  - Category: create, list, update, duplicate error
 *  - Product: add, list (cache primed), update (cache evicted), fresh list, invalid-category error
 *  - Cart: add, list, update quantity, delete, not-found error
 *  - WishList: add, get
 *  - Search: full-text product search
 *  - Order: place order, list, get by ID, not-found error
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestCacheConfig.class)
class E2EFeatureTest {

    /** Prevents Spring Boot from attempting a real Redis TCP connection. */
    @MockBean
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void clearCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            var c = cacheManager.getCache(name);
            if (c != null) c.clear();
        });
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private String uniqueEmail() {
        return "e2e-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10) + "@test.com";
    }

    private String signUpAndGetToken(String email, String password) {
        SignupDto signup = new SignupDto("Test", "User", email, password);
        restTemplate.postForEntity("/user/signup", signup, Map.class);
        SignInDto signIn = new SignInDto(email, password);
        ResponseEntity<SignInResponseDto> resp =
                restTemplate.postForEntity("/user/signIn", signIn, SignInResponseDto.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        return resp.getBody().getToken();
    }

    /** Creates a unique category and returns its DB-generated ID. */
    private Integer createCategoryAndGetId() {
        String name = "TestCat-" + UUID.randomUUID().toString().substring(0, 6);
        restTemplate.postForEntity("/category/create",
                new Category(name, "desc", "img.png"), Map.class);
        List<Map<String, Object>> cats =
                (List<Map<String, Object>>) restTemplate.getForEntity("/category/", List.class).getBody();
        return cats.stream()
                .filter(c -> name.equals(c.get("categoryName")))
                .map(c -> (Integer) c.get("id"))
                .findFirst().orElseThrow(() -> new AssertionError("category not saved: " + name));
    }

    /** Adds a product to the given category and returns its DB-generated ID. */
    private Integer createProductAndGetId(Integer categoryId) {
        String name = "TestProd-" + UUID.randomUUID().toString().substring(0, 6);
        restTemplate.postForEntity("/product/add",
                new ProductDto(name, "img.jpg", 19.99, "desc", categoryId), Map.class);
        // cache was just evicted by addProduct, so next read hits DB
        List<Map<String, Object>> prods =
                (List<Map<String, Object>>) restTemplate.getForEntity("/product/", List.class).getBody();
        return prods.stream()
                .filter(p -> name.equals(p.get("name")))
                .map(p -> (Integer) p.get("id"))
                .findFirst().orElseThrow(() -> new AssertionError("product not saved: " + name));
    }

    // ── User ─────────────────────────────────────────────────────────────────

    @Test
    void signup_thenSignIn_returnsToken() {
        String email = uniqueEmail();

        ResponseEntity<Map> signupResp =
                restTemplate.postForEntity("/user/signup",
                        new SignupDto("Alice", "Smith", email, "pass123"), Map.class);
        assertThat(signupResp.getStatusCode().is2xxSuccessful()).isTrue();

        ResponseEntity<SignInResponseDto> signInResp =
                restTemplate.postForEntity("/user/signIn",
                        new SignInDto(email, "pass123"), SignInResponseDto.class);
        assertThat(signInResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(signInResp.getBody().getToken()).isNotBlank();
        assertThat(signInResp.getBody().getStatus()).isEqualTo("success");
    }

    @Test
    void signup_duplicateEmail_returnsError() {
        String email = uniqueEmail();
        SignupDto signup = new SignupDto("Bob", "Jones", email, "pass");
        restTemplate.postForEntity("/user/signup", signup, Map.class);
        ResponseEntity<Map> dup = restTemplate.postForEntity("/user/signup", signup, Map.class);
        assertThat(dup.getStatusCode().isError()).isTrue();
    }

    @Test
    void getUserProfile_withValidToken_returnsEmail() {
        String email = uniqueEmail();
        String token = signUpAndGetToken(email, "pass");
        ResponseEntity<Map> resp =
                restTemplate.getForEntity("/user/userProfile?token=" + token, Map.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody().get("email")).isEqualTo(email);
    }

    @Test
    void authEndpoint_withInvalidToken_returnsError() {
        ResponseEntity<Map> resp =
                restTemplate.getForEntity("/cart/?token=totally-invalid-token-xyz", Map.class);
        assertThat(resp.getStatusCode().isError()).isTrue();
    }

    // ── Category ─────────────────────────────────────────────────────────────

    @Test
    void category_createListUpdate() {
        String name = "Cat-" + UUID.randomUUID().toString().substring(0, 6);

        ResponseEntity<Map> createResp = restTemplate.postForEntity("/category/create",
                new Category(name, "Electronics", "img.png"), Map.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp.getBody().get("success")).isEqualTo(true);

        List<Map<String, Object>> cats =
                (List<Map<String, Object>>) restTemplate.getForEntity("/category/", List.class).getBody();
        Integer catId = cats.stream()
                .filter(c -> name.equals(c.get("categoryName")))
                .map(c -> (Integer) c.get("id"))
                .findFirst().orElseThrow(() -> new AssertionError("category missing from list"));

        ResponseEntity<Map> updateResp = restTemplate.postForEntity(
                "/category/update/" + catId,
                new Category("Updated-" + name, "updated-desc", "new.png"), Map.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResp.getBody().get("success")).isEqualTo(true);
    }

    @Test
    void category_duplicate_returnsConflict() {
        String name = "DupCat-" + UUID.randomUUID().toString().substring(0, 6);
        Category cat = new Category(name, "desc", "img.png");
        restTemplate.postForEntity("/category/create", cat, Map.class);
        ResponseEntity<Map> dup = restTemplate.postForEntity("/category/create", cat, Map.class);
        assertThat(dup.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(dup.getBody().get("success")).isEqualTo(false);
    }

    // ── Product ──────────────────────────────────────────────────────────────

    @Test
    void product_addListUpdate_cacheEvictedOnUpdate() {
        Integer catId = createCategoryAndGetId();
        String name = "Prod-" + UUID.randomUUID().toString().substring(0, 6);

        ResponseEntity<Map> addResp = restTemplate.postForEntity("/product/add",
                new ProductDto(name, "img.jpg", 29.99, "a description", catId), Map.class);
        assertThat(addResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // First GET — populates cache
        ResponseEntity<List> list1 = restTemplate.getForEntity("/product/", List.class);
        assertThat(list1.getStatusCode()).isEqualTo(HttpStatus.OK);
        int countBefore = list1.getBody().size();

        // Second GET — served from cache
        ResponseEntity<List> list2 = restTemplate.getForEntity("/product/", List.class);
        assertThat(list2.getBody().size()).isEqualTo(countBefore);

        // Find the product ID
        Integer prodId = ((List<Map<String, Object>>) list1.getBody()).stream()
                .filter(p -> name.equals(p.get("name")))
                .map(p -> (Integer) p.get("id"))
                .findFirst().orElseThrow();

        // Update — should evict cache
        ResponseEntity<Map> updateResp = restTemplate.postForEntity(
                "/product/update/" + prodId,
                new ProductDto("Updated-" + name, "new.jpg", 39.99, "updated", catId), Map.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // GET after eviction — fresh data with updated name
        List<Map<String, Object>> freshList =
                (List<Map<String, Object>>) restTemplate.getForEntity("/product/", List.class).getBody();
        assertThat(freshList.stream().anyMatch(p -> ("Updated-" + name).equals(p.get("name")))).isTrue();
    }

    @Test
    void product_add_withInvalidCategory_returnsConflict() {
        ResponseEntity<Map> resp = restTemplate.postForEntity("/product/add",
                new ProductDto("BadProd", "img.jpg", 9.99, "desc", 99999), Map.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(resp.getBody().get("success")).isEqualTo(false);
    }

    // ── Cart ─────────────────────────────────────────────────────────────────

    @Test
    void cart_addListUpdateDelete() {
        String email = uniqueEmail();
        String token = signUpAndGetToken(email, "pass");
        Integer catId = createCategoryAndGetId();
        Integer prodId = createProductAndGetId(catId);

        // Add
        ResponseEntity<Map> addResp = restTemplate.postForEntity(
                "/cart/add?token=" + token,
                new AddToCartDto(null, prodId, 2), Map.class);
        assertThat(addResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // List — verify item and quantity
        ResponseEntity<Map> listResp =
                restTemplate.getForEntity("/cart/?token=" + token, Map.class);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Map<String, Object>> items =
                (List<Map<String, Object>>) listResp.getBody().get("cartItems");
        assertThat(items).hasSize(1);
        assertThat(items.get(0).get("quantity")).isEqualTo(2);
        Integer cartItemId = (Integer) items.get(0).get("id");

        // Update quantity to 5
        ResponseEntity<Map> updateResp = restTemplate.exchange(
                "/cart/update/" + cartItemId + "?token=" + token,
                HttpMethod.PUT,
                new HttpEntity<>(new AddToCartDto(cartItemId, prodId, 5)),
                Map.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Map<String, Object>> afterUpdate =
                (List<Map<String, Object>>) restTemplate
                        .getForEntity("/cart/?token=" + token, Map.class)
                        .getBody().get("cartItems");
        assertThat(afterUpdate.get(0).get("quantity")).isEqualTo(5);

        // Delete
        ResponseEntity<Map> deleteResp = restTemplate.exchange(
                "/cart/delete/" + cartItemId + "?token=" + token,
                HttpMethod.DELETE, null, Map.class);
        assertThat(deleteResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Cart must be empty
        List<Map<String, Object>> empty =
                (List<Map<String, Object>>) restTemplate
                        .getForEntity("/cart/?token=" + token, Map.class)
                        .getBody().get("cartItems");
        assertThat(empty).isEmpty();
    }

    @Test
    void cart_deleteNonExistentItem_returnsError() {
        String email = uniqueEmail();
        String token = signUpAndGetToken(email, "pass");
        ResponseEntity<Map> resp = restTemplate.exchange(
                "/cart/delete/99999?token=" + token,
                HttpMethod.DELETE, null, Map.class);
        assertThat(resp.getStatusCode().isError()).isTrue();
    }

    // ── WishList ─────────────────────────────────────────────────────────────

    @Test
    void wishlist_addAndGet() {
        String email = uniqueEmail();
        String token = signUpAndGetToken(email, "pass");
        Integer catId = createCategoryAndGetId();
        Integer prodId = createProductAndGetId(catId);

        Map<String, Object> productBody = new HashMap<>();
        productBody.put("id", prodId);
        ResponseEntity<Map> addResp = restTemplate.postForEntity(
                "/wishlist/add?token=" + token, productBody, Map.class);
        assertThat(addResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<List> getResp =
                restTemplate.getForEntity("/wishlist/" + token, List.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody()).hasSize(1);
    }

    // ── Search ───────────────────────────────────────────────────────────────

    @Test
    void search_productsByName_returnsMatches() {
        Integer catId = createCategoryAndGetId();
        String uniqueName = "Searchable-" + UUID.randomUUID().toString().substring(0, 6);
        restTemplate.postForEntity("/product/add",
                new ProductDto(uniqueName, "img.jpg", 9.99, "desc", catId), Map.class);

        ResponseEntity<List> resp =
                restTemplate.getForEntity("/search/api?query=" + uniqueName, List.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotEmpty();
    }

    // ── Orders ───────────────────────────────────────────────────────────────

    @Test
    void order_placeThenListThenGetById() {
        String email = uniqueEmail();
        String token = signUpAndGetToken(email, "pass");

        // Place order (empty cart is allowed — creates a $0 order)
        ResponseEntity<Map> placeResp = restTemplate.exchange(
                "/order/add?token=" + token + "&sessionId=dummy-session-e2e",
                HttpMethod.POST, HttpEntity.EMPTY, Map.class);
        assertThat(placeResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // List orders
        ResponseEntity<List> listResp =
                restTemplate.getForEntity("/order/?token=" + token, List.class);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResp.getBody()).hasSize(1);

        Integer orderId =
                (Integer) ((Map<String, Object>) listResp.getBody().get(0)).get("id");

        // Get by ID
        ResponseEntity<Map> orderResp =
                restTemplate.getForEntity("/order/" + orderId + "?token=" + token, Map.class);
        assertThat(orderResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(orderResp.getBody().get("id")).isEqualTo(orderId);
    }

    @Test
    void order_getByNonExistentId_returns404() {
        String email = uniqueEmail();
        String token = signUpAndGetToken(email, "pass");
        ResponseEntity<Object> resp =
                restTemplate.getForEntity("/order/99999?token=" + token, Object.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void order_placeAndVerifyContainsCartItems() {
        String email = uniqueEmail();
        String token = signUpAndGetToken(email, "pass");
        Integer catId = createCategoryAndGetId();
        Integer prodId = createProductAndGetId(catId);

        // Add product to cart
        restTemplate.postForEntity("/cart/add?token=" + token,
                new AddToCartDto(null, prodId, 3), Map.class);

        // Place order — should capture cart items, then clear cart
        restTemplate.exchange("/order/add?token=" + token + "&sessionId=session-with-items",
                HttpMethod.POST, HttpEntity.EMPTY, Map.class);

        // Cart should be empty after order placement
        List<Map<String, Object>> cartAfterOrder =
                (List<Map<String, Object>>) restTemplate
                        .getForEntity("/cart/?token=" + token, Map.class)
                        .getBody().get("cartItems");
        assertThat(cartAfterOrder).isEmpty();

        // Order should have non-zero total price
        ResponseEntity<List> orders =
                restTemplate.getForEntity("/order/?token=" + token, List.class);
        Map<String, Object> order = (Map<String, Object>) orders.getBody().get(0);
        assertThat((Number) order.get("totalPrice")).matches(p -> p.doubleValue() > 0);
    }
}
