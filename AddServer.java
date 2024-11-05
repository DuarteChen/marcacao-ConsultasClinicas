import java.net.*;
import java.rmi.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddServer {
  public static void main(String args[]) {

    try {
      AddServerImpl addServerImpl = new AddServerImpl();
      Naming.rebind("AddServer", addServerImpl);
    }
    catch(Exception e) {
      System.out.println("Exception: " + e);
    }


    String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
    String user = "user";
    String password = "user";

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      if (conn != null) {
          System.out.println("Conectado à base de dados com sucesso!");
     }
    } catch (SQLException e) {
      System.out.println("Erro ao conectar à base de dados:");
      e.printStackTrace();
    }

    
  }
}
