package com.example.kylab.androidthingshx711.tools;

import java.util.LinkedList;

public class MyQueue {
    private LinkedList list = new LinkedList();

    public void clear()//销毁队列
    {
        list.clear();
    }

    public synchronized boolean QueueEmpty()//判断队列是否为空
    {
        return list.isEmpty();
    }

    public synchronized void enQueue(Object o)//进队
    {
        list.addLast(o);
    }

    public synchronized Object deQueue()//出队
    {
        if (!list.isEmpty()) {
            return list.removeFirst();
        }
        return "队列为空";
    }

    public int QueueLength()//获取队列长度
    {
        return list.size();
    }

    public Object QueuePeek()//查看队首元素
    {
        return list.getFirst();
    }
}