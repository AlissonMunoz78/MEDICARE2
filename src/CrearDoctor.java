import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CrearDoctor extends JFrame {
    private JPanel panelcreardoctor;
    private JTextField nombreTextField;
    private JTextField horarioMañanaTextField;
    private JTextField horarioTardeTextField;
    private JButton guardarButton;
    private JButton cancelarButton;
    private JComboBox<String> especialidadComboBox;

    public CrearDoctor(){
        super("Crear Doctor");

        panelcreardoctor = new JPanel();
        panelcreardoctor.setLayout(new GridLayout(5, 2));

        nombreTextField = new JTextField();
        horarioMañanaTextField = new JTextField();
        horarioTardeTextField = new JTextField();
        especialidadComboBox = new JComboBox<>();
        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");

        panelcreardoctor.add(new JLabel("Nombre:"));
        panelcreardoctor.add(nombreTextField);
        panelcreardoctor.add(new JLabel("Especialidad:"));
        panelcreardoctor.add(especialidadComboBox);
        panelcreardoctor.add(new JLabel("Horario Mañana:"));
        panelcreardoctor.add(horarioMañanaTextField);
        panelcreardoctor.add(new JLabel("Horario Tarde:"));
        panelcreardoctor.add(horarioTardeTextField);
        panelcreardoctor.add(guardarButton);
        panelcreardoctor.add(cancelarButton);

        setContentPane(panelcreardoctor);
        setSize(500,400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        cargarEspecialidades();

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDoctor();
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

    private void guardarDoctor() {
        String nombre = nombreTextField.getText().trim();
        String especialidad = (String) especialidadComboBox.getSelectedItem();
        String horarioMañana = horarioMañanaTextField.getText().trim();
        String horarioTarde = horarioTardeTextField.getText().trim();

        if (nombre.isEmpty() || especialidad.isEmpty() || horarioMañana.isEmpty() || horarioTarde.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = conexion_base()) {
            String sql = "INSERT INTO medicos (nombre, especialidad, horario_mañana, horario_tarde) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, especialidad);
            pst.setString(3, horarioMañana);
            pst.setString(4, horarioTarde);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al crear el doctor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEspecialidades() {
        try (Connection connection = conexion_base()) {
            String sql = "SELECT DISTINCT especialidad FROM Medicos";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                especialidadComboBox.addItem(rs.getString("especialidad"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar especialidades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
