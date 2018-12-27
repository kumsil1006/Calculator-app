/*
 * StudentID : 12153183
 * Name : 윤수빈(중국학과)
 */

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Calculator {
	private double result = 0.0;
	private int counter = 0; // it counts the process
	private String initialVal = ""; // this value is for printing whole inputs and outputs including operators and numbers
	private String storedInput; // 숫자를 저장하기 위한 string 변수
	public JFrame frame;
	private JTextField textField;
	private JButton buttons[];
	ArrayList<String> operators = new ArrayList<>(); // input numbers
	ArrayList<Double> vals = new ArrayList<>(); // input operators
	
	// Listener
	private MouseAdapter buttonClickListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		
			JButton button = (JButton) e.getComponent();
			int ind = Integer.parseInt(button.getActionCommand()); // find out the index	
			
			// 값을 맨 처음 입력할 때
			if(counter == 0 && initialVal == "") {				
				// 1~9까지의 값을 입력했다면
				if(ind > 0 && ind < 10) {					
					textField.setText(initialVal); // remove 0
					textField.getText();
					
					storedInput = String.valueOf(ind);
					initialVal = String.valueOf(ind);
					textField.setText(initialVal);
						
					// print the inputs
					textField.getText();
						
					// counter counts the length of a number : 자릿수 계산하기
					setCounter(getCounter() + 1);
				}
				// +/- 를 눌렀다면 -> 음수처리
				else if(ind == 18) {					
					initialVal = "-";
					storedInput = "-";
					textField.setText(initialVal);
					textField.getText();
					setCounter(getCounter() + 1);
				}
				// .을 눌렀다면 -> 소수점 표시
				else if(ind == 17) {
					initialVal = "0.";
					storedInput = "0.";
					textField.setText(initialVal);
					textField.getText();
					setCounter(getCounter() + 1);
				}
				// 0 눌렀으면 0 표시
				else if(ind == 0) {
					storedInput = "0";
					initialVal = "0";
					setCounter(getCounter() + 1);
				}			
			}
			// 처음 입력하는 게 아닌 경우	
			else {
				if(ind == 13) {
					newCalc();
				}
				// 0인데 앞에 이미 값이 입력되어 있을 경우에만 0을 저장한다.
				if(ind == 0 && !storedInput.equals("0")) {
					
					textField.setText(initialVal);
					textField.getText();
						
					storedInput = storedInput + "0";
					initialVal = initialVal + "0";
						
					textField.setText(initialVal);
					textField.getText();
					setCounter(getCounter() + 1);
				}
				// 1~9까지의 값을 입력했다면
				if(ind > 0 && ind < 10)  {
					// 처음 입력한 값이 0이면, 0을 지우고 새 숫자를 넣어준다
					if(storedInput.equals("0") || initialVal.equals("0")) {
						CE();
					}
									
					storedInput = storedInput + String.valueOf(ind);
					initialVal = initialVal + String.valueOf(ind);
					textField.setText(initialVal);
					
					// print the inputs
					textField.getText();
						
					// counter counts the length of a number : 자릿수 계산하기
					setCounter(getCounter() + 1);
				}
				// 연산자를 입력했다면
				else if(ind == 10 || ind == 11 || ind == 12 || ind == 15) {
					// 만약에 연산자를 여러 번 누른다면 마지막에 누른 1개만 입력
					if(storedInput == "") {				
						initialVal = initialVal.substring(0, initialVal.length()-1) + button.getText();							 
						operators.remove(operators.size()-1);
						operators.add(button.getText());					
					}	
					// 
					else if(storedInput != "" && storedInput.contains("(-")) {
						initialVal = initialVal + ")" + button.getText();
						operators.add(button.getText());
						storedInput = storedInput.substring(1);
						vals.add(Double.parseDouble(storedInput));
						storedInput = "";
					}
					else {
						vals.add(Double.parseDouble(storedInput));
						operators.add(button.getText());
						storedInput = "";
							
						initialVal = initialVal + button.getText();
						setCounter(0);
					}						
					textField.setText(initialVal);
					textField.getText(); // print the inputs							
				}
				// =을 눌렀다면
				else if(ind == 14) {						
					/*	counter != 0
					 * if the user inputs = operator right after input the other operators,
					 *  there is no number which can be calculated, so ignore it
					 */

					if(!operators.isEmpty() && !storedInput.contains("(") && storedInput != "") {
						initialVal = initialVal + button.getText();	
						vals.add(Double.parseDouble(storedInput));
						operators.add(button.getText());
						CalculateResult();
					}
					else if(storedInput.contains("(")){
						initialVal = initialVal + ")=";
						storedInput = storedInput.substring(1);
						vals.add(Double.parseDouble(storedInput));	
						operators.add(button.getText());
						CalculateResult();
					}										
					textField.setText(initialVal);
					textField.getText(); // print the inputs and output
				}
				// CE를 눌렀다면
				else if(ind == 16) {
					CE();					
				}
				// .을 누른다면
				else if(ind == 17) {
					if(!storedInput.contains(".")) {
						initialVal = initialVal + ".";
						storedInput = storedInput + ".";
					}
					textField.setText(initialVal);
					textField.getText();
				}
				// +/-를 눌렀다면(부호를 바꿔준다)
				else if(ind == 18) {
					PlusMinusCalc();
				}		
			}
		}		
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator window = new Calculator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Calculator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 500, 500); // set size and location
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close
		
		JPanel panel = new JPanel();
		
		// textField
		textField = new JTextField();
		textField.setFont(new Font("굴림", Font.BOLD, 22));
		textField.setEditable(false);
		textField.setBackground(Color.WHITE);
		textField.setColumns(10);
		textField.setHorizontalAlignment(JTextField.RIGHT);
		textField.setText("0");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 494, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(28, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(textField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE))
					.addGap(26))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
		);
		panel_1.setLayout(new GridLayout(5, 4, 2, 2)); // grid layout
		
		// make buttons
		JButton btnCe = new JButton("CE");
		btnCe.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnCe);
		
		JButton btnNewButton_16 = new JButton(".");
		btnNewButton_16.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_16);
		
		// an extra button for changing positive number to negative, negative number to positive
		JButton button = new JButton("+/-");
		button.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(button);
		
		JButton btnNewButton_3 = new JButton("÷");
		btnNewButton_3.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_3);
		
		JButton btnNewButton = new JButton("7");
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("8");
		btnNewButton_1.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("9");
		btnNewButton_2.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_2);
		
		JButton btnNewButton_7 = new JButton("*");
		btnNewButton_7.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_7);
		
		JButton btnNewButton_4 = new JButton("4");
		btnNewButton_4.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("5");
		btnNewButton_5.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_5);
		
		JButton btnNewButton_6 = new JButton("6");
		btnNewButton_6.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_6);
		
		JButton btnNewButton_11 = new JButton("-");
		btnNewButton_11.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_11);
		
		JButton btnNewButton_8 = new JButton("1");
		btnNewButton_8.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_8);
		
		JButton btnNewButton_9 = new JButton("2");
		btnNewButton_9.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_9);
		
		JButton btnNewButton_10 = new JButton("3");
		btnNewButton_10.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_10);
		
		JButton btnNewButton_15 = new JButton("+");
		btnNewButton_15.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_15);
		
		JButton btnNewButton_13 = new JButton("C");
		btnNewButton_13.setBackground(SystemColor.info);
		btnNewButton_13.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_13);
		
		JButton btnNewButton_12 = new JButton("0");
		btnNewButton_12.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_12);
		
		JButton btnNewButton_14 = new JButton("=");
		btnNewButton_14.setBackground(SystemColor.activeCaption);
		btnNewButton_14.setFont(new Font("굴림", Font.BOLD, 20));
		panel_1.add(btnNewButton_14);
		
	
		frame.getContentPane().setLayout(groupLayout);
		
		buttons = new JButton[] { btnNewButton_12, btnNewButton_8, btnNewButton_9, btnNewButton_10, btnNewButton_4,
				btnNewButton_5, btnNewButton_6, btnNewButton, btnNewButton_1, btnNewButton_2,
				btnNewButton_3, btnNewButton_7, btnNewButton_11, btnNewButton_13, btnNewButton_14, btnNewButton_15, btnCe, btnNewButton_16, button};
		
		for (int n = 0; n < buttons.length; n++) {
			buttons[n].addMouseListener(buttonClickListener);
			buttons[n].setActionCommand(String.valueOf(n));
		}
	}
	
	private void CE() {
		// 연산한 적 없으면
		if(operators.isEmpty() == true)
			newCalc();
		// 연산한 적 있으면
		else if(storedInput != "" && operators.get(operators.size()-1) != "=") {
			initialVal = initialVal.substring(0, initialVal.length() - storedInput.length());
			storedInput = "";
			
			textField.setText(initialVal);
			textField.getText();
		}	

	}
	// C 눌렀을 때 초기화
	private void newCalc() {
		result = 0.0;
		textField.setText("0");
		initialVal = "";
		storedInput = "";
		setCounter(0);
		
		operators.clear();
		vals.clear();
	}
	// 계산기
	private void CalculateResult(){
		
		for(int i = 0 ; i < vals.size(); i++) {
			// 처음엔 입력받은 값을 result에 저장
			if(i == 0) {
				result = vals.get(i);
			}
			// 처음이 아니면
			else {
				// add
				if(operators.get(i-1) == "+") {
					result += vals.get(i);
				}
				// 빼기
				else if(operators.get(i-1) == "-") {
					result -= vals.get(i);
				}
				// 나누기
				else if(operators.get(i-1) == "÷") {	
					try{
						result /= vals.get(i);
						result = Math.floor(result*10)/10.0;
					}catch(Exception e) {
						System.out.println("");
					}
					
				}
				// 곱하기
				else if(operators.get(i-1) == "*") {
					result = result * vals.get(i);
				}
			}		
		}
		initialVal = initialVal + String.valueOf(result);
		storedInput = String.valueOf(result);

		textField.setText(initialVal);
		textField.getText();
		setCounter(0);
	}
	
	// +/- operation
	private void PlusMinusCalc() {
		// 원래 음수라면?! -> 양수로
		// 1번 연산을 거친 후
		if(operators.contains("=")) {
			if(storedInput.contains("-") ) {
				storedInput = storedInput.substring(1);
				initialVal = initialVal.substring(0, initialVal.length() - storedInput.length() - 1) + storedInput;
			}
			// 앞선 연산결과가 양수일 때 -> 음수로!
			else {
				storedInput = "(-" + storedInput;
				initialVal = initialVal.substring(0,  initialVal.length() - storedInput.length() + 2) + storedInput;
			}										
			result = result * -1;
			operators.clear();
			vals.clear();			
		}
		// 부호 바꾸기!
		// -면 +로!
		else if(operators.size() != 0) {
			if(storedInput.contains("(-")) {
				storedInput = storedInput.substring(2);
				initialVal = initialVal.substring(0, initialVal.length()-storedInput.length()-2) + storedInput;			
			}
			else if(operators.get(operators.size()-1).contains("-") && !storedInput.contains("(-")) {
				storedInput = "(-" + storedInput;
				initialVal = initialVal.substring(0, initialVal.length()-storedInput.length() + 1) + "-" + storedInput;
			}
			// 더하기 연산이었으면 -로 바꾼다.
			else if(operators.get(operators.size()-1).equals("+") && !storedInput.contains("(")) {
				storedInput = "(-" + storedInput;
				initialVal = initialVal.substring(0, initialVal.length() - storedInput.length()+1) + "+" + storedInput;
			}
			else if(operators.get(operators.size()-1).contains("*")||operators.get(operators.size()-1).contains("÷")) {
				// if storedInput >= 0
				if(!storedInput.contains("(-")) {
					storedInput = "(-" + storedInput;
					if(storedInput == "") {					
						initialVal = initialVal.substring(0, initialVal.length()) +  storedInput;
					}
					else {
						initialVal = initialVal.substring(0, initialVal.length() - storedInput.length() + 2) + storedInput;
					}
				}
				// if storedInput value < 0
				else {
					storedInput = storedInput.substring(1);
					initialVal = initialVal.substring(0, initialVal.length() - storedInput.length() - 2) + storedInput;
				}
		
			textField.setText(initialVal);
			textField.getText();
				
		}	
		}	
		// 연산을 한 번도 하지 않았을 때
		else if(operators.isEmpty()) {
			if(!storedInput.contains("-")) {	
				storedInput = "(-" + storedInput;			
				initialVal = storedInput;			
			}
			// 음수라면 
			else if(storedInput.contains("(-")) {
				storedInput = storedInput.substring(2);
				initialVal = storedInput;
			}
			else if(storedInput.contains("-") && !storedInput.contains("(")) {
				storedInput = storedInput.substring(1);
				initialVal = storedInput;
			}
		}
		textField.setText(initialVal);
		textField.getText();	
}
	// getter and setter
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
}// end class Calculator
