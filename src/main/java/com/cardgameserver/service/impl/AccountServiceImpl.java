package com.cardgameserver.service.impl;


import com.cardgameserver.dao.AccountDao;
import com.cardgameserver.entity.Account;
import com.cardgameserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@SuppressWarnings("all")
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountDao accountDao;

    @Override
    public int insert(Long id) {
        int insert = accountDao.insert(id);
        return insert;
    }

    @Override
    public int update(Account account) {
        int update = accountDao.update(account);
        return update;
    }

    @Override
    public Account findById(Long id) {
        Account account = accountDao.selectById(id);
        return account;
    }

    @Override
    public boolean transfer(Account account1,Account account2) {
        int update1=accountDao.update(account1);
        int update2=accountDao.update(account2);
        if(update1+update2==2){
            return true;
        }
        return false;
    }

    @Override
    public List<Account> selectAll() {
        return accountDao.selectAllAccount();
    }
}
