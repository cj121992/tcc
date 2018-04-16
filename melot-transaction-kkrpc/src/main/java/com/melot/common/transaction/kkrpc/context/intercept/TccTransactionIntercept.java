package com.melot.common.transaction.kkrpc.context.intercept;

import com.melot.common.melot_utils.JsonSerializable;
import com.melot.common.transaction.api.TccTransactionContext;
import com.melot.common.transaction.api.TccTransactionContextLocal;
import com.melot.module.kkrpc.core.api.RpcContext;
import com.melot.module.kkrpc.core.api.wrapper.filter.RpcContextSwapIntercept;

public class TccTransactionIntercept implements RpcContextSwapIntercept{

	private final String TCC_TRANSACTION_CONTEXT = "TCC_TRANSACTION_CONTEXT";
	
	@Override
	public String getName() {
		return "tcctransaction";
	}

	@Override
	public void swapInAsConsumer() {
		 final TccTransactionContext tccTransactionContext =
				 TccTransactionContextLocal.getInstance().get();
         if (tccTransactionContext != null) {
             RpcContext.getContext()
                     .setAttachment(TCC_TRANSACTION_CONTEXT,
                    		 JsonSerializable.toJson(tccTransactionContext, false));
         }
	}

	@Override
	public void swapOutAsProvider() {
		if(RpcContext.getContext().getAttachment(TCC_TRANSACTION_CONTEXT) != null)
		{
			String tccContextJsonStr = RpcContext.getContext().getAttachment(TCC_TRANSACTION_CONTEXT);
			TccTransactionContext tccTransContext = JsonSerializable.fromJson(tccContextJsonStr, TccTransactionContext.class);
			
			TccTransactionContextLocal.getInstance().set(tccTransContext);
		}
		else
		{
			TccTransactionContextLocal.getInstance().remove();
		}
	}

}
