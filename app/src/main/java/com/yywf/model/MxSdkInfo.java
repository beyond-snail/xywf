package com.yywf.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class MxSdkInfo implements Serializable{

    private Arguments arguments;


    public Arguments getArguments() {
        return arguments;
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public class Arguments implements Serializable{
        private String username;
        private String idp_pass;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIdp_pass() {
            return idp_pass;
        }

        public void setIdp_pass(String idp_pass) {
            this.idp_pass = idp_pass;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
