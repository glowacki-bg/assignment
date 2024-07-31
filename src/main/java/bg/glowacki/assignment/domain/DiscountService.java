package bg.glowacki.assignment.domain;

public interface DiscountService {

    CalculatedPrice calculateTotalPrice(Order order);
}
