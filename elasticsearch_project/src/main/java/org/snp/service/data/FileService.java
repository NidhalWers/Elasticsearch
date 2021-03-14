package org.snp.service.data;

import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class FileService {

    private TableDao tableDao = new TableDao();
    private DataDao dataDAO = new DataDao();

    public String getAllDataAtPos(String fileName, int pos, int size){
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r");
            randomAccessFile.seek(pos);

            String value= randomAccessFile.readLine();

            randomAccessFile.close();
            return value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public String updateColumnAtPos(String fileName, int pos, int columnNumber, String newValue){
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(fileName, "rw");
            randomAccessFile.seek(pos);

            String line = randomAccessFile.readLine();
            String[] arrayLine = line.split(",");
            int total = line.getBytes().length;

            int newPos=pos;
            int i = 0;
            while(i<columnNumber){
                newPos += arrayLine[i].length() + 1; //+1 because of the ','
                i++;
            }

            byte[] byteValue = newValue.getBytes();

            randomAccessFile.seek(newPos);
            randomAccessFile.write(byteValue,0,byteValue.length);



            randomAccessFile.seek(pos);
            String res = randomAccessFile.readLine();
            randomAccessFile.close();
            return res;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Message parseCSVAndInsert(String tableName, InputStream csvFile, String fileName) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(csvFile);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        String[] values;
        List<Column> columns;
        Table table = tableDao.find(tableName);
        int position=0;
        if(table==null){
            return new MessageAttachment<>(404, "table "+tableName+" does not exists");
        }
        HashMap<String, String> lineToInsert;
        try {
            columns =table.getColumns();
            line = bufferedReader.readLine();
            position+=line.getBytes().length+1;
            while ((line = bufferedReader.readLine())!= null) {
                values = line.split(",");
                lineToInsert = new HashMap<>();
                for (int i = 0; i < values.length; i++) {
                    lineToInsert.put(columns.get(i).getName(), values[i]);
                }
                int  lineLength = line.getBytes().length;
                dataDAO.insert(table, lineToInsert,fileName+","+position+","+lineLength);
                position+=lineLength+1;
            }
        } catch (Exception e) {
            System.err.println(e);
            return new Message(500);
        } finally {
            bufferedReader.close();
            inputStreamReader.close();
        }

        return new MessageAttachment<Table>(200, table);
    }

}
