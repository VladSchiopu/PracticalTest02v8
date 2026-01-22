package ro.pub.cs.systems.eim.practicaltest02v8.network

import android.util.Log
import android.widget.TextView
import ro.pub.cs.systems.eim.practicaltest02v8.general.Constants
import ro.pub.cs.systems.eim.practicaltest02v8.general.Utilities
import java.io.BufferedReader
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class ClientThread(
    private val address: String,
    private val port: Int,
    private val URL: String,
    private val resultTextView: TextView
) : Thread() {

    private var socket: Socket? = null

    override fun run() {
        try {
            // Deschiderea canalului de comunicație
            val socket = Socket(address, port)

            val bufferedReader = Utilities.getReader(socket!!)
            val printWriter = Utilities.getWriter(socket!!)

            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!")
                return
            }

            printWriter.println(URL)

            val result = bufferedReader.readLine()

            resultTextView.post {
                resultTextView.text = "Rezultat: $result"
            }

            socket.close()


        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: ${ioException.message}")
            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        } finally {
            if (socket != null) {
                try {
                    socket!!.close()
                } catch (ioException: IOException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: ${ioException.message}")
                }
            }
        }
    }
}