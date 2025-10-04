package dao;

import model.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    public List<Loan> getLoansByMember(int memberId) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loans WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(new Loan(
                    rs.getInt("loan_id"),
                    rs.getInt("member_id"),
                    rs.getDouble("amount_requested"),
                    rs.getObject("amount_approved") != null ? rs.getDouble("amount_approved") : null,
                    rs.getObject("repayment_months") != null ? rs.getInt("repayment_months") : null,
                    rs.getString("status"),
                    rs.getDate("application_date")
                ));
            }
        }
        return loans;
    }
}
