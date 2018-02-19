package pcmr;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.MarkupUtil;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	protected AuthService auth;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean signedIn = auth.testConnection();
		
		String page = MarkupUtil.startHTML("");
		
		page = MarkupUtil.addStartHead(page);
		page = MarkupUtil.addHTMLTitle(page, "Bot Dashboard");
		page = MarkupUtil.addStylesheetLink(page, request.getContextPath() + "/style/style.css");
		page = MarkupUtil.addEndHead(page);
		
		page = MarkupUtil.addStartBody(page);
		
		page = MarkupUtil.addStartDivWithId(page, "nav");
		page = MarkupUtil.addStartDivWithClass(page, "wrapper");
		
		page = page + "<h1>Twitter Bot</h1>\n";
		
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addEndDiv(page);
		
		page = MarkupUtil.addStartDivWithId(page, "content");
		page = MarkupUtil.addStartDivWithClass(page, "wrapper");
		
		page = page + "<h1>Dashboard</h1>\n";
		page = page + "<p>This dashboard controls the bot.</p>\n";
		
		page = MarkupUtil.addStartDivWithClass(page, "setting");
		page = MarkupUtil.addStartDivWithClass(page, "name");
		page = page + "<p><strong>Bot Authenticated</strong></p>\n";
		page = page + "<p>Whether the bot can talk to Twitter.</p>\n";
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addStartDivWithClass(page, "value");
		page = MarkupUtil.addStartDiv(page);
		if (signedIn) {
			page = page + "<p>Signed in</p>\n";
			page = page + "<p>User: <strong>" + auth.getUser() + "</strong></p>\n";
		} else {
			page = page + "<p>Not signed in</p>\n";
			page = page + "<a href=\"" 
					+ request.getContextPath() + Authorize.SERVLET_PATH
					+ "?" 
					+ Authorize.SIGN_IN_PARAM 
					+ "=" 
					+ Authorize.SIGN_IN_VALUE_REQUEST 
					+ "\">Click here to sign in.</a></span>\n";
		}
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addEndDiv(page);
		
		page = MarkupUtil.addStartDivWithClass(page, "setting");
		page = MarkupUtil.addStartDivWithClass(page, "name");
		page = page + "<p><strong>Bot Enabled</strong></p>\n";
		page = page + "<p>Whether the bot <i>is</i> talking to Twitter.</p>\n";
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addStartDivWithClass(page, "value");
		page = MarkupUtil.addStartDiv(page);
		page = page + "<p>Off</p>";
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addEndDiv(page);
		
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addEndDiv(page);
		
		page = MarkupUtil.addStartDivWithId(page, "footer");
		page = MarkupUtil.addStartDivWithClass(page, "wrapper");
		
		page = page + "<p>Twitter is real bad</p>\n";
		
		page = MarkupUtil.addEndDiv(page);
		page = MarkupUtil.addEndDiv(page);
		
		page = MarkupUtil.addEndBody(page);
		
		response.getWriter().append(page);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
