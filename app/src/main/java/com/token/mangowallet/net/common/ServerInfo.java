package com.token.mangowallet.net.common;

public class ServerInfo {
    private boolean isTest;
    private String kserverName;
    private String kserverApi;
    private String nodeBTC;
    private String nodeETH;
    private String nodeEOS;
    private String nodeMGP;
    private String kserverKey;

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public String getKserverName() {
        return kserverName;
    }

    public void setKserverName(String kserverName) {
        this.kserverName = kserverName;
    }

    public String getKserverApi() {
        return kserverApi;
    }

    public void setKserverApi(String kserverApi) {
        this.kserverApi = kserverApi;
    }

    public String getNodeBTC() {
        return nodeBTC;
    }

    public void setNodeBTC(String nodeBTC) {
        this.nodeBTC = nodeBTC;
    }

    public String getNodeETH() {
        return nodeETH;
    }

    public void setNodeETH(String nodeETH) {
        this.nodeETH = nodeETH;
    }

    public String getNodeEOS() {
        return nodeEOS;
    }

    public void setNodeEOS(String nodeEOS) {
        this.nodeEOS = nodeEOS;
    }

    public String getNodeMGP() {
        return nodeMGP;
    }

    public void setNodeMGP(String nodeMGP) {
        this.nodeMGP = nodeMGP;
    }

    public String getKserverKey() {
        return kserverKey;
    }

    public void setKserverKey(String kserverKey) {
        this.kserverKey = kserverKey;
    }
}
