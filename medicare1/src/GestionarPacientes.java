import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionarPacientes extends JFrame {
    private JPanel gpacientes;
    private JButton bpaciente;
    private JButton cpaciente;
    private JButton epaciente;
    private JButton salir;
    private JButton apaciente;

    public GestionarPacientes() {
        super("Gestionar Pacientes");
        setContentPane(gpacientes);
        setSize(500,400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        cpaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearPaciente();
            }
        });
        bpaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuscarPaciente();
            }
        });
        apaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ActualizarPaciente();
            }
        });
        epaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EliminarPaciente();
            }
        });
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Administrador();
            }
        });
    }
}
