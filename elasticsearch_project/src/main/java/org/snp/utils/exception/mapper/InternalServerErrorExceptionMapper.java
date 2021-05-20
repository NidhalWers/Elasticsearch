package org.snp.utils.exception.mapper;


import org.snp.utils.exception.InternalServerErrorException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalServerErrorExceptionMapper implements ExceptionMapper<InternalServerErrorException> {
    @Override
    public Response toResponse(InternalServerErrorException e) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage())
                .build();
    }
}