package com.examplecode;

import java.math.BigDecimal;

public class LoanProcessor implements Runnable{

    private final Loan loan;
    private final CompanyLedger ledger;
    private BigDecimal totalPaid = BigDecimal.ZERO;

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public LoanProcessor(Loan loan, CompanyLedger ledger) {
        this.loan = loan;
        this.ledger = ledger;
    }

    @Override
    public void run(){
        for(int i = 1; i <= loan.getTerm(); i++){
            PaymentResult result = loan.applyMonthlyPayment();
            ledger.addPayment(loan.getMonthlyPayment());
            totalPaid = totalPaid.add(result.principalPortion().add(result.interestPortion()));

            System.out.printf("Loan ID: %d - Month %d: Interest=$%.2f, Principal=$%.2f, Remaining Balance=$%.2f%n",
                    loan.getLoanID(), i, result.interestPortion(), result.principalPortion(), result.newBalance());
        }
    }
}
