import java.rmi.*;
import java.rmi.server.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Statement;

public class AddServerImpl extends UnicastRemoteObject implements AddServerIntf {

  public AddServerImpl() throws RemoteException {
    super();
  }

  @Override
  public String listarMedicos(String clinicName) throws RemoteException {
    String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
    String user = "user";
    String password = "user";
    StringBuilder result = new StringBuilder();

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      String sql = "SELECT M.* FROM Medico M JOIN Clinica C ON M.Clinica_idClinica = C.idClinica WHERE C.nome = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, clinicName);

        try (ResultSet rs = stmt.executeQuery()) {
          while (rs.next()) {
            int idMedicoDb = rs.getInt("idMedico");
            String nomeMedico = rs.getString("nomeMedico");
            result.append("ID: ").append(idMedicoDb).append(", Nome: ").append(nomeMedico).append("\n");
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RemoteException("Erro ao listar médicos.", e);
    }

    return result.length() > 0 ? result.toString() : "Nenhum médico encontrado.";
  }

  @Override
public String marcarConsulta(int dia, int mes, int  ano, int hora, int clientID, int medicID) throws RemoteException {
    String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
    String user = "user";
    String password = "user";

    if (hora < 8 || hora > 20) {
      return "Às " + hora + ":00 a clínica está fechada";
    }
    
    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO Consulta (data, Cliente_idCliente, Medico_idMedico) " + 
                    "VALUES ('" + ano + "-" + mes + "-" + dia + " " + hora + ":00', " + clientID + ", " + medicID + ")";
              
      int x = stmt.executeUpdate(sql);
      
      if (x == 0){            
        return "Erro a marcar a consulta";  
      }

    } catch (SQLException e) {
    e.printStackTrace();
    throw new RemoteException("Erro ao marcar consulta: ", e);
  }

  return "Consulta marcada com sucesso para dia" + ano + "-" + mes + "-" + dia + " " + hora + ":00, Cliente: " + clientID + ", Médico: " + medicID;            

}

@Override
public String cancelarConsulta(int dia, int mes, int  ano, int hora, int clientID) throws RemoteException {
    String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
    String user = "user";
    String password = "user";

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      Statement stmt = conn.createStatement();
      String sql = "DELETE FROM Consulta WHERE data = '" + ano + "-" + mes + "-" + dia + " " + hora + ":00' AND Cliente_idCliente = " + clientID;
      
      int x = stmt.executeUpdate(sql);
      
      if (x == 0){            
        return "Erro a marcar a consulta";  
      }

    } catch (SQLException e) {
        e.printStackTrace();
        throw new RemoteException("Erro ao marcar consulta", e);
  }

  return "Consulta cancelada com sucesso para dia" + ano + "-" + mes + "-" + dia + " " + hora + ":00, Cliente: " + clientID;

}

@Override
public String listarConsultas(String idCliente) throws RemoteException {
    String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
    String user = "user";
    String password = "user";
    StringBuilder result = new StringBuilder();

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
        String sql = "SELECT C.idConsulta, C.data, M.nomeMedico " +
                     "FROM Consulta C " +
                     "JOIN Medico M ON C.Medico_idMedico = M.idMedico " +
                     "WHERE C.Cliente_idCliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idConsulta = rs.getInt("idConsulta");
                    String data = rs.getString("data");
                    String nomeMedico = rs.getString("nomeMedico");
                    result.append("ID Consulta: ").append(idConsulta)
                          .append(", Data: ").append(data)
                          .append(", Médico: ").append(nomeMedico).append("\n");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RemoteException("Erro ao listar consultas.", e);
    }

    return result.length() > 0 ? result.toString() : "Nenhuma consulta encontrada para o cliente.";
}
  
}
