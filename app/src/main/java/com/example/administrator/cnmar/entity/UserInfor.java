package com.example.administrator.cnmar.entity;

/**
 * Created by Administrator on 2016/9/1.
 */
public class UserInfor {

    /**
     * status : true
     * msg : 成功
     * data : {"id":1,"username":"admin","password":"c4ca4238a0b923820dcc509a6f75849b","name":"管理员","gender":"M","birthday":null,"isSuper":true,"isEnable":true,"imgId":0,"img":null}
     */

    private boolean status;
    private String msg;
    /**
     * id : 1
     * username : admin
     * password : c4ca4238a0b923820dcc509a6f75849b
     * name : 管理员
     * gender : M
     * birthday : null
     * isSuper : true
     * isEnable : true
     * imgId : 0
     * img : null
     */

    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
        private int id;
        private String username;
        private String password;
        private String name;
        private String gender;
        private Object birthday;
        private boolean isSuper;
        private boolean isEnable;
        private int imgId;
        private Object img;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public boolean isIsSuper() {
            return isSuper;
        }

        public void setIsSuper(boolean isSuper) {
            this.isSuper = isSuper;
        }

        public boolean isIsEnable() {
            return isEnable;
        }

        public void setIsEnable(boolean isEnable) {
            this.isEnable = isEnable;
        }

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }

        public Object getImg() {
            return img;
        }

        public void setImg(Object img) {
            this.img = img;
        }
    }
}
