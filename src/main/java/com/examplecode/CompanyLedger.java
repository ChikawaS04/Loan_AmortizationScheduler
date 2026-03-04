package com.examplecode;

import java.math.BigDecimal;

public class CompanyLedger {
    private BigDecimal totalCollected = BigDecimal.ZERO;

    public synchronized BigDecimal getTotalCollected() {
        return totalCollected;
    }

    public CompanyLedger(BigDecimal totalCollected) {
        this.totalCollected = totalCollected;
    }

    public synchronized void addPayment(BigDecimal amount) {
        totalCollected = totalCollected.add(amount); //adds the amount to the total collected
    }
}
