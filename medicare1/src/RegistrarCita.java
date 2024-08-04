import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrarCita extends JFrame {
    private JPanel panelcita;
    private JTextField nombreBuscar;
    private JButton buscarbtn;
    private JTextField nombreDoc; // Nombre del doctor
    private JTextField fechacita; // Fecha de cita
    private JTextField HoraCita; // Hora de cita
    private JComboBox<String> comboBoxEspecialidad; // Especialidad
    private JButton guardarCitaButton;
    private JButton cancelarButton;
    private JLabel idpaciente;
    private JButton mostrarDoctoresButton; // Botón para mostrar doctores

    // Constructor de la clase
    public RegistrarCita() {
        super("Registrar Cita");
        setContentPane(panelcita);
        setSize(500, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Cargar especialidades en el comboBox
        cargarEspecialidades();

        // Acción para el botón de buscar
        buscarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPaciente();
            }
        });

        // Acción para el botón de guardar cita
        guardarCitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCita();
            }
        });

        // Acción para el botón de cancelar
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Acción para el botón de mostrar doctores
        mostrarDoctoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDoctores();
            }
        });
    }

    // Método para buscar paciente
    private void buscarPaciente() {
        String nombre = nombreBuscar.getText();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexion_base();
            String sql = "SELECT id FROM Pacientes WHERE nombre = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            rs = stmt.executeQuery();

            if (rs.next()) {
                idpaciente.setText(String.valueOf(rs.getInt("id")));
            } else {
                JOptionPane.showMessageDialog(this, "Paciente no encontrado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar el paciente: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }

    // Método para guardar la cita
    private void guardarCita() {
        String fecha = fechacita.getText();
        String hora = HoraCita.getText();
        String detalles = nombreDoc.getText();
        String especialidad = (String) comboBoxEspecialidad.getSelectedItem();
        String pacienteId = idpaciente.getText();

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = conexion_base();
            String sql = "INSERT INTO Citas (paciente_id, medico_id, fecha, hora, detalles, especialidad) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(pacienteId));
            stmt.setInt(2, 1); // Reemplaza esto con el ID del médico adecuado
            stmt.setDate(3, Date.valueOf(fecha));
            stmt.setTime(4, Time.valueOf(hora));
            stmt.setString(5, detalles);
            stmt.setString(6, especialidad);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cita guardada exitosamente");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la cita: " + ex.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }

    // Método para conectar a la base de datos
    private Connection conexion_base() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/medicare";
        String usuarioBD = "root";
        String contraseña = "123456";
        return DriverManager.getConnection(url, usuarioBD, contraseña);
    }

    // Método para cargar especialidades en el comboBox
    private void cargarEspecialidades() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexion_base();
            String sql = "SELECT nombre FROM Especialidades";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                comboBoxEspecialidad.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las especialidades: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }

    // Método para mostrar doctores según la especialidad seleccionada
    private void mostrarDoctores() {
        String especialidad = (String) comboBoxEspecialidad.getSelectedItem();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexion_base();
            String sql = "SELECT nombre, horario FROM Doctores WHERE especialidad = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, especialidad);
            rs = stmt.executeQuery();

            StringBuilder doctores = new StringBuilder();
            while (rs.next()) {
                doctores.append("Doctor: ").append(rs.getString("nombre"))
                        .append(", Horario: ").append(rs.getString("horario")).append("\n");
            }
            JOptionPane.showMessageDialog(this, doctores.toString(), "Doctores Disponibles", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al mostrar los doctores: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new RegistrarCita();
    }
}
