import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GestionEmpleadosApp extends JFrame {
    private JTextField nombreField, apellidoField, edadField, cargoField;
    private JButton btnCrear, btnLeer, btnActualizar, btnBorrar;
    private JTextArea resultadoArea;
    private Connection connection;

    // Constructor de la clase
    public GestionEmpleadosApp() {
        super("Gestión de Empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        setSize(400, 300); // Establece el tamaño de la ventana
        setLayout(new BorderLayout()); // Establece el diseño de la ventana

        // Campos de texto para ingresar información del empleado
        nombreField = new JTextField(10);
        apellidoField = new JTextField(10);
        edadField = new JTextField(5);
        cargoField = new JTextField(10);

        // Botones para realizar acciones
        btnCrear = new JButton("Crear");
        btnLeer = new JButton("Leer");
        btnActualizar = new JButton("Actualizar");
        btnBorrar = new JButton("Borrar");

        // Área de texto para mostrar resultados
        resultadoArea = new JTextArea(10, 20);
        resultadoArea.setEditable(false);

        // Panel para organizar los campos de entrada y botones
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(apellidoField);
        formPanel.add(new JLabel("Edad:"));
        formPanel.add(edadField);
        formPanel.add(new JLabel("Cargo:"));
        formPanel.add(cargoField);
        formPanel.add(btnCrear);
        formPanel.add(btnLeer);
        formPanel.add(btnActualizar);
        formPanel.add(btnBorrar);

        // Agrega el panel de formulario y el área de texto a la ventana
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultadoArea), BorderLayout.CENTER);

        // Asocia acciones a los botones
        btnCrear.addActionListener(e -> crearEmpleado());
        btnLeer.addActionListener(e -> leerEmpleados());
        // Implementa acciones para actualizar y borrar

        // Conecta a la base de datos al iniciar la aplicación
        conectarBD();
    }

    // Método para establecer la conexión con la base de datos SQLite
    private void conectarBD() {
        try {
            // URL de conexión a la base de datos
            String url = "jdbc:sqlite:gestion_empleados.db";
            // Establece la conexión
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para agregar un nuevo empleado a la base de datos
    private void crearEmpleado() {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        int edad = Integer.parseInt(edadField.getText());
        String cargo = cargoField.getText();

        try {
            // Prepara la sentencia SQL para insertar un nuevo empleado
            PreparedStatement statement = connection.prepareStatement("INSERT INTO empleados (nombre, apellido, edad, cargo) VALUES (?, ?, ?, ?)");
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setInt(3, edad);
            statement.setString(4, cargo);
            // Ejecuta la sentencia SQL
            statement.executeUpdate();
            // Muestra un mensaje de éxito
            JOptionPane.showMessageDialog(this, "Empleado creado correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para leer y mostrar todos los empleados de la base de datos
    private void leerEmpleados() {
        try {
            // Crea una declaración SQL para seleccionar todos los empleados
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM empleados");
            // Limpia el área de texto
            resultadoArea.setText("");
            // Recorre el resultado y muestra la información de cada empleado
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                int edad = resultSet.getInt("edad");
                String cargo = resultSet.getString("cargo");
                resultadoArea.append("ID: " + id + ", Nombre: " + nombre + ", Apellido: " + apellido + ", Edad: " + edad + ", Cargo: " + cargo + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método principal, se inicia la aplicación
    public static void main(String[] args) {
        // Crea y muestra la ventana de la aplicación en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            GestionEmpleadosApp app = new GestionEmpleadosApp();
            app.setVisible(true);
        });
    }
}
