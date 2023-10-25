package labCollection;

import data.CommandData;
import data.LabWork;
import data.User;
import exceptions.users.NotRegisteredUserException;
import exceptions.users.WrongPasswordException;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import static data.User.hashPassword;


public class DataBaseHandler {
    Connection connection;
    public DataBaseHandler(){}

    public Connection connectToDB(String rootUserName, String rootPasswd){

        String url = "jdbc:postgresql://pg:5432/studs";

        try {
            System.out.println("Try to connect to DB");
            Class.forName("org.postgresql.Driver");
            System.out.println("DB driver has been loaded.");
            connection = DriverManager.getConnection(url, rootUserName, rootPasswd);
            System.out.println("Connect to DB!");
            connection.setAutoCommit(false);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return connection;
    }
    public LinkedList<LabWork> loadCollection() throws SQLException, Exception{
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT  id,
                            name, user_name,
                            creation_date,
                            coordinates_x,
                            coordinates_y,
                            minimal_point,
                            tuned_in_works,
                            difficulty,
                            person_name,
                            person_height,
                            person_eye_color,
                            person_hair_color,
                            person_nationality,
                            person_location_x,
                            person_location_y,
                            person_location_name
                            FROM lab_works;
                    """
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        connection.commit();

        LinkedList<LabWork> works = new LinkedList<>();
        while(resultSet.next()){
            LabWork labWork = new LabWork();
            labWork.setId(resultSet.getInt(1));
            labWork.setName(resultSet.getString(2));
            User user = new User();
            user.setName(resultSet.getString(3));
            labWork.setUser(user);
            labWork.setCreationDate(resultSet.getTimestamp(4).toLocalDateTime().toString());
            labWork.setCoordinatesX(Long.toString(resultSet.getLong(5)));
            labWork.setCoordinatesY(Long.toString(resultSet.getLong(6)));
            labWork.setMinimalPoint(Double.toString(resultSet.getDouble(7)));
            labWork.setTunedInWorks(Integer.toString(resultSet.getInt(8)));
            labWork.setDifficulty(resultSet.getString(9));
            labWork.setPersonName(resultSet.getString(10));
            labWork.setPersonHeight(Long.toString(resultSet.getLong(11)));
            labWork.setPersonEyeColor(resultSet.getString(12));
            labWork.setPersonHairColor(resultSet.getString(13));
            labWork.setPersonNationality(resultSet.getString(14));
            labWork.setPersonLocationX(Integer.toString(resultSet.getInt(15)));
            labWork.setPersonLocationY(Float.toString(resultSet.getFloat(16)));
            labWork.setPersonLocationName(resultSet.getString(17));
            works.add(labWork);
        }
        return works;
    }

    public User registerNewUser(CommandData commandData) throws SQLException {
        User user = commandData.user;
        String salt = UUID.randomUUID().toString();
        byte[] password = hashPassword(user.getPassword(), salt);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users (user_name, passwd, salt, is_admin) VALUES (?, ?, ?, ?)"
            );
            preparedStatement.setString(1, user.getName());
            preparedStatement.setBytes(2, password);
            preparedStatement.setString(3, salt);
            preparedStatement.setBoolean(4, user.isAdmin());
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            connection.rollback();
            throw e;
        }
        return user;
    }
    public User logInUser(CommandData commandData) throws SQLException, WrongPasswordException, NotRegisteredUserException{
        User user = commandData.user;
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT salt, passwd, is_admin FROM users WHERE user_name = ?"
        );
        preparedStatement.setString(1, user.getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        connection.commit();
        if(!resultSet.next()){
            throw new NotRegisteredUserException();
        }
        user.setAdmin(resultSet.getBoolean("is_admin"));
        String salt = resultSet.getString("salt");
        byte[] tablePasswd = resultSet.getBytes("passwd");
        byte[] userPasswd = hashPassword(user.getPassword(), salt);
        if (!Arrays.equals(tablePasswd, userPasswd)){
            throw new WrongPasswordException();
        }
        return user;
    }



    public void addLabWork(CommandData commandData) throws SQLException{
        try {
            LabWork labWork = commandData.element;

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO lab_works (name, user_name," +
                            "coordinates_x, coordinates_y," +
                            "minimal_point, tuned_in_works, difficulty," +
                            "person_name, person_height, person_eye_color," +
                            "person_hair_color, person_nationality, person_location_x," +
                            "person_location_y, person_location_name)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setString(1, labWork.getName());
            preparedStatement.setString(2, commandData.user.getName());
            preparedStatement.setFloat(3, labWork.getCoordinatesX());
            preparedStatement.setLong(4, labWork.getCoordinatesY());
            preparedStatement.setDouble(5, labWork.getMinimalPoint());
            preparedStatement.setInt(6, labWork.getTinedInWork());
            preparedStatement.setString(7, labWork.getDifficulty());
            preparedStatement.setString(8, labWork.getPersonName());
            preparedStatement.setLong(9, labWork.getPersonHeight());
            preparedStatement.setString(10, labWork.getPersonEyeColor());
            preparedStatement.setString(11, labWork.getPersonHairColor());
            preparedStatement.setString(12, labWork.getPersonNationality());
            preparedStatement.setInt(13, labWork.getPersonLocationX());
            preparedStatement.setFloat(14, labWork.getPersonLocationY());
            preparedStatement.setString(15, labWork.getPersonLocationName());

            preparedStatement.executeUpdate();
            connection.commit();

        }
        catch (SQLException e){
            connection.rollback();
            throw e;
        }

    }
    public void clearUserElements(CommandData commandData) throws SQLException{
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM lab_works WHERE user_name = ?;"
            );
            preparedStatement.setString(1, commandData.user.getName());
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            connection.rollback();
            throw e;
        }
    }

    public void truncate(CommandData commandData) throws SQLException{
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "TRUNCATE TABLE lab_works"
            );
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            connection.rollback();
            throw e;
        }
    }
    public int removeByID(CommandData commandData) throws SQLException{
        int rawDelete = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM lab_works WHERE  id = ?;"
            );
            preparedStatement.setInt(1, commandData.intDigit);
            rawDelete = preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            connection.rollback();
            throw e;
        }
        return rawDelete;
    }
    public int updateByID(CommandData commandData) throws SQLException{
        int rawUpdate = 0;
        try {
                LabWork labWork = commandData.element;
                PreparedStatement preparedStatement = connection.prepareStatement(
                        """
                UPDATE lab_works SET    name = ?,
                                        coordinates_x = ?,
                                        coordinates_y = ?,
                                        minimal_point = ?,
                                        tuned_in_work = ?,
                                        difficulty = ?,
                                        person_name = ?,
                                        person_height = ?, 
                                        person_eye_color = ?,
                                        person_hair_color = ?,
                                        person_nationality = ?,
                                        person_location_x = ?,
                                        person_location_y = ?,                                         
                                        person_location_name = ?                                        
                WHERE id = ?                      
                ;
                """
            );
            preparedStatement.setString(1, labWork.getName());
            preparedStatement.setFloat(2, labWork.getCoordinatesX());
            preparedStatement.setLong(3, labWork.getCoordinatesY());
            preparedStatement.setDouble(4, labWork.getMinimalPoint());
            preparedStatement.setInt(5, labWork.getTinedInWork());
            preparedStatement.setString(6, labWork.getDifficulty());
            preparedStatement.setString(7, labWork.getPersonName());
            preparedStatement.setLong(8, labWork.getPersonHeight());
            preparedStatement.setString(9, labWork.getPersonEyeColor());
            preparedStatement.setString(10, labWork.getPersonHairColor());
            preparedStatement.setString(11, labWork.getPersonNationality());
            preparedStatement.setInt(12, labWork.getPersonLocationX());
            preparedStatement.setFloat(13, labWork.getPersonLocationY());
            preparedStatement.setString(14, labWork.getPersonLocationName());

            preparedStatement.setInt(15, commandData.intDigit);
            rawUpdate = preparedStatement.executeUpdate();
            connection.commit();

            connection.commit();
        }
        catch (SQLException e){
            connection.rollback();
            throw e;
        }
        return rawUpdate;
    }



    public int getCurrentID() throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT last_value FROM lab_works_id_seq;"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        connection.commit();
        resultSet.next();
        int curID = resultSet.getInt(1);
        return curID;

    }
    public boolean permissionCheck(User user, int labID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT user_name FROM lab_works WHERE id = ?;"
        );
        preparedStatement.setInt(1, labID);
        ResultSet resultSet = preparedStatement.executeQuery();
        connection.commit();
        if (resultSet.next()){
            String labUser = resultSet.getString(1);
            return labUser.equals(user.getName());
        }
        return false;
    }
}
