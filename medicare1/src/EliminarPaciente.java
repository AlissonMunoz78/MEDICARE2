import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EliminarPaciente extends JFrame {
    private JPanel panelEliminarPaciente;
    private JTextField nombreTextField;
    private JTextField apellidoTextField;
    private JTextField lugar_nacimientoTextField;
    private JTextField edadTextField;
    private JTextField generoTextField;
    private JTextField telefonoTextField;
    private JButton buscarButton;
    private JButton eliminarButton;
    private JButton cancelarButton;
    private JTextField nombreBuscarTextField;

    public EliminarPaciente() {
        super("Eliminar Paciente");

        // Inicializar el panel manualmente
        panelEliminarPaciente = new JPanel();
        panelEliminarPaciente.setLayout(new BorderLayout());

        nombreBuscarTextField = new JTextField(20);
        nombreTextField = new JTextField(20);
        apellidoTextField = new JTextField(20);
        lugar_nacimientoTextField = new JTextField(20);
        edadTextField = new JTextField(20);
        generoTextField = new JTextField(20);
        telefonoTextField = new JTextField(20);
        buscarButton = new JButton("Buscar");
        eliminarButton = new JButton("Eliminar");
        cancelarButton = new JButton("Cancelar");


        JPanel buscarPanel = new JPanel();
        buscarPanel.add(new JLabel("Nombre a buscar:"));
        buscarPanel.add(nombreBuscarTextField);
        buscarPanel.add(buscarButton);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 2));
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreTextField);
        inputPanel.add(new JLabel("Apellido:"));
        inputPanel.add(apellidoTextField);
        inputPanel.add(new JLabel("Lugar de Nacimiento:"));
        inputPanel.add(lugar_nacimientoTextField);
        inputPanel.add(new JLabel("Edad:"));
        inputPanel.add(edadTextField);
        inputPanel.add(new JLabel("Género:"));
        inputPanel.add(generoTextField);
        inputPanel.add(new JLabel("Teléfono:"));
        inputPanel.add(telefonoTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buscarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(cancelarButton);

        panelEliminarPaciente.add(buscarPanel, BorderLayout.NORTH);
        panelEliminarPaciente.add(inputPanel, BorderLayout.CENTER);
        panelEliminarPaciente.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panelEliminarPaciente);
        setSize(500, 400);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPaciente();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPaciente();
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

    private void buscarPaciente() {
        String idBuscar = nombreBuscarTextField.getText().trim();

        if (idBuscar.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el nombre del paciente a buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = conexion_base()) {
            String sql = "SELECT * FROM Pacientes WHERE nombre LIKE ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, idBuscar);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nombreTextField.setText(rs.getString("nombre"));
                apellidoTextField.setText(rs.getString("apellido"));
                lugar_nacimientoTextField.setText(rs.getString("lugar_nacimiento"));
                edadTextField.setText(rs.getString("edad"));
                generoTextField.setText(rs.getString("genero"));
                telefonoTextField.setText(rs.getString("telefono"));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron pacientes con el ID: " + idBuscar, "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPaciente() {
        String id = nombreTextField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, busque y seleccione un paciente antes de eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar al paciente con ID " + id + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = conexion_base()) {
                String sql = "DELETE FROM Pacientes WHERE nombre = ?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setString(1, id);
                int rowsDeleted = pst.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Paciente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el paciente a eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        nombreTextField.setText("");
        apellidoTextField.setText("");
        lugar_nacimientoTextField.setText("");
        edadTextField.setText("");
        generoTextField.setText("");
        telefonoTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EliminarPaciente();
            }
        });
    }
}
