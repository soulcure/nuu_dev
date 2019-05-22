package com.nuu.socket;

import android.util.Log;


import com.nuu.nuuinfo.BuildConfig;
import com.nuu.util.HexUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public abstract class PduUtil {

    private static final String TAG = "TcpClient";

    public abstract void OnRec(PduBase pduBase);

    public abstract void OnCallback(PduBase pduBase);

    public int ParsePdu(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        if (buffer.limit() >= PduBase.PDU_HEADER_LENGTH) {
            //has full header
            int totalLength = PduBase.PDU_HEADER_LENGTH + buffer.getShort(PduBase.PDU_BODY_LENGTH_INDEX);
            if (totalLength <= buffer.limit()) {
                //has a full pack.
                byte[] packByte = new byte[totalLength];
                buffer.get(packByte);
                PduBase pduBase = buildPdu(packByte);
                buffer.compact();//compact()方法只会清除已经读过的数据
                //read to read.
                buffer.flip();  //准备从缓冲区中读取数据

                if (pduBase != null) {
                    OnRec(pduBase);
                }

                return totalLength;
            } else {
                Log.v(TAG, "包头长度符合，包体长度未读完，继续读socket");
                buffer.position(buffer.limit());
                buffer.limit(buffer.capacity());
                return -1;
            }

        } else {
            Log.v(TAG, "包头长度未读完，继续读socket");
            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
            return -1;
        }
    }


    private PduBase buildPdu(byte[] bytes) {
        PduBase units = new PduBase();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);//准备从缓冲区中读取数据
        buffer.flip();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "tcp rec package msgType:" + buffer.get(1));
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            Log.d(TAG, "tcp rec buffer:" + HexUtil.bytes2HexString(data));
            buffer.flip();
        }

        short msgType = buffer.getShort();
        short length = buffer.getShort();

        units.msgType = msgType;
        units.length = length;

        Log.d(TAG, "tcp rec package params Length:" + length);

        if (length > 0) {
            units.body = new byte[length];
            buffer.get(units.body);
        }

        return units;
    }


    public ByteBuffer serializePdu(PduBase req) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(PduBase.PDU_HEADER_LENGTH + req.length);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putShort(req.msgType);
        byteBuffer.putShort(req.length);
        if (req.body != null) {
            byteBuffer.put(req.body);
        }
        return byteBuffer;

    }


}
