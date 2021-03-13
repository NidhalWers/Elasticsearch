package org.snp.service.data;

import org.snp.dao.TableDao;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.JoinCredentials;
import org.snp.model.credentials.QueryCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FunctionService {

    private TableDao tableDao = new TableDao();
    @Inject DataService dataService;

    public Message join(JoinCredentials joinCredentials){
        /**
         * verification & exception
         */
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


        List<String> result = new ArrayList<>();
        /**
         * query
         */
        Message messageTable1 = dataService.query(new QueryCredentials(joinCredentials.tables.get(0).tableName));
        if(messageTable1.getCode() == 200){
            List<String> allRowsTable1 = (List<String>) ((MessageAttachment)messageTable1).getAttachment();
            for(String rowTable1 : allRowsTable1){
                String columnValueTable1 = getValueForColumn(table1, joinCredentials.tables.get(0).columnName, rowTable1 );

                /**
                 * find in table 2
                 */
                QueryCredentials queryCredentials = new QueryCredentials()
                                                    .setQueryParams()
                                                    .addAttribute(joinCredentials.tables.get(1).columnName, columnValueTable1);

                Message messageTable2 = dataService.query(queryCredentials);
                if(messageTable2.getCode()==200){
                    List<String> rowsTable2 = (List<String>) ((MessageAttachment)messageTable2).getAttachment();
                    for(String rowTable2 : rowsTable2){
                        result.add(rowTable1+"|"+rowTable2);
                    }
                }
            }

        }else{
            return messageTable1;
        }

        return new MessageAttachment<List>(200, result);
    }

    private String getValueForColumn(Table table, String columnName, String value){
        String[] valueSplitted = value.split(",");
        int columnPosition = table.positionOfColumn(columnName);
        return valueSplitted[ columnPosition ];
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
