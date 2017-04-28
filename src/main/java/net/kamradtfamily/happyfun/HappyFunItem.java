package net.kamradtfamily.happyfun;

import org.apache.log4j.Logger;

/**
 *
 * @author rkamradt
 */
public class HappyFunItem implements Runnable {  
  private final String message;
  private final boolean happy;
  private final HappyFunMakerRunner maker;

  public HappyFunItem(Boolean happy, HappyFunMakerRunner maker) {
    this.message = happy ? "Are we happy yet" : "Unhappy";
    this.happy = happy;
    this.maker = maker;
  }

  @Override
  public void run() {
    maker.getItemBack(this);
  }

  public boolean isHappy() {
    return happy;
  }

  public String getMessage() {
    return message;
  }
  
}
