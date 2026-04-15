package com.mycompany.studentfeetracker;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    double totalFee = 0;
    double paidAmount = 0;

    @Override
    public void start(Stage stage) {

        // 🔹 Title
        Label title = new Label("Student Fee Management System");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // 🔹 Student Info
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Student Name");

        TextField totalFeeField = new TextField();
        totalFeeField.setPromptText("Enter Total Fee");

        Label feeLabel = new Label("Total Fee: Not set");

        // 🔹 Payment Labels (DECLARE FIRST ✅)
        Label paidLabel = new Label("Paid: ₹0");
        Label balanceLabel = new Label("Balance: ₹0");

        // 🔹 Receipt Area (DECLARE FIRST ✅)
        TextArea receiptArea = new TextArea();
        receiptArea.setEditable(false);

        // 🔹 Buttons
        Button setFeeBtn = new Button("Set Fee");
        Button payBtn = new Button("Pay Fee");
        Button receiptBtn = new Button("Generate Receipt");

        // ✅ FIXED: Set Fee Logic (with reset)
        setFeeBtn.setOnAction(e -> {
            try {
                totalFee = Double.parseDouble(totalFeeField.getText());

                // 🔥 Reset for new student
                paidAmount = 0;

                feeLabel.setText("Total Fee: ₹" + totalFee);

                // Reset UI
                paidLabel.setText("Paid: ₹0");
                balanceLabel.setText("Balance: ₹" + totalFee);
                receiptArea.clear();

                totalFeeField.clear();

            } catch (Exception ex) {
                showAlert("Enter valid fee amount!");
            }
        });

        // 🔹 Payment Logic
        TextField payField = new TextField();
        payField.setPromptText("Enter Payment Amount");

        payBtn.setOnAction(e -> {
    try {
        String name = nameField.getText();

        if (name.isEmpty()) {
            showAlert("Enter student name first!");
            return;
        }

        if (totalFee == 0) {
            showAlert("Set total fee first!");
            return;
        }

        double payment = Double.parseDouble(payField.getText());

        // 🚫 Prevent overpayment
        if (paidAmount >= totalFee) {
            showAlert("Fee already fully paid!");
            return;
        }

        if (paidAmount + payment > totalFee) {
            showAlert("Payment exceeds total fee!");
            return;
        }

        paidAmount += payment;

        double balance = totalFee - paidAmount;

        paidLabel.setText("Paid: ₹" + paidAmount);
        balanceLabel.setText("Balance: ₹" + balance);

        payField.clear();

    } catch (Exception ex) {
        showAlert("Enter valid amount!");
    }
});

        // 🔹 Receipt Logic
        receiptBtn.setOnAction(e -> {
            String name = nameField.getText();

            if (name.isEmpty() || totalFee == 0) {
                showAlert("Enter student name and set fee!");
                return;
            }

            double balance = totalFee - paidAmount;

            String receipt = "----- RECEIPT -----\n"
                    + "Student: " + name + "\n"
                    + "Total Fee: ₹" + totalFee + "\n"
                    + "Paid: ₹" + paidAmount + "\n"
                    + "Balance: ₹" + Math.max(balance, 0) + "\n"
                    + "Status: " + (balance <= 0 ? "PAID" : "PENDING") + "\n"
                    + "-------------------";

            receiptArea.setText(receipt);
        });

        // 🔹 Layout
        VBox inputBox = new VBox(10,
                new Label("Student Details"),
                nameField,
                totalFeeField,
                setFeeBtn,
                feeLabel
        );
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-border-color: black;");

        VBox paymentBox = new VBox(10,
                new Label("Fee Payment"),
                payField,
                payBtn,
                paidLabel,
                balanceLabel
        );
        paymentBox.setPadding(new Insets(10));
        paymentBox.setStyle("-fx-border-color: black;");

        VBox receiptBox = new VBox(10,
                new Label("Receipt"),
                receiptBtn,
                receiptArea
        );
        receiptBox.setPadding(new Insets(10));
        receiptBox.setStyle("-fx-border-color: black;");

        HBox mainLayout = new HBox(20, inputBox, paymentBox, receiptBox);
        mainLayout.setPadding(new Insets(10));

        VBox root = new VBox(10, title, mainLayout);

        Scene scene = new Scene(root, 750, 400);
        stage.setScene(scene);
        stage.setTitle("Fee Tracking System");
        stage.show();
    }

    // 🔹 Alert method
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}