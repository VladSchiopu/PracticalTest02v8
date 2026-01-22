package ro.pub.cs.systems.eim.practicaltest02v8.network


import android.util.Log
import java.io.IOException
import java.net.ServerSocket
import ro.pub.cs.systems.eim.practicaltest02v8.general.Constants
import java.net.Socket
import ro.pub.cs.systems.eim.practicaltest02v8.general.HttpUtil
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class ServerThread(private val port: Int) : Thread() {
    private var serverSocket: ServerSocket? = null

    private var cacheData: String? = null


    fun getServerSocket(): ServerSocket? {
        return serverSocket
    }


    override fun run() {
        try {
            // Verificăm dacă firul nu a fost întrerupt
            while (!Thread.currentThread().isInterrupted) {
                Log.i("PracticalTest02", "[SERVER THREAD] Waiting for a client invocation...")

                // Acceptăm conexiunea de la client
                val socket: Socket? = serverSocket?.accept()
                Log.i("PracticalTest02", "[SERVER THREAD] A connection request was received from ${socket?.inetAddress}:${socket?.localPort}")

                if (socket != null) {
                    // Pornim un CommunicationThread pentru fiecare client
                    val communicationThread = CommunicationThread(this, socket)
                    communicationThread.start()
                }
            }
        } catch (ioException: IOException) {
            Log.e("PracticalTest02", "[SERVER THREAD] An exception has occurred: ${ioException.message}")
        }
    }


    init {
        try {
            this.serverSocket = ServerSocket(port)
        } catch (ioException: IOException) {
            Log.e("PracticalTest02", "An exception has occurred: ${ioException.message}")
        }
    }


    @Synchronized
    fun getData(
        url: String
    ): String? {

        Log.d("PracticalTest", "[Thread] Requesting: $url")

        val URL = URL(url)
        val `in` = BufferedReader(InputStreamReader(URL.openStream()))
        var input: String?
        val stringBuffer = StringBuffer()
        while ((`in`.readLine().also { input = it }) != null) {
            stringBuffer.append(input)
        }
        `in`.close()

        val htmlData = stringBuffer.toString()

        // 3a) Afișare răspuns complet în LogCat
        Log.d("PracticalTest", "[Thread] Response: $htmlData")

        return htmlData
    }

    fun stopThread() {
        interrupt()
        if (serverSocket != null) {
            try {
                serverSocket!!.close()
            } catch (ioException: IOException) {
                Log.e(
                    Constants.TAG,
                    "[SERVER THREAD] An exception has occurred: " + ioException.message
                )
                if (Constants.DEBUG) {
                    ioException.printStackTrace()
                }
            }
        }
    }


}