package edu.nyu.bridge.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import edu.nyu.bridge.gen.Bridge.Direction;
import edu.nyu.cards.Hand;
import edu.nyu.cards.HandView;
import edu.nyu.cards.gen.Cards;

import java.util.List;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Utility functions for determining what is happening in a trick.
 */
public final class Tricks {
  private Tricks() {
  }

  public static ImmutableList<Cards.Card> legalPlays(List<Cards.Card> currentTrick,
                                                     HandView from) {
    return legalPlays(from, currentTrick.size() > 0 ? currentTrick.get(0).getSuit() : null);
  }

  public static ImmutableList<Cards.Card> legalPlays(
      HandView from, @Nullable Cards.Suit suitLead) {
    if (suitLead == null || from.size(suitLead) == 0) {
      return ImmutableList.copyOf(from);
    }
    return ImmutableList.copyOf(from.suit(suitLead));
  }

  public static ImmutableList<Cards.Card> legalPlays(
      Cards.Hand from, Cards.Card... currentTrick) {
    return legalPlays(from, ImmutableList.copyOf(currentTrick));
  }

  public static ImmutableList<Cards.Card> legalPlays(
      Cards.Hand from, List<Cards.Card> currentTrick) {
    return legalPlays(from, currentTrick.size() > 0 ? currentTrick.get(0).getSuit() : null);
  }

  public static ImmutableList<Cards.Card> legalPlays(
      Cards.Hand from, @Nullable Cards.Suit suitLead) {
    ImmutableList suit = from.getCardsList().stream().filter(c -> c.getSuit() == suitLead).collect(toImmutableList());
    if (suitLead == null || suit.size() == 0) {
      return ImmutableList.copyOf(from.getCardsList());
    }
    return suit;
  }

  /**
   * Is this a legal play from this hand given the suitLead? (which can be null for the leader).
   */
  public static boolean legal(Cards.Card card, HandView from, @Nullable Cards.Suit suitLead) {
    if (!from.hasCard(card)) {
      return false;
    }
    return suitLead == null || card.getSuit() == suitLead || from.size(suitLead) == 0;
  }

  /**
   * Returns the high-card of the winner so far, and the next card played.
   */
  public static Cards.Card high(Cards.Card winning, Cards.Card next, @Nullable Cards.Suit trump) {
    if (next.getSuit() != winning.getSuit()) {
      return next.getSuit() == trump ? next : winning;
    }
    return next.getRank().getNumber() > winning.getRank().getNumber() ? next : winning;
  }

  public static Direction winner(Direction leader, @Nullable Cards.Suit trump, List<Cards.Card> already,
                                 Cards.Card... trick) {
    ImmutableList<Cards.Card> cards = ImmutableList.<Cards.Card>builder().addAll(already).add(trick).build();
    return winner(cards, leader, trump);
  }

  public static Direction winner(Direction leader, @Nullable Cards.Suit trump, Cards.Card... trick) {
    return winner(ImmutableList.copyOf(trick), leader, trump);
  }

  /**
   * Returns the winner so far in this trick
   */
  public static Direction winner(
      List<Cards.Card> trick, Direction leader, @Nullable Cards.Suit trump) {
    checkArgument(
        trick.size() > 0 && trick.size() <= 4, "A trick consists of 1-4 cards, not %s", trick);
    Direction winner = leader;
    Cards.Card highCard = trick.get(0);
    for (int i = 1; i < trick.size(); ++i) {
      Cards.Card card = trick.get(i);
      leader = Directions.lho(leader);
      if (high(highCard, card, trump) == card) {
        winner = leader;
        highCard = card;
      }
    }
    return winner;
  }
}
