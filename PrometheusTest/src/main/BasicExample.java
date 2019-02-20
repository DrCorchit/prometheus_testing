package main;

import java.util.Random;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Gauge.Child;

public class BasicExample implements Runnable {
	
	static final Gauge GAUGE;
	static final Counter COUNTER;
	
	static {
		GAUGE = Gauge.build()
				.name("task_duration_seconds")
				.help("Testing prometheus gauge functionality")
				.labelNames("task1", "task2")
				.register();
		
		COUNTER = Counter.build()
				.name("requests_total")
				.help("The number of times run() was called.")
				.register();
	}
	
	@Override
	public void run() {
		//System.out.println("1");
		System.out.println("Execution "+COUNTER.get());
		
		COUNTER.inc();
		GAUGE.setToTime(
			new Runnable() {
				@Override
				public void run() {
					simulateProcessing();
				}
			});
		
		/*
		long start = System.currentTimeMillis();
		simulateProcessing();
		long mid = System.currentTimeMillis();
		simulateProcessing();
		long end = System.currentTimeMillis();
		double latency_task1 = .001 * (mid-start);
		double latency_task2 = .001 * (end-mid);
		//System.out.println("2");
		
		Child child1 = GAUGE.labels("task1");
		Child child2 = GAUGE.labels("task2");
		
		//System.out.println("3");
		child1.set(latency_task1);
		child2.set(latency_task2);
		
		System.out.printf("Completed task1 in %f seconds; task2 in %f seconds", latency_task1, latency_task2);
		*/
	}
	
	private void simulateProcessing() {
		Random r = new Random();
		long wait = (long) (r.nextDouble() * 2000);

		System.out.println(wait);
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
