package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.entities.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class StripeService {

    private final String secretKey;

    public StripeService(@Value("${stripe.api.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Product createStripeProduct(CatalogItem localItem) throws StripeException {

        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName(localItem.getName())
                .setDescription(localItem.getDescription())
                .setActive(true)
                .putMetadata("local_id", String.valueOf(localItem.getId()))
                .build();

        return Product.create(productParams);
    }

    //Note: currency must be an iso string for currency "usd" "ron" etc
    public Price createStripeProductVariant(ProductVariant localVariant, Product stripeProduct, String currency) throws StripeException {


        Long priceInCents = localVariant.getCatalogItem().getPrice().multiply(new BigDecimal(100)).longValueExact();

        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setProduct(stripeProduct.getId())
                .setCurrency(currency)
                .setUnitAmount(priceInCents)
                .putMetadata("local_variant_id", String.valueOf(localVariant.getId()))
                .putMetadata("sku", localVariant.getSku())
                .setBillingScheme(PriceCreateParams.BillingScheme.PER_UNIT)
                .build();

        return Price.create(priceParams);

    }


    public Customer createStripeCustomer(User user) throws StripeException {

        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setName(user.getFirst_name() + " " + user.getLast_name())
                .build();


        return Customer.create(customerParams);
    }


    //Note: currency must be an iso string for currency "usd" "ron" etc
    public PaymentIntent createPaymentIntent(Cart cart, String currency) throws StripeException {

        BigDecimal totalPrice = BigDecimal.ZERO;

        for(CartItem item : cart.getCartItems()) {

            totalPrice = totalPrice.add(item.getProductVariant().getCatalogItem().getPrice()
                    .multiply(new BigDecimal(item.getQuantity())));
        }
        Long priceInCents = totalPrice
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .longValueExact();


        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount(priceInCents)
                .setCurrency(currency)
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.AUTOMATIC)
                .build();

        return PaymentIntent.create(paymentIntentParams);
    }
}
