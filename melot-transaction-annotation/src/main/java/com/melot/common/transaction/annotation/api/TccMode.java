package com.melot.common.transaction.annotation.api;

public enum TccMode {
	/**
     * Tcc tcc mode enum.
     */
    TCC(1, "try,confirm,cancel模式"),

    /**
     * Cc tcc mode enum.
     */
    CC(2, "confirm,cancel模式");

    private int mode;

    private String desc;

    TccMode(int mode, String desc) {
        this.mode = mode;
        this.desc = desc;
    }

    public static TccMode valueOf(int mode)
    {
    	switch(mode)
    	{
    	case 1:
    		return TCC;
    	default:
    		return CC;
    	}
    }
    
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
