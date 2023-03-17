import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
public class DisplayData extends JFrame implements ActionListener {
    JFrame frame1;
    JLabel l0, l1, l2;
    JTextField t1;
    JComboBox c1;
    JButton b1;
    Connection con;
    ResultSet rs, rs1;
    Statement st, st1;
    String ids;
    static JTable table;
    String[] columnNames = {"Transaction_id","Account_id","Transaction_type", "Time_stamp","Amount_transacted","Transaction_date"};
    DisplayData() {
        l0 = new JLabel("Fetching Branch Information");
        l0.setForeground(Color.red);
        l0.setFont(new Font("Times New Roman", Font.BOLD, 20));
        l1 = new JLabel("Select Branch_id");
        l2=new JLabel("Enter date (yyyy-mm-dd):");
        t1=new JTextField(20);
        b1 = new JButton("Submit");
        l0.setBounds(100, 50, 400, 40);
        l1.setBounds(75, 110, 150, 20);
        l2.setBounds(75,150,150,20);
        t1.setBounds(250, 150, 150, 20);
        b1.setBounds(150, 200, 150, 20);
        b1.addActionListener(this);
        setTitle("Fetching info From database");
        setLayout(null);
        setVisible(true);
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(l0);
        add(l1);
        add(l2);
        add(t1);
        add(b1);
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila","root","vinay");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select Branch_id from branch");
            Vector v = new Vector();
            while (rs.next()) 
            {
                ids = rs.getString(1);
                v.add(ids);
            }
            c1 = new JComboBox(v);
            c1.setBounds(180, 110, 150, 20);
            add(c1);
            st.close();
            rs.close();
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }
    public void actionPerformed(ActionEvent ae) 
    {
        if (ae.getSource() == b1) 
        {
            showTableData();
        }
    }
    public void showTableData() 
    {
        frame1 = new JFrame("Database Search Result");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLayout(new BorderLayout());
        frame1.setBackground(Color.GRAY);
        String f = (String) c1.getSelectedItem();
        String g=(String)t1.getText();
        //TableModel tm = new TableModel();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        //DefaultTableModel model = new DefaultTableModel(tm.getData1(), tm.getColumnNames());
        //table = new JTable(model);
        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //making table resizable
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        //enabling table to scroll
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila","root","vinay");
            //Fetching information from db to retrieve the details of transaction for selected branch_id and given date through SQL query
            PreparedStatement pst = con.prepareStatement("select * from transaction where account_id in(select account_id from account where branch_id="+f+") and Transaction_date='"+g+"'");
            ResultSet rs = pst.executeQuery();
            int i = 0;
            System.out.println("Account transactions belonging to the branch_id: "+f+" on "+g);
            System.out.println("Transaction_id\tAccount_id\tTransaction_type\tTime_Stamp\t\tAmount_transacted\tTransaction_date");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
            while(rs.next()) 
            {
                String a = rs.getString("Transaction_id");
                //getting Transaction_id
                String b = rs.getString("Account_id");
                //getting Account_id
                String c = rs.getString("Transaction_type");
                //getting type of transaction
                String d = rs.getString("Time of transaction");
                //getting time at which transaction happened
                String e = rs.getString("Amount_transacted");
                //getting amount details
                String x = rs.getString("Transaction_date");
                //indicates the date of transaction
                System.out.println(a+"\t\t"+b+"\t\t"+c+"\t\t\t"+d+"\t\t"+e+"\t\t\t"+x);
                //Printing rows in the table
                model.addRow(new Object[]{a,b,c,d,e,x});
                //adding records into the table
                i++;
                //counting the no. of records in the table
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
            if (i==0) 
            {
                JOptionPane.showMessageDialog(null, "No record Found", "Error", JOptionPane.ERROR_MESSAGE);
                //showing a message "No record found" in a new pop-up window
                System.out.println("No record found");
            }
            else 
            {
                System.out.println(i + " record(s) Found");
            }
            //printing no. of records in the terminal
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            //printing exceptions in a new pop up window
        }
        frame1.add(scroll);
        //enables the user to scroll top to down 
        frame1.setVisible(true);
        //enabling the frame visible
        frame1.setSize(400, 300);
        //set frame size
    }
    public static void main(String args[]) throws Exception 
    {
        new DisplayData();
    }
}
