package com.ucard.core.mapper

import com.ucard.core.model.TestInfo
import org.apache.ibatis.annotations.Select


/**
 * Des
 *
 * @author Wang Hui
 */
interface TestMapper {

    @Select('''
        SELECT
            id,
            name
        FROM
           test_v1
        #whereExt()
        #end
    ''')
    public List<TestInfo> filter();
}
