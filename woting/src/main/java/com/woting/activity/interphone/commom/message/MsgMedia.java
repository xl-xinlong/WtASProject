package com.woting.activity.interphone.commom.message;

import com.woting.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


/**
 * 媒体消息：媒体流数据
 * @author wanghui
 */
public class MsgMedia extends Message {
    private final static int COMPACT_LEN=36;//若删除ObjId，则这个值为24

    private int mediaType; //流类型:1音频2视频
    private int bizType; //流业务类型:0对讲组；1电话
    private String talkId; //会话Id，或一次媒体传输的信道编号
    private int seqNo; //流中包的序列号
    private int returnType; //返回消息类型
    private byte[] mediaData=null; //包数据

    private Object extInfo=null;//扩展信息，这个信息是不参与传输的
    public Object getExtInfo() {
        return extInfo;
    }
    public void setExtInfo(Object extInfo) {
        this.extInfo = extInfo;
    }

    //以下信息在TCP原消息格式中有意义，在新的消息传输模型中需要删掉（不管是用TCP还是UDP）
    private String objId; //在组对讲中是组Id,在电话对讲中是电话通话Id
    public String getObjId() {
        return objId;
    }
    public void setObjId(String objId) {
        this.objId=objId;
    }

    /**
     * 空构造函数
     */
    public MsgMedia() {
    }

    /**
     * 通过字节数组构造消息
     * @param binaryMsg 字节数组
     */
    public MsgMedia(byte[] binaryMsg) throws Exception {
        this.fromBytes(binaryMsg);
    }

    public int getMediaType() {
        return mediaType;
    }
    public void setMediaType(int mediaType) {
        this.mediaType=mediaType;
    }

    public int getBizType() {
        return bizType;
    }
    public void setBizType(int bizType) {
        this.bizType=bizType;
    }

    public String getTalkId() {
        return talkId;
    }
    public void setTalkId(String talkId) {
        this.talkId=talkId;
    }

    public int getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(int seqNo) {
        this.seqNo=seqNo;
    }

    public int getReturnType() {
        return returnType;
    }
    public void setReturnType(int returnType) {
        this.returnType=returnType;
    }

    public byte[] getMediaData() {
        return mediaData;
    }
    public void setMediaData(byte[] mediaData) {
        this.mediaData=mediaData;
    }

    @Override
    public void fromBytes(byte[] binaryMsg) throws Exception{
        if (MessageUtils.decideMsg(binaryMsg)!=1) throw new Exception("消息类型错误！");

        int _offset=2;
        byte f1=binaryMsg[_offset++];
        this.setMsgType(((f1&0x80)==0x80)?1:0);
        this.setAffirm(((f1&0x40)==0x40)?1:0);

        if (affirm==1&&msgType==1) throw new Exception("消息格式异常：回复消息不需要确认！");
        if (msgType==1&&binaryMsg.length!=COMPACT_LEN+1) throw new Exception("消息格式异常：回复消息长度错误！");

        if ((f1&0x30)==0x10) this.setFromType(1);//服务器
        else
        if ((f1&0x30)==0x20) this.setFromType(0);//设备
        else
        throw new Exception("消息from位异常！");

        if ((f1&0x0C)==0x04) this.setToType(1);//服务器
        else
        if ((f1&0x0C)==0x08) this.setToType(0);//设备
        else
        throw new Exception("消息to位异常！");

        if ((f1&0x03)==0x01) this.setMediaType(1);//音频
        else
        if ((f1&0x02)==0x02) this.setMediaType(2);//视频
        else
        throw new Exception("消息媒体类型位异常！");

        this.setBizType(binaryMsg[_offset++]);

        byte[] _tempBytes=Arrays.copyOfRange(binaryMsg, _offset, _offset+8);//ByteBuffer.wrap(binaryMsg, _offset, 8).array();
        this.setSendTime(ByteConvert.bytes2long(_tempBytes));

        _offset+=8;
        String _tempStr;
        try {
            _tempStr=MessageUtils.parse_String(binaryMsg, _offset, 8, null);
        } catch(Exception e) {
            throw new Exception("消息会话Id异常！", e);
        }
        String[] _sa=_tempStr.split("::");
        if (_sa.length!=2) throw new Exception("消息会话Id异常！");
        if (Integer.parseInt(_sa[0])==-1) throw new Exception("消息会话Id异常！");
        _offset=Integer.parseInt(_sa[0]);
        this.setTalkId(_sa[1]);

        _tempBytes=Arrays.copyOfRange(binaryMsg, _offset, _offset+4);
        this.setSeqNo(ByteConvert.bytes2int(_tempBytes));

        _offset+=4;
        //objId，可能需要删除掉
        try {
            _tempStr=MessageUtils.parse_String(binaryMsg, _offset, 12, null);
        } catch(Exception e) {
            throw new Exception("对象Id异常！", e);
        }
        _sa=_tempStr.split("::");
        if (_sa.length!=2) throw new Exception("对象Id异常！");
        if (Integer.parseInt(_sa[0])==-1) throw new Exception("对象Id异常！");
        _offset=Integer.parseInt(_sa[0]);
        this.setObjId(_sa[1]);
        //删除结束

        if (isAck()) this.setReturnType(binaryMsg[_offset]);
        else {
            short len=(short)(((binaryMsg[_offset+1]<<8)|binaryMsg[_offset]&0xff));
            if (len>0) mediaData=Arrays.copyOfRange(binaryMsg, _offset+2, _offset+2+len);
        }
    }

    @Override
    public byte[] toBytes() throws Exception {
        int _offset=0;
        byte[] ret=new byte[_MAXLENGTH];
        byte zeroByte=0;

        ret[_offset++]=BEGIN_MDA[0];
        ret[_offset++]=BEGIN_MDA[1];

        if (msgType==1) zeroByte|=0x80;
        if (affirm==1) zeroByte|=0x40;
        zeroByte|=(fromType==1?0x10:0x20);
        zeroByte|=(toType==1?0x04:0x08);
        zeroByte|=(mediaType==1?0x01:0x02);
        ret[_offset++]=zeroByte;

        ret[_offset++]=(byte)bizType;

        byte[] _tempBytes=ByteConvert.long2bytes(sendTime);
        int i=0;
        for (; i<8; i++) ret[_offset++]=_tempBytes[i];

        if (StringUtils.isNullOrEmptyOrSpace(talkId)) throw new Exception("媒体消息异常：未设置有效会话id！");
        try {
            _offset=MessageUtils.set_String(ret, _offset, 8, talkId, null);
        } catch (UnsupportedEncodingException e) {
        }

        _tempBytes=ByteConvert.int2bytes(seqNo);
        for (i=0; i<4; i++) ret[_offset++]=_tempBytes[i];

        //为objId，要删除掉
        _tempBytes=objId.getBytes();
        for (i=0; i<12; i++) ret[_offset++]=_tempBytes[i];
        //为objId，要删除掉

        if (msgType==1) ret[_offset++]=(byte)returnType;

        if (!isAck()) {
            short len=(short)(mediaData==null?0:mediaData.length);
            System.out.println("消息体长度::"+len);
            ret[_offset++]=(byte)(len>>0);
            ret[_offset++]=(byte)(len>>8);

            if (mediaData!=null) {
                for (i=0; i<mediaData.length; i++) ret[_offset++]=mediaData[i];
            }
        }

        byte[] _ret=Arrays.copyOfRange(ret, 0, _offset);
        return _ret;
    }

    /**
     * 判断是否是应答消息
     */
    public boolean isAck() {
        return affirm==0&&msgType==1;
    }
}