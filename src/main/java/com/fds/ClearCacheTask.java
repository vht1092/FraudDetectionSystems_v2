package com.fds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;

import com.fds.components.CaseDetail;

public class ClearCacheTask {
	@Autowired
	private CacheManager cacheManager;
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseDetail.class);

	/**
	 * Xoa het cache luc 0h moi ngay
	 */
	@Scheduled(cron = "0 30 1 * * *")
	public void reportCurrentTime() {
		cacheManager.getCacheNames().parallelStream().forEach(name -> {
			cacheManager.getCache(name).clear();
			LOGGER.info("Clean all cache......");
		});

	}
}
