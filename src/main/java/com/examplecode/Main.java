package com.examplecode;

import java.math.BigDecimal;

/**
 * Main application demonstrating concurrent loan processing and amortization schedule calculation.
 * Creates multiple loans and processes them in parallel threads while tracking payments in a shared ledger.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========== AMORTIZATION SCHEDULE ==========\n");

        // Initialize a shared ledger to track total collections across all loans
        CompanyLedger davidsLedger = new CompanyLedger(BigDecimal.ZERO);

        // Create three sample loans with different principals and terms (all at 5.5% APR)
        Loan newLoan = new Loan(474444774L, "900000", "5.5", 10);
        Loan newLoan2 = new Loan(346798493L, "50000", "5.5", 6);
        Loan newLoan3 = new Loan(908872356L, "239900", "5.5", 4);

        // Create loan processors to handle payment calculations for each loan
        LoanProcessor t1 = new LoanProcessor(newLoan, davidsLedger);
        LoanProcessor t2 = new LoanProcessor(newLoan2, davidsLedger);
        LoanProcessor t3 = new LoanProcessor(newLoan3, davidsLedger);

        // Process loans concurrently using separate threads
        Thread threadOne = new Thread(t1);
        Thread threadTwo = new Thread(t2);
        Thread threadThree = new Thread(t3);
        threadOne.start();
        threadTwo.start();
        threadThree.start();

        // Wait for all loan processing threads to complete
        try {
            threadOne.join();
            threadTwo.join();
            threadThree.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display summary information for all processed loans
        System.out.println("\n=== Monthly Payment Schedule ===");
        System.out.printf("Loan %-12d | Rate: %.2f%% | Term: %2d months | Monthly Payment: $%,.2f%n",
                newLoan.getLoanID(), newLoan.getApr(), newLoan.getTerm(), newLoan.getMonthlyPayment());
        System.out.printf("Loan %-12d | Rate: %.2f%% | Term: %2d months | Monthly Payment: $%,.2f%n",
                newLoan2.getLoanID(), newLoan2.getApr(), newLoan2.getTerm(), newLoan2.getMonthlyPayment());
        System.out.printf("Loan %-12d | Rate: %.2f%% | Term: %2d months | Monthly Payment: $%,.2f%n",
                newLoan3.getLoanID(), newLoan3.getApr(), newLoan3.getTerm(), newLoan3.getMonthlyPayment());

        System.out.println("\n=== Loan Summary ===");
        System.out.printf("Loan %-12d | Principal: $%,12.2f | Total Paid: $%,12.2f%n",
                newLoan.getLoanID(), newLoan.getPrincipal(), t1.getTotalPaid());
        System.out.printf("Loan %-12d | Principal: $%,12.2f | Total Paid: $%,12.2f%n",
                newLoan2.getLoanID(), newLoan2.getPrincipal(), t2.getTotalPaid());
        System.out.printf("Loan %-12d | Principal: $%,12.2f | Total Paid: $%,12.2f%n",
                newLoan3.getLoanID(), newLoan3.getPrincipal(), t3.getTotalPaid());

        System.out.printf("%nBank Total Collected: $%,15.2f%n", davidsLedger.getTotalCollected());
    }
}