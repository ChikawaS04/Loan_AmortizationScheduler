package com.examplecode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a loan with amortization capabilities.
 * Handles loan calculations including monthly payments, interest, and principal allocation.
 */
public class Loan {
    private final long loanID;                  // Unique identifier for the loan
    private final BigDecimal principal;          // Original loan amount
    private final BigDecimal apr;                // Annual percentage rate (APR)
    private final BigDecimal monthlyRate;        // Monthly interest rate (APR / 1200)
    private final int term;                      // Loan term in months
    private final BigDecimal monthlyPayment;     // Fixed monthly payment amount
    private BigDecimal balance;                  // Current outstanding balance

    public long getLoanID() {
        return loanID;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getApr() {
        return apr;
    }

    public int getTerm() {
        return term;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Constructs a new Loan object with the specified parameters.
     *
     * @param loanID     Unique identifier for the loan
     * @param principal  Loan principal amount as a string (for precision)
     * @param apr        Annual percentage rate as a string (for precision)
     * @param termYears  Loan term in years (will be converted to months)
     */
    public Loan(long loanID, String principal, String apr, int termYears) {
        this.loanID = loanID;
        this.principal = new BigDecimal(principal);
        this.apr = new BigDecimal(apr);
        this.monthlyRate = new BigDecimal(apr).divide(BigDecimal.valueOf(1200), 15, RoundingMode.HALF_UP);
        this.term = termYears * 12; // convert years to months once, used everywhere
        this.balance = new BigDecimal(principal);
        this.monthlyPayment = calculateMonthlyPayment();
    }

    /**
     * Calculates the fixed monthly payment amount using the standard amortization formula.
     * Formula: pmt = P[r(1+r)^n] / [(1+r)^n - 1]
     * where P = principal, r = monthly rate, n = term in months
     *
     * @return The fixed monthly payment amount
     */
    private BigDecimal calculateMonthlyPayment(){
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRtoN = onePlusR.pow(term);
        BigDecimal numerator = monthlyRate.multiply(onePlusRtoN);
        BigDecimal denominator = onePlusRtoN.subtract(BigDecimal.ONE);
        BigDecimal fraction = numerator.divide(denominator, 15, RoundingMode.HALF_UP);
        return principal.multiply(fraction).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Applies a monthly payment to the loan, calculating interest and principal portions.
     * Updates the loan balance and handles final payment adjustments to ensure zero balance.
     *
     * @return PaymentResult containing interest portion, principal portion, and new balance
     */
    public PaymentResult applyMonthlyPayment(){
        // Calculate interest portion based on current balance
        BigDecimal monthlyInterestPortion = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthlyPrincipalPortion = monthlyPayment.subtract(monthlyInterestPortion);

        // Last payment adjustment to ensure balance is zero (handles rounding differences)
        if(balance.subtract(monthlyPrincipalPortion).abs().compareTo(new BigDecimal("1.00")) < 0){
            monthlyPrincipalPortion = balance;
        }

        balance = balance.subtract(monthlyPrincipalPortion);
        return new PaymentResult(monthlyInterestPortion, monthlyPrincipalPortion, balance);
    }
}