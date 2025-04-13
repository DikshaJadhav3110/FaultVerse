package com.mtnl.faultverse;

public class ExpandableAdminClass {
    private String ComplaintID;
    private String Service; //
    private String Date;
    private String CustomerName; //name
    private String ServiceID;
    private String CustomerID; //phn
    private String CustomerAddr; //addr
    private String Issue;
    private String StatusService;
    private String DP,RE,ADSL_IN,ADSL_OUT;
    private String ACStatus;
    private String ServiceType;
    public ExpandableAdminClass(String ID,String Date, String Service,String CustomerName,String ServiceID,String CustomerID,String CustomerAddr,String Issue,String StatusService,String DP,String RE, String ADSL_IN,String ADSL_OUT,String ACStatus,String ServiceType){
        this.ComplaintID= ID;
        this.Service = Service;
        this.Date= Date;
        this.CustomerName = CustomerName;
        this.ServiceID = ServiceID;
        this.CustomerID = CustomerID;
        this.CustomerAddr = CustomerAddr;
        this.Issue = Issue;
        this.StatusService = StatusService;
        this.DP = DP;
        this.RE = RE;
        this.ADSL_IN=ADSL_IN;
        this.ADSL_OUT=ADSL_OUT;
        this.ACStatus = ACStatus;
        this.ServiceType = ServiceType;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getACStatus() {
        return ACStatus;
    }

    public void setACStatus(String ACStatus) {
        this.ACStatus = ACStatus;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public void setCustomerAddr(String customerAddr) {
        CustomerAddr = customerAddr;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public String getStatusService() {
        return StatusService;
    }

    public void setStatusService(String statusService) {
        StatusService = statusService;
    }

    public String getDP() {
        return DP;
    }

    public void setDP(String DP) {
        this.DP = DP;
    }

    public String getRE() {
        return RE;
    }

    public void setRE(String RE) {
        this.RE = RE;
    }

    public String getADSL_IN() {
        return ADSL_IN;
    }

    public void setADSL_IN(String ADSL_IN) {
        this.ADSL_IN = ADSL_IN;
    }

    public String getADSL_OUT() {
        return ADSL_OUT;
    }

    public void setADSL_OUT(String ADSL_OUT) {
        this.ADSL_OUT = ADSL_OUT;
    }

    public String getComplaintID() {
        return ComplaintID;
    }

    public void setComplaintID(String ID) {
        this.ComplaintID = ID;
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

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerAddr() {
        return CustomerAddr;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getServiceID() {
        return ServiceID;
    }
}
