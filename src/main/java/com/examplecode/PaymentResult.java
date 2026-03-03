package com.examplecode;

import java.math.BigDecimal;

public record PaymentResult(
        BigDecimal interestPortion,
        BigDecimal principalPortion,
        BigDecimal newBalance) {
}
