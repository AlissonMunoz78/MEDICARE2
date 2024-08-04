import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class EliminarDoctor extends JFrame {
    private JPanel panelEliminarDoctor;
    private JTextField nombreBuscarTextField;
    private JTextField nombreTextField;
    private JTextField horarioMañanaTextField;
    private JTextField horarioTardeTextField;
    private JButton buscarButton;
    private JButton eliminarButton;
    private JButton cancelarButton;
    private JComboBox<String> especialidadComboBox;

    public EliminarDoctor() {
        super("Eliminar Doctor");

        // Inicializar el panel manualmente
        panelEliminarDoctor = new JPanel();
        panelEliminarDoctor.setLayout(new BorderLayout());

        nombreBuscarTextField = new JTextField(20);
        nombreTextField = new JTextField(20);
        horarioMañanaTextField = new JTextField(20);
        horarioTardeTextField = new JTextField(20);
        buscarButton = new JButton("Buscar");
        eliminarButton = new JButton("Eliminar");
        cancelarButton = new JButton("Cancelar");
        especialidadComboBox = new JComboBox<>();

        cargarEspecialidades();

        JPanel buscarPanel = new JPanel();
        buscarPanel.add(new JLabel("Nombre a buscar:"));
        buscarPanel.add(nombreBuscarTextField);
        buscarPanel.add(buscarButton);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreTextField);
        inputPanel.add(new JLabel("Especialidad:"));
        inputPanel.add(especialidadComboBox);
        inputPanel.add(new JLabel("Horario Mañana:"));
        inputPanel.add(horarioMañanaTextField);
        inputPanel.add(new JLabel("Horario Tarde:"));
        inputPanel.add(horarioTardeTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buscarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(cancelarButton);

        panelEliminarDoctor.add(buscarPanel, BorderLayout.NORTH);
        panelEliminarDoctor.add(inputPanel, BorderLayout.CENTER);
        panelEliminarDoctor.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panelEliminarDoctor);
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

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarDoctor();
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
        String nombreBuscar = nombreBuscarTextField.getText().trim();

        if (nombreBuscar.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el nombre del doctor a buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = conexion_base()) {
            String sql = "SELECT * FROM Medicos WHERE nombre LIKE ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, "%" + nombreBuscar + "%");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nombreTextField.setText(rs.getString("nombre"));
                especialidadComboBox.setSelectedItem(rs.getString("especialidad"));
                horarioMañanaTextField.setText(rs.getString("horario_mañana"));
                horarioTardeTextField.setText(rs.getString("horario_tarde"));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron doctores con el nombre: " + nombreBuscar, "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el doctor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarDoctor() {
        String nombre = nombreTextField.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, busque y seleccione un doctor antes de eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar al doctor " + nombre + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = conexion_base()) {
                String sql = "DELETE FROM Medicos WHERE nombre = ?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setString(1, nombre);
                int rowsDeleted = pst.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Doctor eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el doctor a eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el doctor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        nombreTextField.setText("");
        especialidadComboBox.setSelectedIndex(-1);
        horarioMañanaTextField.setText("");
        horarioTardeTextField.setText("");
        nombreBuscarTextField.setText("");
    }

    private void cargarEspecialidades() {
        try (Connection connection = conexion_base()) {
            String sql = "SELECT DISTINCT especialidad FROM Medicos";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                especialidadComboBox.addItem(rs.getString("especialidad"));
            }
            especialidadComboBox.setSelectedIndex(-1); // No seleccionar ninguna especialidad por defecto
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar especialidades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}


