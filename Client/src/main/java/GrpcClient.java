import com.demo.grpc.User;
import com.demo.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;
import java.util.logging.Logger;

public class GrpcClient {

    private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        Integer choice;
        System.out.println("Choose what you want to do:");
        System.out.println("1. Create an account: Type 1");
        System.out.println("2. Log in: 2");

        choice = Integer.valueOf(scanner.nextLine());
        if(choice == 1){
            System.out.print("Enter username:");
            String userName = scanner.nextLine();
            System.out.print("Enter password:");
            String password = scanner.nextLine();

            System.out.println("Set your profile:");
            System.out.print("Enter emailId:");
            String email = scanner.nextLine();
            System.out.print("Enter department:");
            String department = scanner.nextLine();
            System.out.print("Enter session:");
            String session = scanner.nextLine();

            User.registrationReq request = User.registrationReq.newBuilder()
                    .setName(userName)
                    .setEmailId(email)
                    .setPassword(password)
                    .setDepartment(department)
                    .setSession(session)
                    .build();
            User.APIRes apiRes = userStub.register(request);
            logger.info(apiRes.getResCode() + "\n" + apiRes.getMessage());


        }
        else if(choice == 2){
            System.out.print("Enter username:");
            String userName= scanner.nextLine();
            System.out.print("Enter password:");
            String pass = scanner.nextLine();
            User.LoginReq request = User.LoginReq.newBuilder()
                    .setUsername(userName)
                    .setPassword(pass)
                    .build();
            User.APIRes apiRes = userStub.login(request);
            logger.info(apiRes.getResCode() + "\n" + apiRes.getMessage());

            Integer serviceChoice;
            System.out.println("Select an option:");
            System.out.println("1. View Profile (Type 1)");
            System.out.println("2. Update Profile (Type 2)");

            serviceChoice = Integer.valueOf(scanner.nextLine());

            if(serviceChoice == 1){
                User.viewReq requestforview = User.viewReq.newBuilder()
                        .setName(userName)
                        .setPassword(pass)
                        .build();
                User.viewRes view_res = userStub.viewProfile(requestforview);
                logger.info(view_res.getMessage());

            }
            else if(serviceChoice == 2){
                System.out.println("Press enter if you don't want to change a data.");
                System.out.print("Change email id:");
                String email = scanner.nextLine();
                System.out.print("Change department:");
                String department = scanner.nextLine();
                System.out.print("Change session:");
                String session = scanner.nextLine();

                User.updateReq update_request = User.updateReq.newBuilder()
                        .setName(userName)
                        .setPassword(pass)
                        .setEmailId(email)
                        .setDepartment(department)
                        .setSession(session)
                        .build();
                User.APIRes apiRes1 = userStub.update(update_request);
                logger.info(apiRes1.getResCode() + "\n" + apiRes1.getMessage());

                System.out.println("Updated data:");
                User.viewReq requestforview = User.viewReq.newBuilder()
                        .setName(userName)
                        .setPassword(pass)
                        .build();
                User.viewRes view_res = userStub.viewProfile(requestforview);
                logger.info("Updated data: " + "\n" + view_res.getMessage());
            }

            else{
                System.out.println("Invalid input.");
            }



        }
        else{
            System.out.println("Invalid input.");
        }






        /*

        User.LoginReq request = User.LoginReq.newBuilder()
                .setUsername("raihan123")
                .setPassword("abc123")
                .build();

         */



/*
        User.LoginReq request = User.LoginReq.newBuilder()
                .setUsername(userName)
                .setPassword(pass)
                .build();
*/



    }
}
