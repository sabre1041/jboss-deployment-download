package com.andyserver.jboss.deployment.download.servlet;

import java.io.Serializable;

/**
 * Model representation of a JBoss deployment
 * 
 * @author Andrew Block
 *
 */
public class JBossDeployment implements Serializable {

	private static final long serialVersionUID = -1448850767624008101L;

	private String name;
	private String runtimeName;
	private byte[] hashContent;
	private boolean persistent;
	private boolean enabled;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRuntimeName() {
		return runtimeName;
	}
	public void setRuntimeName(String runtimeName) {
		this.runtimeName = runtimeName;
	}
	public byte[] getHashContent() {
		return hashContent;
	}
	public void setHashContent(byte[] hashContent) {
		this.hashContent = hashContent;
	}
	public boolean isPersistent() {
		return persistent;
	}
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
