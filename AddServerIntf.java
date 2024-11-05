import java.rmi.*;
public interface AddServerIntf extends Remote {
  String listMedics(String clinicName) throws RemoteException;
  String marcarConsulta(int idClient, int idMedico, String dataHora) throws RemoteException;
  String listConsultas(String idCliente) throws RemoteException;
  String cancelarConsulta(int idClient, String dataHora) throws RemoteException;

}
