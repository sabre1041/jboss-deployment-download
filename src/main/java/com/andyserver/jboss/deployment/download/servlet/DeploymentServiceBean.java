package com.andyserver.jboss.deployment.download.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.as.repository.ContentRepository;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceName;
import org.jboss.vfs.VirtualFile;

/**
 * Contains the functionality to connect to the local JBoss server and return a 
 * list of deployed applications and return an InputStream containing a particular
 * deployment file
 * 
 * @author Andrew Block
 *
 */
@ApplicationScoped
public class DeploymentServiceBean {

	private ServiceContainer sc;
	private ContentRepository repo;
	private ModelController controller;

	@PostConstruct
	public void init() {
		sc = CurrentServiceContainer.getServiceContainer();
		repo = (ContentRepository) sc.getService(
				ServiceName.parse("jboss.content-repository")).getValue();
		controller = (ModelController) sc.getService(
				ServiceName.parse("jboss.as.server-controller")).getValue();
	}
	
	/**
	 * Returns the {@link InputStream} of a JBoss {@link VirtualFile}
	 * 
	 * @param deploymentName name of the Deployment
	 * @return InputStream of the JBoss VitrualFile containing the deployment
	 * @throws IOException
	 */
	public InputStream getDeploymentInputStream(String deploymentName) throws IOException {
		List<JBossDeployment> deployments = getDeployments();
		
		for(JBossDeployment deployment : deployments) {
			if(deploymentName.equals(deployment.getName())) {
				VirtualFile vf = repo.getContent(deployment.getHashContent());
				return vf.openStream();
			}
		}
		
		return null;
	}

	/**
	 * @return a List of Model Nodes Representing the Deployments for A Server
	 * @throws IOException
	 */
	public List<JBossDeployment> getDeployments() throws IOException {

		final ModelNode op = new ModelNode();
		final List<JBossDeployment> deployments = new ArrayList<JBossDeployment>();
		
		try (ModelControllerClient client = controller.createClient(Executors
				.newCachedThreadPool())) {

			op.get(ClientConstants.OP).set(
					ClientConstants.READ_RESOURCE_OPERATION);
			op.get(ClientConstants.OP_ADDR)
					.add(ClientConstants.DEPLOYMENT, "*");

			ModelNode result = client.execute(op);

			if(result.hasDefined(ClientConstants.OUTCOME)) {
				if (result.get(ClientConstants.OUTCOME).asString()
						.equals(ClientConstants.SUCCESS)) {
					for (ModelNode jbossDeployment : result.get(ClientConstants.RESULT)
							.asList()) {
						
						JBossDeployment deployment = getJBossDeployment(jbossDeployment.get(ClientConstants.RESULT));
						deployments.add(deployment);
						
					}
					
					return deployments;
				}
				else {
		            throw new IllegalStateException(String.format("A non successful result was returned. Result: %s", result));
				}

			}
			else {
	            throw new IllegalStateException(String.format("An unexpected response was found checking the deployment. Result: %s", result));
			}
			

		}
		catch(Exception e) {
	        throw new IllegalStateException(String.format("Could not execute operation '%s'", op), e);
		}
	}
	
	/**
	 * Translates JBoss DMR to a JBoss deployment domain object
	 * 
	 * @param JBoss DMR containing the Deployment 
	 * @return a domain deployment object
	 */
	private JBossDeployment getJBossDeployment(ModelNode deployment) {

		JBossDeployment jbossDeployment = new JBossDeployment();
		jbossDeployment.setName(deployment.get("name").asString());
		jbossDeployment.setRuntimeName(deployment.get("runtime-name").asString());
		jbossDeployment.setEnabled(Boolean.valueOf(deployment.get("enabled").asString()));
		jbossDeployment.setPersistent(Boolean.valueOf(deployment.get("persistent").asString()));

		ModelNode deploymentContent = deployment.get("content");

		for(ModelNode content : deploymentContent.asList()) {
			jbossDeployment.setHashContent(content.get("hash").asBytes());
			break;
		}
		
		return jbossDeployment;
	}

}
