package com.ucard.core.dao;

import com.ucard.core.mapper.TestMapper;
import com.ucard.core.model.TestInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Des
 *
 * @author Wang Hui
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestDao {

    private final TestMapper mapper;

    public List<TestInfo> filter() {
        return mapper.filter();
    }
}
