package org.master.task.scheduing.manager.entity;

public class Response {
    int code;
    String message;
    String type;

    String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Response(int code,String message,String type){
        this.code=code;
        this.message=message;
        this.type=type;
        this.token=null;
    }
    public Response(int code,String message,String type,String token){
        this.code=code;
        this.message=message;
        this.token=token;
        this.type=type;
    }
    public Response(){}
}
