package org.snp.service.data;

import org.snp.Main;
import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.httpclient.SlaveClient;
import org.snp.indexage.Column;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.redirection.RowCredentials;
import org.snp.service.TableService;
import org.snp.utils.OSValidator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class FileService {

    private TableDao tableDao = new TableDao();
    private DataDao dataDAO = new DataDao();
    private SlaveClient [] slaveClients;


    final String NODE_NAME = Main.isMasterTest() ? "Master" : System.getProperty("name");
    final String MESSAGE_PREFIX = NODE_NAME + " : ";

    public FileService(){
        if(Main.isMasterTest())
            slaveClients = new SlaveClient[]{new SlaveClient(8081), new SlaveClient(8082)};
    }


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
        if(table==null){
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+tableName+" does not exists");
        }
        int position;
        if(OSValidator.isWindows()) {
            position = 1;
        }else{
            if(OSValidator.isUnix()){
                position=0;
            }else
                position=1;
        }
        //create file
        File tempFile = File.createTempFile(fileName,".csv");
        //delete data on system exit
        tempFile.deleteOnExit();
        String tempFileName = tempFile.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream(tempFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        int nbLigne = 0;
        try {
            //skip header
            bufferedReader.readLine();
            String line;
            int  lineLength;
            while ((line = bufferedReader.readLine())!= null) {
                System.out.println("ligne n°"+nbLigne);
                insertLineIntoNode(line,table,position,tempFileName);
                lineLength = line.getBytes().length;
                position+=lineLength+1;
                nbLigne++;
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

        return new MessageAttachment<String>(200, "nombre de ligne insérées : " + nbLigne);
    }


    private void insertLineIntoNode(String line, Table table, int position, String fileName ){
        int hash = line.hashCode();
        int choice =  Math.abs( (hash+3) % 3);
        if(choice==2){ //if Master
            insertCsvLineIntoTable(line, table, position,  fileName );
            return;
        }else{
            slaveClients[choice].insertLine(RowCredentials.builder()
                                                        .tableName(table.getName())
                                                        .line(line)
                                                        .position(position)
                                                        .fileName(fileName)
                                                        .build()
                                            );
            return;
        }
    }

    private void insertCsvLineIntoTable(String line, Table table, int position, String fileName ){
        String []values = line.split(",");
        HashMap<String, String> lineToInsert = new HashMap<>();
        List<Column> columns = table.getColumns();
        for (int i = 0; i < values.length; i++) {
            if( i < columns.size())
                lineToInsert.put(columns.get(i).getName(), values[i]);
        }
        int lineLength = line.getBytes().length;
        dataDAO.insert(table, lineToInsert,fileName+","+position+","+lineLength);
    }


    public Message insertCsvLineIntoTable(RowCredentials rowCredentials){
        Table table = tableDao.find(rowCredentials.tableName);
        if(table==null){
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+rowCredentials.tableName+" does not exists");
        }
        String []values = rowCredentials.line.split(",");
        HashMap<String, String> lineToInsert = new HashMap<>();
        List<Column> columns = table.getColumns();
        for (int i = 0; i < values.length; i++) {
            if( i < columns.size())
                lineToInsert.put(columns.get(i).getName(), values[i]);
        }
        int  lineLength = rowCredentials.line.getBytes().length;
        dataDAO.insert(table, lineToInsert,rowCredentials.fileName+","+rowCredentials.position+","+lineLength);
        return new MessageAttachment<>(200, table);
    }

}
