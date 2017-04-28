package net.kamradtfamily.happyfun;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import org.apache.log4j.Logger;

/**
 *
 * @author rkamradt
 */
public class HappyFunMakerRunner implements Callable {
  private static final Logger logger = Logger.getLogger(HappyFunTakerRunner.class.getName());
  
  private final BlockingQueue<HappyFunItem> queue;
  private final ExecutorService service;

  public HappyFunMakerRunner(BlockingQueue<HappyFunItem> queue, ExecutorService service) {
    logger.info("created HappyFunRunner");
    this.queue = queue;
    this.service = service;
  }

  @Override
  public Boolean call() {
    logger.info("HappyFunMaker started");
    boolean happy = true;
    while (happy) {
      HappyFunMaker maker = new HappyFunMaker(queue, this);
      service.submit(maker);
      try {
        Thread.sleep(10); // throttle
      } catch (InterruptedException ex) {
        logger.error("HappyFunMaker interrupted from nap");
        return Boolean.FALSE;
      }
      happy = !Thread.interrupted();
    }
    logger.info("HappyFunMaker done");
    return happy;
  }

  void getItemBack(HappyFunItem item) {
    logger.info("Got item back " + item.getMessage());
  }
  
}
