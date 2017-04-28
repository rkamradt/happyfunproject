package net.kamradtfamily.happyfun;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;

/**
 *
 * @author rkamradt
 */
public class HappyFunTakerRunner implements Callable {
  private static final Logger logger = Logger.getLogger(HappyFunTakerRunner.class.getName());
  
  BlockingQueue<HappyFunItem> queue;

  public HappyFunTakerRunner(BlockingQueue<HappyFunItem> queue) {
    logger.info("created HappyFunRunner");
    this.queue = queue;
  }

  @Override
  public Boolean call() {
    boolean happy = false;
    try {
      logger.info("runner thread " + Thread.currentThread().getName() + " starting");
      do {
        try {
          logger.info("taking item off of queue");
          HappyFunItem item = queue.take();
          logger.info("item retrieved from queue " + item.isHappy());
          happy = item.isHappy();
          if (happy) {
            item.run();
          }
        } catch (InterruptedException ex) {
          logger.error("Happy funness interrupted while taking", ex);
          happy = false;
        }
      } while (happy);
      logger.info("HappyFunRunner returning false");
    } finally {
      logger.info("runner thread " + Thread.currentThread().getName() + " ending");
    }
    return happy;
  }
  
}
