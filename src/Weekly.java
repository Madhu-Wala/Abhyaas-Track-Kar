import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;


class Weekly extends JFrame {
    Weekly(String username){
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

                float[] dist = {0.5f,1.0f};
                Color[] colors = {
                        new Color(255, 255, 230),
                        new Color(152, 246, 251)

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
        JLabel l2 = new JLabel("Weekly Report", JLabel.CENTER);
        JLabel l3 = new JLabel(username);
        JLabel l4 = new JLabel("Stream: Science");

        String[] columnNames = {"Subjects", "Total Sessions", "Time Spend (in mins)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(tableFont);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(224, 224, 224));
        JScrollPane scrollPane = new JScrollPane(table);

        JButton back = new JButton("Back");

        // Set fonts
        l1.setFont(f);
        l2.setFont(f2);
        l3.setFont(f2);
        l4.setFont(f2);
        back.setFont(f2);

        back.setBackground(new Color(255, 0, 136));
        back.setForeground(Color.white);

        // Positioning
        l1.setBounds(100, 30, 580, 50);
        l2.setBounds(280, 80, 250, 40);
        l3.setBounds(100, 130, 300, 40);
        l4.setBounds(100, 170, 300, 40);
        scrollPane.setBounds(50, 230, 680, 250);
        back.setBounds(50, 920, 150, 40);


        ImageIcon userIcon = new ImageIcon("C:\\Users\\Madhura Walawalkar\\Downloads\\1-personal-center-20x20-48.png");
        Image scaledImage = userIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel userLogo = new JLabel(new ImageIcon(scaledImage));
        userLogo.setBounds(60, 130, 32, 32);

        panel.add(userLogo);
        panel.add(l1);
        panel.add(l2);
        panel.add(l3);
        panel.add(l4);
        panel.add(scrollPane);
        panel.add(back);

        // Scroll pane for full window
        JScrollPane mainScroll = new JScrollPane(panel);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        setContentPane(mainScroll);

        // DB logic
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        int userId = 0;
        try (Connection con = DriverManager.getConnection(url, "C##MAJAKAAM", "majajava123")) {

            String sql = "SELECT user_id FROM users_of_abhyas WHERE uname = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, username);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                }
            }

            // Updated query
            String weeklySql = "SELECT s.subname, sess.start_time, sess.end_time " +
                    "FROM subjects s " +
                    "LEFT JOIN sessions sess ON s.sub_id = sess.sub_id " +
                    "AND TRUNC(sess.start_time) BETWEEN TRUNC(SYSDATE, 'IW') AND TRUNC(SYSDATE) " +
                    "WHERE s.user_id = ?";

            try (PreparedStatement pst = con.prepareStatement(weeklySql)) {
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();

                Map<String, Duration> subDuration=new HashMap<>();
                Map<String, Integer> subSessCount=new HashMap<>();

                while (rs.next()) {
                    String subject = rs.getString("subname");
                    subDuration.putIfAbsent(subject, Duration.ZERO);
                    subSessCount.putIfAbsent(subject, 0);

                    Timestamp startTime = rs.getTimestamp("start_time");
                    Timestamp endTime = rs.getTimestamp("end_time");

                    if (startTime != null && endTime != null) {
                        LocalDateTime startLD = startTime.toLocalDateTime();
                        LocalDateTime endLD = endTime.toLocalDateTime();
                        Duration sessDura = Duration.between(startLD, endLD);

                        subDuration.put(subject, subDuration.get(subject).plus(sessDura));
                        subSessCount.put(subject, subSessCount.get(subject) + 1);
                    }


                }
                for(String subject:subDuration.keySet()){
                    ///hashmap ka key set traverse kar
                    Duration dur=subDuration.get(subject);  //duration utha
                    int totalsessions=subSessCount.getOrDefault(subject,0);  //count utha

                    //Duration se number me convert kar
                    long totalMin=dur.toMinutes();
                    long remianingSec=dur.minusMinutes(totalMin).getSeconds();

                    String timeSpent=String.format("%02d:%02d", totalMin,remianingSec);

                    tableModel.addRow(new Object[]{subject,totalsessions,timeSpent});
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String subject = (String) tableModel.getValueAt(i, 0);
            String timeStr = (String) tableModel.getValueAt(i, 2); // "MM:SS"
            String[] parts = timeStr.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            // Convert to decimal minutes with seconds precision
            double timeInDecimalMins = minutes + (seconds / 60.0);
            dataset.addValue(timeInDecimalMins, "Time Spent", subject);
        }

// Create bar chart

        JFreeChart barChart = ChartFactory.createBarChart(
                "Time Spent per Subject (This Week)", // Chart title
                "Subjects",                          // X-axis label
                "Time (minutes)",                    // Y-axis label
                dataset
        );
// Get the plot
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Set custom color for bars
        renderer.setSeriesPaint(0, new Color(123, 4, 102)); // Blue
        

// Embed in panel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBounds(50, 500, 550, 400); // adjust size and position
        panel.add(chartPanel);


        // Back button logic
        back.addActionListener(a -> {
            new Dashboard(username);
            dispose();
        });

        setVisible(true);
        setSize(800, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Weekly Report");
    }

    public static void main(String[] args) {
        new Weekly("Madhura");
    }
}
