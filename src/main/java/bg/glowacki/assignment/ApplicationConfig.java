package bg.glowacki.assignment;

import bg.glowacki.assignment.domain.AmountDiscountService;
import bg.glowacki.assignment.domain.DiscountService;
import bg.glowacki.assignment.domain.DiscountConfig;
import bg.glowacki.assignment.domain.PercentageDiscountService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    @ConditionalOnProperty(value="discount.type", havingValue = "AMOUNT")
    public DiscountService amountDiscountService(DiscountConfig discountConfig) {
        return new AmountDiscountService(discountConfig);
    }

    @Bean
    @ConditionalOnProperty(value="discount.type", havingValue = "PERCENTAGE")
    public DiscountService percentageDiscountService(DiscountConfig discountConfig) {
        return new PercentageDiscountService(discountConfig);
    }
}
