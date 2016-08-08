package com.ucard.core.api.controller.v1;

import com.ucard.core.dao.TestDao;
import com.ucard.core.model.TestInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final TestDao testDao;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "Hello,test!";
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/test/data", method = RequestMethod.GET)
    public List<TestInfo> getTestData() {
        return testDao.filter();
    }
}
