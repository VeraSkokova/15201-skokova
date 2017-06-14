package ru.nsu.ccfit.skokova.threadpool;

import org.junit.Assert;
import org.junit.Test;

public class BlockingQueueTest {

    int size = 10;
    BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(size);

    @Test
    public void test() {
      Thread first = new Thread(new Runnable() {
        @Override
        public void run() {
          try {
              for (int i = 0; i < size; i++) {
                  blockingQueue.enqueue(i);
                }
              } catch (InterruptedException e) {
              System.out.println(e.getMessage());
            }
          }
        });
        Thread second = new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              for (int i = 0; i < size; i++) {
                  int temp = blockingQueue.dequeue();
                  Assert.assertEquals(i, temp);
                }
              } catch (InterruptedException e) {
                System.out.println(e.getMessage());
              }
            }
          });

          first.start();
          second.start();
          System.out.println("TEST BlockingQueueTest PASSED");
        }
    }
