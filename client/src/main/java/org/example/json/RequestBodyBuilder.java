package org.example.json;

import org.example.entity.ResponseTypeEnum;

public class RequestBodyBuilder {
    private String typeRequest;
    private Object body;

    public String getTypeRequest() {
        return typeRequest;
    }

    public Object getBody() {
        return body;
    }

    public RequestBodyBuilder withTypeRequest(ResponseTypeEnum typeRequest) {
        this.typeRequest = typeRequest.getValue();
        return this;
    }

    public RequestBodyBuilder withBody(Object body) {
        this.body = body;
        return this;
    }

    public RequestBody build() {
        return new RequestBody(this);
    }
}
