package org.example.json;

import org.example.entity.StatusCodeEnum;

public class RequestBody {
    private int statusCode;
    private Object body;

    public RequestBody(RequestBodyBuilder builder) {
        this.statusCode = builder.getStatusCode();
        this.body = builder.getBody();
    }

    public RequestBody() {

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCodeEnum) {
        this.statusCode = statusCodeEnum;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static class RequestBodyBuilder {
        private int statusCode;
        private Object body;

        public int getStatusCode() {
            return statusCode;
        }

        public RequestBodyBuilder setStatusCode(StatusCodeEnum statusCode) {
            this.statusCode = statusCode.getValue();
            return this;
        }

        public Object getBody() {
            return body;
        }

        public RequestBodyBuilder setBody(Object body) {
            this.body = body;
            return this;
        }

        public RequestBody build() {
            return new RequestBody(this);
        }
    }
}
