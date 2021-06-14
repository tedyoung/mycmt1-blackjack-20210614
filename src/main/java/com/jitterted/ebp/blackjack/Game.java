package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

  private final Deck deck;

  private final List<Card> dealerHand = new ArrayList<>();
  private final List<Card> playerHand = new ArrayList<>();

  public static void main(String[] args) {
    displayWelcomeScreen();

    playGame();

    resetScreen();
  }

  // YAGNI => You Ain't Gonna Need It : "What If?"


  private static void playGame() {
    Game game = new Game();
    game.initialDeal();
    game.play();
  }

  private static void resetScreen() {
    System.out.println(ansi().reset());
  }

  private static void displayWelcomeScreen() {
    System.out.println(ansi()
                           .bgBright(Ansi.Color.WHITE)
                           .eraseScreen()
                           .cursor(1, 1)
                           .fgGreen().a("Welcome to")
                           .fgRed().a(" Jitterted's")
                           .fgBlack().a(" BlackJack"));
  }

  public Game() {
    deck = new Deck();
  }

  public void initialDeal() {
    dealRoundOfCards();
    dealRoundOfCards();

  }

  /**
   * Deals a round of cards, to players first due to the rules of Blackjack
   */
  private void dealRoundOfCards() {
    playerHand.add(deck.draw());
    dealerHand.add(deck.draw());
  }

  public void play() {
    boolean playerBusted = playerTurn();

    dealerTurn(playerBusted);

    displayFinalGameState();

    determineGameOutcome(playerBusted);
  }

  private void determineGameOutcome(boolean playerBusted) {
    if (playerBusted) {
      System.out.println("You Busted, so you lose.  💸");
    } else if (handValueOf(dealerHand) > 21) {
      System.out.println("Dealer went BUST, Player wins! Yay for you!! 💵");
    } else if (handValueOf(dealerHand) < handValueOf(playerHand)) {
      System.out.println("You beat the Dealer! 💵");
    } else if (handValueOf(dealerHand) == handValueOf(playerHand)) {
      System.out.println("Push: You tie with the Dealer. 💸");
    } else {
      System.out.println("You lost to the Dealer. 💸");
    }
  }

  private void dealerTurn(boolean playerBusted) {
    if (!playerBusted) {
      while (shouldDealerHit()) {
        dealerHand.add(deck.draw());
      }
    }
  }

  private boolean shouldDealerHit() {
    // Rule (Why?) Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>=stand)
    return handValueOf(dealerHand) <= 16;
  }

  private boolean playerTurn() {
    // get Player's decision: hit until they stand, then they're done (or they go bust)
    boolean playerBusted = false;
    while (!playerBusted) {
      displayGameState();
      String playerChoice = inputFromPlayer().toLowerCase();
      if (playerChoseStand(playerChoice)) {
        break;
      }
      if (playerHits(playerChoice)) {
        playerBusted = playerHits(playerBusted);
      } else {
        System.out.println("You need to [H]it or [S]tand");
      }
    }
    return playerBusted;
  }

  private boolean playerHits(boolean playerBusted) {
    playerHand.add(deck.draw());
    if (isPlayerBusted()) {
      playerBusted = true;
    }
    return playerBusted;
  }

  private boolean playerHits(String playerChoice) {
    return playerChoice.startsWith("h");
  }

  private boolean isPlayerBusted() {
    return handValueOf(playerHand) > 21;
  }

  private boolean playerChoseStand(String playerChoice) {
    return playerChoice.startsWith("s");
  }

  public int handValueOf(List<Card> hand) {
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

  private String inputFromPlayer() {
    System.out.println("[H]it or [S]tand?");
    Scanner scanner = new Scanner(System.in);
    return scanner.nextLine();
  }

  private void displayGameState() {
    System.out.print(ansi().eraseScreen().cursor(1, 1));
    System.out.println("Dealer has: ");
    System.out.println(dealerHand.get(0).display()); // first card is Face Up

    // second card is the hole card, which is hidden
    displayBackOfCard();

    System.out.println();
    System.out.println("Player has: ");
    displayHand(playerHand);
    System.out.println(" (" + handValueOf(playerHand) + ")");
  }

  private void displayBackOfCard() {
    System.out.print(
        ansi()
            .cursorUp(7)
            .cursorRight(12)
            .a("┌─────────┐").cursorDown(1).cursorLeft(11)
            .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
            .a("│░ J I T ░│").cursorDown(1).cursorLeft(11)
            .a("│░ T E R ░│").cursorDown(1).cursorLeft(11)
            .a("│░ T E D ░│").cursorDown(1).cursorLeft(11)
            .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
            .a("└─────────┘"));
  }

  private void displayHand(List<Card> hand) {
    System.out.println(hand.stream()
                           .map(Card::display)
                           .collect(Collectors.joining(
                               ansi().cursorUp(6).cursorRight(1).toString())));
  }

  private void displayFinalGameState() {
    System.out.print(ansi().eraseScreen().cursor(1, 1));
    System.out.println("Dealer has: ");
    displayHand(dealerHand);
    System.out.println(" (" + handValueOf(dealerHand) + ")");

    System.out.println();
    System.out.println("Player has: ");
    displayHand(playerHand);
    System.out.println(" (" + handValueOf(playerHand) + ")");
  }
}
