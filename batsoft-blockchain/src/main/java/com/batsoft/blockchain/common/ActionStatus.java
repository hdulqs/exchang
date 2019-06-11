package com.batsoft.blockchain.common;

public enum ActionStatus {

    ACTION_NO_START(0,"活动未开始"),
    ACTIONING(1,"活动中"),
    ACTION_END(2,"活动已经结束");

    private int status;
    private String descripe;
    ActionStatus(int status,String descripe){
        this.status = status;
        this.descripe = descripe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescripe() {
        return descripe;
    }

    public void setDescripe(String descripe) {
        this.descripe = descripe;
    }
}
