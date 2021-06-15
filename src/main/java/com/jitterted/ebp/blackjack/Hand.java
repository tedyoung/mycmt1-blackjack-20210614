package com.jitterted.ebp.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class Hand {
    private final List<Card> hand = new ArrayList<Card>();

    public Hand() {
    }

    public Hand(List<Card> cards) {
        this.hand.addAll(cards);
    }

    public int value() {
      int handValue = hand
          .stream()
          .mapToInt(Card::rankValue)
          .sum();

      // does the hand contain at least 1 Ace?
      boolean hasAce = hand
          .stream()
          .anyMatch(card -> card.rankValue() == 1);

      // if the total hand value <= 11, then count the Ace as 11 by adding 10
      if (hasAce && handValue < 11) {
        handValue += 10;
      }

      return handValue;
    }

    public void drawCard(Deck deck) {
        hand.add(deck.draw());
    }

    public Card firstCard() {
        return hand.get(0);
    }

    public void displayHand() {
        System.out.println(hand.stream()
                               .map(Card::display)
                               .collect(Collectors.joining(
                                 ansi().cursorUp(6).cursorRight(1).toString())));
    }

    public boolean isBusted() {
      return value() > 21;
    }

    public boolean pushes(Hand hand) {
        return value() == hand.value();
    }

    public boolean beats(Hand hand) {
        return hand.value() < value();
    }

    //--- move the methods below to DealerHand subclass

    private boolean shouldDealerHit() {
      // Rule (Why?) Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>=stand)
      return value() <= 16;
    }

    public void drawCardsUntilDone(Deck deck) {
      while (shouldDealerHit()) {
        drawCard(deck);
      }
    }
}