import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;


class Session extends JFrame {
    private Timer timer;
    private int secondsPassed = 0;
    private JLabel timerLabel;

    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;


    Session(String username){
        Font f = new Font("Arial Rounded MT Bold", Font.BOLD, 40);
        Font f2 = new Font("Candara", Font.PLAIN, 22);

        JLabel l1 = new JLabel("Abhyas TRACK-kar", JLabel.CENTER);
        JLabel l2 = new JLabel("Session", JLabel.CENTER);
        JLabel l3 = new JLabel(username);
        JLabel l4=new JLabel("Enter Subject:");
        JTextField t1=new JTextField(10);
        JButton start=new JButton("Start the Session");
        JButton end=new JButton("End the Session");
        JButton back=new JButton("Back");

        timerLabel = new JLabel("00:00:00", JLabel.CENTER);
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 28));
        timerLabel.setBounds(300, 180, 200, 50);



        l1.setFont(f);
        l2.setFont(f2);
        l3.setFont(f2);
        l4.setFont(f2);
        t1.setFont(f2);
        start.setFont(f2);
        end.setFont(f2);
        back.setFont(f2);

        start.setBackground(new Color(60, 228, 39));
        start.setForeground(Color.white);
        end.setBackground(new Color(239, 1, 1));
        end.setForeground(Color.white);
        back.setBackground(new Color(9, 231, 239));

        l1.setBounds(100, 50, 580, 50);
        l2.setBounds(250, 100, 300, 50);
        l3.setBounds(100, 150, 300, 50);
        l4.setBounds(200, 250, 300, 50);
        t1.setBounds(350, 250, 250, 40);
        start.setBounds(150, 350, 200, 40);
        end.setBounds(400, 350, 200, 40);
        back.setBounds(325, 400, 100, 40);

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                // Vertical gradient from top to bottom
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 255, 255),
                        0, height, new Color(198, 229, 255)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(null); 

        setContentPane(gradientPanel);  

        Container c=getContentPane();
        c.setLayout(null);

        ImageIcon userIcon = new ImageIcon("imgpath");
        Image scaledImage = userIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel userLogo = new JLabel(new ImageIcon(scaledImage));
        userLogo.setBounds(50, 150, 48, 48); // Adjust as needed
        c.add(userLogo);
        c.add(l1);
        c.add(l2);
        c.add(l3);
        c.add(l4);
        c.add(t1);
        c.add(start);
        c.add(end);
        c.add(back);
        c.add(timerLabel);

        start.addActionListener(
                a->{
                    //--------------DB part--------------

                    //check if empty
                    String subjectName = t1.getText().trim();
                    if (subjectName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter a subject name.");
                        return;
                    }

                    String url="jdbc:oracle:thin:@localhost:1521:orcl";
                    int userId=0;
                    int subId=0;
                    try(Connection con= DriverManager.getConnection(url,"db_username","db_password")) {
                        String sql = "select user_id from users_of_abhyas where uname= ? ";
                        try (PreparedStatement pst = con.prepareStatement(sql)) {
                            pst.setString(1, username);
                            ResultSet rs = pst.executeQuery();
                            if (rs.next()) {
                                userId = rs.getInt("user_id");
                            }
                        }
                        //check if sub existst and find subid
                        String subsql = "select sub_id from subjects where user_id= ? and LOWER(subname) = LOWER(?)";
                        try (PreparedStatement pst = con.prepareStatement(subsql)) {
                            pst.setInt(1, userId);
                            pst.setString(2, subjectName);
                            ResultSet rs = pst.executeQuery();
                            if (!rs.next()) {
                                JOptionPane.showMessageDialog(null, "Subject Not Found");
                                return;
                            }subId = rs.getInt("sub_id");
                        }
                        //check if session is already started?
                        String checkSess="SELECT * FROM sessions WHERE user_id = ? AND sub_id = ? AND end_time IS NULL";
                        try(PreparedStatement pst=con.prepareStatement(checkSess)){
                            pst.setInt(1, userId);
                            pst.setInt(2, subId);
                            ResultSet rs = pst.executeQuery();
                            if (rs.next()) {
                                JOptionPane.showMessageDialog(null, "A session is already running for this subject.");
                                return;
                            }
                        }

                        //userid and subid mil gaya
                        String sessionStart = "INSERT INTO sessions (user_id, sub_id, start_time) VALUES (?, ?, ?)";
                        try (PreparedStatement pst = con.prepareStatement(sessionStart)) {
                            pst.setInt(1, userId);
                            pst.setInt(2, subId);
                            sessionStartTime = LocalDateTime.now().withNano(0);
                            pst.setTimestamp(3, Timestamp.valueOf(sessionStartTime));
                            pst.executeUpdate();
                            t1.setEnabled(false);

                        }
                        //ui timer part
                        secondsPassed=0;//har bar reset
                        timerLabel.setText("00:00:00");

                        timer=new Timer(1000, event->{
                            Duration duration = Duration.between(sessionStartTime, LocalDateTime.now());
                            long seconds = duration.getSeconds();
                            int hours = (int) (seconds / 3600);
                            int minutes = (int) ((seconds % 3600) / 60);
                            int secs = (int) (seconds % 60);

                            timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
                        });
                        timer.start();


                    }catch (Exception e){
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }
        );

        end.addActionListener(
                a -> {
            if (timer != null) {
                timer.stop();
            }

                    String subjectName = t1.getText().trim();
                    if (subjectName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter the subject name used during session start.");
                        return;
                    }

                    //--------------DB part--------------

                    String url="jdbc:oracle:thin:@localhost:1521:orcl";
                    int userId=0;
                    int subId=0;
                    try(Connection con= DriverManager.getConnection(url,"db_username","db_password")) {
                        String sql = "select user_id from users_of_abhyas where uname= ?";
                        try (PreparedStatement pst = con.prepareStatement(sql)) {
                            pst.setString(1, username);
                            ResultSet rs = pst.executeQuery();
                            if (rs.next()) {
                                userId = rs.getInt("user_id");
                            }
                        }

                        String subsql = "select sub_id from subjects WHERE user_id = ? AND LOWER(subname) = LOWER(?)";
                        try (PreparedStatement pst = con.prepareStatement(subsql)) {
                            pst.setInt(1, userId);
                            pst.setString(2, subjectName);
                            ResultSet rs = pst.executeQuery();
                            if (!rs.next()) {
                                JOptionPane.showMessageDialog(null, "Subject not found");
                                return;
                            }subId = rs.getInt("sub_id");
                        }

                        //userid and subid mil gaya
                        String sessionEnd = "UPDATE sessions SET end_time = ? WHERE user_id = ? AND sub_id = ? AND end_time IS NULL";
                        try (PreparedStatement pst = con.prepareStatement(sessionEnd)) {
                            sessionEndTime = LocalDateTime.now().withNano(0);
                            pst.setTimestamp(1, Timestamp.valueOf(sessionEndTime));
                            pst.setInt(2, userId);
                            pst.setInt(3, subId);
                            int updated = pst.executeUpdate();

                            if (updated == 0) {
                                JOptionPane.showMessageDialog(null, "No active session found to end.");
                            } else {
                                // show real duration
                                Duration duration = Duration.between(sessionStartTime, sessionEndTime);
                                long hours = duration.toHours();
                                long minutes = duration.toMinutes() % 60;
                                long seconds = duration.getSeconds() % 60;

                                String realDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                                JOptionPane.showMessageDialog(null, "Session ended! Duration: " + realDuration);
                            }


                            //reset
                            timerLabel.setText("00:00:00");
                            t1.setEnabled(true);
                            t1.setText("");
                        }


                    }catch (Exception e){
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }

        });


        back.addActionListener(
                a->{
                    new Dashboard(username);
                    dispose();
                }
        );

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Session Room");
    }
    public static void main(String[] args) {
        new Session("Madhura");
    }
}

