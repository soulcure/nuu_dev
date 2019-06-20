package com.nuu.entity;

public class LoginRsp {


    /**
     * code : 0
     * msg : success
     * data : {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjEyNTI4MTgsInV1aWQiOiJhZjNjNmM4Yy03NjBiLTQxNTctOWQ1MS00NWNlYjhhY2FkNTEifQ.YpqjwGCax8YY5BXSyfS2bfsLKw-heK-_HKvd4Jo6vqg","uuid":"af3c6c8c-760b-4157-9d51-45ceb8acad51"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return code == 0;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjEyNTI4MTgsInV1aWQiOiJhZjNjNmM4Yy03NjBiLTQxNTctOWQ1MS00NWNlYjhhY2FkNTEifQ.YpqjwGCax8YY5BXSyfS2bfsLKw-heK-_HKvd4Jo6vqg
         * uuid : af3c6c8c-760b-4157-9d51-45ceb8acad51
         */

        private String token;
        private String uuid;
        private long expired;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public long getExpired() {
            return expired;
        }

        public void setExpired(long expired) {
            this.expired = expired;
        }
    }
}
