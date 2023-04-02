package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import server.Server;

public class Main extends Application {

	@FXML
	private Button login;
	@FXML
	private TextField emailField;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextArea textArea;
	@FXML
	private Button send;
	@FXML
	private TextField myTextField;

	@FXML
	private TextArea activeUserTextArea;

	private Client client;

	private Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInPage.fxml"));

		Parent root = loader.load();

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	@FXML
	public void onSend(ActionEvent e) {

		if (myTextField.getText().substring(0, 1).equals(" ")) {
			return;
		}
		client.sendMessage(myTextField.getText());
		textArea.setText(textArea.getText() + myTextField.getText() + "\n");
		myTextField.setText("");
		System.out.println(Client.activeUsers);
	}

	@FXML
	public void logIn(ActionEvent e) throws IOException {
		if (!(Server.getSql().getUser().logIn(emailField.getText(), passwordField.getText()))) {
			System.out.println("Wrong Log In Info");
			return;
		}

		client = new Client(1234, emailField.getText());
		client.setMain(this);
		
		
		
		client.sendUsername(this.emailField.getText());

		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(Client.activeUsers.contains(emailField.getText())) {
			
			client.getSocket().close();
			return;
		}
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatUI.fxml"));
		loader.setController(this);

		Parent root = loader.load();

		StackPane container = new StackPane(root);
		container.setPrefSize(1031, 500);

		Scene scene = new Scene(container);

		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

		stage.setScene(scene);

	}

	@FXML
	public void onSignUpLink(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
		loader.setController(this);

		Parent root = loader.load();

		StackPane container = new StackPane(root);

		Scene scene = new Scene(container);

		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

		stage.setScene(scene);

	}

	@FXML
	public void onSignUp(ActionEvent e) throws IOException {

		if (Server.getSql().getUser().signUp(emailField.getText(), usernameField.getText(), passwordField.getText())) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInPage.fxml"));

			Parent root = loader.load();

			StackPane container = new StackPane(root);

			Scene scene = new Scene(container);

			stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

			stage.setScene(scene);
		}

	}

	public void setText(String readLine) {
		textArea.setText(textArea.getText() + readLine + "\n");

	}

	public TextArea getActiveUserTextArea() {
		return this.activeUserTextArea;
	}

}