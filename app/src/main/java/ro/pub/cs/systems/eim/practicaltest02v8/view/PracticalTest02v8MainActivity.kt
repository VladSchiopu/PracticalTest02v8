package ro.pub.cs.systems.eim.practicaltest02v8.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ro.pub.cs.systems.eim.practicaltest02v8.R
import ro.pub.cs.systems.eim.practicaltest02v8.network.ServerThread
import ro.pub.cs.systems.eim.practicaltest02v8.network.ClientThread
import ro.pub.cs.systems.eim.practicaltest02v8.general.Constants

class PracticalTest02v8MainActivity : AppCompatActivity() {
    private var serverThread: ServerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02v8_main)
        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // UI References
        val serverPortEt = findViewById<EditText>(R.id.server_port_edit_text)
        val startServerBtn = findViewById<Button>(R.id.start_server_button)

        val clientAddrEt = findViewById<EditText>(R.id.client_address_edit_text)
        val clientPortEt = findViewById<EditText>(R.id.client_port_edit_text)
        val URLEt = findViewById<EditText>(R.id.client_URL_edit_text)

        val init_cerere_Btn = findViewById<Button>(R.id.init_cerere_button)
        val resultTv = findViewById<TextView>(R.id.result_text_view)

        startServerBtn.setOnClickListener {
            val port = serverPortEt.text.toString().toIntOrNull()
            if (port != null) {
                serverThread = ServerThread(port)
                serverThread?.start()
                Toast.makeText(this, "Server Started", Toast.LENGTH_SHORT).show()
            }
        }

        init_cerere_Btn.setOnClickListener{
            val addr = clientAddrEt.text.toString()
            val portStr = clientPortEt.text.toString()
            val URL = URLEt.text.toString()

            if (addr.isNotEmpty() && portStr.isNotEmpty() && URL.isNotEmpty()) {
                val client = ClientThread(addr, portStr.toInt(), URL, resultTv)
                client.start()
            } else {
                Toast.makeText(this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked")

        // Folosim ?. pentru a evita eroarea de smart cast
        serverThread?.stopThread()

        super.onDestroy()
    }

}