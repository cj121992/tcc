package com.melot.common.transaction.tcc;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.tcc.config.TccConfig;
import com.melot.common.transaction.tcc.coordinator.CoordinatorService;

@Component
@Slf4j
public class TccTransactionBoostrap{

	@Autowired
	private TccConfig tccConfig;
	
	@Autowired
	private CoordinatorService coordinatorService;
	
	@PostConstruct
	private void init()
	{
		try {
			coordinatorService.start();
		} catch (Exception e) {
			log.error("Tcc transaction init failed", e);
		}
		log.info("Tcc transaction init successfully！");
	}
}
