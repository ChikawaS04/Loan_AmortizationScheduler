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
        System.out.printf("Loan %d Total Paid: $%.2f%n", newLoan.getLoanID(), t1.getTotalPaid());
        System.out.printf("Loan %d Total Paid: $%.2f%n", newLoan2.getLoanID(), t2.getTotalPaid());
        System.out.printf("Loan %d Total Paid: $%.2f%n", newLoan3.getLoanID(), t3.getTotalPaid());
    }
}