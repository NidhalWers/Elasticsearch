package org.snp.utils.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AlreadyExistException extends WebApplicationException {

    public AlreadyExistException(String msg){
        super(msg, Response.Status.CONFLICT);
    }

    public synchronized Throwable fillInStackTrace()  { return this; }
}
