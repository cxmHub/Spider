package com.cxm.dao;

import com.cxm.pojo.Comment;

import java.sql.*;

public class CommentDao {

    private Connection connection;
    private PreparedStatement preparedStatement;

    public CommentDao() {
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

    public void addComment(Comment comment) {
        try {
            String sql = "INSERT INTO music_comments(nickname,comment,time) value(?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, comment.getNickname());
            preparedStatement.setString(2, comment.getComment());
            preparedStatement.setString(3, comment.getTime());

            preparedStatement.execute();
            System.out.println("插入成功>>" + comment.getNickname() + "::" + comment.getComment());
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

    public String existComment(Comment comment) {

        try {
            String sql = "select id from music_comments where nickename = ? and time = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, comment.getNickname());
            preparedStatement.setString(2, comment.getTime());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return "e";
            }
        }catch (Exception e){
            System.out.println("插入失败《《《《《《《 ");
            e.printStackTrace();
        }finally {
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
       return "o";
    }
}
