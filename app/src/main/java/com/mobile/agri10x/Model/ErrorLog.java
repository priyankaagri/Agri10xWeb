package com.mobile.agri10x.Model;

public class ErrorLog {
    private String url;
    private String varName;
    private String reqDataType;
    private String time;
    private String functionName;
    private String device;
    private String pageName;

    public ErrorLog(String url, String varName, String reqDataType, String time, String functionName, String device, String pageName) {
        this.url = url;
        this.varName = varName;
        this.reqDataType = reqDataType;
        this.time = time;
        this.functionName = functionName;
        this.device = device;
        this.pageName = pageName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getReqDataType() {
        return reqDataType;
    }

    public void setReqDataType(String reqDataType) {
        this.reqDataType = reqDataType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getUniqueVal(ErrorLog e){
        String total="";
        total+=e.getDevice();
        total+=e.getFunctionName();
        total+=e.getPageName();
        total+=e.getReqDataType();
        total+=e.getUrl();
        total+=e.getVarName();
        return total;
    }
}
