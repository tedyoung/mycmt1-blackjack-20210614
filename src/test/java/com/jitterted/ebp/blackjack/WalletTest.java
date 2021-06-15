package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WalletTest {

    @Test
    public void newWalletIsEmpty() throws Exception {
        Wallet wallet = new Wallet();

        assertThat(wallet.isEmpty())
                .isTrue();
    }

    // setup, action, verification
    // AAA: arrange, act, assert
    // GWT: Given, When, Then (BDD, behavior-driven development)

    @Test
    public void newWalletThenAddMoneyThenIsNotEmpty() throws Exception {
        // Given
        Wallet wallet = new Wallet();

        // When
        wallet.addMoney(10);

        // Then
        assertThat(wallet.isEmpty())
                .isFalse();
    }

}
