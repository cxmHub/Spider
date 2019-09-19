package com.cxm.dao;

import com.cxm.pojo.DontCry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author cxm
 * @description
 * @date 2019-09-19 14:05
 **/
public class DontCryDao {


    private Connection connection;
    private PreparedStatement preparedStatement;

    public DontCryDao() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://47.94.174.237/douban?useUnicode=true&characterEncoding=utf8";
            String username = "root";
            String password = "Chenxinmao0.";
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(DontCry dontCry) {
        try {
            String sql = "INSERT INTO dont_cry(username,content,star,time) value(?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dontCry.getUsername());
            preparedStatement.setString(2, dontCry.getContent());
            preparedStatement.setInt(3, dontCry.getStar());
            preparedStatement.setString(4, dontCry.getTime());


            preparedStatement.execute();
            System.out.println("插入成功>>" + dontCry.getUsername() + "::" + dontCry.getContent());
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
