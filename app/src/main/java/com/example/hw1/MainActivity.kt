package com.example.hw1

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private val products = mutableMapOf<String, Int>()
    var productsAdapter: ProductsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        findViewById<Button>(R.id.add_product).setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivityForResult(intent, 1);
        }
        val rv: RecyclerView = findViewById(R.id.products)
        productsAdapter = ProductsAdapter(products, this)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = productsAdapter

        findViewById<Button>(R.id.final_order).setOnClickListener {
            if (products.isEmpty()) {
                Toast.makeText(this, "Нельзя заказать 0 товаров!", Toast.LENGTH_LONG).show()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")

                builder.setMessage("Order description")
                builder.setPositiveButton(
                        "Agree") { _, _ ->
                    val builder2 = AlertDialog.Builder(this)
                    builder2.setTitle("Succesful!")

                    builder2.setMessage("Order description")
                    builder2.setPositiveButton(
                            "Agree") { _, _ ->
                        Toast.makeText(this, "Успешно!", Toast.LENGTH_LONG).show()
                        products.clear()
                        productsAdapter!!.notifyDataSetChanged()
                    }
                    builder2.show()
                }
                builder.setNegativeButton(
                        "Disagree") { _, _ ->
                }
                builder.show()
            }
        }
    }

    class ProductsAdapter(private val dataSet: MutableMap<String, Int>, private val context: Context) :
            RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var name: TextView? = itemView.findViewById(R.id.name)
            var count: TextView? = itemView.findViewById(R.id.count)
            var image: ImageView? = itemView.findViewById(R.id.image)
            var delete: Button = itemView.findViewById(R.id.delete)

            var layout: LinearLayout? = itemView.findViewById(R.id.item_layout)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.text_row_item, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val images = mapOf("Cola" to R.drawable.cola, "Water" to R.drawable.water, "Juice" to R.drawable.juice)
            if (dataSet[dataSet.keys.elementAt(position)] == 0) return
            if (dataSet[dataSet.keys.elementAt(position)] != 0) {
                viewHolder.name?.text  = dataSet.keys.elementAt(position)
                viewHolder.count?.text = dataSet[dataSet.keys.elementAt(position)].toString()
                viewHolder.image?.setImageDrawable(images[dataSet.keys.elementAt(position)]?.let { context.resources.getDrawable(it) })
                viewHolder.delete.setOnClickListener {
                    dataSet.remove(dataSet.keys.elementAt(position))
                    notifyDataSetChanged()
                }
            }
        }

        override fun getItemCount() = dataSet.size

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val count = data?.extras?.get("count")
        val name = data?.extras?.get("name")
        if (count != null && name != null) {
            products[name.toString()] = count as Int
            Log.e("data", products.toString())
            productsAdapter?.notifyDataSetChanged()
        }
    }
}