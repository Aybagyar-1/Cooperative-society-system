package view;

import dao.LoanDAO;
import dao.SavingDAO;
import dao.RepaymentDAO;
import dao.NoticeDAO;
import model.Member;
import model.Loan;
import model.Saving;
import model.Repayment;
import model.Notice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MemberDashboard extends JFrame {
    private Member member;
    private JTabbedPane tabbedPane;
    private JTable savingsTable, loanTable, repaymentTable, noticeTable;
    private JButton addSavingsButton, applyLoanButton, addRepaymentButton;
    private SavingDAO savingDAO;
    private LoanDAO loanDAO;
    private RepaymentDAO repaymentDAO;
    private NoticeDAO noticeDAO;

    public MemberDashboard(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        this.member = member;
        setTitle("Member Dashboard - " + member.getFirstName() + " " + member.getSurname());
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/resources/logo.png")).getImage());

        savingDAO = new SavingDAO();
        loanDAO = new LoanDAO();
        repaymentDAO = new RepaymentDAO();
        noticeDAO = new NoticeDAO();

        System.out.println("MemberDashboard initialized with member ID: " + member.getMemberId());

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logoLabel = new JLabel();
        try {
            java.net.URL logoUrl = getClass().getResource("/resources/logo.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                logoLabel.setText("Cooperative Society");
                logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            }
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
        } catch (Exception e) {
            logoLabel.setText("Cooperative Society");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            System.err.println("Error loading logo: " + e.getMessage());
        }
        mainPanel.add(logoLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        JPanel profilePanel = new JPanel(new GridLayout(5, 2, 10, 10));
        profilePanel.add(new JLabel("Member ID:"));
        profilePanel.add(new JLabel(String.valueOf(member.getMemberId())));
        profilePanel.add(new JLabel("Name:"));
        profilePanel.add(new JLabel(member.getFirstName() + " " + member.getSurname()));
        profilePanel.add(new JLabel("Email:"));
        profilePanel.add(new JLabel(member.getEmail()));
        profilePanel.add(new JLabel("Join Date:"));
        profilePanel.add(new JLabel(String.valueOf(member.getJoinDate())));
        profilePanel.add(new JLabel("Status:"));
        profilePanel.add(new JLabel(member.getStatus()));
        tabbedPane.addTab("Profile", profilePanel);

        JPanel savingsPanel = new JPanel(new BorderLayout());
        savingsTable = new JTable();
        try {
            loadSavings();
        } catch (SQLException e) {
            System.err.println("Error loading savings: " + e.getMessage());
            savingsTable.setModel(new DefaultTableModel(new Object[][]{{"Error loading savings"}}, new String[]{"Message"}));
        }
        savingsPanel.add(new JScrollPane(savingsTable), BorderLayout.CENTER);
        addSavingsButton = new JButton("Add Savings");
        savingsPanel.add(addSavingsButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Savings", savingsPanel);

        JPanel loanPanel = new JPanel(new BorderLayout());
        loanTable = new JTable();
        try {
            loadLoans();
        } catch (SQLException e) {
            System.err.println("Error loading loans: " + e.getMessage());
            loanTable.setModel(new DefaultTableModel(new Object[][]{{"Error loading loans"}}, new String[]{"Message"}));
        }
        loanPanel.add(new JScrollPane(loanTable), BorderLayout.CENTER);
        applyLoanButton = new JButton("Apply for Loan");
        loanPanel.add(applyLoanButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Loans", loanPanel);

        JPanel repaymentPanel = new JPanel(new BorderLayout());
        repaymentTable = new JTable();
        try {
            loadRepayments();
        } catch (SQLException e) {
            System.err.println("Error loading repayments: " + e.getMessage());
            repaymentTable.setModel(new DefaultTableModel(new Object[][]{{"Error loading repayments"}}, new String[]{"Message"}));
        }
        repaymentPanel.add(new JScrollPane(repaymentTable), BorderLayout.CENTER);
        addRepaymentButton = new JButton("Make Repayment");
        repaymentPanel.add(addRepaymentButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Repayments", repaymentPanel);

        JPanel noticePanel = new JPanel(new BorderLayout());
        noticeTable = new JTable();
        try {
            loadNotices();
        } catch (SQLException e) {
            System.err.println("Error loading notices: " + e.getMessage());
            noticeTable.setModel(new DefaultTableModel(new Object[][]{{"Error loading notices"}}, new String[]{"Message"}));
        }
        noticePanel.add(new JScrollPane(noticeTable), BorderLayout.CENTER);
        tabbedPane.addTab("Notices", noticePanel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(logoutButton, BorderLayout.SOUTH);
        add(mainPanel);
    }

    public void loadSavings() throws SQLException {
        List<Saving> savings = savingDAO.getSavingsByMember(member.getMemberId());
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Saving ID", "Amount", "Date", "Description"}, 0);
        if (savings.isEmpty()) {
            System.out.println("No savings found for member ID: " + member.getMemberId());
            model.addRow(new Object[]{"No savings available", "", "", ""});
        }
        for (Saving s : savings) {
            model.addRow(new Object[]{s.getSavingId(), s.getAmount(), s.getDate(), s.getDescription()});
        }
        savingsTable.setModel(model);
    }

    public void loadLoans() throws SQLException {
        List<Loan> loans = loanDAO.getLoansByMember(member.getMemberId());
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Loan ID", "Amount Requested", "Amount Approved", "Repayment Months", "Status", "Application Date"}, 0);
        if (loans.isEmpty()) {
            System.out.println("No loans found for member ID: " + member.getMemberId());
            model.addRow(new Object[]{"No loans available", "", "", "", "", ""});
        }
        for (Loan l : loans) {
            model.addRow(new Object[]{l.getLoanId(), l.getAmountRequested(), l.getAmountApproved(),
                l.getRepaymentMonths(), l.getStatus(), l.getApplicationDate()});
        }
        loanTable.setModel(model);
    }

    public void loadRepayments() throws SQLException {
        List<Repayment> repayments = repaymentDAO.getRepaymentsByMember(member.getMemberId());
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Repayment ID", "Loan ID", "Amount", "Date"}, 0);
        if (repayments.isEmpty()) {
            System.out.println("No repayments found for member ID: " + member.getMemberId());
            model.addRow(new Object[]{"No repayments available", "", "", ""});
        }
        for (Repayment r : repayments) {
            model.addRow(new Object[]{r.getRepaymentId(), r.getLoanId(), r.getAmount(), r.getDate()});
        }
        repaymentTable.setModel(model);
    }

    public void loadNotices() throws SQLException {
        List<Notice> notices = noticeDAO.getAllNotices();
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Notice ID", "Title", "Description", "Date"}, 0);
        if (notices.isEmpty()) {
            System.out.println("No notices found");
            model.addRow(new Object[]{"No notices available", "", "", ""});
        }
        for (Notice n : notices) {
            model.addRow(new Object[]{n.getNoticeId(), n.getTitle(), n.getDescription(), n.getDate()});
        }
        noticeTable.setModel(model);
    }

    public JButton getAddSavingsButton() { return addSavingsButton; }
    public JButton getApplyLoanButton() { return applyLoanButton; }
    public JButton getAddRepaymentButton() { return addRepaymentButton; }
    public Member getMember() { return member; }
}
