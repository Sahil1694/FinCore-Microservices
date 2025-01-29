package com.skillcanvas.accounts.service.impl;


import com.skillcanvas.accounts.dto.AccountsDto;
import com.skillcanvas.accounts.dto.CardsDto;
import com.skillcanvas.accounts.dto.CustomerDetailsDto;
import com.skillcanvas.accounts.dto.LoansDto;
import com.skillcanvas.accounts.entiity.Accounts;
import com.skillcanvas.accounts.entiity.Customer;
import com.skillcanvas.accounts.exception.ResourceNotFoundException;
import com.skillcanvas.accounts.mapper.AccountsMapper;
import com.skillcanvas.accounts.mapper.CustomerMapper;
import com.skillcanvas.accounts.repository.AccountsRepository;
import com.skillcanvas.accounts.repository.CustomerRepository;
import com.skillcanvas.accounts.service.ICustomersService;
import com.skillcanvas.accounts.service.client.CardsFeignClient;
import com.skillcanvas.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {


    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
