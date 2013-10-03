package org.badong2210.demo.controller.account.cucumber;


import cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Cucumber.Options(features = "classpath:features/account_controller.feature", format = {"pretty", "html:target/cucumber"})
public class AccountControllerTest {
}
