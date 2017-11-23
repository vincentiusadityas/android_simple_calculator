package com.example.vasun.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {
    // Text view to show output (user cannot change it)
    private TextView TextShow;
    // numeric button IDs
    private int[] numButtons = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
    // operator button IDs
    private int[] optButtons = {R.id.btnDiv, R.id.btnMult, R.id.btnSub, R.id.btnAdd};
    // to mark if the last button preseed is numeric or not
    private boolean lastNum;
    // to mark if the current state is in error or not
    private boolean stateError;
    // if true, we are not allowed to add another dot
    private boolean lastPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find the text view
        this.TextShow = (TextView) findViewById(R.id.TextShow);
        // find and set OnClickListener to numeric buttons
        setNumOnClickListener();
        // find and set OnClickListener to operator buttons, equal button, decimal point button,
        // clear button, and togle sign button
        setOptOnClickListener();
    }

    // find and set OnClickListener to numeric buttons
    private void setNumOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // append the text of the clicked button
                Button button = (Button) v;
                if (stateError){
                    // if current state is Error, replace the error message
                    TextShow.setText(button.getText());
                    stateError = false;
                } else {
                    // if not, append the valid expression
                    TextShow.append(button.getText());
                }
                // set the flag that the last button pressed is numeric
                lastNum = true;
            }
        };
        // assign the listener to all the numeric buttons
        for (int id : numButtons){
            findViewById(id).setOnClickListener(listener);
        }
    }

    // find and set OnClickListener to operator buttons, equal button, decimal point button,
    // clear button, and togle sign button
    private void setOptOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if the current state is error, do not append the operator
                // if the last input is number only(lastNum=true), append the operator
                if (lastNum && !stateError){
                    Button button = (Button) v;
                    TextShow.append(button.getText());
                    lastNum = false;
                    lastPoint = false;      // reset the deciml point flag
                }
            }
        };
        // assign the listener to all the operator buttons
        for (int id : optButtons){
            findViewById(id).setOnClickListener(listener);
        }
        // decimal point button
        findViewById(R.id.btnPt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNum && !stateError && !lastPoint){
                    TextShow.append(".");
                    lastNum = false;
                    lastPoint = false;
                }
            }
        });
        // clear button
        findViewById(R.id.btnClr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextShow.setText("");
                lastNum = false;
                lastPoint = false;
                stateError = false;
            }
        });
        // result button
        findViewById(R.id.btnRslt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResult();
            }
        });
        // togle sign button
        findViewById(R.id.btnSign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTogle();
            }
        });
    }
    // logic to change to positive/negative
    private void  onTogle() {
        if (lastNum && !stateError){
            // if the the current state is not error, do not change the sign
            // if the last input is numeric, change the sign
            String text = TextShow.getText().toString();
            Float num = Float.parseFloat(text);
            num = -num;
            String str = Float.toString(num);
            TextShow.setText(str);
            lastNum = true;
            lastPoint = true;
        }
    }
    //logic to calculate the problem
    private void onResult() {
        if (lastNum && !stateError){
            // if the current state is error, don't do anything
            // if the last input is numeric, the problem can be calculated
            String text = TextShow.getText().toString();
            // Create an Expression (A class from exp4j library)
            Expression expression = new ExpressionBuilder(text).build();
            try {
                // Calculate the result and display
                double result = expression.evaluate();
                TextShow.setText(Double.toString(result));
                lastPoint = true;       // Result contains a dot
            } catch (ArithmeticException e){
                // Display an error message
                TextShow.setText("Error");
                stateError = true;
                lastNum = false;
            }
        }
    }
}

// Referensi dari: http://www.javahelps.com/2015/03/android-simple-calculator.html
// Icon: https://www.google.co.id/imgres?imgurl=http%3A%2F%2Fwww.megaicons.net%2Fstatic%2Fimg%2Ficons_sizes%2F27%2F89%2F256%2Fmetroui-apps-calculator-icon.png&imgrefurl=http%3A%2F%2Fwww.megaicons.net%2Ficonspack-89%2F1721%2F&docid=kuKToiGOiEiphM&tbnid=wEgWTNrUtrm7aM%3A&vet=1&w=256&h=256&bih=638&biw=1366&q=calculator%20icon%20256x256%20pixels&ved=0ahUKEwjRjpLq64zSAhVCuI8KHQmtBdYQMwgfKAQwBA&iact=mrc&uact=8#h=256&imgrc=wEgWTNrUtrm7aM:&vet=1&w=256