package com.melot.common.transaction.tcc;

import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.tcc.config.TccConfig;
import com.melot.common.transaction.tcc.coordinator.TccTransactionThreadPool;

@Component
public class TccAsyncCompensateService {
	
	@Autowired
	private TccConfig tccConfig;
	
	private ExecutorService executorService;
	
	@PostConstruct
	private void init()
	{
		int coreSize = tccConfig.getAsyncCompensateThreadCore();
		int maxSize = tccConfig.getAsyncCompensateThreadMax();
		executorService = TccTransactionThreadPool.newCustomCacheableThreadPool(
 				tccConfig, "asyncCompensateExecutor", coreSize, maxSize);
	}
	
	public void execute(Runnable r)
	{
		executorService.submit(r);
	}
}
