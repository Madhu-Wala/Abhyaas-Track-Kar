import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class Dashboard extends JFrame {
    Dashboard(String username){
        Font f = new Font("Arial Rounded MT Bold", Font.BOLD, 40);
        Font f2 = new Font("Candara", Font.PLAIN, 22);
        Font tableFont = new Font("Candara", Font.PLAIN, 18);

        // Main scrollable panel
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                float centerX = width;  // top-right corner x
                float centerY = 0;      // top-right corner y
                float radius = (float) Point2D.distance(centerX, centerY, 0, height);

                float[] dist = {0.0f,1.0f};
                Color[] colors = {
                        new Color(255, 255, 230),
                        new Color(255, 214, 214)

                };

                RadialGradientPaint rgp = new RadialGradientPaint(
                        new Point2D.Float(centerX, centerY),
                        radius,
                        dist,
                        colors
                );

                g2d.setPaint(rgp);
                g2d.fillRect(0, 0, width, height);
            }



        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800, 1100)); // Height exceeds JFrame for scroll

        JLabel l1 = new JLabel("Abhyas TRACK-kar", JLabel.CENTER);
        JLabel l2 = new JLabel("Dashboard", JLabel.CENTER);
        JLabel l3 = new JLabel(username);
        JButton logout = new JButton("Log out");
//find and write stream
        String stream="";
        String url="jdbc:oracle:thin:@localhost:1521:orcl";
        try(Connection con= DriverManager.getConnection(url,"db_username","db_password")){
            String sql="SELECT stream FROM users_of_abhyas WHERE uname= ?";
            try(PreparedStatement pst= con.prepareStatement(sql)){
                pst.setString(1, username);
                ResultSet rs=pst.executeQuery();
                if(rs.next()){
                    stream=rs.getString("stream");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        JLabel l4 = new JLabel("Stream: "+stream);



        String[] columnNames = {"My Subjects", "Total Sessions", "Total Time Spent "};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(tableFont);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(5, 100, 100));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(224, 224, 224));
        JScrollPane scrollPane = new JScrollPane(table);


        JButton add = new JButton("Add Subject"){};
        JButton delete = new JButton("Delete Subject");
        JButton startSession = new JButton("Start a Session");

        // today's Report
        JLabel todayReport = new JLabel("Today's Report");
        todayReport.setFont(f2);
        JButton viewWeekly = new JButton("View Weekly Report");
        viewWeekly.setFont(f2);

        String[] todayCols = {"Subject", "Time Spent (hours) "};
        DefaultTableModel todayModel = new DefaultTableModel(todayCols, 0);
        JTable todayTable = new JTable(todayModel);
        todayTable.setFont(tableFont);
        todayTable.setRowHeight(30);
        todayTable.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 18));
        todayTable.getTableHeader().setBackground(new Color(0, 102, 204));
        todayTable.getTableHeader().setForeground(Color.WHITE);
        todayTable.setGridColor(new Color(224, 224, 224));
        JScrollPane todayScroll = new JScrollPane(todayTable);

        l1.setFont(f);
        l2.setFont(f2);
        l3.setFont(f2);
        logout.setFont(f2);
        l4.setFont(f2);
        add.setFont(f2);
        delete.setFont(f2);
        startSession.setFont(f2);

        logout.setBackground(new Color(246, 43, 43));
        logout.setForeground(Color.white);

        startSession.setBackground(new Color(44, 178, 0));
        startSession.setForeground(Color.white);


        l1.setBounds(100, 50, 580, 50);
        l2.setBounds(250, 100, 300, 50);
        l3.setBounds(100, 150, 300, 50);
        logout.setBounds(600, 150, 150, 40);
        l4.setBounds(50, 200, 400, 50);
        scrollPane.setBounds(50, 270, 680, 200);
        add.setBounds(60, 500, 150, 40);
        delete.setBounds(250, 500, 200, 40);
        startSession.setBounds(60, 550, 200, 40);
        todayReport.setBounds(50, 620, 200, 30);
        viewWeekly.setBounds(500, 615, 250, 40);
        todayScroll.setBounds(50, 670, 680, 200);

        ImageIcon userIcon = new ImageIcon("imgpath");
        Image scaledImage = userIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel userLogo = new JLabel(new ImageIcon(scaledImage));
        userLogo.setBounds(50, 150, 48, 48); // Adjust as needed
        panel.add(userLogo);

        // Add all to panel
        panel.add(l1);
        panel.add(l2);
        panel.add(l3);
        panel.add(logout);
        panel.add(l4);
        panel.add(scrollPane);
        panel.add(add);
        panel.add(delete);
        panel.add(startSession);
        panel.add(todayReport);
        panel.add(viewWeekly);
        panel.add(todayScroll);

        // Scroll pane for full window
        JScrollPane mainScroll = new JScrollPane(panel);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        setContentPane(mainScroll);

        int userId = 0; // Replace with actual user ID if you can get it dynamically
        try (Connection con=DriverManager.getConnection(url,"db_username","db_password")){

            //find user id
            String sql="select user_id from users_of_abhyas where uname= ?";
            try (PreparedStatement pst=con.prepareStatement(sql)){
                pst.setString(1, username);
                ResultSet rs= pst.executeQuery();
                if(rs.next()){
                    userId=rs.getInt("user_id");
                }
            }

            //filling report table
            String sessionSql = "SELECT s.subname,sess.start_time,sess.end_time FROM subjects s LEFT JOIN sessions sess ON s.sub_id = sess.sub_id where s.user_id=? ";
            try (PreparedStatement pst = con.prepareStatement(sessionSql)) {

                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();

                Map<String, Duration> subDuration=new HashMap<>();
                Map<String, Integer> subSessCount=new HashMap<>();

                while (rs.next()) {
                    String subject = rs.getString("subname");
                    Timestamp startTime=rs.getTimestamp("start_time");
                    Timestamp endTime=rs.getTimestamp("end_time");

                    if(startTime!=null && endTime!=null){
                        LocalDateTime startLD =startTime.toLocalDateTime();
                        LocalDateTime endLD=endTime.toLocalDateTime();
                        Duration sessDura=Duration.between(startLD, endLD);

                        subDuration.put(subject, subDuration.getOrDefault(subject, Duration.ZERO).plus(sessDura));
                        subSessCount.put(subject, subSessCount.getOrDefault(subject, 0)+1);

                    } else {
                        // Subject with no sessions, initialize if not already present
                        subDuration.putIfAbsent(subject, Duration.ZERO);
                        subSessCount.putIfAbsent(subject, 0);
                    }

                }
                for(String subject:subDuration.keySet()){
                    Duration dur=subDuration.get(subject);  
                    int totalsessions=subSessCount.getOrDefault(subject,0);  

                    long totalSeconds = dur.getSeconds();
                    long totalHr = totalSeconds / 3600;
                    long totalMin = (totalSeconds % 3600) / 60;
                    long remainingSec = totalSeconds % 60;

                    String timeSpent = String.format("%02d:%02d:%02d hours", totalHr, totalMin, remainingSec);

                    tableModel.addRow(new Object[]{subject,totalsessions, timeSpent});
                }

            }


            //today's report
            String todaySql ="SELECT s.subname,sess.start_time,sess.end_time FROM subjects s JOIN sessions sess ON s.sub_id = sess.sub_id where s.user_id=? AND TRUNC(sess.start_time) = TRUNC(SYSDATE)";

            try (PreparedStatement pst = con.prepareStatement(todaySql)) {
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();

                Map<String, Duration> subDuration=new HashMap<>();
                Map<String, Integer> subSessCount=new HashMap<>();

                while (rs.next()) {
                   
                    String subject = rs.getString("subname");
                    Timestamp startTime=rs.getTimestamp("start_time");
                    Timestamp endTime=rs.getTimestamp("end_time");

                   
                    if(startTime!=null && endTime!=null){
                        //translate sql to java
                        LocalDateTime startLD =startTime.toLocalDateTime();
                        LocalDateTime endLD=endTime.toLocalDateTime();
                      
                        Duration sessDura=Duration.between(startLD, endLD);

                        subDuration.put(subject, subDuration.getOrDefault(subject, Duration.ZERO).plus(sessDura));
                        subSessCount.put(subject, subSessCount.getOrDefault(subject, 0)+1);

                    }

                }
                for(String subject:subDuration.keySet()){
                   
                    Duration dur=subDuration.get(subject);  
                    int totalsessions=subSessCount.getOrDefault(subject,0); 


                    long totalSeconds = dur.getSeconds();
                    long totalHr = totalSeconds / 3600;
                    long totalMin = (totalSeconds % 3600) / 60;
                    long remainingSec = totalSeconds % 60;

                    String timeSpent = String.format("%02d:%02d:%02d", totalHr, totalMin, remainingSec);

                    todayModel.addRow(new Object[]{subject, timeSpent});
                }

            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }



        logout.addActionListener(
                a->{
                    int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?","Confirm logout",JOptionPane.YES_NO_OPTION);
                    if(choice==JOptionPane.YES_OPTION){
                        new First();
                        dispose();

                    }
                }
        );

        add.addActionListener(
                a->{
                    new AddSub(username);
                    dispose();
                }
        );

        delete.addActionListener(
                a->{
                    new DeleteSub(username);
                    dispose();
                }
        );

        startSession.addActionListener(
                a->{
                    new Session(username);
                    dispose();
                }
        );

        viewWeekly.addActionListener(
                a->{
                    new Weekly(username);
                    dispose();
                }
        );

        // JFrame settings
        setVisible(true);
        setSize(800, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Dashboard");
    }

    public static void main(String[] args) {
        new Dashboard("sunil02");
    }
}

