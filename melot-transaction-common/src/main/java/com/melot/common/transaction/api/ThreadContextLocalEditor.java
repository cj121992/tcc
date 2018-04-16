package com.melot.common.transaction.api;

import java.util.Map;

import com.google.common.collect.Maps;
import com.melot.common.melot_utils.TenantContext;

public class ThreadContextLocalEditor {
	
	private static final String TENANT_ID_ATTR_NAME = "TENANT_ID";
	
	public Map<String, String> getLocalContextAttachments()
	{
		Map<String, String> attachments = Maps.newHashMap();
		Integer tenantId = TenantContext.getContext().getTenantId();
		if(tenantId != null)
		{
			attachments.put(TENANT_ID_ATTR_NAME, tenantId.toString());
		}
		
		return attachments;
	}
	
	public void setLocalContextFromAttachments(Map<String, String> attachments)
	{
		if(attachments != null)
		{
			if(attachments.containsKey(TENANT_ID_ATTR_NAME))
			{
				Integer tenantId = Integer.valueOf(attachments.get(TENANT_ID_ATTR_NAME));
				TenantContext.getContext().setTenantId(tenantId);
			}
		}
	}
}
