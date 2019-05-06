package com.nuu.socket;

/**
 * 命令码      包体长度    包体参数
 * 2Bytes	    2Bytes	   NByte
 */
public class PduBase {

    /****************************************************
     * basic unit of data type length
     */
    public static final int PDU_BODY_LENGTH_INDEX = 2; //标识包体数据长度的位置索引
    public static final int PDU_HEADER_LENGTH = 4;  //包头长度 （包头固定数据域长度总和，不包括动态数据域）

    /****************************************************
     * index 0. pos:[0-2) 命令码
     */
    public short commandId;

    /****************************************************
     * index 1. pos:[2-4) 参数长度
     * 帧长度：起始域到校验和域整个报文长度
     */
    public short length;


    /****************************************************
     * index 3. pos:[4-4+n) 参数
     */
    public byte[] body;

}


