# Abhyas-Track-Kar

Abhyas Track Kar is a **Java-based study tracking application** developed using **Core Java, Java Swing, and Oracle SQL**.

The application allows students to track their study hours subject-wise. Users can **sign up** and **log in**, add subjects, and **start/end study sessions** for each subject.

The system stores session data in an **Oracle SQL** database, enabling persistent tracking. Students can view **total hours spent per subject over a week**, displayed in both **tabular format** and **graphical representation** for better analysis of study patterns.

## ✨ Key Features

- User Sign Up & Login
- Add and manage subjects
- Start / End study sessions with automatic time calculation
- View weekly study hours subject-wise
- Tabular view of study data
- Graphical visualization for better analysis
- Secure Oracle SQL database integration

---

## 🔧 Technical Highlights

- ### Core Java (OOP)

  Implemented core object-oriented principles such as encapsulation, abstraction, inheritance, and polymorphism for clean and modular code structure.

- ### Java Swing GUI

  Built an interactive desktop interface using JFrame, JPanel, JButton, JLabel, JTable, JTextField, and Swing layouts to ensure smooth user interaction.

- ### Database Integration using JDBC

  Connected the Java application to Oracle SQL using JDBC, enabling secure and efficient data exchange between the UI and database.

- ### Oracle SQL Database Design

  Designed normalized database tables for:

  - User authentication
  - Subject management
  - Study session tracking
    Used primary keys, foreign keys, and constraints to maintain data integrity.

- ### Session-Based Time Tracking Logic

  Implemented logic to record start and end timestamps for study sessions and calculate total study hours per subject dynamically.

- ### Weekly Data Aggregation

  Used SQL queries and Java date-time APIs to fetch and compute week-wise study hours for accurate progress tracking.

- ### Tabular & Graphical Data Representation

  Displayed study data using:

  - JTable for structured tabular view
  - Graph/Chart visualization (via Java graphics / charting logic) for better analytical insights

- ### Authentication & Validation
  Implemented login/sign-up system, input validation, and error handling to ensure secure and reliable user experience.
