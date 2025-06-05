package learning.lxl.week1111.step4;

import cn.hutool.db.ds.simple.SimpleDataSource;
import core.PropCore;
import db.MySql;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentExtractor {

    public static void main(String[] args) {
        PropCore propCore = new PropCore();

        propCore.run();

        action(propCore.getMySql());

        propCore.destroy();
    }

    @SneakyThrows
    public static void action(MySql mysql) {
        String studentSchema = "students";
        String getAllSql = "SELECT * FROM " + studentSchema;

        SimpleDataSource simpleDataSource = mysql.getSimpleDataSource();

        Connection connection = simpleDataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(getAllSql);

        while (resultSet.next()) {
            System.out.print(resultSet.getInt("id") + " ");
            System.out.print(resultSet.getString("name") + " ");
            System.out.print(resultSet.getString("gender") + " ");
            System.out.print(resultSet.getInt("score"));

            System.out.println("");
        }

        System.out.println("end");
    }

}
