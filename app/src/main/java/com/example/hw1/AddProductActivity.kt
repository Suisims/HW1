package com.example.hw1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.doAfterTextChanged

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val spinner: Spinner = findViewById(R.id.spinner)
        val minus: Button = findViewById(R.id.plus)
        val plus: Button = findViewById(R.id.minus)
        val order: Button = findViewById(R.id.order)

        // cancel activity
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // count
        val countInput = findViewById<EditText>(R.id.count_input)
        var intCount = 0

        minus.setOnClickListener {
            if (intCount > 0) {
                intCount -= 1
            }
            countInput.setText(intCount.toString())
        }

        plus.setOnClickListener {
            intCount += 1
            countInput.setText(intCount.toString())
        }

        countInput.doAfterTextChanged {
            if (countInput.text.toString().toInt() >= 0) {
                intCount = countInput.text.toString().toInt()
            }
        }

        // order button
        order.setOnClickListener {
            if (intCount == 0) {
                Toast.makeText(this, "Нельзя заказать 0 товара!", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("name", spinner.selectedItem.toString())
                intent.putExtra("count", intCount)
                setResult(1, intent)
                finish()
            }
        }


        val images = mapOf(0 to R.drawable.cola, 1 to R.drawable.water, 2 to R.drawable.juice)

        // get products
        val products = resources.getStringArray(R.array.products)

        // adapter for spinner
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, products)
        spinner.adapter = adapter

        // set image
        findViewById<ImageView>(R.id.image)
                .setImageDrawable(images[0]?.let { resources.getDrawable(it) })

        // item change listener
        spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {


            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                val image: ImageView = findViewById(R.id.image)
                image.setImageDrawable(images[position]?.let { resources.getDrawable(it) })
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}