package com.ucard.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * The vendor info.
 *
 * @author Wang Hui
 **/
@Data
@ToString
@EqualsAndHashCode
public class VendorInfo {
    private long id;
    private String accountUuid;
    private Date lastDumpTime;
    private Date createTime;
}
