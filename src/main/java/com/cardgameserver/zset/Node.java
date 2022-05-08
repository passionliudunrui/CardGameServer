package com.cardgameserver.zset;


/**
 * 跳表中的节点
 *
 *1.解决方案的性能如何和实现难度
 * (1)直接从mysql中按照happybean进行排序 性能很差 千行数据 10ms
 * (2)将所有数据存放到内存中，进行排序算法 快排 堆排序 nlogn
 * (3)红黑树和搜索二叉树   左旋右旋复杂度 加锁的粒度  算法的实现难度  是否支持部分数据返回 nlogn
 * (4)跳表  nlogn   优化hashmap中直接 是id和Node   删除的时候 将Node.pre=node.left  删除上面的节点 其实是麻烦  hasmap中 id和score  直接根据score删除节点
 *
 *2.如何实现实时更新数据
 * 通过在内存中设置了skiplist和concurrentmap来进行映射
 *
 * 3.如何保证线程安全
 *(1)synchronized 性能比较差
 *(2)reentrantLock 性能比较差
 *(3)reentrantReadWriteLock  支持读写锁的分离 读读不阻塞 读写阻塞
 *(4)再次优化  使用了cas的算法 更能体现树跳表和红黑树的优化  加锁问题
 *
 * 4.分数相同怎么解决
 *
 * 先获取这个分数的人排名高   1-状态修改的时间long
 * 一种复杂的业务场景： 前十名中  7 8 9 10 11都相同显示哪个数据呢
 * 根据插入数据库的时间 获得插入的数据的id  然后小数点后面就是这个id
 * 然后再次判断 score的值 根据这个值进行返回
 * 业务发生了改变  通过时间-->long类型来修改
 *
 *
 * 5.缓存一致性怎么保证
 * 其实在数据库插入的时候使用了Comparable 然后能够异步的获取插入数据库的
 * 结果 然后添加一个 whenCompare 在这个语法中判断
 * 在充值成功然后 commit之后对缓存中的数据进行修改
 *
 * 6.为什么不使用redis
 * (1)项目迭代发展
 * (2)要考虑缓存不一致的问题 如果jvm都出现问题的话 那就说明整个的项目有重大的问题  网络的延迟问题
 * (3)重要的不是使用某个中间件 而是去学习去了解核心的思想 如果我能把跳表写一遍 或者是照着源码抄一遍也比不清楚原理强
 *
 *
 * */
public class Node {
    public  Double score;
    public Long id;

    public Node left;
    public Node right;
    public Node up;
    public Node down;

    public Node(Double score){
        this.score=score;
        left=up=right=down=null;
    }

    public Node(Double score,Long id){
        this.score=score;
        this.id=id;
        up=down=left=right=null;
    }


}
