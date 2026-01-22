package ro.pub.cs.systems.eim.practicaltest02v8.network


import android.util.Log
import org.json.JSONException
import ro.pub.cs.systems.eim.practicaltest02v8.general.Constants
import ro.pub.cs.systems.eim.practicaltest02v8.general.Utilities
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket
import java.net.URL


class CommunicationThread(private val serverThread: ServerThread, private val socket: Socket?) : Thread() {

    override fun run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!")
            return
        }

        try {
            // Pas 1: Obținere BufferedReader și PrintWriter din Utilities
            val bufferedReader = Utilities.getReader(socket)
            val printWriter = Utilities.getWriter(socket)

            if (bufferedReader == null || printWriter == null) {
                Log.e(
                    Constants.TAG,
                    "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!"
                )
                return
            }

            Log.i(
                Constants.TAG,
                "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!"
            )

            val URL = bufferedReader.readLine()

            Log.d("App", "[COMM] Received: $URL")

            if (URL != null) {
                if(URL.contains("bad"))
                {
                    printWriter.println("URL blocked by firewall")
                }
                else{

                    val responseData = serverThread.getData(URL)

                    Log.d("PracticalTest", "[Thread] Definition found: $responseData")
                    printWriter.println("$responseData")
                }
            } else {
                printWriter.println("Error: Invalid parameters")
            }

            //rest of code


        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: ${ioException.message}")
            if (Constants.DEBUG) ioException.printStackTrace()
        } catch (jsonException: JSONException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: ${jsonException.message}")
            if (Constants.DEBUG) jsonException.printStackTrace()
        } finally {
            // Pas 9: Eliberare resurse
            try {
                socket.close()
            } catch (ioException: IOException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error closing socket: ${ioException.message}")
            }
        }

    }
}
