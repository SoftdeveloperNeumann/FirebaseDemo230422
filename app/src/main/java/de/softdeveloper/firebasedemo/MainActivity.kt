package de.softdeveloper.firebasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.softdeveloper.firebasedemo.databinding.ActivityMainBinding

data class User(val name:String, val first:String, val value:String){
    constructor():this("","","")
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var db = Firebase.database
    private lateinit var messageRef: DatabaseReference
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageRef = db.getReference("message")
        userRef = db.getReference("user")

        binding.btnSend.setOnClickListener {
            val message = binding.etInput.text.toString()
//            messageRef.setValue(message) // für einfache Werte

//            val map = hashMapOf(
//               "name"  to "Neumann",
//                "first" to "Frank",
//                "value" to message
//            )

//            messageRef.setValue(map)
//            messageRef.push().setValue(map)

            val user = User("Neumann","Frank",message)
            val userId = userRef.push().key
            userRef.child(userId!!).setValue(user)
//            userRef.child("Frank Neumann").setValue(user)
        }

        // Listener für einfache Werte
        messageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               val value = snapshot.value.toString()
                binding.tvOutput.text = value
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Fehler beim lesen der Daten", Toast.LENGTH_SHORT).show()
            }
        })

        // Listener für Childs
        userRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
               val user = snapshot.getValue(User::class.java)
                binding.tvOutput.text = "${user?.first} ${user?.name} schreibt ${user?.value}"
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                binding.tvOutput.text = "${user?.first} ${user?.name} ändert ${user?.value}"
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}