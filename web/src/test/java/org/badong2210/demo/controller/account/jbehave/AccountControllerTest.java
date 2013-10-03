package org.badong2210.demo.controller.account.jbehave;

import org.badong2210.demo.controller.account.AccountController;
import org.badong2210.demo.controller.account.AccountListResponse;
import org.badong2210.demo.domain.vo.AccountVO;
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
    public void testAddAccount() {
        AccountVO vo = new AccountVO();
        vo.setName("shiela");
        vo.setDescription("description");
        controller.add(vo,"session4");

        AccountListResponse responseSession4 = controller.list("session4");
        assertEquals(responseSession4.getRows().size(), 1);
    }


    @Test
    public void testDataSet() {
        AccountListResponse responseSession1 = controller.list("session1");
        AccountListResponse responseSession2 = controller.list("session2");

        assertEquals(responseSession1.getRows().size(), 2);
        assertEquals(responseSession2.getRows().size(), 1);
    }
}
