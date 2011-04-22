package test.gae;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Gae_masaoyano_sharepaintServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world 2");
//		Stroke stroke = new Stroke();
		Stroke.save("                                    ");
	}
}
