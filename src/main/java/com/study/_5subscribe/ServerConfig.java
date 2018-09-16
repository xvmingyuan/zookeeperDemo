package com.study._5subscribe;
/**
 * 记录WorkServer的配置信息
 * @author xmy
 * @time：2018年9月13日 下午8:33:51
 */
public class ServerConfig {
	private String dbUrl;
	private String dbPwd;
	private String dbUser;
	public String getDbUrl() {
		return dbUrl;
	}
	public String getDbPwd() {
		return dbPwd;
	}
	public String getDbUser() {
		return dbUser;
	}
	public ServerConfig setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
		return this;
	}
	public ServerConfig setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
		return this;
	}
	public ServerConfig setDbUser(String dbUser) {
		this.dbUser = dbUser;
		return this;
	}
	@Override
	public String toString() {
		return "ServerConfig [dbUrl=" + dbUrl + ", dbPwd=" + dbPwd + ", dbUser=" + dbUser + "]";
	}
	
}
