package com.iptv.satellite.domain.model;

/**
 * DataSourceModel
 */
public class DataSourceModel {

    private final String driverClassName;
    private String url;
    private String userName;
    private String password;
    private static final String URL_PREFIX = "jdbc:mysql://";

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

    public DataSourceModel() {
        driverClassName = "com.mysql.jdbc.Driver";
    }

    public void formatUrl() {
        if (url != null && url.length() > 0) {
            if (!url.contains(URL_PREFIX)) {
                url = URL_PREFIX + url; 
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("url:").append(url).append(' ').append("username:").append(userName).append(' ').append("password:").append(password);
        return stringBuffer.toString();
    }
}