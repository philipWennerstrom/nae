package com.aionemu.gameserver.utils;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.concurrent.PriorityThreadFactory;

/**
 * @author Rolandas
 */
public class WorkStealThreadFactory extends PriorityThreadFactory implements ForkJoinWorkerThreadFactory {

	public WorkStealThreadFactory(String namePrefix) {
		super(namePrefix, Thread.NORM_PRIORITY);
	}

	public void setDefaultPool(ForkJoinPool pool) {
		// In Java 8 and jsr166 common pool exists, not in Java 7
		/*
		if (pool == null)
			pool = ForkJoinPool.commonPool();
		*/
		super.setDefaultPool(pool);
	}

	public ForkJoinPool getDefaultPool() {
		return (ForkJoinPool) super.getDefaultPool();
	}

	@Override
	public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
		return new WorkStealThread(pool);
	}

	private static class WorkStealThread extends ForkJoinWorkerThread {

		private static final Logger log = LoggerFactory.getLogger(WorkStealThread.class);

		public WorkStealThread(ForkJoinPool pool) {
			super(pool);
		}

		@Override
		protected void onStart() {
			super.onStart();
		}

		protected void onTermination(Throwable exception) {
			if (exception != null)
				log.error("Error - Thread: " + this.getName() + " terminated abnormaly: " + exception);
			super.onTermination(exception);
		}
	}
}