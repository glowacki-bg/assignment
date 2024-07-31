package bg.glowacki.assignment.domain;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

@RequiredArgsConstructor
public class AmountDiscountService implements DiscountService {

    private final DiscountConfig discountConfig;

    /**
     * Calculates amount-based discount using calculation input data and discount configuration for given amount threshold
     * <p>
     * NOTE1: returns 0 if discount is greater than non-discounted price
     * <p>
     * NOTE2: always rounds discount down
     *
     * @param order calculation input
     * @return {@link CalculatedPrice} for given calculation input and amount-based discount configuration
     */
    public CalculatedPrice calculateTotalPrice(Order order) {
        DiscountConfig.Amount amountConfig = getAmountConfig(order);
        BigDecimal nonDiscountedTotalPrice = order.unitPrice().multiply(BigDecimal.valueOf(order.amount()));
        BigDecimal discount = amountConfig.cashDiscount();
        BigDecimal discountedTotalPrice = nonDiscountedTotalPrice.subtract(discount).setScale(2, RoundingMode.DOWN);
        if (discountedTotalPrice.compareTo(BigDecimal.ZERO) < 0) {
            discountedTotalPrice = BigDecimal.ZERO;
        }
        return new CalculatedPrice(discountedTotalPrice);
    }

    private DiscountConfig.Amount getAmountConfig(Order order) {
        return discountConfig.amount()
                .stream()
                .sorted(Comparator.comparingInt(DiscountConfig.Amount::items).reversed())
                .filter(amount -> amount.items() <= order.amount())
                .findFirst()
                .orElse(DiscountConfig.Amount.EMPTY);
    }
}
