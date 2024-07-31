package bg.glowacki.assignment.api;

import bg.glowacki.assignment.api.model.DiscountRequest;
import bg.glowacki.assignment.api.model.DiscountResponse;
import bg.glowacki.assignment.domain.CalculatedPrice;
import bg.glowacki.assignment.domain.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiscountRestEndpoint {

    private final DiscountService discountService;

    @PostMapping("/discounts")
    public ResponseEntity<DiscountResponse> calculateTotalPrice(@RequestBody @Valid DiscountRequest request) {
        CalculatedPrice calculatedPrice = discountService.calculateTotalPrice(request.toOrder());
        return ResponseEntity.ok(DiscountResponse.of(calculatedPrice));
    }
}
