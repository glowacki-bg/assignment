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
class AmountDiscountServiceTest {

    @InjectMocks
    private AmountDiscountService discountService;

    @Mock
    private DiscountConfig discountConfig;

    @BeforeEach
    void initMocks() {
        lenient().when(discountConfig.amount()).thenReturn(
                List.of(
                        new DiscountConfig.Amount(10, BigDecimal.valueOf(2)),
                        new DiscountConfig.Amount(100, BigDecimal.valueOf(5))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("amountArguments")
    void calculateTotalPriceByAmount(Order order, CalculatedPrice expectedResult) {
        CalculatedPrice calculatedPrice = discountService.calculateTotalPrice(order);
        assertThat(calculatedPrice).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> amountArguments() {
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
                        new CalculatedPrice(new BigDecimal("98.00"))),
                Arguments.of(Order.builder()
                                .productId(UUID.randomUUID())
                                .amount(100)
                                .unitPrice(BigDecimal.TEN)
                                .build(),
                        new CalculatedPrice(new BigDecimal("995.00"))),
                Arguments.of(Order.builder()
                                .productId(UUID.randomUUID())
                                .amount(100)
                                .unitPrice(new BigDecimal("0.01"))
                                .build(),
                        new CalculatedPrice(BigDecimal.ZERO))
        );
    }
}