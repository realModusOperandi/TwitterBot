package pcmr;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Authorize
 */
@WebServlet("/Authorize")
public class Authorize extends HttpServlet {
	
	public static final String SIGN_IN_PARAM = "signIn";
	public static final String SIGN_IN_VALUE_REQUEST = "request";
	public static final String OAUTH_TOKEN_PARAM = "oauth_token";
	public static final String OAUTH_VERIFIER_PARAM = "oauth_verifier";
	public static final String SERVLET_PATH = "/Authorize";

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected AuthService authService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authorize() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter(SIGN_IN_PARAM);
		if (login != null && login.equals(SIGN_IN_VALUE_REQUEST)) {
			String authURL = authService.doTwitterRequest(request.getRequestURL().toString());
			System.out.println("Redirecting to " + authURL);
			response.sendRedirect(authURL);
			
		} else if (request.getParameter(OAUTH_TOKEN_PARAM) != null && request.getParameter(OAUTH_VERIFIER_PARAM) != null) {
			authService.doTwitterAccess(request.getParameter(OAUTH_VERIFIER_PARAM));
			
			if (authService.testConnection()) {
				System.out.println("Connection to Twitter succeeded.");
				response.sendRedirect(request.getContextPath());
			}
			else {
				response.getWriter().append("Connection to Twitter failed.");
			}
		} else {
			if (authService.testConnection()) {
				response.getWriter().append("Connection to Twitter succeeded.");
			} else if (!authService.hasTokens()) {
				response.getWriter().append("<html><head></head><body>"
												+ "Not signed in! <a href=\"" 
												+ request.getContextPath() + request.getServletPath() 
												+ "?" 
												+ SIGN_IN_PARAM 
												+ "=" 
												+ SIGN_IN_VALUE_REQUEST 
												+ "\">Click here to sign in.</a></body></html>");
												
			} else {
				response.getWriter().append("Connection to Twitter failed. Consult the logs.");
			}
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
