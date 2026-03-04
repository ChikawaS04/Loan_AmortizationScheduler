package com.examplecode;

import java.math.BigDecimal;

/**
 * Processes loan payments over the full term of a loan.
 * Implements Runnable to allow concurrent processing of multiple loans.
 */
public class LoanProcessor implements Runnable{

    private final Loan loan;                     // The loan being processed
    private final CompanyLedger ledger;          // Shared ledger to record payments
    private BigDecimal totalPaid = BigDecimal.ZERO;  // Total amount paid for this loan

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    /**
     * Constructs a new LoanProcessor.
     *
     * @param loan   The loan to be processed
     * @param ledger The shared ledger for recording payments
     */
    public LoanProcessor(Loan loan, CompanyLedger ledger) {
        this.loan = loan;
        this.ledger = ledger;
    }

    /**
     * Processes all monthly payments for the loan's term.
     * Called when the processor runs as a thread. Calculates and displays
     * the amortization schedule for each payment period.
     */
    @Override
    public void run(){
        // Process each monthly payment over the full term
        for(int i = 1; i <= loan.getTerm(); i++){
            PaymentResult result = loan.applyMonthlyPayment();
            ledger.addPayment(loan.getMonthlyPayment());
            totalPaid = totalPaid.add(result.principalPortion().add(result.interestPortion()));

            // Display payment details for this month
            System.out.printf("Loan ID: %d - Month %d: Interest=$%.2f, Principal=$%.2f, Remaining Balance=$%.2f%n",
                    loan.getLoanID(), i, result.interestPortion(), result.principalPortion(), result.newBalance());
        }
    }
}
