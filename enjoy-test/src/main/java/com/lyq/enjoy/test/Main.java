package com.lyq.enjoy.test;

import java.util.concurrent.*;
public class Main {
    public static void main(String[] args) {
        LinkedTransferQueue<Integer> blockingQueue = new
                LinkedTransferQueue<Integer>();
        Producer producer = new Producer(blockingQueue);
        Consumer consumer = new Consumer(blockingQueue);
        new Thread(consumer).start();
        new Thread(producer).start();

    }
}
class Producer implements Runnable {
    private LinkedTransferQueue<Integer> linkedTransferQueue;
    private static int element = 0;
    public Producer(LinkedTransferQueue<Integer> linkedTransferQueue) {
        this.linkedTransferQueue = linkedTransferQueue;
    }
    public void run() {
        try {
            while(element < 20) {
                System.out.println("生产元素："+element);
                linkedTransferQueue.transfer(element++);
                System.out.println("生产元素===========：");
            }
        } catch (Exception e) {
            System.out.println("生产者在等待空闲空间的时候发生异常！");
            e.printStackTrace();
        }
        System.out.println("生产者终止了生产过程！");
    }
}
class Consumer implements Runnable {
    private LinkedTransferQueue<Integer> linkedTransferQueue;

    public Consumer(LinkedTransferQueue<Integer> linkedTransferQueue) {
        this.linkedTransferQueue = linkedTransferQueue;
    }

    public void run() {
        try {
            while (true) {
                System.out.println("消费元素：" + linkedTransferQueue.take());
            }
        } catch (Exception e) {
            System.out.println("消费者在等待新产品的时候发生异常！");
            e.printStackTrace();
        }
        System.out.println("消费者终止了消费过程！");
    }
}