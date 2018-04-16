package com.melot.common.transaction.tcc.coordinator;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.melot.common.transaction.api.TccTransaction;

@Getter
@Setter
@AllArgsConstructor
public class CoordinatorAction implements Serializable{

	private static final long serialVersionUID = 4048225106338968210L;
	
	private CoordinatorActionType actionType;
	
	private TccTransaction tccTransaction;

}
