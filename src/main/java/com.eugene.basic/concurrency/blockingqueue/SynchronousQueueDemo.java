package com.eugene.basic.concurrency.blockingqueue;

import java.util.concurrent.*;

/**
 * SynchronousQueue 只有一个容量，
 * 一对一服务，0库存，最多存一个元素，
 *
 * 你不消费，我不生产
 *
 */
public class SynchronousQueueDemo {

    // 测试同步队列。
    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            5,
            6,
            3000,
            TimeUnit.MILLISECONDS,
            new SynchronousQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    private static void testSyncQueueWithThreadPool() {
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            Future<?> submit = threadPoolExecutor.submit(() -> {
                try {
                    // 每次提交后打印。
                    System.out.println("已提交任务第" + finalI + "个任务");
                    // 每个线程睡眠5s
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        // 验证: 期望结果？第7个任务会被阻塞。
        // 实际结果：错！ 不会阻塞，是直接执行拒绝策略了！！！也能理解。
    }

    public static void main(String[] args) {
        // testQueue();

        // 测试线程池功能
        testSyncQueueWithThreadPool();
    }



    private static void testQueue() {
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();

        new Thread(() -> {
            try {
                blockingQueue.put("a");
                System.out.println("线程A生产：a");
                // 因为put是阻塞式的添加，因此只有队列中的a元素被消费完了，b才能被put进去。此时b在put时会阻塞。
                blockingQueue.put("b");
                System.out.println("线程A生产：b");
                blockingQueue.put("c");
                System.out.println("线程A生产：c");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "线程A").start();

        new Thread(() -> {
            try {
                // 线程b每隔2s消费一次
                System.out.println("线程B消费: " + blockingQueue.take());
                Thread.sleep(2000);

                System.out.println("线程B消费: " + blockingQueue.take());
                Thread.sleep(2000);

                System.out.println("线程B消费: " + blockingQueue.take());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "线程B").start();
    }
}
