# DigiCash Payment Kit

[![N|Solid](http://www.digicash.tn/images/logo_digicash.png)](http://digitus.tech/) Payment-Kit

Digicash PaymentKit is a library for easily accept payments on Android apps | [![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

# How to use the kit
### First step:
  - Go to your root build.gradle, add **jitpack** like the code below
 ```sh
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Step 2. Add the dependency
  - Go at app build.gradle and update dependencies like below code
  ```sh
  dependencies {
	        compile 'com.github.DigitUs-tech:mcommerce:V0.1'
	}
  ```
 ### Step 3. Synchronize your project ! you are ready to use the payment kit
![N|Solid](https://camo.githubusercontent.com/ac1d3d96180c0b84cb7e8c8407c02511f4067b38/68747470733a2f2f7472617669732d63692e6f72672f677265656e726f626f742f4576656e744275732e7376673f6272616e63683d6d6173746572)

### How it work

- **1. Implement DigiCashPaymentListener**
```java
public class PayActivity extends Activity implements DigiCashPaymentListener
```
- **2. init your payment builder and payment request**
```java
myPaymentBuilder = PaymentBuilder.createWith(this, this)
                .addMerchantName("Simple Store")
                .addApiKey(" YOUR WALLET API KEY")
                .addDigiCode(" YOUR WALLET DIGI CODE")
                .addMerchantId("YOUR WALLET NYM"); 
                
    try {
            long amount = 1000; // 1 TND
            paymentRequest = myPaymentBuilder.preparePayment(amount, null);
            paymentRequest.pay();
        } catch (Exception e) {
            e.printStackTrace();
        }
```
- **3. Override onActivityResult**
```java
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ...
        myPaymentBuilder.onPaymentResult(requestCode, resultCode, data);
        ...
    }
```
```java
    ...
    
    public void onPaymentSuccessfullySend() {
        // Your payment being validated
        // Start the payment verification
        try {
            this.paymentRequest.verifyPayment();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onPaymentFailed() {
        // Payment not received
    }

    public void onError(Throwable throwable) {
    }

    public void onPaymentSuccessfullyConfirmed() {
        // Payment successfully received and confirmed :)
    }

    public void onPaymentPending() {
    // Payment successfully sended and pending confirmation :)
    }
```
