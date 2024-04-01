package com.midas.app.providers.external.stripe;

import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Component
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);
  @Autowired private final StripeConfiguration configuration;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(CreateAccount details) {
    Account account = null;
    Stripe.apiKey = configuration + "";
    String fullName = details.getFirstName() + details.getLastName();
    CustomerCreateParams params =
        CustomerCreateParams.builder().setName(fullName).setEmail(details.getEmail()).build();

    try {
      Customer customer = Customer.create(params);

      String fname = customer.getName();
      String[] nameParts = fname.split(" ");
      String firstName = nameParts[0];
      String lastName = nameParts[1];
      UUID accountId = UUID.randomUUID();

      account =
          new Account(
              accountId,
              firstName,
              lastName,
              customer.getEmail(),
              OffsetDateTime.now(),
              OffsetDateTime.now(),
              ProviderType.Stripe,
              customer.getId());

    } catch (StripeException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return account;
  }
}
