package org.snp.utils.exception.mapper;

import org.snp.utils.exception.AlreadyExistException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyExistExceptionMapper implements ExceptionMapper<AlreadyExistException> {
    @Override
    public Response toResponse(AlreadyExistException e) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(e.getMessage())
                .build();

    }
}
