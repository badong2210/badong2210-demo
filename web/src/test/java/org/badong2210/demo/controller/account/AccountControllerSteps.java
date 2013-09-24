package org.badong2210.demo.controller.account;

import org.badong2210.demo.domain.vo.AccountVO;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AccountControllerSteps {

    @Autowired
    private AccountController controller;

    private AccountVO vo;

    @Given("the account is available")
    public void createAccountVo() {
        vo = new AccountVO();
        System.out.println("...the AccountVo is available");
    }

    @When("the account name is $name with description $description")
    public void setAccountName(String name, String description) {
        vo.setName(name);
        vo.setDescription(description);
    }

    @When("the account name is <account_name>")
    public void setAccountName(@Named("account_name") String name) {
        vo.setName(name);
    }

    @When("the account description is <account_description>")
    public void setAccountDescription(@Named("account_description") String description) {
        vo.setDescription(description);
    }

    @Then("the account should be added and return $status")
    public void addAccount(String status) {
        StatusResponse sr = controller.add(vo,"session4");

        assertThat(sr.getStatus(), is(status));
    }

}
