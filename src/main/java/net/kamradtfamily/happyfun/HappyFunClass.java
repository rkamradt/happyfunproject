package net.kamradtfamily.happyfun;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author rkamradt
 */
public class HappyFunClass implements Runnable {

  volatile int threadNumber = 0;
  private static final Logger logger = Logger.getLogger(HappyFunClass.class.getName());

  /**
   *
   * @param args
   * @throws java.lang.InterruptedException
   * @throws java.util.concurrent.ExecutionException
   */
  static public void main(String[] args) throws InterruptedException, ExecutionException {
    System.out.println("happy fun project");
    HappyFunClass app = new HappyFunClass();
    app.run();
  }

  /**
   * construct a happy fun object
   */
  public HappyFunClass() {

  }

  /**
   * run some happy fun things
   */
  @Override
  public void run() {
    ExecutorService service = Executors.newFixedThreadPool(10, r -> new Thread(r, "HappyFunThread-" + threadNumber++));
    BlockingQueue<HappyFunItem> queue = new SynchronousQueue<>();
    Future<Boolean> runner = service.submit(new HappyFunTakerRunner(queue));
    HappyFunMakerRunner maker = new HappyFunMakerRunner(queue, service);
    service.submit(maker);
    try {
      logger.info("runner returned "+runner.get());
      synchronized (maker) {
        maker.notify();
      }
      logger.info("shuting down");
      service.shutdown();
      logger.info("awaiting termination");
      service.awaitTermination(10, TimeUnit.SECONDS);
    } catch(InterruptedException | ExecutionException ex) {
      logger.error("exception thrown during run", ex);
    }
    logger.info("done");
  }



}
