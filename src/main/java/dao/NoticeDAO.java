package dao;

import model.Notice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDAO {
    public List<Notice> getAllNotices() throws SQLException {
        List<Notice> notices = new ArrayList<>();
        String sql = "SELECT * FROM Notices";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                notices.add(new Notice(
                    rs.getInt("notice_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDate("date")
                ));
            }
        }
        return notices;
    }
}