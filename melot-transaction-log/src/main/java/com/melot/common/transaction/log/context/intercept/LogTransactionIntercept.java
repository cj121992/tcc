package com.melot.common.transaction.log.context.intercept;

import com.melot.common.melot_utils.JsonSerializable;
import com.melot.common.melot_utils.StringUtils;
import com.melot.common.transaction.api.LogTransactionContext;
import com.melot.common.transaction.api.LogTransactionContextLocal;
import com.melot.module.kkrpc.core.api.RpcContext;
import com.melot.module.kkrpc.core.api.wrapper.filter.RpcContextSwapIntercept;

public class LogTransactionIntercept implements RpcContextSwapIntercept{

	public static final String TRANSACTION_LOG_KEY = "transactionLog";
	
	public static final String DEFAULT_VALID_EVENT = "1";
	public static final String DEFAULT_EVENT_EXTENDER = ".0";

	@Override
	public String getName() {
		return "LogTransaction";
	}

	@Override
	public void swapInAsConsumer() {
		
		 final LogTransactionContext logTransactionContext =
				 LogTransactionContextLocal.getInstance().get();
         if (logTransactionContext != null) {
        	String curEventSeqId = logTransactionContext.getTransEventSeqId();
        	if(StringUtils.isEmpty(curEventSeqId))
 			{
        		curEventSeqId = DEFAULT_VALID_EVENT;
 			}
 			String remoteEventSeqId = curEventSeqId + DEFAULT_EVENT_EXTENDER;
 			
 			LogTransactionContext childLogTransactionContext = new LogTransactionContext();
 			childLogTransactionContext.setTransOrderId(logTransactionContext.getTransOrderId());
 			childLogTransactionContext.setTransEventSeqId(remoteEventSeqId);
 			
            RpcContext.getContext()
                     .setAttachment(TRANSACTION_LOG_KEY,
                    		 JsonSerializable.toJson(childLogTransactionContext, false));
         }
         
	}

	@Override
	public void swapOutAsProvider() {

		if(RpcContext.getContext().getAttachment(TRANSACTION_LOG_KEY) != null)
		{
			String logContextJsonStr = RpcContext.getContext().getAttachment(TRANSACTION_LOG_KEY);
			LogTransactionContext logTransContext = JsonSerializable.fromJson(logContextJsonStr, LogTransactionContext.class);
			
			LogTransactionContextLocal.getInstance().set(logTransContext);
		}
		else
		{
			LogTransactionContextLocal.getInstance().remove();
		}
	}

}
