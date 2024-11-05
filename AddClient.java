 import java.rmi.*;
public class AddClient {
  public static void main(String args[]) {
    try {
      String addServerURL = "rmi://" + args[0] + "/AddServer";
      AddServerIntf addServerIntf = (AddServerIntf)Naming.lookup(addServerURL);
      if (args[1].trim().equals("cancelarConsulta")){

      
        if (args.length == 4) {
          System.out.println("Data: " + args[2]);
          String dataHora = args[2];
          
          System.out.println("Client: " + args[3]);
          int clientID = Integer.parseInt(args[3].trim());
          
          System.out.println("Output: " + addServerIntf.cancelarConsulta(clientID, dataHora));
          } else {
            System.out.println("Output: Número de argumentos errado");
          }


    } else if (args[1].trim().equals("listarConsultasDeCliente")){
        System.out.println("Cliente: " + args[2]);
        String cliente = args[2];
        System.out.println("Output: " + addServerIntf.listarConsultas(cliente));
    } else if (args[1].trim().equals("listarMedicosDe")){
        System.out.println("Clinica: " + args[2]);
        String clinica = args[2];
        System.out.println("Output: " + addServerIntf.listarMedicos(clinica));
    } else if (args[1].trim().equals("marcar")) {
    
        if (args.length == 5) {
        System.out.println("Data: " + args[2]);
        String dataHora = args[2];
        
        System.out.println("Client: " + args[3]);
        int clientID = Integer.parseInt(args[3].trim());
        
        System.out.println("Medic: " + args[4]);
        int medicID = Integer.parseInt(args[4].trim());
        
        System.out.println("Output: " + addServerIntf.marcarConsulta(clientID, medicID, dataHora));
        } else {
          System.out.println("Output: Número de argumentos errado");
        }
        
    } else {
      System.out.println("Output: Erro");
    }
    } catch(Exception e) {
      System.out.println("Exception: " + e);
    }
  }









}
