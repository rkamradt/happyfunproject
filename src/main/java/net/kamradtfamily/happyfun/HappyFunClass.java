package net.kamradtfamily.happyfun;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    BlockingQueue<HappyFunItem> queue1 = new ArrayBlockingQueue<>(100);
    BlockingQueue<HappyFunItem> queue2 = new ArrayBlockingQueue<>(100);
    Future taker = service.submit(new HappyFunTakerRunner(queue1, queue2));
    Future runner = service.submit(new HappyFunMakerRunner(queue2, queue1));
    try {
      runner.get(1000, TimeUnit.SECONDS);
      taker.get(1000, TimeUnit.SECONDS);
      logger.info("shuting down");
      service.shutdown();
      logger.info("awaiting termination");
      service.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      logger.error("interrupted while awaiting termination", ex);
    } catch (TimeoutException ex) {
      logger.error("timeout while awaiting termination", ex);
    } catch (ExecutionException ex) {
      logger.error("excepted while awaiting termination", ex);
    }
    logger.info("done");
  }



}
