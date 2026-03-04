package com.examplecode;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Loan newLoan = new Loan(474444774L, "900000", "5.5", 10);
        Loan newLoan2 = new Loan(346798493L, "50000", "5.5", 6);
        Loan newLoan3 = new Loan(908872356L, "239900", "5.5", 4);

        CompanyLedger davidsLedger = new CompanyLedger(BigDecimal.ZERO);

        LoanProcessor t1 = new LoanProcessor(newLoan, davidsLedger);
        LoanProcessor t2 = new LoanProcessor(newLoan2, davidsLedger);
        LoanProcessor t3 = new LoanProcessor(newLoan3, davidsLedger);

        Thread threadOne = new Thread(t1);
        Thread threadTwo = new Thread(t2);
        Thread threadThree = new Thread(t3);
        threadOne.start();
        threadTwo.start();
        threadThree.start();

        try {
            threadOne.join();
            threadTwo.join();
            threadThree.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print per-loan summary
        BigDecimal loan1Total = newLoan.getMonthlyPayment().multiply(BigDecimal.valueOf(newLoan.getTerm()));
        BigDecimal loan2Total = newLoan2.getMonthlyPayment().multiply(BigDecimal.valueOf(newLoan2.getTerm()));
        BigDecimal loan3Total = newLoan3.getMonthlyPayment().multiply(BigDecimal.valueOf(newLoan3.getTerm()));

        System.out.println("\n=== Loan Summary ===");
        System.out.printf("Loan %d Total Paid: $%.2f%n", newLoan.getLoanID(), loan1Total);
        System.out.printf("Loan %d Total Paid: $%.2f%n", newLoan2.getLoanID(), loan2Total);
        System.out.printf("Loan %d Total Paid: $%.2f%n", newLoan3.getLoanID(), loan3Total);
        System.out.printf("\nCompany Grand Total Collected: $%.2f%n", davidsLedger.getTotalCollected());
    }
}