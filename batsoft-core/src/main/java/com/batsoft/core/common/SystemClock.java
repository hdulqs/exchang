package com.batsoft.core.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *      高并发场景下System.currentTimeMillis()的性能问题的优化
 *
 *
 */
public class SystemClock {
	
	private static final String THREAD_NAME = "system.clock";
    private static final SystemClock MILLIS_CLOCK = new SystemClock(1);
    private final long precision;
    private final AtomicLong now;

    private SystemClock(long precision) {
        this.precision = precision;
        now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    public static SystemClock millisClock() {
        return MILLIS_CLOCK;
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, THREAD_NAME);
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> 
		now.set(System.currentTimeMillis()), precision, precision, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return now.get();
    }
    
    public static void main(String[] args) {
        int times = Integer.MAX_VALUE;

        long start = System.currentTimeMillis();
        for (long i = 0; i < times; i++) {
            SystemClock.millisClock().now();
        }
        long end = System.currentTimeMillis();

        System.out.println("SystemClock Time:" + (end - start) + "毫秒");
        
        long beginTime = SystemClock.millisClock().now();
        long start2 = System.currentTimeMillis();
        for (long i = 0; i < times; i++) {
            System.currentTimeMillis();
        }
        long end2 = System.currentTimeMillis();
        long endTime = SystemClock.millisClock().now();
        System.out.println("SystemCurrentTimeMillis Time:" + (end2 - start2) + "毫秒");
        System.out.println("SystemClock Time :" + (endTime - beginTime) + "毫秒");
        
    }
    
}
