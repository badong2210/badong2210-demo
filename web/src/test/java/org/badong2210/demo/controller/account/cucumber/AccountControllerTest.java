package org.badong2210.demo.controller.account.cucumber;


import cucumber.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(Cucumber.class)
@Cucumber.Options(features = "classpath:features/account_controller.feature", format = {"pretty", "html:target/cucumber"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AccountControllerTest {
}
