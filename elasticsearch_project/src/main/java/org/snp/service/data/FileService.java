package org.snp.service.data;

import javax.enterprise.context.ApplicationScoped;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

@ApplicationScoped
public class FileService {

    public String getAllDataAtPos(String fileName, int pos, int size){
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r");
            randomAccessFile.seek(pos);
            /*String v =""+randomAccessFile.readUTF();
            v=v;*/
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

}
