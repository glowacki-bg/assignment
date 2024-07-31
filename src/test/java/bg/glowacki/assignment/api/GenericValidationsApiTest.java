package bg.glowacki.assignment.api;

import bg.glowacki.assignment.api.model.DiscountRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GenericValidationsApiTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @ParameterizedTest
    @MethodSource("invalidRequestArguments")
    void invalidRequestsTest(Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        ResponseEntity<Map<String, Object>> responseEntity = testRestTemplate.exchange(
                "/discounts",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<>() {
                });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public static Stream<Arguments> invalidRequestArguments() {
        return Stream.of(
                Arguments.of(DiscountRequest.builder()
                        .productId(UUID.randomUUID())
                        .amount(-1)
                        .unitPrice(BigDecimal.ONE)
                        .build()),
                Arguments.of(DiscountRequest.builder()
                        .productId(UUID.randomUUID())
                        .amount(1)
                        .unitPrice(BigDecimal.valueOf(-1))
                        .build()),
                Arguments.of(DiscountRequest.builder()
                        .productId(UUID.randomUUID())
                        .amount(1)
                        .unitPrice(BigDecimal.valueOf(1111111111111L))
                        .build()),
                Arguments.of(DiscountRequest.builder()
                        .amount(1)
                        .unitPrice(BigDecimal.ONE)
                        .build()),
                Arguments.of(DiscountRequest.builder()
                        .productId(UUID.randomUUID())
                        .unitPrice(BigDecimal.ONE)
                        .build()),
                Arguments.of(DiscountRequest.builder()
                        .productId(UUID.randomUUID())
                        .amount(1)
                        .build()),
                Arguments.of("""
                        {
                            "productId": "definitely-not-UUID",
                            "amount": 1,
                            "unitPrice: 0.35
                        }
                        """)
        );
    }
}
