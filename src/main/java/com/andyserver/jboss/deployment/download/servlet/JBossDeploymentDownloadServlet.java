package com.andyserver.jboss.deployment.download.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * Default Servlet containing statistics of deployments of the local JBoss server. Provides
 * the ability to download the deployment artifact to an end users machine
 * 
 * @author Andrew Block
 *
 */
@WebServlet("/")
public class JBossDeploymentDownloadServlet extends GenericServlet {

	private static final long serialVersionUID = 6771145812001841277L;

	@Inject
	DeploymentServiceBean bean;
	
	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {		
		
		HttpServletResponse res = (HttpServletResponse) response;
		
		res.setContentType("text/html");
		
		List<JBossDeployment> deployments = bean.getDeployments();
		
		PrintWriter writer = response.getWriter();
		
		writer.append("<html><head><title>JBoss Deployments</title></head><body>");
		
		writer.append("<table width='100%' border='1'>");
		
		
		if(deployments != null && !deployments.isEmpty()) {
			
			writer.append("<tr><td><b>Name<b></td><td><b>Runtime Name</b></td><td><b>Enabled</b></td><td><b>Persistent</b></td><td><b>Action<b></td></tr>");
			
			for(JBossDeployment deployment : deployments) {
				
				writer.append("<form action='download' method='POST' target='_new'>");
				writer.append("<tr>");
				writer.append("<td>"+deployment.getName()+"</td>");
				writer.append("<td>"+deployment.getRuntimeName()+"</td>");
				writer.append("<td>"+deployment.isEnabled()+"</td>");
				writer.append("<td>"+deployment.isPersistent()+"</td>");
				writer.append("<td><input type='submit' value='Download'></td>");
				writer.append("<input type='hidden' name='deploymentName' value='"+deployment.getName()+"'/>");
				writer.append("</tr>");
				writer.append("</form>");
			}
		}
		else {
			writer.append("<tr><td colspan='2'>No Deployments Exist</td></tr>");
		
		}
		
		writer.append("</table>");
		writer.append("</body></html>");
		
		
	}

}
