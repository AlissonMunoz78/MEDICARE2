import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class BuscarDoctor extends JFrame{
    private JPanel panelBuscarDoctor;
    private JTextField nombreTextField;
    private JButton buscarButton;
    private JTextArea resultadoTextArea;
    private JButton cancelarButton;

    public BuscarDoctor() {
        super("Buscar Doctor");

        panelBuscarDoctor = new JPanel();
        panelBuscarDoctor.setLayout(new BorderLayout());

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

        panelBuscarDoctor.add(inputPanel, BorderLayout.NORTH);
        panelBuscarDoctor.add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);
        panelBuscarDoctor.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panelBuscarDoctor);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarDoctor();
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

    private void buscarDoctor() {
        String nombre = nombreTextField.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el nombre del doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = conexion_base()) {
            String sql = "SELECT * FROM Medicos WHERE nombre LIKE ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, "%" + nombre + "%");
            ResultSet rs = pst.executeQuery();

            StringBuilder resultados = new StringBuilder();
            while (rs.next()) {
                resultados.append("ID: ").append(rs.getInt("id")).append("\n")
                        .append("Nombre: ").append(rs.getString("nombre")).append("\n")
                        .append("Especialidad: ").append(rs.getString("especialidad")).append("\n")
                        .append("Horario Mañana: ").append(rs.getString("horario_mañana")).append("\n")
                        .append("Horario Tarde: ").append(rs.getString("horario_tarde")).append("\n\n");
            }

            if (resultados.length() == 0) {
                resultados.append("No se encontraron doctores con el nombre: ").append(nombre);
            }

            resultadoTextArea.setText(resultados.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el doctor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
