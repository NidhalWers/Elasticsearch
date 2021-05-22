package org.snp.service.data;

import org.snp.Main;
import org.snp.dao.TableDao;
import org.snp.indexage.Table;
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

    final String NODE_NAME = Main.isMasterTest() ? "Master" : System.getProperty("name");
    final String MESSAGE_PREFIX = NODE_NAME + " : ";

    /**
     * join
     * @param joinCredentials
     * @return List<String> if code 200
     */

    public Message join(JoinCredentials joinCredentials){
        /**
         * verification & exception
         */
        //first table
        Table table1 = tableDao.find(joinCredentials.tables.get(0).tableName);
        if(table1 == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+joinCredentials.tables.get(0).tableName+" does not exists");
        if(! table1.containsColumn( joinCredentials.tables.get(0).columnName ))
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"column "+joinCredentials.tables.get(0).columnName+" does not exists in "+table1.getName());
        //second table
        Table table2 = tableDao.find(joinCredentials.tables.get(1).tableName);
        if(table2 == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+joinCredentials.tables.get(1).tableName+" does not exists");
        if(! table2.containsColumn( joinCredentials.tables.get(1).columnName ))
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"column "+joinCredentials.tables.get(1).columnName+" does not exists in "+table2.getName());


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
                QueryCredentials queryCredentials = new QueryCredentials(joinCredentials.tables.get(1).tableName)
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
            return new MessageAttachment<List>(200, result);

        }else{
            return messageTable1;
        }


    }

    private String getValueForColumn(Table table, String columnName, String value){
        String[] valueSplitted = value.split(",");
        int columnPosition = table.positionOfColumn(columnName);
        return valueSplitted[ columnPosition ];
    }

    /**
     * sum
     * @param tableName
     * @param columnName
     * @return double if code 200
     */

    public Message sum(String tableName, String columnName){
        Table table = tableDao.find(tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ tableName+" does not exists");
        if(! table.getColumnFromName(columnName).getType().equals("double") && ! table.getColumnFromName(columnName).getType().equals("int") )
            return new MessageAttachment<>(400, MESSAGE_PREFIX+"column's type does not correspond to int or double");


        QueryCredentials queryCredentials = new QueryCredentials(tableName)
                                            .setColumnSelected()
                                            .addColumn(columnName);

        Message message = dataService.query(queryCredentials);
        if(message.getCode() == 200){
            List<String> string_value = (List<String>) ((MessageAttachment)message).getAttachment();
            double value = 0;
            for(String s : string_value){
                value+= Double.valueOf(s);
            }

            return new MessageAttachment<>(200, value );
        }else{
            return message;
        }

    }

    /**
     * avg
     * @param tableName
     * @param columnName
     * @return double if code 200
     */

    public Message avg(String tableName, String columnName){
        Table table = tableDao.find(tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ tableName+" does not exists");
        if(! table.getColumnFromName(columnName).getType().equals("double") && ! table.getColumnFromName(columnName).getType().equals("int") )
            return new MessageAttachment<>(400, MESSAGE_PREFIX+"column's type does not correspond to int or double");


        QueryCredentials queryCredentials = new QueryCredentials(tableName)
                .setColumnSelected()
                .addColumn(columnName);

        Message message = dataService.query(queryCredentials);
        if(message.getCode() == 200){
            List<String> string_value = (List<String>) ((MessageAttachment)message).getAttachment();
            double value = 0;
            for(String s : string_value){
                value+= Double.valueOf(s);
            }
            value=value/Double.valueOf(string_value.size());

            return new MessageAttachment<>(200, value );
        }else{
            return message;
        }
    }

    /**
     * min
     * @param tableName
     * @param columnName
     * @return double if code 20à
     */

    public Message min(String tableName, String columnName){
        Table table = tableDao.find(tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ tableName+" does not exists");
        if(! table.getColumnFromName(columnName).getType().equals("double") && ! table.getColumnFromName(columnName).getType().equals("int") )
            return new MessageAttachment<>(400, MESSAGE_PREFIX+"column's type does not correspond to int or double");


        QueryCredentials queryCredentials = new QueryCredentials(tableName)
                .setColumnSelected()
                .addColumn(columnName);

        Message message = dataService.query(queryCredentials);
        if(message.getCode() == 200){
            List<String> string_value = (List<String>) ((MessageAttachment)message).getAttachment();
            double value = 0;
            boolean first = true;
            for(String s : string_value){
                if(first) {
                    value = Double.valueOf(s);
                    first = false;
                }else{
                    if(value > Double.valueOf(s))
                        value = Double.valueOf(s);
                }
            }

            return new MessageAttachment<>(200, value );
        }else{
            return message;
        }
    }

    /**
     * max
     * @param tableName
     * @param columnName
     * @return double if code 200
     */

    public Message max(String tableName, String columnName){
        Table table = tableDao.find(tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ tableName+" does not exists");
        if(! table.getColumnFromName(columnName).getType().equals("double") && ! table.getColumnFromName(columnName).getType().equals("int") )
            return new MessageAttachment<>(400, MESSAGE_PREFIX+"column's type does not correspond to int or double");


        QueryCredentials queryCredentials = new QueryCredentials(tableName)
                .setColumnSelected()
                .addColumn(columnName);

        Message message = dataService.query(queryCredentials);
        if(message.getCode() == 200){
            List<String> string_value = (List<String>) ((MessageAttachment)message).getAttachment();
            double value = 0;
            boolean first = true;
            for(String s : string_value){
                if(first) {
                    value = Double.valueOf(s);
                    first = false;
                }else{
                    if(value < Double.valueOf(s))
                        value = Double.valueOf(s);
                }
            }

            return new MessageAttachment<>(200, value );
        }else{
            return message;
        }
    }

    /**
     * count
     * @param tableName
     * @param columnName
     * @return int if code 200
     */

    public Message count(String tableName, String columnName){
        Table table = tableDao.find(tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ tableName+" does not exists");


        QueryCredentials queryCredentials = new QueryCredentials(tableName)
                .setColumnSelected()
                .addColumn(columnName);

        Message message = dataService.query(queryCredentials);
        if(message.getCode() == 200){
            List<String> string_value = (List<String>) ((MessageAttachment)message).getAttachment();
            double value = string_value.size();

            return new MessageAttachment<>(200, value );
        }else{
            return message;
        }
    }

    /**
     * count
     * @param tableName
     * @return int if code 200
     */

    public Message count(String tableName){
        Table table = tableDao.find(tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ tableName+" does not exists");


        QueryCredentials queryCredentials = new QueryCredentials(tableName);

        Message message = dataService.query(queryCredentials);
        if(message.getCode() == 200){
            List<String> string_value = (List<String>) ((MessageAttachment)message).getAttachment();
            double value = string_value.size();

            return new MessageAttachment<>(200, value );
        }else{
            return message;
        }
    }
}
