/**
 * The ChatClient class function is to connect the UI with the Client class
 * 
 * @author	Albi Zhaku
 */

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.Server;

public class ChatClient extends Application {

	@FXML private Button login;
	@FXML private TextField emailField;
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private TextArea textArea;
	@FXML private Button send;
	@FXML private TextField myTextField;
	@FXML private Text wrongText;
	@FXML private TextArea activeUserTextArea;

	private Client client;
	private Stage stage;

	
	/**
	 * This is the start method for the GUI 
	 */
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

	
	
	/**
	 * This method is responsible for sending the messages from
	 * the client over to the server.
	 * 
	 * @param ActionEvent e - Action clicked on the screen
	 */
	@FXML
	public void onSend(ActionEvent e) {

		if (myTextField.getText().substring(0, 1).equals(" ")) { //Checks if the message is blank
			return;
		}
		
		//Sends the message
		client.sendMessage(myTextField.getText());
		textArea.setText(textArea.getText() + myTextField.getText() + "\n");
		myTextField.setText("");
	}
	
	
	/**
	 * This function essentially connects the user onto the chat board server
	 * 
	 * @param ActionEvent e - Action clicked on the screen
	 * @throws IOException
	 */
	@FXML
	public void logIn(ActionEvent e) throws IOException {
		if (!(Server.getSql().getUser().logIn(emailField.getText(), passwordField.getText()))) {
			wrongText.setText("Wrong Log-In Information");
			return;
		}

		client = new Client(1234, emailField.getText()); //Creates the client instance
		client.setMain(this);


		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatUI.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		StackPane container = new StackPane(root);
		
		container.setPrefSize(1031, 500);

		Scene scene = new Scene(container);
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

		stage.setScene(scene);

	}

	
	/**
	 * Handles the HyperLink and redirects the user to the sign-up page
	 * 
	 * @param ActionEvent e - Action clicked on the screen
	 * @throws IOException
	 */
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
	
	
	/**
	 * The function of this method is to allow the user to sign-up for the
	 * chat board server and stores the information onto a SQL server.
	 * 
	 * @param ActionEvent e - Action clicked on the screen
	 * @throws IOException
	 */
	@FXML
	public void onSignUp(ActionEvent e) throws IOException {

		if (Server.getSql().getUser().signUp(emailField.getText(), usernameField.getText(), passwordField.getText())) { //Checks if the log-in fails
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInPage.fxml"));
			Parent root = loader.load();
			StackPane container = new StackPane(root);
			Scene scene = new Scene(container);
			stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

			stage.setScene(scene);
		}

	}

	
	/**
	 * Sets the TextArea with the incoming messages coming from
	 * the server side of the server-client chat architecture.
	 * 
	 * @param readLine
	 */
	public void setText(String readLine) {
		textArea.setText(textArea.getText() + readLine + "\n");

	}

	/**
	 * Sets the activeUserTextArea with the current online users.
	 * 
	 * @return TextArea
	 */
	public TextArea getActiveUserTextArea() {
		return this.activeUserTextArea;
	}

}