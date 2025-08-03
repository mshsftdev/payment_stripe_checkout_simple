package org.msh.mshstripe.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.msh.mshstripe.dto.ProductRequest;
import org.msh.mshstripe.dto.StripeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    /*
    * input: amount, quantity, name, currency
    * output: sessionId and url
    */

    @Value("${stripe.secretkey}")
    private String secretKey;

    public StripeResponse checkoutProducts(ProductRequest productRequest)
    {
        Stripe.apiKey = secretKey;
        String currency = productRequest.getCurrency()==null?"USD": productRequest.getCurrency();
        //
        SessionCreateParams.LineItem.PriceData.ProductData productData
            =SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(productRequest.getName())
                .build();
        SessionCreateParams.LineItem.PriceData priceData
            =SessionCreateParams.LineItem.PriceData.builder()
                .setProductData(productData)
                .setCurrency(currency)
                .setUnitAmount(productRequest.getAmount())
                .build();
        SessionCreateParams.LineItem lineItem
            =SessionCreateParams.LineItem.builder()
                .setPriceData(priceData)
                .setQuantity(productRequest.getQuantity())
                .build();
        SessionCreateParams sessionCreateParams
            =SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .build();

        Session session=new Session();
        try{
            session=Session.create(sessionCreateParams);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());;
        }
        System.out.println("session.getId: "+session.getId());
        return StripeResponse.builder()
                .status("OK")
                .message("Session was created.")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }
}
