package com.demo.service;


import com.demo.entities.UserAccount;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {


    private JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager transactionManager;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    public void exchangeAmount(String senderUser, String receiverUser, Integer amountToBeTransferred) {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        System.out.println("Before Exchange");
        System.out.println("Sender::");
        getAccountStateOfUser(senderUser);
        System.out.println("Receiver::");
        getAccountStateOfUser(receiverUser);
        try {
            String sql = "UPDATE user_account SET amount = amount - ? where username = ?";
            jdbcTemplate.update(sql, new Object[]{amountToBeTransferred, senderUser});

            sql = "UPDATE user_account SET amount = amount + ? where username = ?";
            jdbcTemplate.update(sql, new Object[]{amountToBeTransferred, receiverUser});


        } catch (DataAccessException e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
        }

        System.out.println("After Exchange");
        System.out.println("Sender::");
        getAccountStateOfUser(senderUser);
        System.out.println("Receiver::");
        getAccountStateOfUser(receiverUser);
    }

    public void getAccountStateOfUser(String userName) {
        System.out.println("Account Status for" + userName);
        String sql = "SELECT * from user_account where userName = ?";
        UserAccount userAccount = jdbcTemplate.queryForObject(sql, new Object[]{userName}, new RowMapper<UserAccount>() {
            @Override
            public UserAccount mapRow(ResultSet resultSet, int i) throws SQLException {
                UserAccount userAccount = new UserAccount();
                userAccount.setBalance(resultSet.getInt("amount"));
                userAccount.setUsername(resultSet.getString("username"));
                return userAccount;
            }
        });

        System.out.println(userAccount);
    }

}
