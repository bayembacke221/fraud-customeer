package sn.bmbacke.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService{
    public final CustomerRepository customerRepository;
    public final RestTemplate restTemplate;
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();
        customerRepository.saveAndFlush(customer);
        FraudCheckResponse fraudCheckResponse=restTemplate.getForObject(
                "http://localhost:8081/api/v1/fraud-check/{customerId}",
                 FraudCheckResponse.class,
                customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw new RuntimeException("Fraudulent customer");
        }

    }
}
