package net.kamradtfamily.happyfun;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author rkamradt
 */
public class HappyFunMakerRunner implements Runnable {

  private static final Logger logger = Logger.getLogger(HappyFunMakerRunner.class.getName());

  volatile int threadNumber = 0;
  private final BlockingQueue<HappyFunItem> inqueue;
  private final BlockingQueue<HappyFunItem> outqueue;
  private final ScheduledExecutorService service;

  public HappyFunMakerRunner(BlockingQueue<HappyFunItem> inqueue, BlockingQueue<HappyFunItem> outqueue) {
    this.inqueue = inqueue;
    this.outqueue = outqueue;
    this.service = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "MakerThread-" + threadNumber++));
  }

  @Override
  public void run() {
    service.scheduleAtFixedRate(new HappyFunMaker(outqueue), 1, 1, TimeUnit.SECONDS);
    boolean happy = true;
    while (happy) {
      try {
        HappyFunItem item = inqueue.poll(100, TimeUnit.SECONDS);
        if(item == null) {
          logger.error("HappyFunMakerRunner Timeout");
          break;
        }
        logger.info("HappyFunItem returned " + item.getMessage());
        item.getBored();
        happy = item.isHappy();
        handOff(item);
      } catch (InterruptedException ex) {
        logger.error("HappyFunMapperRunner interrupted", ex);
        happy = false;
      }
    }
    logger.info("Item unhappy, shutting down");
    service.shutdown();
  }
  
  private void handOff(HappyFunItem item) throws InterruptedException {
    outqueue.put(item);
    Thread.sleep(100);
  }
}
