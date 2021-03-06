package edu.nyu.bridge.util;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Converter;
import edu.nyu.bridge.gen.Bridge.Direction;
import edu.nyu.util.ShortEnumConverter;
import org.junit.Test;

public class CallDirectionTest {
  @Test
  public void testCalls() {
    assertThat("2H").isEqualTo(Calls.call2String(Calls.string2Call("2H")));
  }

  @Test
  public void testDirectionConverter() {
    Converter<String, Direction> directionConverter = ShortEnumConverter.create(Direction.class);
    assertThat("N")
        .isEqualTo(directionConverter.reverse().convert(directionConverter.convert("N")));
  }

  @Test
  public void testDirections() {
    assertThat(Direction.EAST).isEqualTo(Directions.lho(Direction.NORTH));
  }
}
