package com.digicash.paymentkitlibrary.model;

import java.io.Serializable;

/**
 * Created by hechmi on 12/12/17.
 */

public enum Status implements Serializable{
    SUCCESS("success"),
    PENDING("pending"),
    ERROR("error");

    String statusValue;

    Status(String status) {
        this.statusValue = status;
    }
}
