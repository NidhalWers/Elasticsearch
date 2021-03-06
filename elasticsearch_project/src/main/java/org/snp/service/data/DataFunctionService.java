package org.snp.service.data;

import org.snp.dao.TableDao;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.JoinCredentials;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DataFunctionService {

    private TableDao tableDao = new TableDao();

    public Message join(JoinCredentials joinCredentials){
        //first table
        Table table1 = tableDao.find(joinCredentials.tables.get(0).tableName);
        if(table1 == null)
            return new MessageAttachment<>(404, "table "+joinCredentials.tables.get(0).tableName+" does not exists");
        if(! table1.containsColumn( joinCredentials.tables.get(0).columnName ))
            return new MessageAttachment<>(404, "column "+joinCredentials.tables.get(0).columnName+" does not exists in "+table1.getName());
        //second table
        Table table2 = tableDao.find(joinCredentials.tables.get(1).tableName);
        if(table2 == null)
            return new MessageAttachment<>(404, "table "+joinCredentials.tables.get(1).tableName+" does not exists");
        if(! table2.containsColumn( joinCredentials.tables.get(1).columnName ))
            return new MessageAttachment<>(404, "column "+joinCredentials.tables.get(1).columnName+" does not exists in "+table2.getName());


        return null;
    }

    public double sum(Table table, String columnName){

        return 0;
    }

    public double avg(Table table, String columnName){

        return 0;
    }

    public double min(Table table, String columnName){

        return 0;
    }

    public String max(Table table, String columnName){

        return null;
    }

    public int count(Table table, String columnName){

        return 0;
    }

    public int count(Table table){

        return 0;
    }
}
