package org.badong2210.demo.controller.account.cucumber;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import org.badong2210.demo.controller.account.AccountController;
import org.badong2210.demo.controller.account.StatusResponse;
import org.badong2210.demo.domain.vo.AccountVO;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class AccountControllerStepsdef {

    @Autowired
    private AccountController controller;

    private AccountVO vo;

    @Given("^the account is available$")
    public void createAccountVo() {
        vo = new AccountVO();
        System.out.println("...the AccountVo is available");
    }

    @When("^the account name is '(.+)' with description '(.*)'$")
    public void setAccountName(String name, String description) {
        vo.setName(name);
        vo.setDescription(description);
    }

    @When("^the account name is '(.+)'$")
    public void setAccountName(String name) {
        vo.setName(name);
    }

    @When("^the account description is '(.+)'$")
    public void setAccountDescription(String description) {
        vo.setDescription(description);
    }

    @Then("the account should be added and return '(.+)'$")
    public void addAccount(String status) {
        StatusResponse sr = controller.add(vo,"session4");

        assertThat(sr.getStatus(), is(status));
    }
}
