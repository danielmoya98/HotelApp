package com.example.hotelapp

import com.example.hotelapp.dataclass.Producto
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class RegistrarProductoFragment : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    // Inicializar Supabase
    private val supabase = createSupabaseClient(
        supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
    ) {
        install(Postgrest)
        install(Storage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registrar_producto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre: TextInputEditText = view.findViewById(R.id.etNombreProducto)
        val etDescripcion: TextInputEditText = view.findViewById(R.id.etDescripcionProducto)
        val etPrecio: TextInputEditText = view.findViewById(R.id.etPrecioProducto)
        val etStock: TextInputEditText = view.findViewById(R.id.etStockProducto)
        val btnAddPhoto: MaterialButton = view.findViewById(R.id.btnAddPhotoProducto)
        val btnCreateProduct: MaterialButton = view.findViewById(R.id.btnCreateProduct)
        val imagePreview: CircleImageView = view.findViewById(R.id.imagePreviewProducto)

        // Abrir selector de imágenes
        btnAddPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnCreateProduct.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val precio = etPrecio.text.toString().toDoubleOrNull()
            val stock = etStock.text.toString().toIntOrNull()

            if (nombre.isEmpty() || descripcion.isEmpty() || precio == null || stock == null || imageUri == null) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, completa todos los campos y sube una foto",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                subirFotoYRegistrarProducto(nombre, descripcion, precio, stock)
            }
        }
    }

    private fun subirFotoYRegistrarProducto(
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bucket = supabase.storage.from("productos") // Acceso al bucket `productos`
                val fileName = "${UUID.randomUUID()}.jpg"

                // Convertir InputStream a ByteArray
                val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                val byteArray = inputStream?.readBytes() ?: throw Exception("Error al leer la imagen")

                // Subir imagen a Supabase Storage
                bucket.upload(fileName, byteArray)

                val imageUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co/storage/v1/object/public/productos/$fileName"

                // Crear un objeto com.example.hotelapp.dataclass.Producto
                val producto = Producto(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    stock = stock,
                    imagen_url = imageUrl,
                    activo = true,
                    categoria = "producto"
                )

                // Insertar producto en la base de datos
                supabase.postgrest["productos"].insert(producto)

                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "com.example.hotelapp.dataclass.Producto registrado exitosamente", Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                       Log.e("RegistrarProducto", "Error al registrar producto: ${e.message}", e)
                    }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            val imagePreview: CircleImageView? = view?.findViewById(R.id.imagePreviewProducto)
            imagePreview?.setImageURI(imageUri)
        }
    }
}
