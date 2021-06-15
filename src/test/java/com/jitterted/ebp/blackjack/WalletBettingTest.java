package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WalletBettingTest {

    @Test
    public void walletWith12BalanceWhenBet8ThenBalanceIs4() throws Exception {
        // given 12
        Wallet wallet = new Wallet();
        wallet.addMoney(12);

        // when bet 8
        wallet.bet(8);

        // then
        assertThat(wallet.balance())
                .isEqualTo(12 - 8);
    }

    @Test
    public void walletWith27BalanceWhenBet7AndBet9ThenBalanceIs11() throws Exception {
        Wallet wallet = new Wallet();
        wallet.addMoney(27);

        wallet.bet(7);
        wallet.bet(9);

        assertThat(wallet.balance())
                .isEqualTo(27 - 7 - 9);    // EVIDENT DATA
    }
    
    @Test
    public void walletWithMoneyWhenBetFullBalanceThenIsEmpty() throws Exception {
        Wallet wallet = new Wallet();
        wallet.addMoney(33);

        wallet.bet(33);

        assertThat(wallet.isEmpty())
                .isTrue();
    }

    @Test
    public void betMoreThanAvailableBalanceThrowsException() throws Exception {
        Wallet wallet = new Wallet();
        wallet.addMoney(73);

        assertThatThrownBy(() -> {
            wallet.bet(74);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void betNegativeAmountThrowsException() throws Exception {
        Wallet wallet = new Wallet();

        assertThatThrownBy(() -> {
            wallet.bet(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
