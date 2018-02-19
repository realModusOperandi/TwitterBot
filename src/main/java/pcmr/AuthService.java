package pcmr;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

@Singleton
@Startup
public class AuthService {
	protected OAuth1RequestToken requestToken = null;
	protected OAuth1AccessToken accessToken = null;
	protected OAuth10aService service = null;
	
	protected boolean loggedIn = false;
	protected String user = null;
	
	protected OAuth10aService getTwitterService(String callback) {
		if (service == null) {
			service = new ServiceBuilder()
					.apiKey("NOPE")
					.apiSecret("NOPE")
					.callback(callback)
					.build(TwitterApi.instance());
		}
		
		return service;
	}
	
	protected OAuth1RequestToken createRequestToken(String callback) throws IOException, InterruptedException, ExecutionException {
		if (requestToken == null) {
			requestToken = getTwitterService(callback).getRequestToken();
		}
		
		return requestToken;
		
	}
	
	public String doTwitterRequest(String callback) {
		OAuth1RequestToken token;
		try {
			token = createRequestToken(callback);
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			token = null;
		}
		
		if (token == null) {
			return null;
		}
		
		return service.getAuthorizationUrl(token);
	}
	
	public void doTwitterAccess(String verifier) {
		try {
			accessToken = service.getAccessToken(requestToken, verifier);
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			accessToken = null;
		}
	}
	
	public boolean testConnection() {
		if (service == null || accessToken == null) {
			return false;
		}
		final OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");
		service.signRequest(accessToken, oAuthRequest); // the access token from step 4
		Response oAuthResponse = null;
		try {
			oAuthResponse = service.execute(oAuthRequest);
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//response.getWriter().append(oAuthResponse.getBody());
		if (oAuthResponse == null || !oAuthResponse.isSuccessful()) {
			return false;
		}
		
		JsonReader response = Json.createReader(oAuthResponse.getStream()); 
		JsonObject responseBody = response.readObject();
		user = responseBody.getString("screen_name", null);
		
		return true;
	}
	
	public boolean hasTokens() {
		return (service != null && accessToken != null);
	}
	
	public String getUser() {
		if (user == null) {
			testConnection();
		}
		
		return user;
	}
}
