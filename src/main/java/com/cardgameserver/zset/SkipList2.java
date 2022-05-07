package com.cardgameserver.zset;

import lombok.SneakyThrows;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 升序排列的跳表
 */
public class SkipList2 {
    private Node head;
    private Node tail;

    private int level;
    private int size;  //插入节点的个数

    private Random random;

    //保证线程安全使用了reentrantwriteReadLock来解决   更优化的方案是CAS
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = readWriteLock.readLock();
    private static Lock writeLock = readWriteLock.writeLock();

    public SkipList2() {
        level = 0;
        size = 0;
        head = new Node(null);
        tail = new Node(null);

        head.right = tail;
        tail.left = head;
        random = new Random();
    }

    /**
     * 寻找第一个这个节点应该在的位置
     *
     * @param score
     * @return 当前节点的前一个节点
     */
    public Node findFirst(Double score) throws InterruptedException {

        try {
            readLock.lockInterruptibly();
            Node p = head;
            while (true) {

                while (p.right.score != null && p.right.score <= score) {
                    p = p.right;
                }
                //向下找
                if (p.down != null) {
                    p = p.down;
                } else {
                    break;
                }

            }
            //如果能够成功的获取锁就继续执行  如果没有获取就等待
            return p;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        //查找的时候先从第一层节点开始找
        return null;

    }

    /**
     *             没有造成死锁  因为同一个线程 写的时候可以进行读操作
     *             读的时候 哪个线程都不可以写
     * @param score
     * @param id
     * @throws InterruptedException
     */

    public void insert(Double score, Long id) throws InterruptedException {

        try {
            writeLock.lockInterruptibly();
            Node curr = findFirst(score);
            Node q = new Node(score, id);
            //要保证 socre和id的值不能都相同
            //也就是当score一样的时候 id不能一样
            if (curr.score != null && curr.score == q.score && curr.id != null && String.valueOf(curr.id).equals(String.valueOf(q.id))) {
                System.out.println("插入的值已经存在");
                return;
            }
                Thread.sleep(5000);
                q.right = curr.right;
                q.left = curr;
                curr.right.left = q;
                curr.right = q;

                int i = 0;//一开始在0层

                while (random.nextDouble() < 0.5) {
                    //判断是否需要增加层数
                    if (i >= level) {
                        Node p1 = new Node(null);
                        Node p2 = new Node(null);
                        p1.right = p2;
                        p1.down = head;
                        p2.left = p1;
                        p2.down = tail;
                        head.up = p1;
                        tail.up = p2;
                        head = p1;
                        tail = p2;
                        level++;
                    }

                    //常规的在上一层增加这个节点
                    while (curr.up == null) {
                        curr = curr.left;

                    }
                    curr = curr.up;
                    Node e = new Node(score);
                    e.left = curr;
                    e.right = curr.right;
                    curr.right.left = e;
                    curr.right = e;
                    e.down = q;
                    q.up = e;
                    q = e;
                    i++;
                }
                size++;
                //先获取读锁

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        }


    /**
     * 查找这个节点  这个节点是跳表最底层的节点
     *
     * @param score
     * @return
     */
    public Node search(Double score) {
        try {
            readLock.lockInterruptibly();
            Node p = head;
            while (true) {
                while (p.right.score != null && p.right.score <= score) {
                    p = p.right;
                }
                if (p.down != null) {
                    p = p.down;
                } else {
                    if (String.valueOf(p.score).equals(String.valueOf(score))) {
                        return p;
                    }
                    return null;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return null;

    }


    /**
     * 删除节点
     *
     * @param score
     */
    public void delete(Double score) {
        //先读锁
        Node temp = search(score);

        try {
            writeLock.lockInterruptibly();
            while (temp != null) {
                temp.left.right = temp.right;
                temp.right.left = temp.left;
                temp = tail.up;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }

    }


    /**
     * 打印当前跳表的信息
     * 从上往下打印
     */
    public void display() {
        try {
            readLock.lockInterruptibly();
            while (level >= 0) {
                Node p = head;
                while (p != null) {
                    System.out.println(p.score + "---------->");
                    p = p.right;
                }
                System.out.println();
                System.out.println("*******************");
                level--;
                head = head.down;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }

    }


    public void dumpAllDesc() {

        //
        try {
            readLock.lockInterruptibly();
            //读的时候 也是不可以写的
            //造成了死锁的现象

            writeLock.lockInterruptibly();


            Node newNode = tail;
            while (newNode.down != null) {
                newNode = newNode.down;
            }
            while (newNode.left.score != null) {
                System.out.println(newNode.left.score + "----->" + newNode.left.id);
                newNode = newNode.left;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();

        }


    }

    public void dumpTenDesc() {
        try {
            readLock.lockInterruptibly();
            Node newNode = tail;
            while (newNode.down != null) {
                newNode = newNode.down;
            }
            int i = 9;
            while (newNode.left.score != null && i >= 0) {
                System.out.println("happyBean是 " + newNode.left.score + "  用户的id是 " + newNode.left.id);
                newNode = newNode.left;
                i--;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }


    }

    public static void main(String[] args) throws InterruptedException {
        SkipList2 skipList2 = new SkipList2();
        skipList2.insert(111.0, 12L);


//        Thread thread1 = new Thread(new Runnable() {
//
//            @SneakyThrows
//            @Override
//            public void run() {
//                skipList2.insert(123.1, 12L);
//
//            }
//        });

        Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                skipList2.dumpAllDesc();

            }
        });

//        Thread thread3 = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                skipList2.dumpAllDesc();
//
//            }
//        });

        thread2.start();
        thread2.join();
        System.out.println("11111111");

        Thread.sleep(110000);
        System.out.println("over");


//        list.insert(12.2,13L);
//        list.insert(11.2,16L);
//        list.insert(10.2,143L);
//        list.insert(412.2,1334L);
//        list.insert(122.2,14333L);
//
//        System.out.println(list.level);
//        System.out.println(list.size);
//        System.out.println("-------------------------");
//
//        list.dempAllDesc();
//
//        list.insert(122.1,3323L);
//        list.dempAllDesc();
//
//        list.delete(12.2);
//        list.dempAllDesc();


    }
}