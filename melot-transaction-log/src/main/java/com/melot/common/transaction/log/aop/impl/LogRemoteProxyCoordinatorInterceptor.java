package com.melot.common.transaction.log.aop.impl;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.annotation.api.Propagation;
import com.melot.common.transaction.api.LogTransactionContext;
import com.melot.common.transaction.api.LogTransactionContextLocal;
import com.melot.common.transaction.api.ThreadContextLocalEditor;
import com.melot.common.transaction.api.TransactionLog;
import com.melot.common.transaction.log.LogTransactionService;
import com.melot.common.transaction.log.aop.impl.LogTransactionHandler.TransactionProperties;
import com.melot.common.transaction.utils.MethodAnnotationUtil;
import com.melot.module.kkrpc.core.api.coordinator.ProxyCoordinatorInterceptor;
import com.melot.module.kkrpc.core.api.coordinator.ProxyCoordinatorInterceptorManager;

@Component
public class LogRemoteProxyCoordinatorInterceptor implements
		ProxyCoordinatorInterceptor {

	@Autowired
	private LogTransactionService logTransactionService;

	private ThreadContextLocalEditor threadContextLocalEditor = new ThreadContextLocalEditor();

	@Autowired
	private LogTransactionHandler logTransactionHandler;
	
	@PostConstruct
	private void init() {
		ProxyCoordinatorInterceptorManager.getInstance()
				.registerProxyCoordinatorInterceptor(this);
	}

	@Override
	public Object interceptProxyCoordinatorMethod(ProceedingJoinPoint pjp)
			throws Throwable {
		if (logTransactionService.getKkhistory() == null) {
			return pjp.proceed();
		}

		Object retObj = null;
		Method method = MethodAnnotationUtil.getAnnotationedMethod(pjp);

		TransactionProperties transProps = logTransactionHandler.handle(method);

		if (transProps == null) {
			return pjp.proceed();
		}

		Propagation propagation = transProps.getPropagation();
		if (propagation.equals(Propagation.PROPAGATION_SUPPORTS)
				|| propagation.equals(Propagation.PROPAGATION_REQUIRED)) {
			final LogTransactionContext logTransactionContext = LogTransactionContextLocal
					.getInstance().get();
			if (logTransactionContext != null
					&& logTransactionService.getKkhistory().isStartTrans()) {
				Map<String, String> attachments = threadContextLocalEditor
						.getLocalContextAttachments();
				TransactionLog transactionLog = logTransactionService
						.createTransactionLog(pjp, attachments);
				retObj = logTransactionService.processWithEventLog(pjp,
						transProps.getModule(), transactionLog);
			} else {
				retObj = pjp.proceed();
			}
		} else {
			retObj = pjp.proceed();
		}
		return retObj;
	}

}
