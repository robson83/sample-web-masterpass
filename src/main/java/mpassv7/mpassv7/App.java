package mpassv7.mpassv7;


import static spark.Spark.*;
import com.mastercard.merchant.checkout.model.Card;

import spark.Service.StaticFiles;
import spark.Spark;
import static spark.debug.DebugScreen.enableDebugScreen;

public class App
{
	
	private static String consumerKey = "x9CN7cXABdzeOpjVrAOa7v4pHhpkzLNTDcMEIOY60e2a0e46!10e8bc47714141be983e984732a73a4c0000000000000000";
	private static String password = "wDWmmgi3AY0L5qwolJXw";
	private static String path = "/sandbox.p12";
	private static String checkoutId = "d072ca48e19b49c49bb76ea4513b42d1";
	private static String cartId = "21345";			
			
	
    public static void main( String[] args ) throws Exception
    {
    	
    	//Spark.staticFileLocation("/public/html");
    	String projectDir = System.getProperty("user.dir");
        String staticDir = "/src/main/resources/public/html";
        staticFiles.externalLocation(projectDir + staticDir);
    	
    	 get("/callback", (req, res) -> {
    		
    		 Test t = new Test(
    				 		consumerKey, 
    				 		password,
    				 		checkoutId,
    				 		cartId,
    				 		System.getProperty("user.dir") + path);
    		 
    		 Card c =  t.getCard(req.queryParams("oauth_verifier"));
    		 
    		 // simples retorno do cartao
    		 String x = c.getAccountNumber() + "<br>" + c.getBrandName() + "<br>" + c.getLastFour() + "<br> " + c.getCardHolderName();
    		 
    		 //nesse momento deve ir no gateway, adquirente e executar o fluxo normal da transacao
    		 
    		 
    		 
    		 // ao finalizar, se positivo ou negativo, deve executar o postback.
    		 // analisar o método e editar conforme os resultados
    		 t.performPostback(req.queryParams("oauth_verifier"));
    		 
    		 
    		 return x;
    		 
    	 });
    	 
    	 enableDebugScreen();
    	
    }
}
