package org.snp.service.data;

import javax.ws.rs.core.Response;
import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.Column;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.RowCredentials;
import org.snp.model.response.RowInsertedModel;
import org.snp.service.TableService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class FileService {

    private TableDao tableDao = new TableDao();
    private DataDao dataDAO = new DataDao();

    public String getAllDataAtPos(String fileName, int pos){
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

    @Inject
    TableService tableService;

    public String updateColumnAtPos(Table table, String fileName, int pos, int columnNumber, String newValue){
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(fileName, "rw");
            int fileSize = (int) randomAccessFile.length();
            randomAccessFile.seek(pos);

            String line = randomAccessFile.readLine();
            String[] arrayLine = line.split(",");

            int newPos=pos;
            int posToCopy = 0;
            int sizeDifference = 0;
            int i = 0;
            while(i<=columnNumber){
                if(i<columnNumber) {
                    newPos += arrayLine[i].length() + 1; //+1 because of the ','
                }else{
                    posToCopy = newPos + arrayLine[i].length();
                    sizeDifference = newValue.length() - arrayLine[i].length();
                }
                i++;
            }

            /**
             * read the other data
             */
            int followingSize = fileSize - posToCopy;
            byte[] followingData = new byte[followingSize];
            randomAccessFile.seek(posToCopy);
            randomAccessFile.readFully(followingData, 0, followingSize );

            /**
             * update the value
             */

            byte[] byteValue = newValue.getBytes();

            randomAccessFile.seek(newPos);
            randomAccessFile.write(byteValue,0,byteValue.length);

            /**
             * write following
             */
            if(sizeDifference > 0)
                randomAccessFile.setLength( randomAccessFile.length() +  sizeDifference);
            randomAccessFile.write(followingData, 0, followingSize);

            /**
             * update the position
             */

            if(sizeDifference!=0)
                tableService.updateAllReference(table, sizeDifference, pos);

            /**
             * verify the updated value
             */

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
        Table table = tableDao.find(tableName);
        int position=1;
        if(table==null){
            return new MessageAttachment<>(404, "table "+tableName+" does not exists");
        }
        //create file
        File tempFile = File.createTempFile(fileName,".csv");
        //delete data on system exit
        tempFile.deleteOnExit();
        String tempFileName = tempFile.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream(tempFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        try {
            //skip header
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine())!= null) {
                position=insertCsvLineIntoTable(line,table,position,tempFileName);
                //write into data file
                bw.write(line);
                bw.newLine();
            }
        } catch (Exception e) {
            System.err.println(e);
            return new Message(500);
        } finally {
            bufferedReader.close();
            inputStreamReader.close();
            bw.close();
            fos.close();
        }

        return new MessageAttachment<Table>(200, table);
    }

    private int insertCsvLineIntoTable(String line, Table table, int position, String fileName ){
        String []values = line.split(",");
        HashMap<String, String> lineToInsert = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            lineToInsert.put(table.getColumns().get(i).getName(), values[i]);
        }
        int  lineLength = line.getBytes().length;
        dataDAO.insert(table, lineToInsert,fileName+","+position+","+lineLength);
        return position+lineLength+1;
    }
    public Response insertCsvLineIntoTable(RowCredentials rowCredentials){
        String tableName = rowCredentials.table;
        int position = rowCredentials.position;
        String fileName = rowCredentials.fileName;
        String line = rowCredentials.line;

        Table table = tableDao.find(tableName);
        if(table==null){
            return Response.status(404).build();
        }
        String []values = line.split(",");
        HashMap<String, String> lineToInsert = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            lineToInsert.put(table.getColumns().get(i).getName(), values[i]);
        }
        int  lineLength = line.getBytes().length;
        dataDAO.insert(table, lineToInsert,fileName+","+position+","+lineLength);
        return Response.ok(new RowInsertedModel(tableName,position+lineLength+1)).build();
    }

}
