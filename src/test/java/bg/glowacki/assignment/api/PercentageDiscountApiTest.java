package bg.glowacki.assignment.api;

import bg.glowacki.assignment.api.model.DiscountRequest;
import bg.glowacki.assignment.api.model.DiscountResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:percentage.properties")
class PercentageDiscountApiTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @ParameterizedTest
    @MethodSource("happyPathArguments")
    void happyPaths(DiscountRequest request, DiscountResponse expectedResponse) {

        ResponseEntity<DiscountResponse> responseEntity = testRestTemplate.postForEntity(
                "/discounts",
                request,
                DiscountResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse);
    }

    public static Stream<Arguments> happyPathArguments() {
        return Stream.of(
                Arguments.of(DiscountRequest.builder()
                                .productId(UUID.randomUUID())
                                .amount(1)
                                .unitPrice(BigDecimal.ONE)
                                .build(),
                        new DiscountResponse(new BigDecimal("1.00"))),
                Arguments.of(DiscountRequest.builder()
                                .productId(UUID.randomUUID())
                                .amount(10)
                                .unitPrice(BigDecimal.TEN)
                                .build(),
                        new DiscountResponse(new BigDecimal("97.00"))),
                Arguments.of(DiscountRequest.builder()
                                .productId(UUID.randomUUID())
                                .amount(50)
                                .unitPrice(BigDecimal.TEN)
                                .build(),
                        new DiscountResponse(new BigDecimal("475.00"))),
                Arguments.of(DiscountRequest.builder()
                                .productId(UUID.randomUUID())
                                .amount(51)
                                .unitPrice(new BigDecimal("0.01"))
                                .build(),
                        new DiscountResponse(new BigDecimal("0.48")))
        );
    }
}
