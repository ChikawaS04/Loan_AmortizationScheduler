package com.examplecode;

import java.math.BigDecimal;

/**
 * Thread-safe ledger for tracking total loan payments collected across multiple loans.
 * Uses synchronized methods to ensure consistency when accessed by multiple threads.
 */
public class CompanyLedger {
    private BigDecimal totalCollected = BigDecimal.ZERO;

    public synchronized BigDecimal getTotalCollected() {
        return totalCollected;
    }

    /**
     * Constructs a new CompanyLedger with an initial total.
     *
     * @param totalCollected Initial amount collected (typically BigDecimal.ZERO)
     */
    public CompanyLedger(BigDecimal totalCollected) {
        this.totalCollected = totalCollected;
    }

    /**
     * Adds a payment amount to the total collected.
     * Thread-safe method for concurrent access by multiple loan processors.
     *
     * @param amount Payment amount to add to the ledger
     */
    public synchronized void addPayment(BigDecimal amount) {
        totalCollected = totalCollected.add(amount);
    }
}
