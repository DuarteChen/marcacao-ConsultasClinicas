import java.rmi.*;
public interface AddServerIntf extends Remote {
  String listarMedicos(String clinicName) throws RemoteException;
  String marcarConsulta(int dia, int mes, int ano, int hora, int clientID, int clinicaID, int especialidadeID) throws RemoteException;
  String listarConsultas(String idCliente) throws RemoteException;
  String removerConsulta(int idConsulta) throws RemoteException;

}
