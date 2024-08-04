import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CrearPaciente extends JFrame {
    private JPanel panelCrearPaciente;
    private JTextField nombreTextField;
    private JTextField apellidoTextField;
    private JTextField lugarNacimientoTextField;
    private JTextField edadTextField;
    private JTextField generoTextField;
    private JTextField telefonoTextField;
    private JButton guardarButton;
    private JButton cancelarButton;

    public CrearPaciente() {
        super("Crear Paciente");

        panelCrearPaciente = new JPanel();
        panelCrearPaciente.setLayout(new GridLayout(7, 2));

        nombreTextField = new JTextField();
        apellidoTextField = new JTextField();
        lugarNacimientoTextField = new JTextField();
        edadTextField = new JTextField();
        generoTextField = new JTextField();
        telefonoTextField = new JTextField();
        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");

        // Añadir componentes al panel
        panelCrearPaciente.add(new JLabel("Nombre:"));
        panelCrearPaciente.add(nombreTextField);
        panelCrearPaciente.add(new JLabel("Apellido:"));
        panelCrearPaciente.add(apellidoTextField);
        panelCrearPaciente.add(new JLabel("Lugar de Nacimiento:"));
        panelCrearPaciente.add(lugarNacimientoTextField);
        panelCrearPaciente.add(new JLabel("Edad:"));
        panelCrearPaciente.add(edadTextField);
        panelCrearPaciente.add(new JLabel("Género:"));
        panelCrearPaciente.add(generoTextField);
        panelCrearPaciente.add(new JLabel("Teléfono:"));
        panelCrearPaciente.add(telefonoTextField);
        panelCrearPaciente.add(guardarButton);
        panelCrearPaciente.add(cancelarButton);

        setContentPane(panelCrearPaciente);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPaciente();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private Connection conexionBase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/medicare";
        String usuarioBD = "root";
        String contraseña = "123456";
        return DriverManager.getConnection(url, usuarioBD, contraseña);
    }

    private void guardarPaciente() {
        String nombre = nombreTextField.getText().trim();
        String apellido = apellidoTextField.getText().trim();
        String lugarNacimiento = lugarNacimientoTextField.getText().trim();
        String edad = edadTextField.getText().trim();
        String genero = generoTextField.getText().trim();
        String telefono = telefonoTextField.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || lugarNacimiento.isEmpty() ||
                edad.isEmpty() || genero.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = conexionBase()) {
            String sql = "INSERT INTO Pacientes (nombre, apellido, lugar_nacimiento, edad, genero, telefono) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, apellido);
            pst.setString(3, lugarNacimiento);
            pst.setString(4, edad);
            pst.setString(5, genero);
            pst.setString(6, telefono);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Paciente creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new CrearPaciente();
    }
}
