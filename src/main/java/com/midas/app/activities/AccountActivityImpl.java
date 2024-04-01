package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.providers.payment.CreateAccount;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountActivityImpl implements AccountActivity {
  @Autowired private StripePaymentProvider stripePaymentProvider;

  @Override
  public Account saveAccount(Account account) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveAccount'");
  }

  @Override
  public Account createPaymentAccount(Account account) {
    CreateAccount createAccount =
        new CreateAccount(null, account.getFirstName(), account.getLastName(), account.getEmail());

    return stripePaymentProvider.createAccount(createAccount);
  }
}
