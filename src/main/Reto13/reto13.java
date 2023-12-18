import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.BufferedReader;
import java.nio.file.*;
import java.io.*;
import static java.nio.file.StandardOpenOption.*;


public class reto13{
    public static void main(String[] args){
        iCentro principal = new iCentro();
    }
}

class iCentro extends JFrame implements ActionListener{
    String desc, ct, mu, dt, nf, ci;

    JLabel textoDatos = new JLabel("Ingrese los datos del equipo");
    JLabel textoDescripcion = new JLabel("Descripcion: ");
    JTextField descripcion = new JTextField(30);

    JLabel textoCantidad = new JLabel("Cantidad:");
    JTextField cantidad = new JTextField(10);

    JLabel textoCosto = new JLabel("Costo Unitario (Bs.):");
    JTextField costo = new JTextField(10);

    JLabel textoFecha = new JLabel("Fecha de adquisicion:");
    JLabel textoFechaFormato = new JLabel("dd/mm/aaaa");
    JTextField fecha = new JTextField(10);

    JLabel textoFactura = new JLabel("Nro. de Factura:");
    JTextField factura = new JTextField(20);

    JLabel textoCedula = new JLabel("C.I. del Responsable del equipo:");
    JTextField cedula = new JTextField(15);
    //Botones
    JButton botonDatos = new JButton("Registrar data");
    JButton botonReporte = new JButton("Generar reporte");
    JButton botonSalir = new JButton("Salir"); 

    iCentro(){
        super("Registro y Control de Equipos en Centros de Investigacion");
        setSize(600, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        textoDatos.setBounds(10, 10, 600, 25);
        textoDescripcion.setBounds(20, 50, 100, 25);
        descripcion.setBounds(120, 50, 450, 25);

        textoCantidad.setBounds(20, 90, 100, 25);
        cantidad.setBounds(120, 90, 50, 25);

        textoCosto.setBounds(250, 90, 150, 25);
        costo.setBounds(370, 90, 200, 25);

        textoFecha.setBounds(20, 130, 150, 25);
        textoFechaFormato.setBounds(20, 150, 100, 25);
        fecha.setBounds(150, 140, 100, 25);

        textoFactura.setBounds(300, 140, 100, 25);
        factura.setBounds(400, 140, 150, 25);

        textoCedula.setBounds(20, 200, 200, 25);
        cedula.setBounds(220, 200, 100, 25);

        botonDatos.setBounds(100,300,130,25);
        botonReporte.setBounds(235, 300, 140, 25);
        botonSalir.setBounds(380, 300, 130, 25);

        add(textoDatos);
        add(textoDescripcion);
        add(descripcion);
        add(textoCantidad);
        add(cantidad);
        add(textoCosto);
        add(costo);
        add(textoFecha);
        add(textoFechaFormato);
        add(fecha);
        add(textoFactura);
        add(factura);
        add(textoCedula);
        add(cedula);

        botonDatos.addActionListener(this);
        add(botonDatos);

        botonReporte.addActionListener(this);
        add(botonReporte);

        botonSalir.addActionListener(this);
        add(botonSalir); 


        setVisible(true);
    }

    @Override 
    public void actionPerformed(ActionEvent a){
        JButton boton = (JButton) a.getSource();
        
        if(boton == botonDatos){
            desc = descripcion.getText();
            ct = cantidad.getText();
            mu = costo.getText();
            dt = fecha.getText();
            nf = factura.getText(); 
            ci = cedula.getText();

            try{
                Path filePath = Paths.get("inventario.txt");
                OutputStream output = new BufferedOutputStream(Files.newOutputStream(filePath, APPEND));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

                writer.write(desc + "#" + ct + "#" + mu + "#" + dt + "#" + nf + "#" + ci +";\n");
                writer.close();
                output.close();

                descripcion.setText("");
                cantidad.setText("");
                costo.setText("");
                fecha.setText("");
                factura.setText("");
                cedula.setText("");
            }
            catch(Exception e){
                System.out.println("ERROR: " + e);
            }
        }
        else if(boton == botonReporte){
            setVisible(false);
            iReporte reporte = new iReporte();
        }
        else if(boton == botonSalir){
            dispose();
        }
    }
}

class iReporte extends JFrame implements ActionListener{

    JLabel textoReporte = new JLabel("Tipo reporte:");
    JCheckBox individual = new JCheckBox("Individual");
    JCheckBox general = new JCheckBox("General");

    JLabel textoCedulaReporte = new JLabel("C.I. del Responsable de equipos:");
    JTextField cedulaReporte = new JTextField(15);
    JButton botonTotalizar = new JButton("Totalizar");

    JLabel textoTotal = new JLabel("Totalizacion:");
    JLabel textoEquipos = new JLabel("equipos");
    JLabel textoBolivares = new JLabel("Bs.");

    JButton botonContinuar = new JButton("Continuar");

    JTextArea areaTexto = new JTextArea();
    JScrollPane tabla = new JScrollPane(areaTexto);
    
    iReporte(){
        super("Registro y Control de Equipos en Centros de Investigacion");
        setSize(500, 300);
        //Para ubicar el texto
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        textoReporte.setBounds(20, 10, 200, 25);
        individual.setBounds(120, 10, 100, 25);
        general.setBounds(220, 10, 100, 25);

        botonTotalizar.setBounds(350, 50, 100, 25);
        botonContinuar.setBounds(360, 220, 100, 25);
        
        add(textoReporte);
        individual.addActionListener(this);
        add(individual);
        general.addActionListener(this);
        add(general);
        add(textoCedulaReporte);
        add(cedulaReporte);

        botonTotalizar.addActionListener(new botonesiReporte());
        add(botonTotalizar);
        add(textoTotal);
        add(textoEquipos);
        add(textoBolivares);
        botonContinuar.addActionListener(new botonesiReporte());
        add(botonContinuar);

        add(tabla);
        areaTexto.setEditable(false);

        setVisible(true);
    }

    @Override 
    public void actionPerformed(ActionEvent a){
        JCheckBox box = (JCheckBox) a.getSource();
        int numeroEquipos = 0; 
        float costoTotal =0f;
        if(box.isSelected()){
            if (box == individual) {
                general.setSelected(false);

                textoCedulaReporte.setBounds(20, 50, 200, 25);
                cedulaReporte.setBounds(220, 50, 110, 25);
                botonTotalizar.setBounds(350, 50, 100, 25);
                botonContinuar.setBounds(360, 220, 100, 25);

                areaTexto.setText("");
                tabla.setBounds(10, 80, 0, 0);
                textoTotal.setBounds(20, 100, 200, 25);
                textoEquipos.setBounds(20, 120, 200, 25);
                textoBolivares.setBounds(20, 140, 200, 25);

                textoEquipos.setText(" Equipos");
                textoBolivares.setText(" Bs.");
            }
            else if (box == general) {
                individual.setSelected(false);

                textoCedulaReporte.setBounds(20, 50, 0, 0);
                cedulaReporte.setBounds(220, 50, 0, 0);
                botonTotalizar.setBounds(350, 50, 0, 0);

                textoTotal.setBounds(20, 180, 200, 25);
                textoEquipos.setBounds(20, 200, 200, 25);
                textoBolivares.setBounds(20, 220, 200, 25);

                areaTexto.setText("C.I. Responsable \t Cantidad equipos \t Monto Total (Bs.)\t\n"); 
                tabla.setBounds(10, 60, 460, 120);

                LinkedList<Profesor> profesores = new LinkedList<Profesor>();
                String lectura;
                boolean enLista = false;

                try{
                    Path filePath = Paths.get("inventario.txt");
                    InputStream input = new BufferedInputStream(Files.newInputStream(filePath));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    lectura = reader.readLine();                    
                    while(lectura != null){
                        String[] lectureArray = lectura.split("#");
                        String[] cedulaArray = lectureArray[5].split(";");
                        for(int i = 0; i < profesores.size(); i++){
                            if(cedulaArray[0].equals(profesores.get(i).cedula)){
                                enLista = true;
                                profesores.get(i).cantidadEquipos += Integer.parseInt(lectureArray[1]);
                                profesores.get(i).costoTotal += Float.parseFloat(lectureArray[2]) * Integer.parseInt(lectureArray[1]);
                            }
                        }
                        
                        lectura = reader.readLine();
                        if(enLista){
                            System.out.println("HOLA");
                            enLista = false;
                            continue;
                        }
                        else{
                            profesores.add(new Profesor(cedulaArray[0], Integer.parseInt(lectureArray[1]), Integer.parseInt(lectureArray[1]) * Float.parseFloat(lectureArray[2])));
                        }
                    }

                    for(int i = 0; i < profesores.size(); i++){
                        areaTexto.append(profesores.get(i).cedula + "\t\t" + profesores.get(i).cantidadEquipos + "\t\t" + profesores.get(i).costoTotal + "\n");
                    }

                    for(int i = 0; i < profesores.size(); i++){
                        numeroEquipos += profesores.get(i).cantidadEquipos;
                        costoTotal += profesores.get(i).costoTotal;
                    }
                    textoEquipos.setText(numeroEquipos + " equipos");
                    textoBolivares.setText(costoTotal + " Bs.");
                    input.close();
                    reader.close();
                }catch(Exception e){
                    System.out.println("ERROR:" + e);
                }
            }

        }
    }
    class botonesiReporte implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent a){
            JButton boton = (JButton) a.getSource();
            int equipos = 0;
            float dinero = 0f;
            if(boton == botonTotalizar){
                String lecture;
                try{
                    Path filePath = Paths.get("inventario.txt");
                    InputStream input = new BufferedInputStream(Files.newInputStream(filePath));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    lecture = reader.readLine();                    
                    while(lecture != null){
                        String[] lectureArray = lecture.split("#");
                        String[] cedulaArray = lectureArray[5].split(";");
                        if(cedulaReporte.getText().equals(cedulaArray[0])){
                            equipos += Integer.parseInt(lectureArray[1]);
                            dinero += Float.parseFloat(lectureArray[2]) * Integer.parseInt(lectureArray[1]);
                        }
                        lecture = reader.readLine();
                    }
                    textoEquipos.setText(equipos + " equipos");
                    textoBolivares.setText(dinero + " Bs.");
                    

                    input.close();
                    reader.close();
                }catch(Exception e){
                    System.out.println("ERROR:" + e);
                }
            }
            else if(boton == botonContinuar){
                setVisible(false);
                iCentro principal = new iCentro();
            }        
        }
   }


}

class Profesor{
    public String cedula;
    public int cantidadEquipos = 0;
    public float costoTotal = 0;
    public boolean enLista = false; 

    Profesor(String cedulaP, int cantidad, float costo){
        cedula = cedulaP;
        cantidadEquipos += cantidad;
        costoTotal += costo;
        enLista = true;
    }
}




