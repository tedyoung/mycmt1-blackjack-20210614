package com.jitterted.ebp.blackjack;

public class Wallet {

    private int balance = 0;

    public boolean isEmpty() {
        return balance == 0;
    }

    public void addMoney(int amount) {
        requireAmountGreaterThanZero(amount);
        balance += amount;
    }

    public void bet(int betAmount) {
        requireSufficientBalanceForBet(betAmount);
        requireAmountGreaterThanZero(betAmount);
        balance -= betAmount;
    }

    public int balance() {
        return balance;
    }

    private void requireSufficientBalanceForBet(int betAmount) {
        if (betAmount > balance) {
            throw new IllegalStateException();
        }
    }

    private void requireAmountGreaterThanZero(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
