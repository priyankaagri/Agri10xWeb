package com.mobile.agri10x.Model;

import java.io.Serializable;

public class UploadDoc implements Serializable {

    String phone;
    String docType;
    String file;

    public UploadDoc(String phone, String docType, String file) {
        this.phone = phone;
        this.docType = docType;
        this.file = file;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
