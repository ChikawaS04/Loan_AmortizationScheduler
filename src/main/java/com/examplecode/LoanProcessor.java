package com.examplecode;

import java.math.BigDecimal;

public class LoanProcessor implements Runnable{

    private final Loan loan;
    private final CompanyLedger ledger;

    public LoanProcessor(Loan loan, CompanyLedger ledger) {
        this.loan = loan;
        this.ledger = ledger;
    }

    @Override
    public void run(){
        for(int i = 1; i <= loan.getTerm(); i++){
            PaymentResult result = loan.applyMonthlyPayment();
            ledger.addPayment(loan.getMonthlyPayment());

            System.out.printf("Loan %d - Month %d: Interest=$%.2f, Principal=$%.2f, New Balance=$%.2f%n",
                    loan.getLoanID(), i, result.interestPortion(), result.principalPortion(), result.newBalance());
        }
    }
}
