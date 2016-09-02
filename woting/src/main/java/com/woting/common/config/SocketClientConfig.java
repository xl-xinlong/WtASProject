package com.woting.common.config;

import java.util.List;

/**
 * Socket连接客户端配置信息
 * @author wanghui
 */
public class SocketClientConfig {
    private int[] checkGotoP;
    private int[] checkGotoN;
	public String ip=GlobalConfig.socketUrl;
	public int port=GlobalConfig.socketport;
	public static long intervalBeat=15000; //心跳信号发送时间间隔
	public long intervalCheckSocket=10000; //检查Socket连接状况的时间间隔
	public static long expireTime=120000; //过期时间，多长时间未收到服务器消息，就认为连接中断了，这个时间要大于心跳和检查时间
	public List<String> reConnectWays; //重连策略
	
	public String getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}
	public long getIntervalBeat() {
		return intervalBeat;
	}
	public long getIntervalCheckSocket() {
		return intervalCheckSocket;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public List<String> getReConnectWays() {
		return reConnectWays;
	}
	public void setReConnectWays(List<String> reConnectWays) {
		this.reConnectWays=reConnectWays;
	}

	 /**
     * 获得重连接的间隔时间，和下一个策略号
     * @param index 策略号
     * @return 重连接的间隔时间 and 下一个策略号，中间用"::"隔开
     */
    public String getReConnectIntervalTimeAndNextIndex(int index) {
        checkGotoP=new int[reConnectWays.size()];
        for (int i=0; i<checkGotoP.length; i++) checkGotoP[i]=-1;
        checkGotoN=new int[reConnectWays.size()];
        for (int i=0; i<checkGotoN.length; i++) checkGotoN[i]=-1;
        return _getReConnectIntervalTimeAndNextIndex(index);
    }

    /*
     * 获得重连接的间隔时间，和下一个策略号
     * @param index 策略号
     * @return 重连接的间隔时间 and 下一个策略号，中间用"::"隔开
     */
    private String _getReConnectIntervalTimeAndNextIndex(int index) {
        if (index==-1||this.reConnectWays==null||this.reConnectWays.size()==0) return ("-1::60000");//默认重连间隔一分钟
        int _index = (index>=this.reConnectWays.size()?this.reConnectWays.size()-1:index);
        String ways=this.reConnectWays.get(_index);
        String[] _ws=ways.split("::");
        if (_ws[0].equals("INTE")) return _index+1+"::"+_ws[1];
        else if (_ws[0].equals("GOTO")) {//跳转到相应的
            int _gotoIndex=Integer.parseInt(_ws[1]);
            checkGotoP[getLastGotoIndex(checkGotoP)]=index;
            checkGotoN[getLastGotoIndex(checkGotoN)]=_gotoIndex;
            if (isLoopGoto()) return ("-1::60000"); //死循环了，返回默认值
            else return _getReConnectIntervalTimeAndNextIndex(_gotoIndex);
        } else {//出现问题，则到第一行
            return _getReConnectIntervalTimeAndNextIndex(0);
        }
    }
    
    private int getLastGotoIndex(int[] il) {
        for (int i=0; i<il.length; i++) {
            if (il[i]==-1) return i;
        }
        return -1;
    }

    private boolean isLoopGoto() {
    	for (int i=0; i<checkGotoN.length; i++) {
    		if (checkGotoN[i]!=-1) {
    			for (int j=0; j<checkGotoP.length; j++) {
    				if (checkGotoP[j]!=-1&&checkGotoN[i]==checkGotoP[j]) return true;
    			}
    		}
    	}
        return false;
    }
}