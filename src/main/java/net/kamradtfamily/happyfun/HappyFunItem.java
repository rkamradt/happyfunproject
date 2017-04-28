package net.kamradtfamily.happyfun;

/**
 *
 * @author rkamradt
 */
public class HappyFunItem {  
  public final String REALLY_HAPPY_MESSAGE = "Really happy";
  public final String HAPPY_MESSAGE = "Happy";
  public final String UNHAPPY_MESSAGE = "Unhappy";
  private int happiness;

  public HappyFunItem(int happiness) {
    this.happiness = happiness;
  }

  public void getBored() {
    happiness--;
  }
  public boolean isHappy() {
    return happiness > 0;
  }

  public String getMessage() {
    if(happiness > 10) {
      return REALLY_HAPPY_MESSAGE;
    }
    return isHappy() ? HAPPY_MESSAGE : UNHAPPY_MESSAGE;
  }

  public int getHappiness() {
    return happiness;
  }
  
}
