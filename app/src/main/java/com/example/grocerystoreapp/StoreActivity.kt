package com.example.grocerystoreapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystoreapp.databinding.ActivityStoreBinding

data class Product(val name: String, val price: String, val imageUri: String?)

class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreBinding
    private val productList = mutableListOf<Product>()
    private val adapter = ProductAdapter(productList)
    private var selectedImageUri: String? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка RecyclerView
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter

        // Выбор изображения из галереи
        binding.imageViewProduct.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Добавление продукта в список
        binding.buttonAddProduct.setOnClickListener {
            val productName = binding.editTextProductName.text.toString()
            val productPrice = binding.editTextProductPrice.text.toString()

            if (productName.isNotEmpty() && productPrice.isNotEmpty()) {
                productList.add(Product(productName, productPrice, selectedImageUri))
                adapter.notifyDataSetChanged()
                binding.editTextProductName.text.clear()
                binding.editTextProductPrice.text.clear()
                selectedImageUri = null
                binding.imageViewProduct.setImageResource(R.drawable.placeholder_image)
            } else {
                Toast.makeText(this, "Введите название и цену продукта", Toast.LENGTH_SHORT).show()
            }
        }

        // Выход из приложения
        binding.menuExit.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data.toString()
            binding.imageViewProduct.setImageURI(data?.data)
        }
    }
}
