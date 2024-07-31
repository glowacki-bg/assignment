package bg.glowacki.assignment.domain;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

@RequiredArgsConstructor
public class PercentageDiscountService implements DiscountService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final DiscountConfig discountConfig;

    /**
     * Calculates amount-based discount using calculation input data and discount configuration for given amount threshold
     * <p>
     * NOTE: always rounds discount down
     *
     * @param order calculation input
     * @return {@link CalculatedPrice} for given calculation input and amount-based discount configuration
     */
    public CalculatedPrice calculateTotalPrice(Order order) {
        DiscountConfig.Percentage percentageConfig = getPercentageConfig(order);
        BigDecimal nonDiscountedTotalPrice = order.unitPrice().multiply(BigDecimal.valueOf(order.amount()));
        BigDecimal fractionDiscount = BigDecimal.valueOf(percentageConfig.percentageDiscount()).divide(HUNDRED, 2, RoundingMode.HALF_DOWN);
        BigDecimal fractionOfPrice = BigDecimal.ONE.subtract(fractionDiscount);
        BigDecimal discountedTotalPrice = nonDiscountedTotalPrice.multiply(fractionOfPrice).setScale(2, RoundingMode.DOWN);
        return new CalculatedPrice(discountedTotalPrice);
    }

    private DiscountConfig.Percentage getPercentageConfig(Order order) {
        return discountConfig.percentage()
                .stream()
                .sorted(Comparator.comparingInt(DiscountConfig.Percentage::items).reversed())
                .filter(amount -> amount.items() <= order.amount())
                .findFirst()
                .orElse(DiscountConfig.Percentage.EMPTY);
    }
}
