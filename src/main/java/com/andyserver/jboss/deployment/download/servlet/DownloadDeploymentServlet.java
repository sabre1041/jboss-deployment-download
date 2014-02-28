package com.andyserver.jboss.deployment.download.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to Download the Deployment from the JBoss Server
 * 
 * @author Andrew Block
 *
 */
@WebServlet("/download")
public class DownloadDeploymentServlet extends GenericServlet {

	private static final long serialVersionUID = 3677384703604683402L;

	@Inject
	DeploymentServiceBean bean;

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
				
		String deploymentName = request.getParameter("deploymentName");
		
		if(deploymentName == null) {
			response.getWriter().append("Invalid Input: No Deployment File Specified");
			return;
		}
		else {
		
			InputStream in = bean.getDeploymentInputStream(deploymentName);
			
			if(in != null) {
				
				httpResponse.setContentType("application/octet-stream");
				
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", deploymentName);
				httpResponse.setHeader(headerKey, headerValue);
				
				OutputStream outStream = response.getOutputStream();
				
				byte[] buffer = new byte[4096];
				int bytesRead = -1;
				
				while ((bytesRead = in.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
				
				outStream.close();		
				in.close();

			}
			else {
				response.getWriter().append("Invalid Deployment Name Specified");
			}
			
		}
		
		
		
	}

}
