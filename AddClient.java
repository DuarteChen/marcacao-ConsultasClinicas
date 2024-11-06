 import java.rmi.*;
public class AddClient {
  public static void main(String args[]) {
    try {
      String addServerURL = "rmi://" + args[0] + "/AddServer";
      AddServerIntf addServerIntf = (AddServerIntf)Naming.lookup(addServerURL);
      if (args[1].trim().equals("cancelarConsulta")){

      
        if (args.length == 7) {

          int dia = Integer.parseInt(args[2]);
          int mes = Integer.parseInt(args[3]);
          int ano = Integer.parseInt(args[4]);
          int hora = Integer.parseInt(args[5]);
  
          int clientID = Integer.parseInt(args[6].trim());
          
          System.out.println(" A tentar cancelar consulta para o dia: " + dia + "-"+ mes + "-" + ano + " às " + hora + ":00 Para " + clientID + ".");
          
          System.out.println("Output: " + addServerIntf.cancelarConsulta(dia, mes , ano, hora, clientID));
          } else {
            System.out.println("Output: Número de argumentos errado");
          }


      } else if (args[1].trim().equals("listarConsultas")){

        if (args.length == 3) {
          String idClient = args[2].trim();
          System.out.println("Output: " + addServerIntf.listarConsultas(idClient));
        }

      } else if (args[1].trim().equals("listarMedicos")){

        if (args.length == 3) {
          String clinica = args[2];
          System.out.println(addServerIntf.listarMedicos(clinica));

        } else {
          System.out.println("Output: Número de argumentos errado");
        }

      } else if (args[1].trim().equals("marcarConsulta")) {
      
          if (args.length == 8) {

          int dia = Integer.parseInt(args[2]);
          int mes = Integer.parseInt(args[3]);
          int ano = Integer.parseInt(args[4]);
          int hora = Integer.parseInt(args[5]);

          int clientID = Integer.parseInt(args[6].trim());
          
          int medicID = Integer.parseInt(args[7].trim());
          System.out.println(" A tentar marcar consulta para o dia: " + dia + "-"+ mes + "-" + ano + " às " + hora + ":00 Para " + clientID + " com médico " + medicID);
          
          System.out.println("Output: " + addServerIntf.marcarConsulta(dia, mes , ano, hora, clientID, medicID));
          } else {
            System.out.println("Output: Número de argumentos errado");
          }
          
      } else { 
        System.out.println("Output: Erro nos argumentos\n");
      }
    } catch(Exception e) {
      System.out.println("Exception: " + e);
    }
  }









}
