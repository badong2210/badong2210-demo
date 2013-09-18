package org.badong2210.demo.controller.account;

import org.junit.Test;
import org.badong2210.core.carbonfive.Carbon5AbstractDataDrivenTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@Carbon5AbstractDataDrivenTest.DataSet(locations = {"dataset/account-data-set.xml"})
@ContextConfiguration(locations = { "classpath:spring-domain-db-test.xml" })
public class AccountControllerTest extends Carbon5AbstractDataDrivenTest {

    @Autowired
    private AccountController controller;

    @Test
    public void testDataSet() {
        AccountListResponse responseSession1 = controller.list("session1");
        AccountListResponse responseSession2 = controller.list("session2");

        assertEquals(responseSession1.getRows().size(), 2);
        assertEquals(responseSession2.getRows().size(), 1);
    }
}
