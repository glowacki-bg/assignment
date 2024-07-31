package bg.glowacki.assignment.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.List;

@ConfigurationProperties(prefix = "discount")
public record DiscountConfig(
        DiscountType type,
        List<Amount> amount,
        List<Percentage> percentage
) {
    public record Amount(
            int items,
            BigDecimal cashDiscount
    ) {
        public static Amount EMPTY = new Amount(0, BigDecimal.ZERO);
    }

    public record Percentage(
            int items,
            int percentageDiscount
    ) {
        public static Percentage EMPTY = new Percentage(0, 0);
    }
}
