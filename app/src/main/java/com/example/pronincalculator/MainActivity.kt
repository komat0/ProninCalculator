package com.example.pronincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.pronincalculator.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    /*
    Для тестовой работы с Binding View. Вводим переменную с ссылкой на класс Binding, которая будет
    хранить в себе все ссылки из активити и через нее можно будет получать доступ, в том числе к
    ресурсам, для замены findViewById.
    */
    private lateinit var bindingClass: ActivityMainBinding

    private lateinit var calculatorScreenText: TextView
    private lateinit var actionButtons: List<Button>
    private lateinit var digitalButtons: List<Button>
    private lateinit var buttonDivision: Button
    private lateinit var buttonMulti: Button
    private lateinit var buttonPlus: Button
    private lateinit var buttonMinus: Button
    private lateinit var buttonDot: Button
    private lateinit var darkModeSwitcher: SwitchMaterial
    private var digitalCollector: String = ""
    private var digitalMemory: String = ""
    private var actionButtonSign: Char = ' '
    private var decimalClicked: Boolean = false
    private var textViewValueCopyByTap = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Использую LayoutInflater для работы BindingClass. По сути "наполнение". */
//        bindingClass = ActivityMainBinding.inflate(layoutInflater)
//
//        /* Для работы с доступом к ресурсам через BindingClass. */
//        setContentView(bindingClass.root)

        /* Для работы с классическим доступом к ресурсам. */
        setContentView(R.layout.activity_main)

        calculatorScreenText = findViewById(R.id.calculatorScreenText)
        buttonDivision = findViewById(R.id.buttonDivision)
        buttonMulti = findViewById(R.id.buttonMulti)
        buttonPlus = findViewById(R.id.buttonPlus)
        buttonMinus = findViewById(R.id.buttonMinus)
        buttonDot = findViewById(R.id.buttonDot)

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

        darkModeSwitcher = findViewById(R.id.darkModeSwitch)
    }

    fun onClickAnyButton(view: View) {
        val buttonText = (view as Button).text.toString()

        when {
            /*
            Проверка что было завершено вычисление. Если так, то чистка все сохраненных переменные
            */
            digitalButtons.contains(view) -> {
                if (actionButtonSign == '=' && digitalCollector == "0.") {
                    digitalMemory = ""
                    actionButtonSign = ' '
                    calculatorScreenText.text = digitalCollector
                } else if (actionButtonSign == '=') {
                    digitalMemory = ""
                    digitalCollector = ""
                    actionButtonSign = ' '
                    calculatorScreenText.text = ""
                }
                /* При нажатии циферных кнопок, собирает число из последовательных нажатий на цифры и
                отображает их на экране калькулятора. */
                digitalCollector += buttonText
                calculatorScreenText.text = digitalCollector
            }
            /* Обработка кнокок вычисления. Если кнопка нажата, записывает введенное число в
            digitalMemory и очищает digitalCollector для генерации второго числа.
            Добавляет знак математического действия в actionButtonSign для дальнейшего использования
            при нжатии на "=".
            Отменяет флаг использования точки.
            */
            actionButtons.contains(view) -> {
                digitalMemory = digitalCollector
                digitalCollector = ""
                actionButtonSign = view.text.single()
                decimalClicked = false
            }
            /* Обработка кнопки "=".
            В зависимости от знака в переменной actionButtonSign выполняются соответствующие действия.
            Вычисления записываются в result типа Double. */
            buttonText.contains("=") -> {
                var result = 0.0

                when (actionButtonSign) {
                    '+' -> {
                        result = digitalMemory.toDouble() + digitalCollector.toDouble()
                    }

                    '-' -> {
                        result = digitalMemory.toDouble() - digitalCollector.toDouble()
                    }

                    '×' -> {
                        result = digitalMemory.toDouble() * digitalCollector.toDouble()
                    }

                    '÷' -> {
                        val divisor = digitalCollector.toDouble()
                        result = if (divisor != 0.0) {
                            digitalMemory.toDouble() / divisor
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Не пытайтесь делить на 0.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            0.0
                        }
                    }
                }

                val formattedResult = if (isInteger(result)) {
                    result.toInt().toString()
                } else {
                    String.format("%.2f", result)
                }

                calculatorScreenText.text = formattedResult
                digitalCollector = formattedResult
                actionButtonSign = '='
                decimalClicked = false

            }
            /* Обработка нажатия на кнопку "."
            В случае нажатия, добавляет к числу и делает метку что было нажатие. Если было,
            не добавляет вторую.
            При нажатии первой, добавляет впереди "0". */
            view == buttonDot -> {
                if (!decimalClicked) {
                    if (actionButtonSign == '=') {
                        digitalMemory = ""
                        digitalCollector = ""
                        actionButtonSign = ' '
                        calculatorScreenText.text = ""
                    }

                    if (digitalCollector.isEmpty()) {
                        digitalCollector = "0."
                    } else {
                        digitalCollector += "."
                    }
                    calculatorScreenText.text = digitalCollector
                    decimalClicked = true
                }
            }
            /*
            Обработка нажатия на кнопку сброса.
            При нажатии обнуляются все переменные и сохраненные данные,
            а также сбрасывается флаг decimalClicked
            */
            buttonText.contains("C") -> {
                digitalMemory = ""
                digitalCollector = ""
                calculatorScreenText.text = "0"
                actionButtonSign = ' '
                decimalClicked = false
            }
        }
    }

    /*
    Функция проверки строки на дестяичность. Если можно привести
    к Integer - выдает True, если нет - False
    */
    private fun isInteger(number: Double): Boolean {
        return number % 1 == 0.0
    }

    /*
    Обработка нажатия на экран калькулятора. Данные с экрана сохраняются в переменную
    textViewValueCopyByTap и выводится Тост с сообщением что аднные скопированы.
    */
    fun onClickTextView(view: View) {
        textViewValueCopyByTap = calculatorScreenText.text.toString()
        Toast.makeText(
            applicationContext, "В память скопировано значение :$textViewValueCopyByTap",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun darkModeChannge(view: View) {
        darkModeSwitcher.setOnClickListener {
            if (darkModeSwitcher.isChecked) {
                Toast.makeText(
                    applicationContext,
                    "Ночной режим Активирован!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Ночной режим Деактивирован!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}