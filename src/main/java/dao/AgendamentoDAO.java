package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionMySQL;
import models.Agendamento;

public class AgendamentoDAO extends Agendamento {

    private Connection connection;

    public AgendamentoDAO(Connection connection) {
        this.connection = connection;
    }
    
    public AgendamentoDAO() {
    	super();
    }

    public void insertSchedule(Agendamento agendamento) {
        if (!isScheduleConflict(agendamento) && !isScheduleNearby(agendamento)) {
            String query = "insert into schedule (date, time) values (?,?)";
            try (Connection conn = ConnectionMySQL.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, agendamento.getDate());
                stmt.setString(2, agendamento.getTime());

                System.out.print(stmt + "");

                stmt.executeUpdate();
                System.out.print("DAO adicionado ");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.print("não adicionado");
            }
        } else {
            // Trate o conflito de horário aqui, por exemplo, lançando uma exceção ou retornando uma mensagem de erro.
            System.out.print("Conflito de horário: Não foi possível adicionar o agendamento.");
        }
    }

    public void updateSchedule(Agendamento agendamento) {
        String query = "update schedule set date=?,time=? where id=?";
        try (Connection conn = ConnectionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

        	stmt.setString(1, agendamento.getDate());
        	stmt.setString(2, agendamento.getTime());
        	stmt.setInt(3, agendamento.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSchedule(Agendamento agendamento) {
        String query = "delete from schedule where id=?";
        try (Connection conn = ConnectionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, agendamento.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Agendamento> listSchedule() {
        ArrayList<Agendamento> schedules = new ArrayList<>();
        String query = "select * from schedule order by date";
        try (Connection conn = ConnectionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
            	int id = rs.getInt(1);
            	String date = rs.getString(2);
            	String time = rs.getString(3);

                schedules.add(new Agendamento(id, date, time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public void getScheduleById(Agendamento agendamento) {
        String query = "select * from schedule where id = ?";
        try (Connection conn = ConnectionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, agendamento.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	agendamento.setId(rs.getInt(1));
				agendamento.setDate(rs.getString(2));
				agendamento.setTime(rs.getString(3));
				
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isScheduleConflict(Agendamento agendamento) {
        String query = "SELECT COUNT(*) FROM schedule WHERE date = ? AND time = ?";
        try (Connection conn = ConnectionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, agendamento.getDate());
            stmt.setString(2, agendamento.getTime());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Retorna verdadeiro se já houver agendamento no mesmo horário.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isScheduleNearby(Agendamento agendamento) {
        String query = "SELECT COUNT(*) FROM schedule WHERE date = ? AND " +
                       "ABS(TIME_TO_SEC(TIMEDIFF(time, ?))) <= 1740";
        try (Connection conn = ConnectionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, agendamento.getDate());
            stmt.setString(2, agendamento.getTime());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Retorna verdadeiro se houver um agendamento próximo.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
