package com.examplecode;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Loan {
    private final long loanID;
    private final BigDecimal principal;
    private final BigDecimal apr;
    private final BigDecimal monthlyRate;
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

    public Loan(long loanID, String principal, String apr, int termYears) {
        this.loanID = loanID;
        this.principal = new BigDecimal(principal);
        this.apr = new BigDecimal(apr);
        this.monthlyRate = new BigDecimal(apr).divide(BigDecimal.valueOf(1200), 15, RoundingMode.HALF_UP);
        this.term = termYears * 12; // convert years to months once, used everywhere
        this.balance = new BigDecimal(principal);
        this.monthlyPayment = calculateMonthlyPayment();
    }

    private BigDecimal calculateMonthlyPayment(){
        //Formula for monthly payment: pmt = P[r(1+r)^n] / [(1+r)^n - 1]

        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRtoN = onePlusR.pow(term);
        BigDecimal numerator = monthlyRate.multiply(onePlusRtoN);
        BigDecimal denominator = onePlusRtoN.subtract(BigDecimal.ONE);
        BigDecimal fraction = numerator.divide(denominator, 15, RoundingMode.HALF_UP);
        return principal.multiply(fraction).setScale(2, RoundingMode.HALF_UP);
    }

    public PaymentResult applyMonthlyPayment(){
        BigDecimal monthlyInterestPortion = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthlyPrincipalPortion = monthlyPayment.subtract(monthlyInterestPortion);
        // Last payment adjustment to ensure balance is zero
        if(balance.subtract(monthlyPrincipalPortion).abs().compareTo(new BigDecimal("1.00")) < 0){
            monthlyPrincipalPortion = balance;
        }
        balance = balance.subtract(monthlyPrincipalPortion);
        return new PaymentResult(monthlyInterestPortion, monthlyPrincipalPortion, balance);
    }
}