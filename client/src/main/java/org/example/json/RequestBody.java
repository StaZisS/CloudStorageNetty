package org.example.json;

public class RequestBody {
    private String typeRequest;
    private Object body;

    public RequestBody(RequestBodyBuilder builder) {
        this.typeRequest = builder.getTypeRequest();
        this.body = builder.getBody();
    }

    public String getTypeRequest() {
        return typeRequest;
    }

    public Object getBody() {
        return body;
    }
}
