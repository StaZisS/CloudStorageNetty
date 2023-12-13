package org.example.json;

import org.example.entity.ResponseTypeEnum;

public class ResponseBody {
    private String typeRequest;
    private Object body;

    public ResponseBody(ResponseBodyBuilder builder) {
        this.typeRequest = builder.getTypeRequest();
        this.body = builder.getBody();
    }

    public ResponseBody() {
    }

    public String getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static class ResponseBodyBuilder {
        private String typeRequest;
        private Object body;

        public String getTypeRequest() {
            return typeRequest;
        }

        public ResponseBodyBuilder setTypeRequest(ResponseTypeEnum typeRequest) {
            this.typeRequest = typeRequest.getValue();
            return this;
        }

        public Object getBody() {
            return body;
        }

        public ResponseBodyBuilder setBody(Object body) {
            this.body = body;
            return this;
        }

        public ResponseBody build() {
            return new ResponseBody(this);
        }
    }
}
