package com.cxm.pojo;

/**
 * @author cxm
 * @description
 * @date 2019-09-17 14:27
 **/
public class MyProxy {

    private Long id;
    private String ip;
    private int port;

    @Override
    public String toString() {
        return "MyProxy{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
