package edu.nyu.cards;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import edu.nyu.cards.gen.Cards.Card.Rank;

public class RanksTest {
  @Test
  public void testRanks() {
    assertThat(Rank.KING).named("2convertRank").isEqualTo(Ranks.char2Rank('K'));
    assertThat('7').named("Seven").isEqualTo(Ranks.rank2Char(Ranks.char2Rank('7')));
  }
}
