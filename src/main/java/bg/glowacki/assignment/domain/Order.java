package bg.glowacki.assignment.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record Order(
        @NotNull UUID productId,
        @NotNull @Min(1) int amount,
        @NotNull @DecimalMin("0.01") @Digits(integer = 10, fraction = 2) BigDecimal unitPrice
) {
}
