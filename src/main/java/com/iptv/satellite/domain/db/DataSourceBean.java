package com.iptv.satellite.domain.db;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * DataSourceModel
 */
public class DataSourceBean {

    private Integer id;

    @NotEmpty(message = "数据库名不能为空")
    private String beanName;

    @NotEmpty(message = "数据库连接地址不能为空")
    private String url;

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    private final String driverClassName;

    private static final String URL_PREFIX = "jdbc:mysql://";

    private static final String URL_SUFFIX = "useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true";

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the beanName
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * @return the driverClassName
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param beanName the beanName to set
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DataSourceBean() {
        driverClassName = "com.mysql.jdbc.Driver";
    }

    public void formatUrl() {
        StringBuffer stringBuffer = new StringBuffer();
        if (url != null && url.length() > 0) {
            if (!url.contains(URL_PREFIX)) {
                stringBuffer.append(URL_PREFIX).append(url);
            }
            if (url.contains("?")) {
                stringBuffer.append('&').append(URL_SUFFIX);
            } else {
                stringBuffer.append('?').append(URL_SUFFIX);
            }
            url = stringBuffer.toString();
        }
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("url:").append(url).append(' ').append("username:").append(userName).append(' ').append("password:").append(password);
        return stringBuffer.toString();
    }
}