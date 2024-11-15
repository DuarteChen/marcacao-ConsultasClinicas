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
  public String marcarConsulta(int dia, int mes, int ano, int hora, int clientID, int clinicaID, int especialidadeID) throws RemoteException {
      String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
      String user = "user";
      String password = "user";
  
      if (hora < 8 || hora > 20) {
          return "Às " + hora + ":00 a clínica está fechada";
      }
      
      try (Connection conn = DriverManager.getConnection(url, user, password)) {
          // Check for available medics with the specified specialty and clinic
          String checkSql = "SELECT M.idMedico FROM Medico M " +
                            "JOIN tipoMedico T ON M.tipoMedico_idTipoMedico = T.idTipoMedico " +
                            "WHERE T.idTipoMedico = ? AND M.Clinica_idClinica = ? " +
                            "AND M.idMedico NOT IN (SELECT Medico_idMedico FROM Consulta " +
                            "WHERE data = ?)";
          
          try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
              checkStmt.setInt(1, especialidadeID);
              checkStmt.setInt(2, clinicaID);
              checkStmt.setString(3, ano + "-" + mes + "-" + dia + " " + hora + ":00"); //yyyy-mm-dd hh:mm:ss
              
              ResultSet rs = checkStmt.executeQuery();
              if (rs.next()) {
                  int medicoID = rs.getInt("idMedico");
                  
                  // Schedule the consultation
                  String insertSql = "INSERT INTO Consulta (data, Cliente_idCliente, Medico_idMedico) " +
                                     "VALUES (?, ?, ?)";
                  try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                      insertStmt.setString(1, ano + "-" + mes + "-" + dia + " " + hora + ":00");
                      insertStmt.setInt(2, clientID);
                      insertStmt.setInt(3, medicoID);
                      
                      int x = insertStmt.executeUpdate();
                      if (x > 0) {
                          return "Consulta marcada com sucesso para " + ano + "-" + mes + "-" + dia + " " + hora + ":00, Cliente: " + clientID + ", Médico: " + medicoID;
                      } else {
                          return "Erro a marcar a consulta";
                      }
                  }
              } else {
                  return "Não há médicos disponíveis com essa especialidade na data e hora solicitadas.";
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
          throw new RemoteException("Erro ao marcar consulta: ", e);
      }
  }

@Override
public String removerConsulta(int idConsulta) throws RemoteException {
    String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
    String user = "user";
    String password = "user";

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
        String sql = "DELETE FROM Consulta WHERE idConsulta = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConsulta);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Consulta removida com sucesso: ID Consulta " + idConsulta;
            } else {
                throw new RemoteException("Erro: a consulta com ID Consulta " + idConsulta + 
                                          " e ID Cliente não foi encontrada.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RemoteException("Erro ao remover consulta", e);
    }
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


@Override
public String listarClinicas() throws RemoteException {
  String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
  String user = "user";
  String password = "user";
  StringBuilder result = new StringBuilder();

  try (Connection conn = DriverManager.getConnection(url, user, password)) {
    String sql = "SELECT * FROM Clinica;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
     

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          int idClinicaDb = rs.getInt("idClinica");
          String nomeClincaDB = rs.getString("nome");
          result.append(idClinicaDb).append(". - Clinca: ").append(nomeClincaDB).append("\n");
        }
      }
    }
  } catch (SQLException e) {
    e.printStackTrace();
    throw new RemoteException("Erro ao listar clínicas.", e);
  }

  return result.length() > 0 ? result.toString() : "Nenhuma clínica encontrada.";
}

@Override
public String listarEspecialidades(int idClinica) throws RemoteException {
  String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
  String user = "user";
  String password = "user";
  StringBuilder result = new StringBuilder();

  try (Connection conn = DriverManager.getConnection(url, user, password)) {
    String sql = "SELECT DISTINCT tm.tipoMedico " +
                    "FROM clinica c JOIN medico m ON c.idClinica = m.Clinica_idClinica JOIN tipoMedico tm ON m.tipoMedico_idTipoMedico = tm.idTipoMedico" +
                    "WHERE c.idClinica = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        String idClinicaString = "" + idClinica;
        stmt.setString(1, idClinicaString);
     

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          int idEspecialidadeDb = rs.getInt("idTipoMedico");
          String nomeEspecialidadeDB = rs.getString("tipoMedico");
          result.append(idEspecialidadeDb).append(". - Especialidade: ").append(nomeEspecialidadeDB).append("\n");
        }
      }
    }
  } catch (SQLException e) {
    e.printStackTrace();
    throw new RemoteException("Erro ao listar especialidades.", e);
  }

  return result.length() > 0 ? result.toString() : "Nenhuma especialidade encontrada.";
}



  
}
