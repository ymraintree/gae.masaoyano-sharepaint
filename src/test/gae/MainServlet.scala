package test.gae

import java.io.IOException
import javax.servlet.http._
import java.util.logging._

class MainServlet extends HttpServlet {

	val log:Logger = Logger.getLogger(this.getClass.getName)

	@throws(classOf[IOException])
	override def doPost(req:HttpServletRequest, resp:HttpServletResponse) {
		val params = req.getParameter("params")
		log.warning("params:" + params)
		Stroke.save(params)
		val result = Stroke.findAll()
		resp.setContentType("text/plain")
		for (s <- result) resp.getWriter().write(s + "\n")
	}

} 