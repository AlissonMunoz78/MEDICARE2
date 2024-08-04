import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ActualizarPaciente extends JFrame {

    private JPanel panelActualizarPaciente;
    private JTextField idTextField;
    private JTextField nombreTextField;
    private JTextField apellidosTextField;
    private JTextField fechaNacimientoTextField;
    private JTextField generoTextField;
    private JTextField ciudadTextField;
    private JTextField direccionTextField;
    private JTextField telefonoTextField;
    private JTextField contactoemergenciaTextField;
    private JButton guardarButton;
    private JButton cancelarButton;

    public ActualizarPaciente() {
        super("Actualizar Paciente");

        panelActualizarPaciente = new JPanel();
        panelActualizarPaciente.setLayout(new BorderLayout());

        idTextField = new JTextField(20);
        nombreTextField = new JTextField(20);
        apellidosTextField = new JTextField(20);
        fechaNacimientoTextField = new JTextField(20);
        generoTextField = new JTextField(20);
        ciudadTextField = new JTextField(20);
        direccionTextField = new JTextField(20);
        telefonoTextField = new JTextField(20);
        contactoemergenciaTextField = new JTextField(20);
        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 2));
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idTextField);
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreTextField);
        inputPanel.add(new JLabel("Apellidos:"));
        inputPanel.add(apellidosTextField);
        inputPanel.add(new JLabel("Fecha de Nacimiento:"));
        inputPanel.add(fechaNacimientoTextField);
        inputPanel.add(new JLabel("Género:"));
        inputPanel.add(generoTextField);
        inputPanel.add(new JLabel("Ciudad:"));
        inputPanel.add(ciudadTextField);
        inputPanel.add(new JLabel("Dirección:"));
        inputPanel.add(direccionTextField);
        inputPanel.add(new JLabel("Teléfono:"));
        inputPanel.add(telefonoTextField);
        inputPanel.add(new JLabel("Contacto de Emergencia:"));
        inputPanel.add(contactoemergenciaTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);

        panelActualizarPaciente.add(inputPanel, BorderLayout.CENTER);
        panelActualizarPaciente.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panelActualizarPaciente);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarPaciente();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private Connection conexion_base() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/medicare";
        String usuarioBD = "root";
        String contraseña = "123456";
        return DriverManager.getConnection(url, usuarioBD, contraseña);
    }

    private void actualizarPaciente() {
        String id = idTextField.getText().trim();
        String nombre = nombreTextField.getText().trim();
        String apellidos = apellidosTextField.getText().trim();
        String fechaNacimiento = fechaNacimientoTextField.getText().trim();
        String genero = generoTextField.getText().trim();
        String ciudad = ciudadTextField.getText().trim();
        String direccion = direccionTextField.getText().trim();
        String telefono = telefonoTextField.getText().trim();
        String contactoEmergencia = contactoemergenciaTextField.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || fechaNacimiento.isEmpty() || genero.isEmpty() || ciudad.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || contactoEmergencia.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = conexion_base()) {
            String sql = "UPDATE Pacientes SET nombre = ?, apellidos = ?, fecha_nacimiento = ?, genero = ?, ciudad = ?, direccion = ?, telefono = ?, contacto_emergencia = ? WHERE id = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, apellidos);
            pst.setString(3, fechaNacimiento);
            pst.setString(4, genero);
            pst.setString(5, ciudad);
            pst.setString(6, direccion);
            pst.setString(7, telefono);
            pst.setString(8, contactoEmergencia);
            pst.setInt(9, Integer.parseInt(id));

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Paciente actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el paciente a actualizar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }

            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ActualizarPaciente();
    }
}
