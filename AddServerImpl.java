import java.rmi.*;
import java.rmi.server.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
  


  public static boolean isvalidDataHora(String dataHora) {
      // Definindo o formato esperado
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      dateFormat.setLenient(false); // Não permitir datas inválidas

      try {
          // Tenta analisar a data
          Date date = dateFormat.parse(dataHora);
          
          // Extrai a hora
          int hour = date.getHours();
          // Verifica se a hora está entre 8 (08:00) e 20 (20:00)
          if (hour < 8 || hour > 20) {
            System.out.println("Erro: A clinica está fechada às " + hour + " horas.");
            return false;
          }
          System.out.println("A data e a hora são válidas.");
          return true;
      } catch (ParseException e) {
          // Se ocorrer uma exceção, a string não está no formato correto
          System.out.println( "Erro no formato da data e da hora");
          return false;
      }
  }

  @Override
public String marcarConsulta(int idClient, int idMedico, String dataHora) throws RemoteException {
    String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
    String user = "user";
    String password = "user";

    //as consultas são das 8:00 às 20:00
    //O formato da string dataHora é "yyyy-mm-dd hh:mm"
    if (isvalidDataHora(dataHora)) {
      return "Erro";
    } //verifica e dá uma mensagem de feedback no terminal
  
    try (Connection conn = DriverManager.getConnection(url, user, password)) {
        String sql = "INSERT INTO Consulta (data, Cliente_idCliente, Medico_idMedico) " + 
                     "VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dataHora);
            stmt.setInt(2, idClient);
            stmt.setInt(3, idMedico);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Consulta marcada com sucesso: " + dataHora + " - Cliente: " + idClient + "; Médico: " + idMedico;
            } else {
                throw new RemoteException("Erro: a consulta não foi marcada. Verifique os dados fornecidos.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RemoteException("Erro ao marcar consulta", e);
  }
}

@Override
public String cancelarConsulta(int idClient, String dataHora) throws RemoteException {
    String url = "jdbc:mysql://localhost:3306/dbCDProjeto";
    String user = "user";
    String password = "user";

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
        String sql = "DELETE FROM Consulta WHERE Cliente_idCliente = ? AND data = ?";;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setString(2, dataHora);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Consulta cancelada com sucesso: " + dataHora + " - Cliente: " + idClient;
            } else {
                throw new RemoteException("Erro: a consulta não foi marcada. Verifique os dados fornecidos.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RemoteException("Erro ao marcar consulta", e);
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
  
}
