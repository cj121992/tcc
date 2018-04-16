package com.melot.common.transaction.kkrpc.aop;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.annotation.TccTransactional;
import com.melot.common.transaction.annotation.api.TccMode;
import com.melot.common.transaction.api.Participant;
import com.melot.common.transaction.api.TccInvocation;
import com.melot.common.transaction.api.TccStatus;
import com.melot.common.transaction.api.TccTransactionContext;
import com.melot.common.transaction.api.TccTransactionContextLocal;
import com.melot.common.transaction.api.ThreadContextLocalEditor;
import com.melot.common.transaction.exception.TccRuntimeException;
import com.melot.common.transaction.tcc.TccTransactionManager;
import com.melot.common.transaction.tcc.config.TccConfig;
import com.melot.common.transaction.utils.MethodAnnotationUtil;
import com.melot.module.kkrpc.core.api.coordinator.MethodProceedingJoinPoint;
import com.melot.module.kkrpc.core.api.coordinator.ProxyCoordinatorInterceptor;
import com.melot.module.kkrpc.core.api.coordinator.ProxyCoordinatorInterceptorManager;


@Component
public class RemoteInvokerProxyCoordinatorInterceptor implements ProxyCoordinatorInterceptor{

	@Autowired
	private TccTransactionManager tccTransactionManager;
	
	@Autowired
	private TccConfig tccConfig;
	
	@PostConstruct
	private void init()
	{
		ProxyCoordinatorInterceptorManager.getInstance().registerProxyCoordinatorInterceptor(this);
	}
	
	@Override
	public Object interceptProxyCoordinatorMethod(ProceedingJoinPoint pjp) throws Throwable {
		
		Object retObj = pjp.proceed();
		if(pjp instanceof MethodProceedingJoinPoint)
		{
			MethodProceedingJoinPoint mpjp = (MethodProceedingJoinPoint)pjp;
			Method method = MethodAnnotationUtil.getAnnotationedMethod(mpjp);
			
			TccTransactional tcc = method.getAnnotation(TccTransactional.class);
			if(tcc != null)
			{
				Participant participant = buildParticipant(tcc, method, mpjp.getInterfaceClass(),
						mpjp.getArgs(), method.getParameterTypes());
				if(participant != null)
				{
					tccTransactionManager.enlistParticipant(participant);
				}
			}
		}
		return retObj;
	}
	
	@SuppressWarnings({ "rawtypes" })
    private Participant buildParticipant(TccTransactional tcc, Method method,
    		Class clazz, Object[] arguments, Class... args) throws TccRuntimeException {

		TccTransactionContext tccTransactionContext = TccTransactionContextLocal.getInstance().get();
		
        if (tccTransactionContext != null) {
            if (TccStatus.TRYING.getStatus() == tccTransactionContext.getStatus()) {
                //获取协调方法, 对于rpc调用方来说协调方法只能是接口发起调用的方法
                String confirmMethodName = method.getName();
                String cancelMethodName = method.getName();

                //设置模式
                final TccMode mode = tcc.mode();

                tccTransactionManager.getCurrentTransaction().setMode(mode.getMode());

                ThreadContextLocalEditor editor = tccConfig.getThreadContextLocalEditor();
                
                TccInvocation confirmInvocation = new TccInvocation(clazz,
                        confirmMethodName, args, arguments,
                        editor.getLocalContextAttachments());

                TccInvocation cancelInvocation = new TccInvocation(clazz,
                        cancelMethodName, args, arguments,
                        editor.getLocalContextAttachments());
                //封装调用点
                return new Participant(
                        tccTransactionContext.getTransactionId(),
                        confirmInvocation,
                        cancelInvocation);
            }

        }
        return null;
    }

}
