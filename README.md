# LoanAmortizationSimulator

A Java-based loan amortization simulation built as a learning project to explore **OOP design**, **multithreading and synchronization**, and **BigDecimal precision** in financial applications.

---

## Overview

This project simulates a small lending company managing multiple loans concurrently. Each loan is processed on its own thread, computing monthly amortization payments and contributing to a shared company ledger. The goal is to model real-world fixed-rate loan amortization with accurate financial arithmetic and safe concurrent access to shared state.

> ***Simulation Assumptions:** This project operates under ideal conditions — all borrowers pay in full and on time for the life of the loan. No default risk, expected loss, prepayment, restructuring, or black swan events are modelled. It is a pure amortization simulation, not a credit risk model.*
> 

---

## Concepts Covered

**Loan Structure — Fixed-Rate Amortizing Loan**

- Implements a fully amortizing fixed-rate loan, where each monthly payment covers accrued interest on the remaining balance plus a portion of principal
- The monthly payment amount is fixed for the life of the loan — early payments are interest-heavy, later payments are principal-heavy
- Interest accrues monthly on the outstanding balance, meaning the interest portion decreases and the principal portion increases with each payment
- The loan is fully paid off at the end of the term with a balance of exactly `$0.00`

**Object-Oriented Design**

- Immutable fields using `final` wherever mutation is not required
- Balance encapsulated inside `Loan` — only mutated via `applyMonthlyPayment()`
- No setters; all construction-time values set through the constructor
- `PaymentResult` record for decoupled, immutable payment data per period

**BigDecimal in Financial Applications**

- All monetary values represented as `BigDecimal` — no `double` anywhere
- String-based construction to avoid floating-point representation errors
- High-scale intermediate calculations (scale 15) to prevent rounding drift
- `RoundingMode.HALF_UP` for final monetary values at scale 2
- Last-payment adjustment to ensure balances terminate at exactly `$0.00`

**Multithreading and Synchronization**

- One thread per loan via `LoanProcessor implements Runnable`
- `CompanyLedger` uses `synchronized` methods to prevent race conditions on shared state
- `join()` ensures all threads complete before the summary is printed
- Per-loan totals tracked inside `LoanProcessor` and read only after `join()` — no synchronization needed

---

## Project Structure

```
src/
└── com/examplecode/
    ├── Main.java             # Entry point, thread management, output
    ├── Loan.java             # Loan domain object, PMT formula, payment application
    ├── LoanProcessor.java    # Runnable, processes one loan across all periods
    ├── CompanyLedger.java    # Shared ledger with synchronized access
    └── PaymentResult.java    # Immutable record for per-payment data
```

---

## How It Works

1. Each `Loan` is constructed with a principal, APR, and term in years (converted to months internally)
2. The fixed monthly payment (PMT) is calculated once at construction using the standard amortization formula:

```
PMT = P * [r(1+r)^n] / [(1+r)^n - 1]

Where:
  P = principal
  r = monthly interest rate (APR / 1200)
  n = term in months
```

1. A `LoanProcessor` thread iterates through each month, calling `applyMonthlyPayment()` and recording the result
2. After each payment, the thread adds the payment to the shared `CompanyLedger`
3. After all threads finish, a summary is printed per loan and for the company overall

---

## Sample Output

```
========== AMORTIZATION SCHEDULE ==========

Loan 474444774 - Month 1: Interest=$4,125.00, Principal=$5,642.37, New Balance=$894,357.63
Loan 474444774 - Month 2: Interest=$4,099.14, Principal=$5,668.23, New Balance=$888,689.40
...
Loan 474444774 - Month 120: Interest=$44.56, Principal=$9,722.08, New Balance=$0.00

=== Monthly Payment Schedule ===
Loan 474444774    | Rate: 5.50% | Term: 120 months | Monthly Payment: $9,767.37
Loan 346798493    | Rate: 5.50% | Term:  72 months | Monthly Payment:   $816.89
Loan 908872356    | Rate: 5.50% | Term:  48 months | Monthly Payment: $5,579.23

=== Loan Summary ===
Loan 474444774    | Principal: $  900,000.00 | Total Paid: $1,172,083.67
Loan 346798493    | Principal: $   50,000.00 | Total Paid: $    58,816.48
Loan 908872356    | Principal: $  239,900.00 | Total Paid: $   267,802.94

Bank Total Collected: $  1,498,703.09
```

> Note: Monthly output from multiple threads will be interleaved, which is expected behaviour in a concurrent simulation.
> 

---

## Tech Stack

- **Language:** Java
- **Arithmetic:** `java.math.BigDecimal`, `java.math.RoundingMode`
- **Concurrency:** `java.lang.Thread`, `synchronized`, `Runnable`
- **IDE:** IntelliJ IDEA, OpenJDK 24.0.1

---

## Key Design Decisions

| Decision | Rationale |
| --- | --- |
| `BigDecimal` over `double` | Eliminates floating-point precision errors in monetary calculations |
| String-based `BigDecimal` construction | Avoids IEEE 754 representation issues from `double` literals |
| `monthlyRate` as a `final` field | Computed once at construction, consistent across both PMT and payment methods |
| No setters on `Loan` | Enforces that balance can only change through `applyMonthlyPayment()` |
| `synchronized` on `CompanyLedger` | Prevents lost updates when multiple threads add to `totalCollected` concurrently |
| Last-payment threshold of `1.00` | Accommodates rounding residual that accumulates over long loan terms (e.g. 120 months) |
| Term stored in months | Converted from years once in constructor so all downstream logic uses a consistent unit |
