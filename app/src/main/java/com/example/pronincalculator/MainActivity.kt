package com.example.pronincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var calculatorScreenText: EditText
    private lateinit var actionButtons: List<Button>
    private lateinit var digitalButtons: List<Button>
    private lateinit var buttonDivision: Button
    private lateinit var buttonMulti: Button
    private lateinit var buttonPlus: Button
    private lateinit var buttonMinus: Button
    private var digitalCollector: String = ""
    private var digitalMemory: String = ""
    private var actionButtonSign: Char = ' '

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculatorScreenText = findViewById(R.id.calculatorScreenText)
        buttonDivision = findViewById(R.id.buttonDivision)
        buttonMulti = findViewById(R.id.buttonMulti)
        buttonPlus = findViewById(R.id.buttonPlus)
        buttonMinus = findViewById(R.id.buttonMinus)

        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)

        digitalButtons = listOf(
            button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9
        )
        actionButtons = listOf(
            buttonDivision, buttonMulti, buttonPlus, buttonMinus
        )
    }

    fun isIntegerOrDecimal(str: String): Boolean {
        return str.toIntOrNull() != null
    }

    fun onClickAnyButton(view: View) {
        val buttonText = (view as Button).text.toString()

        when {
            digitalButtons.contains(view) -> {
                digitalCollector += buttonText
                calculatorScreenText.setText(digitalCollector)
            }

            actionButtons.contains(view) -> {
                digitalMemory = digitalCollector
                digitalCollector = ""
                actionButtonSign = view.text.single()
            }

            buttonText.contains("=") -> {
                var result: String = ""

                when (actionButtonSign) {

                    '+' -> {
                        result = (digitalMemory.toInt() + digitalCollector.toInt()).toString()
                        calculatorScreenText.setText(result)
                        digitalCollector = calculatorScreenText.toString()
                    }

                    '-' -> {
                        result = (digitalMemory.toInt() - digitalCollector.toInt()).toString()
                        calculatorScreenText.setText(result)
                        digitalCollector = calculatorScreenText.toString()
                    }

                    '×' -> {
                        result = (digitalMemory.toInt() * digitalCollector.toInt()).toString()
                        calculatorScreenText.setText(result)
                        digitalCollector = calculatorScreenText.toString()
                    }

                    '÷' -> {
                        result = (digitalMemory.toInt() / digitalCollector.toInt()).toString()
                        calculatorScreenText.setText(result)
                        digitalCollector = calculatorScreenText.toString()
                    }
                }
            }

            buttonText.contains("C") -> {
                digitalMemory = ""
                digitalCollector = ""
                calculatorScreenText.setText("0")
                actionButtonSign = ' '
            }
        }
    }
}