package edu.nyu.cards;

import static edu.nyu.cards.Cards.card;
import static edu.nyu.cards.Suits.iterateSuitsHighLow;
import static edu.nyu.cards.Suits.iterateSuitsLowHigh;
import static edu.nyu.cards.Suits.lowerSuit;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;

import edu.nyu.cards.gen.Cards;
import edu.nyu.cards.gen.Cards.Card;
import edu.nyu.cards.gen.Cards.Card.Rank;
import edu.nyu.cards.gen.Cards.Suit;

/**
 * Represent a hand as the set of cards in each suit. optimized to use EnumSets,
 * but can map to/from {@link Cards.Hand} for transport.
 * Explicitly mutable, use a Cards.Hand for an Immutable representation.
 */
public class Hand {
  // for each suit, a Set with the cards held.
  @VisibleForTesting EnumMap<Suit, EnumSet<Card.Rank>> cards;

  public Hand() {
    // default - init the map
    cards = new EnumMap<Suit, EnumSet<Card.Rank>>(Suit.class);
    for (Suit suit : iterateSuitsLowHigh()) {
      cards.put(suit, EnumSet.noneOf(Card.Rank.class));
    }
  }

  /** Returns a hand with all cards in a standard deck. */
  public static Hand deck() {
    return new Hand().complement();
  }

  public static Hand fromProto(Cards.Hand hand) {
    Hand result = new Hand();
    for (Cards.Card card : hand.getCardsList()) {
      result.addCard(card);
    }
    return result;
  }

  public Cards.Hand toProto() {
    Cards.Hand.Builder result = Cards.Hand.newBuilder();
    for (Suit suit : iterateSuitsLowHigh()) {
      for (Card.Rank rank : cards.get(suit)) {
        result.addCards(card(suit, rank));
      }
    }
    return result.build();
  }

  /** Returns a hand parsed from the given string in PBN format. */
  public static Hand fromString(String hand) {
    Hand result = new Hand();
    // TODO: support several formats...
    Suit currentSuit = Suit.NOTRUMPS;
    Rank lastRank = Rank.TWO;
    for (char c : hand.toCharArray()) {
      Rank rank = Ranks.char2Rank(c);
      if (rank == null) {
        // treat as break - prob '.' or ' '
        // TODO multiple spaces as a single suit change
        currentSuit = lowerSuit(currentSuit);
        lastRank = null;
        continue;
      }
      if (lastRank != null && rank.ordinal() >= lastRank.ordinal()) {
        // new suit
        currentSuit = lowerSuit(currentSuit);
      }
      result.addCard(currentSuit, rank);
      lastRank = rank;
      // lowerSuit
    }
    return result;
  }

  public Hand addCard(Card card) {
    return addCard(card.getSuit(), card.getRank());
  }

  public Hand addCard(Suit suit, Card.Rank rank) {
    cards.get(suit).add(rank);
    return this;
  }

  public boolean hasCard(Card card) {
    return hasCard(card.getSuit(), card.getRank());
  }

  public boolean hasCard(Suit suit, Card.Rank rank) {
    return cards.get(suit).contains(rank);
  }

  public Hand removeCard(Card card) {
    return removeCard(card.getSuit(), card.getRank());
  }

  public Hand removeCard(Suit suit, Rank rank) {
    cards.get(suit).remove(rank);
    return this;
  }

  public int size(Suit suit) {
    return cards.get(suit).size();
  }

  public EnumSet<Card.Rank> suit(Suit suit) {
    return cards.get(suit);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Hand)) {
      return false;
    }
    return cards.equals(((Hand) other).cards);
  }

  // BBO lin format string, SxxHxxDxxCxx
  public String linString() {
    StringBuffer result = new StringBuffer();
    for (Suit suit : iterateSuitsHighLow()) {
      result.append(suit.name().substring(0, 1));
      EnumSet<Card.Rank> ranks = cards.get(suit);
      for (Card.Rank rank : Ranks.HIGH_TO_LOW) {
        if (ranks.contains(rank)) {
          result.append(Ranks.rank2Char(rank));
        }
      }
    }
    return result.toString();
  }

  @Override
  public String toString() {
    // make same string as parseable in fromString
    StringBuffer result = new StringBuffer();
    for (Suit suit : iterateSuitsHighLow()) {
      EnumSet<Card.Rank> ranks = cards.get(suit);
      for (Card.Rank rank : Ranks.HIGH_TO_LOW) {
        if (ranks.contains(rank)) {
          result.append(Ranks.rank2Char(rank));
        }
      }
      if (suit != Suit.CLUBS) {
        result.append(".");
      }
    }
    return result.toString();
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  public int size() {
    int result = 0;
    for (Suit suit : iterateSuitsHighLow()) {
      result += size(suit);
    }
    return result;
  }

  public void addCards(Suit suit, Collection<Rank> ranks) {
    cards.get(suit).addAll(ranks);
  }

  public Hand addAll(Hand hand) {
    for (Suit suit : iterateSuitsHighLow()) {
      cards.get(suit).addAll(hand.suit(suit));
    }
    return this;
  }

  public Hand addAll(Cards.Hand hand) {
    for (Cards.Card card : hand.getCardsList()) {
      cards.get(card.getSuit()).add(card.getRank());
    }
    return this;
  }

  public Hand removeAll(Hand hand) {
    for (Suit suit : iterateSuitsHighLow()) {
      cards.get(suit).removeAll(hand.suit(suit));
    }
    return this;
  }

  /** Returns all cards Not explicitly in this hand. */
  public Hand complement() {
    Hand result = new Hand();
    for (Suit suit : iterateSuitsHighLow()) {
      result.addCards(suit, EnumSet.complementOf(cards.get(suit)));
    }
    return result;
  }

  @Nullable
  public Card lowest(Suit suit) {
    EnumSet<Rank> thisSuit = suit(suit);
    for (Rank r : Rank.values()) {
      if (thisSuit.contains(r)) {
        return card(suit, r);
      }
    }
    return null;
  }

  @Nullable
  public Card highest(Suit suit) {
    EnumSet<Rank> thisSuit = suit(suit);
    for (Rank r : Ranks.HIGH_TO_LOW) {
      if (thisSuit.contains(r)) {
        return card(suit, r);
      }
    }
    return null;
  }

  public Suit longest() {
    Suit result = Suit.SPADES;
    for (Suit s : Suits.iterateSuitsHighLow()) {
      if (size(s) > size(result)) {
        result = s;
      }
    }
    return result;
  }
}