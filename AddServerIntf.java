import java.rmi.*;
public interface AddServerIntf extends Remote {
  String listarMedicos(String clinicName) throws RemoteException;
  String marcarConsulta(int dia, int mes, int  ano, int hora, int clientID, int medicID) throws RemoteException;
  String listarConsultas(String idCliente) throws RemoteException;
  String cancelarConsulta(int dia, int mes, int  ano, int hora, int clientID) throws RemoteException;

}
