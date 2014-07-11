package com.adyen.examples.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.BindingProvider;

import com.adyen.services.common.Address;
import com.adyen.services.common.Amount;
import com.adyen.services.payment.Card;
import com.adyen.services.payment.Payment;
import com.adyen.services.payment.PaymentPortType;
import com.adyen.services.payment.PaymentRequest;
import com.adyen.services.payment.PaymentResult;
import com.adyen.services.payment.ServiceException;

/**
 * Create Payment through the API (SOAP)
 * 
 * Payments can be created through our API, however this is only possible if you are PCI Compliant. SOAP API payments
 * are submitted using the authorise action. We will explain a simple credit card submission.
 * 
 * Please note: using our API requires a web service user. Set up your Webservice user:
 * Adyen CA >> Settings >> Users >> ws@Company. >> Generate Password >> Submit
 * 
 * @link /2.API/Soap/CreatePaymentAPI
 * @author Created by Adyen - Payments Made Easy
 */

@WebServlet(urlPatterns = { "/2.API/Soap/CreatePaymentAPI" })
public class CreatePaymentAPISoap extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/**
		 * SOAP settings
		 * - wsdl: the WSDL url you are using (Test/Live)
		 * - wsUser: your web service user
		 * - wsPassword: your web service user's password
		 */
		String wsdl = "https://pal-test.adyen.com/pal/Payment.wsdl";
		String wsUser = "YourWSUser";
		String wsPassword = "YourWSPassword";

		/**
		 * Create SOAP client, using classes in adyen-wsdl-cxf.jar library (generated by wsdl2java tool, Apache CXF).
		 * 
		 * @see WebContent/WEB-INF/lib/adyen-wsdl-cxf.jar
		 */
		Payment service = new Payment(new URL(wsdl));
		PaymentPortType client = service.getPaymentHttpPort();

		// Set HTTP Authentication
		((BindingProvider) client).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, wsUser);
		((BindingProvider) client).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, wsPassword);

		/**
		 * A payment can be submitted by sending a PaymentRequest to the authorise action of the web service.
		 * The request should contain the following variables:
		 * 
		 * <pre>
		 * - merchantAccount           : The merchant account for which you want to process the payment
		 * - amount
		 *     - currency              : The three character ISO currency code.
		 *     - value                 : The transaction amount in minor units (e.g. EUR 1,00 = 100).
		 * - reference                 : Your reference for this payment.
		 * - shopperIP                 : The shopper's IP address. (recommended)
		 * - shopperEmail              : The shopper's email address. (recommended)
		 * - shopperReference          : An ID that uniquely identifes the shopper, such as a customer id. (recommended)
		 * - fraudOffset               : An integer that is added to the normal fraud score. (optional)
		 * - card
		 *     - expiryMonth           : The expiration date's month written as a 2-digit string,
		 *                               padded with 0 if required (e.g. 03 or 12).
		 *     - expiryYear            : The expiration date's year written as in full (e.g. 2016).
		 *     - holderName            : The card holder's name, as embossed on the card.
		 *     - number                : The card number.
		 *     - cvc                   : The card validation code, which is the CVC2 (MasterCard),
		 *                               CVV2 (Visa) or CID (American Express).
		 *     - billingAddress (recommended)
		 *         - street            : The street name.
		 *         - houseNumberOrName : The house number (or name).
		 *         - city              : The city.
		 *         - postalCode        : The postal/zip code.
		 *         - stateOrProvince   : The state or province.
		 *         - country           : The country in ISO 3166-1 alpha-2 format (e.g. NL).
		 * </pre>
		 */

		// Create new payment request
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setMerchantAccount("YourMerchantAccount");
		paymentRequest.setReference("TEST-PAYMENT-" + new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date()));
		paymentRequest.setShopperIP("123.123.123.123");
		paymentRequest.setShopperEmail("test@example.com");
		paymentRequest.setShopperReference("YourReference");
		paymentRequest.setFraudOffset(0);

		// Set amount
		Amount amount = new Amount();
		amount.setCurrency("EUR");
		amount.setValue(199L);
		paymentRequest.setAmount(amount);

		// Set card
		Card card = new Card();
		card.setExpiryMonth("06");
		card.setExpiryYear("2016");
		card.setHolderName("John Doe");
		card.setNumber("5555444433331111");
		card.setCvc("737");

		Address billingAddress = new Address();
		billingAddress.setStreet("Simon Carmiggeltstraat");
		billingAddress.setPostalCode("1011 DJ");
		billingAddress.setCity("Amsterdam");
		billingAddress.setHouseNumberOrName("6-50");
		billingAddress.setStateOrProvince("");
		billingAddress.setCountry("NL");
		card.setBillingAddress(billingAddress);

		paymentRequest.setCard(card);

		/**
		 * Send the authorise request.
		 */
		PaymentResult paymentResult;
		try {
			paymentResult = client.authorise(paymentRequest);
		} catch (ServiceException e) {
			throw new ServletException(e);
		}

		/**
		 * If the payment passes validation a risk analysis will be done and, depending on the outcome, an authorisation
		 * will be attempted. You receive a payment response with the following fields:
		 * 
		 * <pre>
		 * - pspReference    : Adyen's unique reference that is associated with the payment.
		 * - resultCode      : The result of the payment. Possible values: Authorised, Refused, Error or Received.
		 * - authCode        : The authorisation code if the payment was successful. Blank otherwise.
		 * - refusalReason   : Adyen's mapped refusal reason, populated if the payment was refused.
		 * </pre>
		 */
		PrintWriter out = response.getWriter();

		out.println("Payment Result:");
		out.println("- pspReference: " + paymentResult.getPspReference());
		out.println("- resultCode: " + paymentResult.getResultCode());
		out.println("- authCode: " + paymentResult.getAuthCode());
		out.println("- refusalReason: " + paymentResult.getRefusalReason());

	}

}
