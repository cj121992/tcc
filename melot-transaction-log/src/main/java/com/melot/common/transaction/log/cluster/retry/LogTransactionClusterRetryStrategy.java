package com.melot.common.transaction.log.cluster.retry;

import java.lang.reflect.Method;

import com.melot.common.transaction.annotation.LogTransactional;
import com.melot.common.transaction.api.LogTransactionContext;
import com.melot.common.transaction.api.LogTransactionContextLocal;
import com.melot.module.kkrpc.cluster.ClusterRetryStrategy;
import com.melot.module.kkrpc.core.api.CglibProxyFactory;
import com.melot.module.kkrpc.core.api.Invoker;
import com.melot.module.kkrpc.core.api.RpcException;
import com.melot.module.kkrpc.core.api.RpcRequest;

public class LogTransactionClusterRetryStrategy implements ClusterRetryStrategy{


	@Override
	public <T> boolean isRetriable(Invoker<T> invoker, RpcRequest request,
			RpcException lastException) {
		if(lastException != null)
		{
			if(lastException.isTimeout() || lastException.isSerialization())
			{
				LogTransactionContext tccTransactionContext = LogTransactionContextLocal.getInstance().get();
				if(tccTransactionContext != null)
				{
					Class<T> cls = invoker.getInterface();
					Method method = CglibProxyFactory.getOnlyMethodByName(cls, request.getMethodName());
					LogTransactional logTransaction = method.getAnnotation(LogTransactional.class);
					if(logTransaction != null)
					{
						return false;
					}
				}
			}
		}
		return true;
	}

}
