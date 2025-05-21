package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalculatorActivity : AppCompatActivity() {
    private lateinit var bGoToMainActivity : Button

    private lateinit var num: TextView
    private lateinit var result: TextView

    private lateinit var zero: Button
    private lateinit var one: Button
    private lateinit var two: Button
    private lateinit var three: Button
    private lateinit var four: Button
    private lateinit var five: Button
    private lateinit var six: Button
    private lateinit var seven: Button
    private lateinit var eight: Button
    private lateinit var nine: Button

    private lateinit var add: Button
    private lateinit var sub: Button
    private lateinit var mult: Button
    private lateinit var div: Button
    private lateinit var equal: Button

    private var delResult=false
    private var displayNum = "0"
    private var firstNum: Double? = null
    private var currentOperator: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bGoToMainActivity = findViewById(R.id.go_to_main_activity)
        bGoToMainActivity.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        num = findViewById(R.id.input)
        result = findViewById(R.id.output)

        zero = findViewById(R.id.btn0)
        one = findViewById(R.id.btn1)
        two = findViewById(R.id.btn2)
        three = findViewById(R.id.btn3)
        four = findViewById(R.id.btn4)
        five = findViewById(R.id.btn5)
        six = findViewById(R.id.btn6)
        seven = findViewById(R.id.btn7)
        eight = findViewById(R.id.btn8)
        nine = findViewById(R.id.btn9)

        add = findViewById(R.id.add)
        sub = findViewById(R.id.sub)
        mult = findViewById(R.id.mult)
        div = findViewById(R.id.div)
        equal = findViewById(R.id.equal)

        zero.setOnClickListener { appendNum("0") }
        one.setOnClickListener { appendNum("1") }
        two.setOnClickListener { appendNum("2") }
        three.setOnClickListener { appendNum("3") }
        four.setOnClickListener { appendNum("4") }
        five.setOnClickListener { appendNum("5") }
        six.setOnClickListener { appendNum("6") }
        seven.setOnClickListener { appendNum("7") }
        eight.setOnClickListener { appendNum("8") }
        nine.setOnClickListener { appendNum("9") }

        add.setOnClickListener { setOperator("+") }
        sub.setOnClickListener { setOperator("-") }
        mult.setOnClickListener { setOperator("*") }
        div.setOnClickListener { setOperator("/") }
        equal.setOnClickListener { calculateResult() }
    }

    private fun appendNum(number: String) {
        if (delResult || displayNum == "0") {
            displayNum = number
            delResult = false
        } else {
            displayNum += number
        }
        updDisplay()
    }

    private fun setOperator(operator: String) {
        if (displayNum.isNotEmpty()) {
            firstNum = displayNum.toDouble()
            currentOperator = operator
            delResult = true
            updDisplay()
        }
    }

    private fun calculateResult() {
        if (firstNum != null && currentOperator != null && !delResult) {
            val secondNum = displayNum.toDouble()
            val res = when (currentOperator) {
                "+" -> firstNum!! + secondNum
                "-" -> firstNum!! - secondNum
                "*" -> firstNum!! * secondNum
                "/" -> if (secondNum != 0.0) firstNum!! / secondNum else Double.NaN
                else -> Double.NaN
            }
            result.text = res.toString()
            displayNum = "0"
            delResult = true
            updDisplay()
        }
    }

    private fun updDisplay() {
        num.text = displayNum
        if (delResult && firstNum == null) {
            result.text = "0"
        }
    }
}