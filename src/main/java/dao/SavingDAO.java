package dao;

import model.Saving;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavingDAO {
    public List<Saving> getSavingsByMember(int memberId) throws SQLException {
        List<Saving> savings = new ArrayList<>();
        String sql = "SELECT * FROM Savings WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                savings.add(new Saving(
                    rs.getInt("saving_id"),
                    rs.getInt("member_id"),
                    rs.getDouble("amount"),
                    rs.getDate("date"),
                    rs.getString("description")
                ));
            }
        }
        return savings;
    }
}