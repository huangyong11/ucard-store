package com.ucard.core.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
public class MapperConfiguration {

    @Configuration
    @EnableTransactionManagement
    protected static class TransactionManagementConfiguration {

    }
}
