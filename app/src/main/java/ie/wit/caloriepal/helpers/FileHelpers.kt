package ie.wit.caloriepal.helpers

import android.content.Context
import android.util.Log
import java.io.*
import java.lang.Exception

fun read(context: Context, fileName:String) : String {
    var str =""
    try{
        val inputStream = context.openFileInput(fileName)
        if(inputStream != null){
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val partialStr = StringBuilder()
            var done = false
            while(!done){
                val line = bufferedReader.readLine()
                done = (line == null )
                if(line!=null) partialStr.append(line)
            }
            inputStream.close()
            str =partialStr.toString()
        }
    } catch (e: FileNotFoundException){
        Log.e("Error: ", "file not found $e")
    } catch (e: IOException){
        Log.e("Error: ", "could not read file $e")
    }
    return str
}

fun write(context: Context, fileName: String, data:String){
    try {
        val outPutStreamWriter =
            OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        outPutStreamWriter.write(data)
        outPutStreamWriter.close()
    } catch (e: Exception) {
        Log.e("Error: ", "Cannot read file: $e")
    }
}

fun exists(context: Context, fileName: String) : Boolean {
    val file = context.getFileStreamPath(fileName)
    return file.exists()
}