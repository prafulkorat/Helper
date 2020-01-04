package com.demo.parag.helper;

public class DataModel {
    private String title_requirement,serviceName,user_name,sub_service,title_address,user_address;



    public DataModel(String title_requirement, String serviceName, String user_name, String sub_service, String title_address, String user_address) {
        this.title_requirement = title_requirement;
        this.serviceName = serviceName;
        this.user_name = user_name;
        this.sub_service = sub_service;
        this.title_address = title_address;
        this.user_address = user_address;
    }

    public String getTitle_requirement() {
        return title_requirement;
    }

    public void setTitle_requirement(String title_requirement) {
        this.title_requirement = title_requirement;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSub_service() {
        return sub_service;
    }

    public void setSub_service(String sub_service) {
        this.sub_service = sub_service;
    }

    public String getTitle_address() {
        return title_address;
    }

    public void setTitle_address(String title_address) {
        this.title_address = title_address;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }
}
