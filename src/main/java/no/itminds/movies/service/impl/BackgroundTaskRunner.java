package no.itminds.movies.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BackgroundTaskRunner {
	
	final static Logger logger = LoggerFactory.getLogger(BackgroundTaskRunner.class);
	private long lastRegistrerdTime = System.currentTimeMillis();
	
	@Scheduled(fixedDelay = 1000*15)
	public void doSomething() {
		long currTime = System.currentTimeMillis();
		logger.info("Time in ms since last update:" + (currTime-lastRegistrerdTime));
		lastRegistrerdTime = currTime;
	}
}
