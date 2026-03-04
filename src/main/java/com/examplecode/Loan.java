package com.examplecode;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Loan {
    private final long loanID;
    private final BigDecimal principal;
    private final BigDecimal apr;
    private final int term;
    private final BigDecimal monthlyPayment;
    private BigDecimal balance;

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

    public Loan(long loanID, String principal, String apr, int term) {
        this.loanID = loanID;
        this.principal = new BigDecimal(principal);
        this.apr = new BigDecimal(apr);
        this.term = term;
        this.balance = new BigDecimal(principal);
        this.monthlyPayment = calculateMonthlyPayment();
    }

    private BigDecimal calculateMonthlyPayment(){
        //Formula for monthly payment: pmt = P[r(1+r)^n] / [(1+r)^n - 1]

        BigDecimal monthlyRate = apr.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP); // the monthly rate can be denoted as r
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRtoN = onePlusR.pow(term);
        BigDecimal numerator = monthlyRate.multiply(onePlusRtoN);
        BigDecimal denominator = onePlusRtoN.subtract(BigDecimal.ONE);
        return principal.multiply(numerator.divide(denominator, 2, RoundingMode.HALF_UP));
    }

    public PaymentResult applyMonthlyPayment(){
        /*Formulas:
        * Monthly Interest Portion ($) = Principal * Monthly Rate (r)
        * Monthly Principal Portion ($) = PMT - Monthly Interest Portion
        * Remaining Balance = Balance - Monthly Principal Portion
        * */

        BigDecimal monthlyRate = apr.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyInterestPortion = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthlyPrincipalPortion = monthlyPayment.subtract(monthlyInterestPortion);

        // Last payment adjustment to ensure balance is zero
        if(balance.subtract(monthlyPrincipalPortion).abs().compareTo(new BigDecimal("0.01")) < 0){
            monthlyPrincipalPortion = balance;
        }
        balance = balance.subtract(monthlyPrincipalPortion);
        return new PaymentResult(monthlyInterestPortion, monthlyPrincipalPortion, balance);
    }
}
