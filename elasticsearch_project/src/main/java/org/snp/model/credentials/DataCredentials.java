package org.snp.model.credentials;

import java.io.InputStream;

import javax.ws.rs.FormParam;

import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class DataCredentials {
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream file;

    @FormParam("table")
    @PartType(MediaType.TEXT_PLAIN)
    public String tableName;
    
}