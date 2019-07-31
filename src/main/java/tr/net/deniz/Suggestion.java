/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.net.deniz;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.persistence.*;

/**
 *
 * @author yukseldeniz
 */
@Entity
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suggestionId", updatable = false, nullable = false)
    private Long suggestionId;

    private String title;
    private String byUserName;

    @Column(length = 10000)
    private String suggestionText;

    @Column(name = "hasFile", nullable = false)
    private boolean hasFile;

    private Long fileIdRef;

    @Column(name = "isLocked", nullable = false)
    private boolean isLocked;

    private String lockedById;

    public Suggestion() {
        return;
    }
    
    public Suggestion(String title, String byUserName, String suggestionText) {
        this.title = title;
        this.byUserName = byUserName;

        if (suggestionText.equals("")) {
            this.suggestionText = "No explanation given.";
        } else {
            this.suggestionText = suggestionText;
        }
    }

    public Suggestion(String json) {
        //json parse'la içine doldur.
        Gson gson = new Gson();
        JsonObject jObj = gson.fromJson(json, JsonObject.class);
        this.title = jObj.get("suggestionTitle").getAsString();
        this.suggestionText = jObj.get("suggestionText").getAsString();

        if (jObj.get("suggestionId") != null) {
            this.suggestionId = jObj.get("suggestionId").getAsLong();
        }

        if (jObj.get("byUserName") != null) {
            this.byUserName = jObj.get("byUserName").getAsString();
        }

        if (jObj.get("hasFile") != null) {
            this.hasFile = jObj.get("hasFile").getAsBoolean();
        }

        if (jObj.get("isLocked") != null) {
            this.isLocked = jObj.get("isLocked").getAsBoolean();
        }

        if (jObj.get("lockedById") != null) {
            this.lockedById = jObj.get("lockedById").getAsString();
        }

        if (jObj.get("fileIdRef") != null) {
            this.fileIdRef = jObj.get("fileIdRef").getAsLong();
        }

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getByUserName() {
        return byUserName;
    }

    public void setByUserName(String byUserName) {
        this.byUserName = byUserName;
    }

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public Long getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(Long suggestionId) {
        this.suggestionId = suggestionId;
    }

    /*
    public FileInstance getFileInstance() {
        return fileInstance;
    }

    public void setFileInstance(FileInstance uploadedFile) {
        this.fileInstance = uploadedFile;
    }
     */
    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /*
    public Member getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(Member lockedBy) {
        this.lockedBy = lockedBy;
    }
     */
    public String getLockedById() {
        return lockedById;
    }

    public void setLockedById(String lockedById) {
        this.lockedById = lockedById;
    }

    public Long getFileIdRef() {
        return fileIdRef;
    }

    public void setFileIdRef(Long fileIdRef) {
        this.fileIdRef = fileIdRef;
    }

}
