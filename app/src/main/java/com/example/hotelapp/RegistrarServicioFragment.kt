package com.example.hotelapp

import com.example.hotelapp.dataclass.Servicio
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

class RegistrarServicioFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_registrar_servicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre: TextInputEditText = view.findViewById(R.id.etNombreServicio)
        val etDescripcion: TextInputEditText = view.findViewById(R.id.etDescripcionServicio)
        val etPrecio: TextInputEditText = view.findViewById(R.id.etPrecioServicio)
        val btnAddPhoto: MaterialButton = view.findViewById(R.id.btnAddPhotoServicio)
        val btnCreateService: MaterialButton = view.findViewById(R.id.btnCreateService)
        val imagePreview: CircleImageView = view.findViewById(R.id.imagePreviewServicio)

        // Abrir selector de imágenes
        btnAddPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnCreateService.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val precio = etPrecio.text.toString().toDoubleOrNull()
            if (nombre.isEmpty() || descripcion.isEmpty() || precio == null || imageUri == null) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos y sube una foto", Toast.LENGTH_SHORT).show()
            } else {
                subirFotoYRegistrarServicio(nombre, descripcion, precio)
            }
        }
    }

    private fun subirFotoYRegistrarServicio(
        nombre: String,
        descripcion: String,
        precio: Double,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bucket = supabase.storage.from("servicios") // Acceso al bucket `servicios`
                val fileName = "${UUID.randomUUID()}.jpg"

                // Convertir InputStream a ByteArray
                val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                val byteArray = inputStream?.readBytes() ?: throw Exception("Error al leer la imagen")

                // Subir imagen a Supabase Storage
                bucket.upload(fileName, byteArray)

                val imageUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co/storage/v1/object/public/servicios/$fileName"

                // Crear un objeto com.example.hotelapp.dataclass.Servicio
                val servicio = Servicio(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    disponibilidad = true,
                    imagen_url = imageUrl,
                    categoria = "servicio"
                )

                // Insertar servicio en la base de datos
                supabase.postgrest["servicios"].insert(servicio)

                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "com.example.hotelapp.dataclass.Servicio registrado exitosamente", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed() // Navegar hacia atrás después del registro
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            val imagePreview: CircleImageView? = view?.findViewById(R.id.imagePreviewServicio)
            imagePreview?.setImageURI(imageUri)
        }
    }
}
