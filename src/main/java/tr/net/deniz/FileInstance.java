/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.net.deniz;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Base64;
import javax.persistence.*;

/**
 *
 * @author yukseldeniz
 */
@Entity
public class FileInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId", updatable = false, nullable = false)
    private Long fileId;
    
    private String fileName;

    //file content is byte array.
    
    @Lob
    private byte[] fileContent;
    
    
    
    public FileInstance() {
        return;
    }

    //Json Constructor
    public FileInstance(String json){
        Gson gson = new Gson();
        JsonObject jObj = gson.fromJson(json, JsonObject.class);
        
        
        
        this.fileId = jObj.get("fileId").getAsLong();
        this.fileName = jObj.get("fileName").getAsString();
        
        this.fileContent = Base64.getDecoder().decode(jObj.get("fileContent").getAsString());      
        
    }
    
    public FileInstance(String fileName, byte[] fileContent) {       
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
