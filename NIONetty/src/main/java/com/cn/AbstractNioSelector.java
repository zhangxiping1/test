package com.cn;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cn.pool.NioSelectorRunnablePool;

/**
 * 抽象selector线程类
 * 
 * @author -zxp-
 * 
 */
public abstract class AbstractNioSelector implements Runnable {

	/**
	 * 线程池
	 */
	private final Executor executor;

	/**
	 * 选择器
	 */
	protected Selector selector;

	/**
	 * 选择器wakenUp状态标记
	 */
	protected final AtomicBoolean wakenUp = new AtomicBoolean();

	/**
	 * 任务队列
	 */
	private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();

	/**
	 * 线程名称
	 */
	private String threadName;
	
	/**
	 * 线程管理对象
	 */
	protected NioSelectorRunnablePool selectorRunnablePool;

	AbstractNioSelector(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
		this.executor = executor;
		this.threadName = threadName;
		this.selectorRunnablePool = selectorRunnablePool;
		openSelector();
	}

	/**
	 * 获取selector并启动线程
	 */
	private void openSelector() {
		try {
			this.selector = Selector.open();
		} catch (IOException e) {
			throw new RuntimeException("Failed to create a selector.");
		}
		executor.execute(this);
	}

	@Override
	public void run() {
		
		Thread.currentThread().setName(this.threadName);
		int i=0;
		while (true) {
			try {
				System.out.println(this.threadName+"开始运行"+i);
				//wakenUp.set(false);
				select(selector);
				System.out.println(this.threadName+"select 收到事件"+i);
				processTaskQueue();
				process(selector);
				i=i+1;
			} catch (Exception e) {
				// ignore
			}
		}

	}

	/**
	 * 注册一个任务并激活selector
	 * 
	 * @param task
	 */
	protected final void registerTask(Runnable task) {
		taskQueue.add(task);

		Selector selector = this.selector;

		if (selector != null) {
			//if (wakenUp.compareAndSet(false, true)) {
				selector.wakeup();
			//}
		} else {
			taskQueue.remove(task);
		}
	}

	/**
	 * 执行队列里的任务
	 */
	private void processTaskQueue() {
		for (;;) {
			final Runnable task = taskQueue.poll();
			if (task == null) {
				System.out.println(this.threadName+"无任务可处理");
				break;
			}
			task.run();
			System.out.println(this.threadName+"处理了一个任务");
		}
	}
	
	/**
	 * 获取线程管理对象
	 * @return
	 */
	public NioSelectorRunnablePool getSelectorRunnablePool() {
		return selectorRunnablePool;
	}

	/**
	 * select抽象方法
	 * 
	 * @param selector
	 * @return
	 * @throws IOException
	 */
	protected abstract int select(Selector selector) throws IOException;

	/**
	 * selector的业务处理
	 * 
	 * @param selector
	 * @throws IOException
	 */
	protected abstract void process(Selector selector) throws IOException;

}
