package com.cxm.dao;

import com.cxm.pojo.MyProxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author cxm
 * @description
 * @date 2019-09-17 14:27
 **/
public class ProxyDao {

    private Connection connection;
    private PreparedStatement preparedStatement;

    public ProxyDao() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://47.94.174.237/music?useUnicode=true&characterEncoding=utf8";
            String username = "root";
            String password = "Chenxinmao0.";
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public void addProxy(MyProxy myProxy) {
            try {
                String sql = "INSERT INTO proxy(ip,port) value(?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, myProxy.getIp());
                preparedStatement.setInt(2, myProxy.getPort());

                preparedStatement.execute();
                System.out.println("插入成功>>" + myProxy.getIp() + "::" + myProxy.getPort());
            } catch (SQLException e) {
                System.out.println("插入失败《《《《《《《 ");
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

}
