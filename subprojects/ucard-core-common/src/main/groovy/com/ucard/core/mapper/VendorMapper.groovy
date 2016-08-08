package com.ucard.core.mapper

import com.ucard.core.model.VendorInfo
import org.apache.ibatis.annotations.*

/**
 * The vendor mapper.
 *
 * @author Wang Hui
 */
interface VendorMapper {
    @Select('''
        SELECT
            id,
            account_uuid AS accountUuid,
            last_dump_time AS lastDumpTime,
            create_time AS createTime
        FROM
            vendor
    ''')
    List<VendorInfo> getAll();

    @Select('''
        SELECT
            id,
            account_uuid AS accountUuid,
            last_dump_time AS lastDumpTime,
            create_time AS createTime
        FROM
            vendor
        WHERE
            account_uuid = @{accountUuid}
    ''')
    VendorInfo get(@Param("accountUuid") String accountUuid);

    @Insert('''
        INSERT INTO vendor
            (
            account_uuid,
            last_dump_time,
            create_time,
            update_time
            )
        VALUES
            (
            @{accountUuid},
            NOW(),
            NOW(),
            NOW()
            )
        ON DUPLICATE KEY UPDATE
            update_time = VALUES(update_time)
    ''')
    int add(VendorInfo vendorInfo);

    @Update('''
        UPDATE
            vendor
        SET
            last_dump_time = @{dumpTime}
        WHERE
            account_uuid = @{accountUuid}
    ''')
    int updateDumpTime(@Param("accountUuid") String accountUuid, @Param("dumpTime") Date dumpTime);
    
    @Delete('''
        DELETE
        FROM
            vendor
        WHERE
            account_uuid = @{accountUuid}
    ''')
    int deleteVendorPermanently(@Param("accountUuid") String accountUuid);
}
