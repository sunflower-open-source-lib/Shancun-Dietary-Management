package com.example.kylab.androidthingshx711.tools;

import java.util.List;

public class familydetail {
    private int code;
    private String msg;
    private FamilyBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FamilyBean getData() {
        return data;
    }

    public void setData(FamilyBean data) {
        this.data = data;
    }

    public class FamilyBean{
        private String fid;
        private List<member> user;

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public List<member> getUser() {
            return user;
        }

        public void setUser(List<member> user) {
            this.user = user;
        }

        public class member{
            private String role;
            private String userName;

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }

    }
}
