package com.sc.salary.process.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_FILE_UPLOAD")
public class SCFileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FILE_UPLD_ID")
    private Long fileUploadId;

    @Column(name = "FILE_UPLD_DTTM", nullable = false)
    private Date fileUploadDatetime = new Date();

    @Column(name = "UPLOADED_BY")
    private Long uploadedBy;

    @Column(name = "FILE_TYPE", length = 20)
    private String fileType;

    @Column(name = "SRC_FILE_NAME" ,length = 50)
    private String sourceFileName;

    @Column(name = "SERVER_FILE_NAME" ,length = 50)
    private String serverFileName;

    @Column(name = "UPLOAD_STATUS", length = 20)
    private String uploadStatus = "LP"; // LP = Load Pending

    @Column(name = "BATCH_CD", length = 10)
    private String batchCode;

    @Column(name = "FILE_PROCESS_DTTM")
    private Date fileProcessDatetime;

    @Column(name = "PROCESS_START_DTTM")
    private Date processStartDatetime;

    @Column(name = "PROCESS_END_DTTM")
    private Date processEndDatetime;

    @Column(name = "ROW_COUNT")
    private Integer rowCount;

    @Column(name = "PROCESSED_ROW")
    private Integer processedRow;

    @Column(name = "PROCESS_COMMENT", length = 4000)
    private String processComment;

    // Getters and Setters for the properties

    public Long getFileUploadId() {
        return fileUploadId;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public String getServerFileName() {
        return serverFileName; // Return the actual serverFileName
    }

    public Date getFileUploadDatetime() {
        return fileUploadDatetime;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public String getFileType() {
        return fileType;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public Date getFileProcessDatetime() {
        return fileProcessDatetime;
    }

    public Date getProcessStartDatetime() {
        return processStartDatetime;
    }

    public Date getProcessEndDatetime() {
        return processEndDatetime;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public Integer getProcessedRow() {
        return processedRow;
    }

    public String getProcessComment() {
        return processComment;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName; // Set the actual serverFileName
    }

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public void setFileUploadDatetime(Date date) {
        this.fileUploadDatetime = date; // Corrected this to use the passed date
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public void setProcessComment(String processComment) {
        this.processComment = processComment;
    }
}
