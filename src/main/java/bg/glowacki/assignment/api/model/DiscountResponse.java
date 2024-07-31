package bg.glowacki.assignment.api.model;

import bg.glowacki.assignment.domain.CalculatedPrice;

import java.math.BigDecimal;

public record DiscountResponse(BigDecimal totalPrice) {
    public static DiscountResponse of(CalculatedPrice calculatedPrice) {
        return new DiscountResponse(calculatedPrice.totalPrice());
    }
}
