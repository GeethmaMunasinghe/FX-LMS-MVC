package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.env.StaticResource;
import com.pcl.lms.model.User;
import com.pcl.lms.util.tools.VerificationCodeGenerator;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class EmailVerificationFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public TextField txtEmail;
    private int generatedOTP;

    public void initialize(){
        setStaticData();
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
    }

    public void navigateVerifyCodeOnAction(ActionEvent actionEvent) throws IOException {
        //Check user is existing
        //otp generate
        //email send
        //navigate with parse data
        try {
            final String fromEmail="gmgee1175@gmail.com";
            final String password="smlw hwqf hdlm dfwp";

            final String toEmail=txtEmail.getText();
            Optional<User> selectedUser= Database.userTable.stream().filter(e->e.getEmail().equals(toEmail)).findFirst();
            if (selectedUser.isPresent()){
                int otp=new VerificationCodeGenerator().getCode(4);
                System.out.println("Generated OTP: "+otp);
                this.generatedOTP=otp;

                Properties props=new Properties();
                props.put("mail.smtp.host","smtp.gmail.com");
                props.put("mail.smtp.auth",true);
                props.put("mail.smtp.starttls.enable",true);
                props.put("mail.smtp.port",587);

                Session session=Session.getDefaultInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(fromEmail,password);
                    }
                });

                Message mimeMessage=new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(fromEmail));
                mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail));
                mimeMessage.setSubject("Email verification code ");
                mimeMessage.setText("Your OTP code is "+new VerificationCodeGenerator().getCode(4));

                Transport.send(mimeMessage);
                System.out.println("OTP sent successfully to "+toEmail);

                FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/pcl/lms/VerifyOTPForm.fxml"));
                Parent parent=loader.load();
                VerifyOTPFormController controller=loader.getController();
                controller.setUserData(generatedOTP,toEmail);
                Stage stage=(Stage) context.getScene().getWindow();
                stage.setScene(new Scene(parent));
            }
        }catch (Exception e){
            throw new RuntimeException();
        }


    }
    private void setUI(String location) throws IOException {
        /*URL resource=getClass().getResource("/com/pcl/lms/"+location+".fxml");
        Parent load= FXMLLoader.load(resource);
        Scene scene=new Scene(load);
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(scene);*/
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
}
