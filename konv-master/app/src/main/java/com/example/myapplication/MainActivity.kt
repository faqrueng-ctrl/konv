package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etAmount: EditText
    private lateinit var etRateUsd: EditText
    private lateinit var etRateEur: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var tvResult: TextView

    // Список доступных валют
    private val currencies = arrayOf("RUB", "USD", "EUR")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Привязка элементов интерфейса
        etAmount = findViewById(R.id.etAmount)
        etRateUsd = findViewById(R.id.etRateUsd)
        etRateEur = findViewById(R.id.etRateEur)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        tvResult = findViewById(R.id.tvResult)

        val btnConvert: Button = findViewById(R.id.btnConvert)

        // Настройка списков валют (Spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        btnConvert.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val amountText = etAmount.text.toString().trim()

        // Проверка: поле не должно быть пустым
        if (amountText.isEmpty()) {
            tvResult.text = "Ошибка: введите сумму"
            return
        }

        // Проверка: введено корректное число
        val amount = amountText.toDoubleOrNull()
        if (amount == null) {
            tvResult.text = "Ошибка: некорректное число"
            return
        }

        // Проверка: сумма не отрицательная
        if (amount < 0) {
            tvResult.text = "Ошибка: сумма не может быть отрицательной"
            return
        }

        val fromCurrency = spinnerFrom.selectedItem.toString()
        val toCurrency = spinnerTo.selectedItem.toString()

        // Если валюты совпадают - возвращаем исходную сумму
        if (fromCurrency == toCurrency) {
            tvResult.text = "Результат: $amount $toCurrency"
            return
        }

        // Получаем курсы валют, введённые пользователем
        val rateUsd = etRateUsd.text.toString().toDoubleOrNull()
        val rateEur = etRateEur.text.toString().toDoubleOrNull()

        if (rateUsd == null || rateEur == null || rateUsd <= 0 || rateEur <= 0) {
            tvResult.text = "Ошибка: некорректный курс валют"
            return
        }

        // Шаг 1: переводим исходную сумму в рубли
        val amountInRub = when (fromCurrency) {
            "USD" -> amount * rateUsd
            "EUR" -> amount * rateEur
            else -> amount // RUB
        }

        // Шаг 2: переводим из рублей в целевую валюту
        val result = when (toCurrency) {
            "USD" -> amountInRub / rateUsd
            "EUR" -> amountInRub / rateEur
            else -> amountInRub // RUB
        }

        tvResult.text = "Результат: %.2f %s".format(result, toCurrency)
    }
}
