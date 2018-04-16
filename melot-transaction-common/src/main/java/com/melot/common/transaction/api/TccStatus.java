package com.melot.common.transaction.api;

public enum TccStatus {
	PRE_TRY(0,"开始执行try"),


    /**
     * Trying tcc action enum.
     */
    TRYING(1, "try阶段完成"),


    /**
     * Confirming tcc action enum.
     */
    CONFIRMING(2, "confirm阶段"),


    /**
     * Canceling tcc action enum.
     */
    CANCELING(3, "cancel阶段");


    private int status;

    private String desc;

    TccStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    public static TccStatus valueOf(int status) {
    	 switch (status) {
    	 case 0:
    		 return PRE_TRY;
         case 1:
             return TRYING;
         case 2:
             return CONFIRMING;
         default:
             return CANCELING;
     }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
