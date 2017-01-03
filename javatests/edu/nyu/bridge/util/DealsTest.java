package edu.nyu.bridge.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.nyu.bridge.gen.Bridge.Vulnerability;

public class DealsTest {
  @Test
  public void testVuls() {
    assertEquals(Vulnerability.NONE, Deals.getVul(8));
    assertEquals(Vulnerability.BOTH, Deals.getVul(4));

    assertEquals(Vulnerability.NS, Deals.getVul(18));
    assertEquals(Vulnerability.EW, Deals.getVul(32));
  }
}
