package bg.glowacki.assignment.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PercentageDiscountServiceTest {

    @InjectMocks
    private PercentageDiscountService discountService;

    @Mock
    private DiscountConfig discountConfig;

    @BeforeEach
    void initMocks() {
        lenient().when(discountConfig.percentage()).thenReturn(
                List.of(
                        new DiscountConfig.Percentage(10, 3),
                        new DiscountConfig.Percentage(50, 5)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("percentageArguments")
    void calculateTotalPriceByPercentage(Order order, CalculatedPrice expectedResult) {
        CalculatedPrice calculatedPrice = discountService.calculateTotalPrice(order);
        assertThat(calculatedPrice).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> percentageArguments() {
        return Stream.of(
                Arguments.of(Order.builder()
                                .productId(UUID.randomUUID())
                                .amount(1)
                                .unitPrice(BigDecimal.ONE)
                                .build(),
                        new CalculatedPrice(new BigDecimal("1.00"))),
                Arguments.of(Order.builder()
                                .productId(UUID.randomUUID())
                                .amount(10)
                                .unitPrice(BigDecimal.TEN)
                                .build(),
                        new CalculatedPrice(new BigDecimal("97.00"))),
                Arguments.of(Order.builder()
                                .productId(UUID.randomUUID())
                                .amount(50)
                                .unitPrice(BigDecimal.TEN)
                                .build(),
                        new CalculatedPrice(new BigDecimal("475.00"))),
                Arguments.of(Order.builder()
                                .productId(UUID.randomUUID())
                                .amount(51)
                                .unitPrice(new BigDecimal("0.01"))
                                .build(),
                        new CalculatedPrice(new BigDecimal("0.48")))
        );
    }
}