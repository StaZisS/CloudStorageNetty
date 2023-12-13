package org.example.json;

import org.example.entity.StatusCodeEnum;

public class ResponseBody {
    private StatusCodeEnum statusCode;
    private Object body;

    public StatusCodeEnum getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCodeEnum statusCode) {
        this.statusCode = statusCode;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "statusCode=" + statusCode +
                ", body='" + body + '\'' +
                '}';
    }
}
