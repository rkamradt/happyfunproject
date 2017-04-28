package net.kamradtfamily.happyfun;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author rkamradt
 */
public class HappyFunMaker implements Runnable {
  private static final Logger logger = Logger.getLogger(HappyFunMaker.class.getName());
  
  private final BlockingQueue queue;
  private final Random r = new Random();

  public HappyFunMaker(BlockingQueue queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    try {
      HappyFunItem item = new HappyFunItem(r.nextInt(100));
      logger.info("created new happy fun item with happiness " + item.getHappiness());
      queue.put(item);
    } catch (InterruptedException ex) {
      logger.error("Happy funness interrupted while putting", ex);
    }
  }

}
