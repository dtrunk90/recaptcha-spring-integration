reCAPTCHA Spring Integration
============================

```xml
<dependency>
	<groupId>com.github.dtrunk90</groupId>
	<artifactId>recaptcha-spring-integration</artifactId>
	<version>1.0.0</version>
</dependency>
```

Usage
-----

Simply extend your `@Valid` annotated Form object from [AbstractRecaptchaForm][1].
Alternatively, if the default doesn't fit your needs you can add your own `@Recaptcha` annotated field/method.
Per default authenticated users will be skipped. So you don't need to display the widget if the user is authenticated.
Add the mandatory properties to your project (the URL is optional):


| Property             | Default value                                   |
| :------------------- | :---------------------------------------------- |
| recaptcha.secret-key |                                                 |
| recaptcha.site-key   |                                                 |
| recaptcha.verify-url | https://www.google.com/recaptcha/api/siteverify |

Do the client side implementation as described [here][2] for reCAPTCHA v2 or [here][3] for Invisible reCAPTCHA.

In case you don't use Spring Boots autoconfiguration feature you have to configure it yourself. Take a look into [RecaptchaAutoConfiguration][4] for more information.

[1]: https://github.com/dtrunk90/recaptcha-spring-integration/blob/master/src/main/java/com/github/dtrunk90/recaptcha/spring/model/AbstractRecaptchaForm.java
[2]: https://developers.google.com/recaptcha/docs/display
[3]: https://developers.google.com/recaptcha/docs/invisible
[4]: https://github.com/dtrunk90/recaptcha-spring-integration/blob/master/src/main/java/com/github/dtrunk90/recaptcha/spring/boot/autoconfigure/RecaptchaAutoConfiguration.java
