package ASimulationSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conn {
    public Connection c;
    public Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATMSystem", "root", "**********");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        Conn c=new Conn();
    }
}
