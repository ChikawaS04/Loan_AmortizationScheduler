package com.examplecode;

import java.math.BigDecimal;

/**
 * Record representing the result of applying a monthly payment to a loan.
 *
 * @param interestPortion  The portion of the payment allocated to interest
 * @param principalPortion The portion of the payment allocated to principal
 * @param newBalance       The remaining loan balance after the payment
 */
public record PaymentResult(
        BigDecimal interestPortion,
        BigDecimal principalPortion,
        BigDecimal newBalance) {
}
