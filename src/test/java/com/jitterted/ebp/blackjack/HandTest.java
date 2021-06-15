package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class HandTest {

    private static final Suit DUMMY_SUIT = Suit.DIAMONDS;

    @Test
    public void handWithValueOf20IsNotBusted() throws Exception {
        List<Card> cards = List.of(new Card(DUMMY_SUIT, "Q"),
                                   new Card(DUMMY_SUIT, "10"));
        Hand hand = new Hand(cards);

        assertThat(hand.isBusted())
                .isFalse();
    }

    @Test
    public void handWithValueOf22IsBusted() throws Exception {
        List<Card> cards = List.of(new Card(DUMMY_SUIT, "Q"),
                                   new Card(DUMMY_SUIT, "J"),
                                   new Card(DUMMY_SUIT, "2")
                                   );
        Hand hand = new Hand(cards);

        assertThat(hand.isBusted())
                .isTrue();
    }

    @Test
    public void handWithValueOf21IsNotBusted() throws Exception {
        List<Card> cards = List.of(new Card(DUMMY_SUIT, "Q"),
                                   new Card(DUMMY_SUIT, "9"),
                                   new Card(DUMMY_SUIT, "2")
                                   );
        Hand hand = new Hand(cards);

        assertThat(hand.isBusted())
                .isFalse();
    }


}
