package net.kamradtfamily.happyfun;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;

/**
 *
 * @author rkamradt
 */
public class HappyFunMaker implements Callable<Boolean> {
  private static final Logger logger = Logger.getLogger(HappyFunMaker.class.getName());
  
  private final BlockingQueue queue;
  private final Random r = new Random();
  private final HappyFunMakerRunner runner;

  public HappyFunMaker(BlockingQueue queue, HappyFunMakerRunner runner) {
    this.queue = queue;
    this.runner = runner;
  }

  @Override
  public Boolean call() {
    try {
      logger.info("maker thread " + Thread.currentThread().getName() + " starting");
      Boolean happy = r.nextInt(100) != 1;
      logger.info("putting item on queue " + happy);
      queue.put(new HappyFunItem(happy, runner));
      logger.info("message queued");
      return happy;
    } catch (InterruptedException ex) {
      logger.error("Happy funness interrupted while putting", ex);
    } finally {
      logger.info("maker thread " + Thread.currentThread().getName() + " ending");
    }
    return Boolean.FALSE;
  }

}
