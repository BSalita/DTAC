package edu.nyu.cards;

import com.google.common.base.Converter;
import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import edu.nyu.cards.gen.Cards.Suit;
import javax.annotation.Nullable;

/** Methods for converting back and forth from {@link Suit} to char. */
public final class Suits {
  private Suits() {}

  public static final Converter<Character, Suit> CHAR_TO_RANK =
      Converter.from(Suits::char2Suit, Suits::suit2Char);

  @Nullable
  public static Suit char2Suit(char suit) {
    return SUIT_2_CHAR_BI.inverse().get(Character.toUpperCase(suit));
  }

  public static char suit2Char(Suit suit) {
    return SUIT_2_CHAR_BI.get(suit);
  }

  // Useful since Suits has NOTRUMP in it for bidding
  public static ImmutableList<Suit> iterateSuitsHighLow() {
    return SUITS_HIGH_LOW;
  }

  public static ImmutableList<Suit> iterateSuitsLowHigh() {
    return SUITS_LOW_HIGH;
  }

  @Nullable
  public static Suit lowerSuit(Suit suit) {
    switch (suit) {
      case CLUBS:
        return null;
      case DIAMONDS:
        return Suit.CLUBS;
      case HEARTS:
        return Suit.DIAMONDS;
      case SPADES:
        return Suit.HEARTS;
      case NOTRUMPS:
        return Suit.SPADES;
      default:
        throw new IllegalArgumentException("Suit unexpected: " + suit);
    }
  }

  private static final ImmutableList<Suit> SUITS_HIGH_LOW =
      ImmutableList.of(Suit.SPADES, Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS);
  private static final ImmutableList<Suit> SUITS_LOW_HIGH =
      ImmutableList.of(Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES);

  private static BiMap<Suit, Character> SUIT_2_CHAR_BI =
      Maps.unmodifiableBiMap(
          EnumHashBiMap.create(
              ImmutableBiMap.<Suit, Character>builder() // trick
                  .put(Suit.SPADES, 'S')
                  .put(Suit.HEARTS, 'H')
                  .put(Suit.DIAMONDS, 'D')
                  .put(Suit.CLUBS, 'C')
                  .put(Suit.NOTRUMPS, 'N')
                  .build()));
}
