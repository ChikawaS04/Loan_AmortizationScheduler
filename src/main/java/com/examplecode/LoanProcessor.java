package com.examplecode;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class LoanProcessor implements Runnable{

    private final Loan loan;

    public LoanProcessor(Loan loan) {
        this.loan = loan;
    }

    static AtomicReference<BigDecimal> totalCollected;

    @Override
    public void run(){
        for(int i = 1; i <= loan.getTerm(); i++){
        }
    }
}
