package eccrm.web.filter;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
    public String datUrl;
    public String sqldriver;
    public String userName;
    public String userPW;

    public String read(String name) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = p.getProperty(name);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ReadProperties() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        sqldriver = p.getProperty("mysqldriver");
        datUrl = p.getProperty("databaseurl");
        userName = p.getProperty("username");
        userPW = p.getProperty("userPW");
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
