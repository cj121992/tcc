package com.melot.common.transaction.kkrpc.cluster.retry;

import java.lang.reflect.Method;

import com.melot.common.transaction.annotation.TccTransactional;
import com.melot.common.transaction.api.TccTransactionContext;
import com.melot.common.transaction.api.TccTransactionContextLocal;
import com.melot.module.kkrpc.cluster.ClusterRetryStrategy;
import com.melot.module.kkrpc.core.api.CglibProxyFactory;
import com.melot.module.kkrpc.core.api.Invoker;
import com.melot.module.kkrpc.core.api.RpcException;
import com.melot.module.kkrpc.core.api.RpcRequest;

public class KkrpcTransactionClusterRetryStrategy implements ClusterRetryStrategy{


	@Override
	public <T> boolean isRetriable(Invoker<T> invoker, RpcRequest request,
			RpcException lastException) {
		if(lastException != null)
		{
			if(lastException.isTimeout() || lastException.isSerialization())
			{
				TccTransactionContext tccTransactionContext = TccTransactionContextLocal.getInstance().get();
				if(tccTransactionContext != null)
				{
					Class<T> cls = invoker.getInterface();
					Method method = CglibProxyFactory.getOnlyMethodByName(cls, request.getMethodName());
					TccTransactional tcc = method.getAnnotation(TccTransactional.class);
					if(tcc != null)
					{
						return false;
					}
				}
			}
		}
		return true;
	}

}
