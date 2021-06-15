package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

  private final Deck deck;

  private final Hand playerHand = new Hand();
  private final Hand dealerHand = new Hand();
  private int playerBalance = 0;
  private int playerBetAmount = 0;

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
    playerHand.drawCard(deck);
    dealerHand.drawCard(deck);
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
      // playerLoses() -> lose their bet
    } else if (dealerHand.isBusted()) {
      System.out.println("Dealer went BUST, Player wins! Yay for you!! 💵");
      // playerWins() -> win their bet + bet
    } else if (playerHand.beats(dealerHand)) { // playerHand.beats(dealerHand)
      // playerWins()
      System.out.println("You beat the Dealer! 💵");
    } else if (dealerHand.pushes(playerHand)) {
      // playerPushes() -> bet is return
      System.out.println("Push: You tie with the Dealer. 💸");
    } else {
      // playerLoses()
      System.out.println("You lost to the Dealer. 💸");
    }
  }

  private void dealerTurn(boolean playerBusted) {
    if (!playerBusted) {
      dealerHand.drawCardsUntilDone(deck);
    }
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
    playerHand.drawCard(deck);
    if (isPlayerBusted()) {
      playerBusted = true;
    }
    return playerBusted;
  }

  private boolean playerHits(String playerChoice) {
    return playerChoice.startsWith("h");
  }

  private boolean isPlayerBusted() {
    return playerHand.isBusted();
  }

  private boolean playerChoseStand(String playerChoice) {
    return playerChoice.startsWith("s");
  }

  private String inputFromPlayer() {
    System.out.println("[H]it or [S]tand?");
    Scanner scanner = new Scanner(System.in);
    return scanner.nextLine();
  }

  private void displayGameState() {
    System.out.print(ansi().eraseScreen().cursor(1, 1));
    System.out.println("Dealer has: ");
    System.out.println(dealerHand.firstCard().display()); // first card is Face Up

    // second card is the hole card, which is hidden
    displayBackOfCard();

    System.out.println();
    System.out.println("Player has: ");
    playerHand.displayHand();
    System.out.println(" (" + playerHand.value() + ")");
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

  private void displayFinalGameState() {
    System.out.print(ansi().eraseScreen().cursor(1, 1));
    System.out.println("Dealer has: ");
    dealerHand.displayHand();
    System.out.println(" (" + dealerHand.value() + ")");

    System.out.println();
    System.out.println("Player has: ");
    playerHand.displayHand();
    System.out.println(" (" + playerHand.value() + ")");
  }

  public int playerBalance() {
    return playerBalance;
  }

  public void playerDeposits(int amount) {
    playerBalance += amount;
  }

  public void playerBets(int betAmount) {
    playerBalance -= betAmount;
    playerBetAmount = betAmount;
  }

  public void playerWins() {
    playerBalance += playerBetAmount * 2;
  }
}
