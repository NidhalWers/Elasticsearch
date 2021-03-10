package org.snp.utils.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InternalServerErrorException extends WebApplicationException {

    public InternalServerErrorException(String msg){
        super(msg, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public synchronized Throwable fillInStackTrace()  { return this; }
}
