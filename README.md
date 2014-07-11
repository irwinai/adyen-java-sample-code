Adyen Java Integration
==============
The code examples in this repository help you integrate with the Adyen platform using Java. Please go through the code examples and read the documentation in the files itself. Each code example requires you to change some parameters to connect to your Adyen account, such as merchant account and skincode.

## Examples overview
```
1.HPP (Hosted Payment Page)
  - CreatePaymentOnHpp             : Simple form creating a payment on our HPP
  - CreatePaymentOnHppAdvanced     : Advanced form creating a payment on our HPP
  - CreatePaymentUrl               : Create payment URL on our HPP
2.API
  - HttpPost
    - CreatePaymentAPI             : Create a payment via our API
    - CreatePaymentCSE             : Create a Client-Side Encrypted payment
  - SOAP
    - CreatePaymentAPI             : Create a payment via our API
    - CreatePaymentCSE             : Create a Client-Side Encrypted payment
4.Modifications
  - HttpPost
    - CancelOrRefundPayment        : Cancel or refund a payment using HTTP Post
    - CancelPayment                : Cancel a payment using HTTP Post
    - CapturePayment               : Capture a payment using HTTP Post
    - RefundPayment                : Request a refund using HTTP Post
  - SOAP
    - CancelOrRefundPayment        : Cancel or refund a payment using SOAP
    - CancelPayment                : Cancel a payment using SOAP
    - CapturePayment               : Capture a payment using SOAP
    - RefundPayment                : Request a refund using SOAP
```

## Code structure
```
src
  - com.adyen.examples.hpp                : Java implementation of 1.HPP
  - com.adyen.examples.api                : Java implementation of 2.API
  - com.adyen.examples.modifications      : Java implementation of 4.Modifications
WebContent
  - 1.HPP
    - create-payment-on-hpp.jsp           : JSP template file for simple HPP
    - create-payment-on-hpp-advanced.jsp  : JSP template file for advanced HPP
    - create-payment-url.jsp              : JSP template file for payment URL
  - 2.API
    - create-payment-cse.jsp              : JSP template file for Client Side Encryption
    - js
      - adyen.encrypt.min.js              : JavaScript file required for encrypting card data
  - WEB-INF
    - lib/                                : Java libraries (JARs) used in the servlets
    - web.xml                             : Deployment descriptor
  index.jsp                               : Dynamic index with links to all examples
```

## Manuals
The code examples are based on our Integration manual and the API manual which provides rich information on how our platform works. Please find our manuals on the Developers section at www.adyen.com.

## Support
If you do have any suggestions or questions regarding the code examples please send an e-mail to support@adyen.com.
