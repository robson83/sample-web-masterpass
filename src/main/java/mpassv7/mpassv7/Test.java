package mpassv7.mpassv7;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Enumeration;

import com.mastercard.merchant.checkout.PaymentDataApi;
import com.mastercard.merchant.checkout.PostbackApi;
import com.mastercard.merchant.checkout.model.Card;
import com.mastercard.merchant.checkout.model.PaymentData;
import com.mastercard.merchant.checkout.model.Postback;
import com.mastercard.sdk.core.MasterCardApiConfig;
import com.mastercard.sdk.core.util.QueryParams;

public class Test 
{
	private String consumerKey;
	private String p12Path;
	private String password;
	private String checkoutId;
	private String cartId;
	
	public Test(String consumerKey, String password, String checkoutId, String cartId, String p12Path)
	{
		this.consumerKey = consumerKey;
		this.p12Path = p12Path;
		this.password = password;
		this.cartId = cartId;
		this.checkoutId = checkoutId;
		
	}
	
	private PrivateKey getPrivateKey() throws Exception
	{
		
		FileInputStream fis = new FileInputStream(this.p12Path);
		PrivateKey k = null;
		
		if(fis!=null)
		{
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(fis, this.password.toCharArray());
			
			k = (PrivateKey) ks.getKey("defaultSandboxKey", password.toCharArray());
			
		}
		
		fis.close();
		
		return k;
	}
	
    public Card getCard(String transactionId) throws Exception
    {
    	MasterCardApiConfig.setSandBox(true);     
    	MasterCardApiConfig.setConsumerKey(consumerKey);
    	MasterCardApiConfig.setPrivateKey(getPrivateKey());
    	
    	
    	QueryParams queryParams = new QueryParams()
                .add("checkoutId", this.checkoutId)
                .add("cartId", this.cartId);
   
    	
    	PaymentData payment = PaymentDataApi.show(transactionId, queryParams);
    
        return payment.getCard();
    }
    
    public Boolean performPostback(String transactionId) throws Exception
    {
    	//MasterCardApiConfig.setSandBox(true);     
    	//MasterCardApiConfig.setConsumerKey(consumerKey);
    	//MasterCardApiConfig.setPrivateKey(getPrivateKey());
    	
    	
    	ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
    	java.util.Date date = java.util.Date.from(zdt.toInstant());
    	 
    	Postback postback = new Postback()
    	                .transactionId(transactionId)
    	                .currency("USD")
    	                .paymentCode("123456")
    	                .paymentSuccessful(true)
    	                .amount(100.00)
    	                .paymentDate(date);
    	 
    	PostbackApi.create(postback);
    	
    	return true;
    }
	
}
