package com.skillcanvas.accounts.service;

import com.skillcanvas.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     * Create a new account
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);

    /**
     * Fetch account details
     * @param mobileNumber
     * @return CustomerDto
     */
    CustomerDto fetchAccountsDetails(String mobileNumber);

    /**
     * Update account details
     * @param customerDto
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     * Delete account details
     * @param mobileNumber
     */
    boolean deleteAccount(String mobileNumber);




}
