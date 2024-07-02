package com.cn.pool;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import com.cn.NioServerBoss;
import com.cn.NioServerWorker;
/**
 * selector线程管理者
 * @author -琴兽-
 *
 */
public class NioSelectorRunnablePool {

	/**
	 * boss线程数组
	 */
	private final AtomicInteger bossIndex = new AtomicInteger();
	private Boss[] bosses;

	/**
	 * worker线程数组
	 */
	private final AtomicInteger workerIndex = new AtomicInteger();
	private Worker[] workeres;

	
	public NioSelectorRunnablePool(Executor boss_threads, Executor worker_threads) {
		initBoss(boss_threads, 1);
		initWorker(worker_threads, 2);
		//initWorker(worker, Runtime.getRuntime().availableProcessors() * 2);
	}

	/**
	 * 初始化boss线程
	 * @param boss_threads
	 * @param count
	 */
	private void initBoss(Executor boss_threads, int count) {
		this.bosses = new NioServerBoss[count];
		for (int i = 0; i < bosses.length; i++) {
			bosses[i] = new NioServerBoss(boss_threads, "boss thread " + (i+1), this);
		}

	}

	/**
	 * 初始化worker线程
	 * @param worker_threads
	 * @param count
	 */
	private void initWorker(Executor worker_threads, int count) {
		this.workeres = new NioServerWorker[count];
		for (int i = 0; i < workeres.length; i++) {
			workeres[i] = new NioServerWorker(worker_threads, "worker thread " + (i+1), this);
		}
	}

	/**
	 * 获取一个worker
	 * @return
	 */
	public Worker nextWorker() {
		 return workeres[Math.abs(workerIndex.getAndIncrement() % workeres.length)];

	}

	/**
	 * 获取一个boss
	 * @return
	 */
	public Boss nextBoss() {
		 return bosses[Math.abs(bossIndex.getAndIncrement() % bosses.length)];
	}

}
