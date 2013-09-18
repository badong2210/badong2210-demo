package org.badong2210.demo.controller.account;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.badong2210.demo.domain.service.AccountManagementService;
import org.badong2210.demo.domain.vo.AccountVO;
import org.badong2210.demo.domain.vo.AccountVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Account controller
 */
@Controller
public class AccountController {

    private static final Logger LOG = Logger.getLogger(AccountController.class);

    @Autowired
    private AccountManagementService service;

    @Autowired
    private Mapper mapper;

    @ResponseBody
    @RequestMapping("/add")
    public StatusResponse add(@ModelAttribute AccountVO account, @CookieValue("JSESSIONID") String sessionID) {
        LOG.info(String.format("[%s] Adding an account with name '%s'.", sessionID, account.getName()));

        if(StringUtils.isBlank(account.getName())) {
            return StatusResponse.FAILED;
        }

        try {
            AccountVO created = new AccountVOBuilder(account)
                    .setSessionID(sessionID)
                    .build();

            service.addAccount(created);
        } catch (Exception e) {
            LOG.error("Error while creating account.", e);

            return StatusResponse.FAILED;
        }

        return StatusResponse.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("/list")
    public AccountListResponse list(@CookieValue("JSESSIONID") String sessionID) {
        LOG.info(String.format("Retrieving list for session '%s'.", sessionID));

        AccountListResponse response = new AccountListResponse();

        try {
            List<AccountVO> lists = service.getSessionAccounts(sessionID);
            List<AccountRow> rows = new ArrayList<AccountRow>(lists.size());

            for(AccountVO account : lists) {
                rows.add(mapper.map(account, AccountRow.class));
            }

            response.setRows(rows);
            response.setStatus(StatusResponse.SUCCESS);
        } catch(Exception e) {
            LOG.error("Error while creating account.", e);
            response.setStatus(StatusResponse.FAILED);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping("/activate")
    public AccountListResponse activate(@ModelAttribute AccountSelection selection, @CookieValue("JSESSIONID") String sessionID) {
        LOG.info(String.format("Activating accounts '%s' for session '%s'.", selection.getNames(), sessionID));

        AccountListResponse response = new AccountListResponse();

        try {
            int result = service.activate(sessionID, selection.getNames());

            LOG.info(String.format("There are %d account activated.", result));
        } catch(Exception e) {
            LOG.error("Error while creating account.", e);
            response.setStatus(StatusResponse.FAILED);

            return response;
        }

        return list(sessionID);
    }

    @ResponseBody
    @RequestMapping("/archive")
    public AccountListResponse archive(@ModelAttribute AccountSelection selection, @CookieValue("JSESSIONID") String sessionID) {
        LOG.info(String.format("Activating accounts '%s' for session '%s'.", selection.getNames(), sessionID));

        AccountListResponse response = new AccountListResponse();

        try {
            int result = service.archive(sessionID, selection.getNames());

            LOG.info(String.format("There are %d account archived.", result));
        } catch(Exception e) {
            LOG.error("Error while creating account.", e);
            response.setStatus(StatusResponse.FAILED);

            return response;
        }

        return list(sessionID);
    }

    @ResponseBody
    @RequestMapping("/delete")
    public AccountListResponse delete(@ModelAttribute AccountSelection selection, @CookieValue("JSESSIONID") String sessionID) {
        LOG.info(String.format("Activating accounts '%s' for session '%s'.", selection.getNames(), sessionID));

        AccountListResponse response = new AccountListResponse();

        try {
            int result = service.delete(sessionID, selection.getNames());

            LOG.info(String.format("There are %d account deleted.", result));
        } catch(Exception e) {
            LOG.error("Error while creating account.", e);
            response.setStatus(StatusResponse.FAILED);

            return response;
        }

        return list(sessionID);
    }
}
