package User;

import com.demo.grpc.User;
import com.demo.grpc.userGrpc;
import database.dbConnection;
import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class UserService extends userGrpc.userImplBase {
    Connection connection = dbConnection.mysqlConnection();
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private User.APIRes.Builder response = User.APIRes.newBuilder();

    @Override
    public void viewProfile(User.viewReq request, StreamObserver<User.APIRes> responseObserver) {
        //Connection connection = dbConnection.mysqlConnection();
        String query = "Select * from registration where name = ? and password = ?";
        String name="";
        String email="";
        String password="";
        String dept="";
        String session="";

        try{
            PreparedStatement statement= connection.prepareStatement(query);
            statement.setString(1,request.getName());
            statement.setString(2,request.getPassword());
            //statement.setString(3,request.getPassword());
            //statement.setString(4,request.getDepartment());
            //statement.setString(5,request.getSession());



            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                name = resultSet.getString("name");
                email = resultSet.getString("emailId");
                password = resultSet.getString("password");
                dept = resultSet.getString("department");
                session = resultSet.getString("session");

                String profile = "Name: " + name + "\n"
                        + "Email id: " + email + "\n"
                        + "Deparment: " + dept + "\n"
                        + "Session: " + session + "\n";

                response.setMessage("\n" + profile);
            }
            else{
                responseObserver.onNext(User.APIRes.newBuilder().build().newBuilder().setMessage("invalid credintials").build());
                responseObserver.onCompleted();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void update(User.updateReq request, StreamObserver<User.APIRes> responseObserver) {
        String query = "UPDATE registration SET emailId = ? , department = ? , session = ? WHERE name = ? AND password = ?;";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,request.getEmailId());
            preparedStatement.setString(2,request.getDepartment());
            preparedStatement.setString(3,request.getSession());
            preparedStatement.setString(4,request.getName());
            preparedStatement.setString(5,request.getPassword());
            preparedStatement.execute();
            System.out.println("Account Updated.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        responseObserver.onNext(User.APIRes.newBuilder().build().newBuilder().setMessage("updated sucessfully").build());
        responseObserver.onCompleted();
    }

    @Override
    public void register(User.registrationReq request, StreamObserver<User.APIRes> responseObserver) {
        //super.register(request, responseObserver);
        //Connection connection = dbConnection.mysqlConnection();
        String query = "insert into registration(name,emailId,password,department, session) values(?,?,?,?,?)";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,request.getName());
            preparedStatement.setString(2,request.getEmailId());
            preparedStatement.setString(3,request.getPassword());
            preparedStatement.setString(4,request.getDepartment());
            preparedStatement.setString(5,request.getSession());
            preparedStatement.execute();
            System.out.println("Account Created.");



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        responseObserver.onNext(User.APIRes.newBuilder().build().newBuilder().setMessage("added sucessfully").build());
        responseObserver.onCompleted();
    }

    @Override
    public void login(User.LoginReq request, StreamObserver<User.APIRes> responseObserver) {
        Connection connection = dbConnection.mysqlConnection();
        String query = "Select * from registration where name = ? and password = ?";
        String name="";
        String password="";
        String inputName = request.getUsername();
        String inputPassword = request.getPassword();
        try{
            PreparedStatement statement= connection.prepareStatement(query);
            statement.setString(1,request.getUsername());
            statement.setString(2,request.getPassword());



            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                name = resultSet.getString("name");
                password = resultSet.getString("password");
            }
            else{
                responseObserver.onNext(User.APIRes.newBuilder().build().newBuilder().setMessage("invalid credintials").build());
                responseObserver.onCompleted();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        if (inputName.equals(name) && inputPassword.equals(password)){
            response.setResCode(200).setMessage("SUCCESS");
            logger.info("Login successful for user : " + name);
        }
        else {
            response.setResCode(400).setMessage("BAD REQUEST");
            logger.info("Login failed for user : " + name);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void logout(User.Empty request, StreamObserver<User.APIRes> responseObserver) {
        super.logout(request, responseObserver);
    }
}
