package com.midas.app.providers.external.stripe;

import com.stripe.Stripe;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("stripe")
public class StripeConfiguration {
  @Value("${stripe.api.key}")
  @NonNull private String apiKey;

  public void initStripe() {
    Stripe.apiKey = apiKey;
  }
}
