package RealizarPedido;


import java.sql.*; //importamos la libreria que permite la conexión con mysql

public class Conexion {

  private Connection conecta; //Objeto utilizado para proporcionar el vinculo entre la aplicación y la BD
  private String host; //Cadena utilizada para definir el host donde esta alojada la BD.
  private String nombreBase; //Cadena utilizada para definir el nombre de la BD.
  private String nombreUsuario; //Cadena utilizada para definir el nombre de usuario para acceder a la BD.
  private String password; //Cadena utilizada para definir la contraseña del usuario para acceder a la BD.
  private String url; //Cadena utilizada para guardar la dirección de la BD

  private static Conexion connect; //Variable de clase

  public Conexion() { //constructor por defecto 
    host = "localhost"; //La base de datos se encuentra en el host local 
    nombreBase = "simubase"; //El nombre de la BD es expendio 
    nombreUsuario = "root"; //El usuario que maneja la BD expendio es admiexpendio
    password = "alanbase";//La contraseña del usuario admiexpendio es sudoexpendio
    url = "jdbc:mysql://" + host + "/" + nombreBase; //Este será el parametro para el método getConnection, que recibe un String con la dirección de la BD y sus datos de acceso. 

    try { //Intentamos conectar con la BD
      Class.forName("com.mysql.jdbc.Driver").newInstance(); //Aquí verificamos que el driver funciona correctamente
      conecta = DriverManager.getConnection(url, nombreUsuario, password); //Establecemos la conexión con la BD
    } catch (SQLException ex) { //Atrapamos excepciones SQL (se relacionan con la conexión con la BD)
      //AQUI IRA UN JOPTION TIPO "ERROR DE MYSQL"
      System.out.println("Se produjo un error de conexion: " + ex.getMessage());
    } catch (ClassNotFoundException e) { //Atrapamos excepciones relacionadas con el driver 
      e.printStackTrace();
    } catch (Exception e) { //Atrapamos todas las demás excepciones
      System.out.println("Se produjo un error inesperado: " + e.getMessage());
    }

    connect = this;
  }

  //Constructor sobrecargado
  public Conexion(String host, String nombreBase, String nombreUsuario, String password) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    this.host = host;
    this.nombreBase = nombreBase;
    this.nombreUsuario = nombreUsuario;
    this.password = password;
    conecta = DriverManager.getConnection("jdbc:mysql://" + host + "/" + nombreBase, nombreUsuario, password);
    connect = this;
  }

  //LOS MÉTODOS SET Y GET NO ME PARECIERON NECESARIOS PARA ESTE PROYECTO
  public Connection connection() {
    try {
      return conecta;
    } finally {

    }
  }
}
