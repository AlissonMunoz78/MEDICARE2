import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ActualizarDoctor extends JFrame {
    private JPanel panelActualizarPaciente;
    private JTextField idTextField;
    private JTextField nombreTextField;
    private JTextField apellidoTextField;
    private JTextField fechaNacimientoTextField;
    private JTextField edadTextField;
    private JTextField generoTextField;
    private JTextField ciudadTextField;
    private JTextField telefonoTextField;
    private JTextField contactoemergenciaTextField;
    private JButton guardarButton;
    private JButton cancelarButton;

    public ActualizarDoctor() {
        super("Actualizar Doctor");

        // Inicializar el panel manualmente
        panelActualizarPaciente = new JPanel();
        panelActualizarPaciente.setLayout(new BorderLayout());

        idTextField = new JTextField(20);
        nombreTextField = new JTextField(20);
        apellidoTextField = new JTextField(20);
        fechaNacimientoTextField = new JTextField(20);
        edadTextField = new JTextField(20);
        generoTextField = new JTextField(20);
        ciudadTextField = new JTextField(20);
        telefonoTextField = new JTextField(20);
        contactoemergenciaTextField = new JTextField(20);
        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2));
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idTextField);
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreTextField);
        inputPanel.add(new JLabel("Apellido:"));
        inputPanel.add(apellidoTextField);
        inputPanel.add(new JLabel("Fecha de Nacimiento:"));
        inputPanel.add(fechaNacimientoTextField);
        inputPanel.add(new JLabel("Edad:"));
        inputPanel.add(edadTextField);
        inputPanel.add(new JLabel("Genero:"));
        inputPanel.add(generoTextField);
        inputPanel.add(new JLabel("Ciudad:"));
        inputPanel.add(ciudadTextField);
        inputPanel.add(new JLabel("Telefono:"));
        inputPanel.add(telefonoTextField);
        inputPanel.add(new JLabel("Contacto de Emergencia:"));
        inputPanel.add(contactoemergenciaTextField);

        JPanel buttinPanel = new JPanel();
        buttinPanel.add(guardarButton);
        buttinPanel.add(cancelarButton);

        panelActualizarPaciente.add(inputPanel, BorderLayout.CENTER);
        panelActualizarPaciente.add(buttinPanel, BorderLayout.SOUTH);
    }
}