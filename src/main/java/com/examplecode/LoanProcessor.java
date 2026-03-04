package com.examplecode;

import java.math.BigDecimal;

public class LoanProcessor implements Runnable{

    private final Loan loan;

    public LoanProcessor(Loan loan) {
        this.loan = loan;
    }

    @Override
    public void run(){

        for(int i = 1; i <= loan.getTerm(); i++){
            loan.applyMonthlyPayment();
        }
    }
}
