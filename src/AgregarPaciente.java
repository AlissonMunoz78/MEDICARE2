import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AgregarPaciente extends JFrame {
    private JPanel panelAgregarPaciente;
    private JTextField nombreTextField;
    private JTextField apellidoTextField;
    private JTextField lugarNacimientoTextField;
    private JTextField edadTextField;
    private JTextField generoTextField;
    private JTextField telefonoTextField;
    private JButton guardarButton;
    private JButton cancelarButton;

    public AgregarPaciente() {
        super("Agregar Paciente");

        // Inicializar el panel manualmente
        panelAgregarPaciente = new JPanel();
        panelAgregarPaciente.setLayout(new BorderLayout());

        nombreTextField = new JTextField(20);
        apellidoTextField = new JTextField(20);
        lugarNacimientoTextField = new JTextField(20);
        edadTextField = new JTextField(20);
        generoTextField = new JTextField(20);
        telefonoTextField = new JTextField(20);
        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreTextField);
        inputPanel.add(new JLabel("Apellido:"));
        inputPanel.add(apellidoTextField);
        inputPanel.add(new JLabel("Lugar de Nacimiento:"));
        inputPanel.add(lugarNacimientoTextField);
        inputPanel.add(new JLabel("Edad:"));
        inputPanel.add(edadTextField);
        inputPanel.add(new JLabel("Género:"));
        inputPanel.add(generoTextField);
        inputPanel.add(new JLabel("Teléfono:"));
        inputPanel.add(telefonoTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);

        panelAgregarPaciente.add(inputPanel, BorderLayout.CENTER);
        panelAgregarPaciente.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panelAgregarPaciente);
        setSize(400, 300);
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
            pst.setInt(4, Integer.parseInt(edad));
            pst.setString(5, genero);
            pst.setString(6, telefono);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Paciente agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AgregarPaciente();
    }
}
