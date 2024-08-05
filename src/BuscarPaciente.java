import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BuscarPaciente extends JFrame {

    private JPanel panelBuscarPaciente;
    private JTextField nombreTextField;
    private JButton buscarButton;
    private JTextArea resultadoTextArea;
    private JButton cancelarButton;

    public BuscarPaciente() {
        super("Buscar Paciente");

        panelBuscarPaciente = new JPanel();
        panelBuscarPaciente.setLayout(new BorderLayout());

        nombreTextField = new JTextField(20);
        buscarButton = new JButton("Buscar");
        resultadoTextArea = new JTextArea(10, 40);
        resultadoTextArea.setEditable(false);
        cancelarButton = new JButton("Cancelar");

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreTextField);
        inputPanel.add(buscarButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelarButton);

        panelBuscarPaciente.add(inputPanel, BorderLayout.NORTH);
        panelBuscarPaciente.add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);
        panelBuscarPaciente.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panelBuscarPaciente);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPaciente();
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

    private void buscarPaciente() {
        String nombre = nombreTextField.getText();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el nombre del paciente a buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = conexionBase()) {
            String sql = "SELECT * FROM Pacientes WHERE nombre LIKE ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, "%" + nombre + "%");
            ResultSet rs = pst.executeQuery();

            StringBuilder resultado = new StringBuilder();
            while (rs.next()) {
                resultado.append("ID: ").append(rs.getInt("id")).append("\n")
                        .append("Nombre: ").append(rs.getString("nombre")).append("\n")
                        .append("Apellido: ").append(rs.getString("apellido")).append("\n")
                        .append("Lugar de Nacimiento: ").append(rs.getString("lugar_nacimiento")).append("\n")
                        .append("Edad: ").append(rs.getInt("edad")).append("\n")
                        .append("Género: ").append(rs.getString("genero")).append("\n")
                        .append("Teléfono: ").append(rs.getString("telefono")).append("\n\n");
            }

            if (resultado.length() == 0) {
                resultado.append("No se encontraron pacientes con el nombre: ").append(nombre);
            }

            resultadoTextArea.setText(resultado.toString());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new BuscarPaciente();
    }
}
