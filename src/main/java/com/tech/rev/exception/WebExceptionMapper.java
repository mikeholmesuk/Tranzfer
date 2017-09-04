package com.tech.rev.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class WebExceptionMapper implements ExceptionMapper<WebException> {
    public Response toResponse(WebException webException) {
        return Response
                .status(webException.getStatusCode())
                .entity(webException.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
