package com.mtnl.faultverse;

public class ExpandableClass {
    private String ID;
    private String Service;
    private String Date;
    private String Remark;
    private String Issue;
    private String CDate;
    public ExpandableClass(String ID,String Service, String Date,String Remark,String CDate,String Issue){
        this.ID= ID;
        this.Service = Service;
        this.Date= Date;
        this.Remark = Remark;
        this.CDate = CDate;
        this.Issue = Issue;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCDate() {
        return CDate;
    }

    public void setCDate(String CDate) {
        this.CDate = CDate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
