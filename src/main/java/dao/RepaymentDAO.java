package dao;

import model.Repayment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepaymentDAO {
    public List<Repayment> getRepaymentsByMember(int memberId) throws SQLException {
        List<Repayment> repayments = new ArrayList<>();
        String sql = "SELECT r.* FROM Repayments r JOIN Loans l ON r.loan_id = l.loan_id WHERE l.member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                repayments.add(new Repayment(
                    rs.getInt("repayment_id"),
                    rs.getInt("loan_id"),
                    rs.getDouble("amount"),
                    rs.getDate("date")
                ));
            }
        }
        return repayments;
    }
}
