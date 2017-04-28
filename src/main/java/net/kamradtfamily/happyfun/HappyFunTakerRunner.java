package net.kamradtfamily.happyfun;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author rkamradt
 */
public class HappyFunTakerRunner implements Runnable {
  private static final Logger logger = Logger.getLogger(HappyFunTakerRunner.class.getName());
  
  BlockingQueue<HappyFunItem> inqueue;
  BlockingQueue<HappyFunItem> outqueue;

  public HappyFunTakerRunner(BlockingQueue<HappyFunItem> inqueue, BlockingQueue<HappyFunItem> outqueue) {
    this.inqueue = inqueue;
    this.outqueue = outqueue;
  }

  @Override
  public void run() {
    boolean happy = true;
    while(happy) {
      try {
        HappyFunItem item = inqueue.poll(100, TimeUnit.SECONDS);
        if(item == null) {
          logger.error("HappyFunTakerRunner Timeout");
          break;
        }
        logger.info("item retrieved from queue " + item.getMessage());
        happy = item.isHappy();
        handOff(item);
      } catch (InterruptedException ex) {
        logger.error("Happy funness interrupted while taking", ex);
        happy = false;
      }
    }
    logger.info("Happy fun taker runner ending");
  }
  private void handOff(HappyFunItem item) throws InterruptedException {
    outqueue.put(item);
    Thread.sleep(100);
  }
}
