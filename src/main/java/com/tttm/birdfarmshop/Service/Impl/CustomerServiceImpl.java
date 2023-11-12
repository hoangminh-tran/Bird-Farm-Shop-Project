package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.Models.Customer;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Repository.CustomerRepository;
import com.tttm.birdfarmshop.Repository.UserRepository;
import com.tttm.birdfarmshop.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

    @Override
    public void createCustomer(User user) {
        Customer customer = new Customer(user.getUserID());
        customerRepository.save(customer);
    }
}
