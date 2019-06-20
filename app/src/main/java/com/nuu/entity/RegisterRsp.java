package com.nuu.entity;

public class RegisterRsp {


    /**
     * code : 0
     * msg : success
     * data : {"uuid":"8010a6c1-14d8-4807-aff1-4684442444cb","username":"soulcure002","email":"colin001@qq.com","password":"123456"}
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
         * uuid : 8010a6c1-14d8-4807-aff1-4684442444cb
         * username : soulcure002
         * email : colin001@qq.com
         * password : 123456
         */

        private String uuid;
        private String username;
        private String email;
        private String password;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
