package com.skillcanvas.accounts.service.impl;

import com.skillcanvas.accounts.constants.AccountsConstants;
import com.skillcanvas.accounts.dto.AccountsDto;
import com.skillcanvas.accounts.dto.CustomerDto;
import com.skillcanvas.accounts.entiity.Accounts;
import com.skillcanvas.accounts.dto.AccountsMsgDto;
import com.skillcanvas.accounts.entiity.Customer;
import com.skillcanvas.accounts.exception.CustomerAlreadyExistsException;
import com.skillcanvas.accounts.exception.ResourceNotFoundException;
import com.skillcanvas.accounts.mapper.AccountsMapper;
import com.skillcanvas.accounts.mapper.CustomerMapper;
import com.skillcanvas.accounts.repository.AccountsRepository;
import com.skillcanvas.accounts.repository.CustomerRepository;
import com.skillcanvas.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private  final StreamBridge streamBridge;

    /**
     * Create a new account
     * @param customerDto
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer>optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already exists with mobile number "+customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer =  customerRepository.save(customer);
        Accounts savedAccount = accountsRepository.save(createNewAccounts(savedCustomer));
        sendCommunication(savedAccount, savedCustomer);
    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }

    /**
     * Fetch account details
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccountsDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDto customerDto =  CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts , new AccountsDto()));

        return customerDto;
    }

    /**
     * Update account details
     * @param customerDto
     * @return boolean value
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }




    /**
     * Create a new account
     * @param customer
     * @return the new account details
     */
    private Accounts createNewAccounts(Customer customer){
        Accounts newAccounts = new Accounts();
        newAccounts.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccounts.setAccountNumber(randomAccNumber);
        newAccounts.setAccountType(AccountsConstants.SAVINGS);
        newAccounts.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccounts;
    }

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
      boolean isUpdated = false;
      if(accountNumber != null){
         Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                 () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
         );
         accounts.setCommunicationSw(true);
         accountsRepository.save(accounts);
         isUpdated = true;
      }
      return isUpdated;
    }
}
