import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {
    private JPanel panelogin;
    private JTextField usuario;
    private JTextField password;
    private JComboBox<String> roles;
    private JButton button1;

    public Login() {
        super("Medicare");
        setContentPane(panelogin);
        setSize(700,500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    private Connection conexion_base() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/medicare";
        String usuarioBD =  "root";
        String contraseña = "123456";
        return DriverManager.getConnection(url, usuarioBD, contraseña);
    }

    public void login() {
        try (Connection connection = conexion_base()) {
            String nombreusu = usuario.getText().trim();
            String contr = password.getText().trim();
            String rol = roles.getSelectedItem().toString();

            String sql = "SELECT * FROM Usuarios WHERE username = ? AND password = ? AND rol = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nombreusu);
            pst.setString(2, contr);
            pst.setString(3, rol);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "CREDENCIALES CORRECTAS", "info", JOptionPane.INFORMATION_MESSAGE);
                if (rol.equals("administrador")) {
                    Administrador administrador = new Administrador();
                    administrador.setVisible(true);
                    dispose();
                }else if (rol.equals("medico")) {
                    Medico medico = new Medico();
                    medico.setVisible(true);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, "CREDENCIALES INCORRECTAS", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL REALIZAR LA CONSULTA: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

}
