package com.emilia.cookingcalculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.emilia.cookingcalculator.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val from = arrayOf(
            "Ml (millilitre)", "L (litre)", "Tsp (teaspoon)",
            "Tbs (tablespoon)", "Cup (cup)"
        );
        val to = arrayOf(
            "Ml (millilitre)", "L (litre)", "Tsp (teaspoon)",
            "Tbs (tablespoon)", "Cup (cup)"
        );

        val arrayAdapterFrom =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, from)
        val arrayAdapterTo = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, to)
        binding.from.adapter = arrayAdapterFrom
        binding.to.adapter = arrayAdapterTo

        var stringInFromSpinner = ""
        var stringInToSpinner = ""

        binding.from.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                stringInFromSpinner = from[pos]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        binding.to.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                stringInToSpinner = to[pos]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        binding.convert.setOnClickListener {
            calculateFromInto(
                stringInFromSpinner,
                stringInToSpinner
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateFromInto(stringInFromSpinner: String, stringInToSpinner: String) {
        val stringInTextField = binding.value.text.toString()
        val mainValue = stringInTextField.toDoubleOrNull()
        if (mainValue == null || mainValue == 0.0) {
            displayResult(0.0, stringInFromSpinner, stringInToSpinner)
            return
        }

        var valueMultiplier = 1.0
        if (stringInFromSpinner == "Ml (millilitre)") {
            valueMultiplier = when (stringInToSpinner) {
                "Ml (millilitre)" -> 1.0
                "L (litre)" -> 0.001
                "Tsp (teaspoon)" -> 0.2
                "Tbs (tablespoon)" -> 0.068
                "Cup (cup)" -> 0.004
                else -> 1.0
            }
        } else if (stringInFromSpinner == "L (litre)") {
            valueMultiplier = when (stringInToSpinner) {
                "Ml (millilitre)" -> 1000.0
                "L (litre)" -> 1.0
                "Tsp (teaspoon)" -> 202.9
                "Tbs (tablespoon)" -> 67.625
                "Cup (cup)" -> 4.2
                else -> 1.0
            }
        } else if (stringInFromSpinner == "Tsp (teaspoon)") {
            valueMultiplier = when (stringInToSpinner) {
                "Ml (millilitre)" -> 4.0
                "L (litre)" -> 0.005
                "Tsp (teaspoon)" -> 1.0
                "Tbs (tablespoon)" -> 0.3
                "Cup (cup)" -> 0.021
                else -> 1.0
            }
        } else if (stringInFromSpinner == "Tbs (tablespoon)") {
            valueMultiplier = when (stringInToSpinner) {
                "Ml (millilitre)" -> 14.787
                "L (litre)" -> 0.015
                "Tsp (teaspoon)" -> 3.0
                "Tbs (tablespoon)" -> 1.0
                "Cup (cup)" -> 0.063
                else -> 1.0
            }
        } else if (stringInFromSpinner == "Cup (cup)") {
            valueMultiplier = when (stringInToSpinner) {
                "Ml (millilitre)" -> 236.6
                "L (litre)" -> 0.237
                "Tsp (teaspoon)" -> 48.0
                "Tbs (tablespoon)" -> 16.0
                "Cup (cup)" -> 1.0
                else -> 1.0
            }
        }

        val result = valueMultiplier * mainValue
        binding.result.text = "{result}"

        // Display the formatted tip value on screen
        displayResult(result, stringInFromSpinner, stringInToSpinner)
    }

    @SuppressLint("StringFormatInvalid", "SetTextI18n")
    private fun displayResult(result: Double, From: String, To: String) {
        binding.fromInTo.text = "$From into $To"
        val formattedResult = NumberFormat.getNumberInstance().format(result)
        binding.result.text = getString(R.string.result, formattedResult)
    }
}