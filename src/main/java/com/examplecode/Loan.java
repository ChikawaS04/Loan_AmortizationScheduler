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

    public Loan(long loanID, BigDecimal principal, BigDecimal apr, int term, BigDecimal monthlyPayment, BigDecimal balance) {
        this.loanID = loanID;
        this.principal = principal;
        this.apr = apr;
        this.term = term;
        this.balance = principal;
        this.monthlyPayment = monthlyPayment;
    }

    public void applyMonthlyPayment(BigDecimal principal, BigDecimal apr, int term, BigDecimal monthlyPayment){
        BigDecimal monthlyRate = apr.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP); // the monthly rate is denoted as r
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRtoN = onePlusR.pow(term);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRtoN);
        BigDecimal denominator = onePlusRtoN.subtract(BigDecimal.ONE);
    }
}
