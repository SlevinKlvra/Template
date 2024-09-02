package com.intec.template.chatGPT

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ChatGPTManager(private val apiKey: String) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    fun fetchGPT3ChatSinonimo(prompt: String, onResponse: (String) -> Unit) {
        coroutineScope.launch {
            val url = URL("https://api.openai.com/v1/chat/completions")
            (url.openConnection() as? HttpURLConnection)?.run {
                try {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Authorization", "Bearer $apiKey")
                    doOutput = true

                    val body = """
                        {
                        "model": "gpt-4o",
                        "messages": [
                            {   
                                "role": "system",
                                "content": "te voy a pasar una lista de comandos, cuando recibas el prompt del usuario buscaras si el contexto es sinonimo del comando que hay en la lista y devuelveme el comando, si no lo encuentras devuelve no encontrado"
                            },
                            {
                                "role": "assistant",
                                "content": "lista de comandos: cita concertada, pedir cita"
                            },
                            {   
                                "role": "user",
                                "content": "$prompt devuelveme solo el comando de la lista"
                            }
                        ]
                    }
                    """.trimIndent()

                    outputStream.use { os ->
                        os.write(body.toByteArray())
                    }

                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Parse the JSON response to get only the ChatGPT's response text
                    val jsonResponse = JSONObject(response)
                    val choices = jsonResponse.getJSONArray("choices")
                    var chatGPTResponse = ""

                    if (choices.length() > 0) {
                        val firstChoice = choices.getJSONObject(0)
                        val message = firstChoice.getJSONObject("message")
                        chatGPTResponse = message.getString("content")
                    }

                    withContext(Dispatchers.Main) {
                        onResponse(chatGPTResponse)
                        Log.d("respuesta", chatGPTResponse)
                    }
                } catch (e: FileNotFoundException) {
                    val errorStream = errorStream.bufferedReader().use { it.readText() }
                    withContext(Dispatchers.Main) {
                        Log.d("Error al obtener respuesta:", errorStream)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Error al obtener respuesta:", e.toString())
                    }
                }
            }
        }
    }
    fun fetchGPT3ChatEmail(prompt: String, onResponse: (String) -> Unit) {
        coroutineScope.launch {
            val url = URL("https://api.openai.com/v1/chat/completions")
            (url.openConnection() as? HttpURLConnection)?.run {
                try {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Authorization", "Bearer $apiKey")
                    doOutput = true

                    val body = """
                        {
                        "model": "gpt-4o",
                        "messages": [
                            {   
                                "role": "system",
                                "content": "te va a llegar un string con un email que puede estar en un formato invalido, transformalo a un formato valido. solo responde con el formato valido del email"
                            },
                            {
                                "role": "assistant",
                                "content": "lista de comandos: reunion, mensajeria, baño "
                            },
                            {   
                                "role": "user",
                                "content": "$prompt"
                            }
                        ]
                    }
                    """.trimIndent()

                    outputStream.use { os ->
                        os.write(body.toByteArray())
                    }

                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Parse the JSON response to get only the ChatGPT's response text
                    val jsonResponse = JSONObject(response)
                    val choices = jsonResponse.getJSONArray("choices")
                    var chatGPTResponse = ""

                    if (choices.length() > 0) {
                        val firstChoice = choices.getJSONObject(0)
                        val message = firstChoice.getJSONObject("message")
                        chatGPTResponse = message.getString("content")
                    }

                    withContext(Dispatchers.Main) {
                        onResponse(chatGPTResponse)
                        Log.d("respuesta", chatGPTResponse)
                    }
                } catch (e: FileNotFoundException) {
                    val errorStream = errorStream.bufferedReader().use { it.readText() }
                    withContext(Dispatchers.Main) {
                        Log.d("Error al obtener respuesta:", errorStream)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Error al obtener respuesta:", e.toString())
                    }
                }
            }
        }
    }

    fun fetchGPT3Response(messages: List<Pair<String, String>>, onResponse: (String) -> Unit) {
        coroutineScope.launch {
            Log.d("CHATGPT", "Dentro de fetchGPT3Response")
            val url = URL("https://api.openai.com/v1/chat/completions")
            (url.openConnection() as? HttpURLConnection)?.apply {
                try {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Authorization", "Bearer $apiKey")
                    doOutput = true

                    // Construcción del cuerpo de la solicitud
                    val messageJsonArray = messages.joinToString(separator = ",") { (role, content) ->
                        """{"role": "$role", "content": "$content"}"""
                    }

                    val body = """
                        {
                            "model": "gpt-4",
                            "messages": [
                                {"role": "system", "content": "Eres un asistente útil y te llamas Paco. Responderás siempre con una sola frase. Podrás responder cualquier tipo de pregunta. Estás ubicado en Alicante. Responderás siempre con educación y amabilidad"},
                                $messageJsonArray
                            ]
                        }
                    """.trimIndent()

                    outputStream.use { it.write(body.toByteArray()) }

                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Parseo de la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val choices = jsonResponse.getJSONArray("choices")
                    val chatGPTResponse = if (choices.length() > 0) {
                        val firstChoice = choices.getJSONObject(0)
                        firstChoice.getJSONObject("message").getString("content")
                    } else {
                        "No response"
                    }

                    // Llamada al callback en el hilo principal
                    withContext(Dispatchers.Main) {
                        onResponse(chatGPTResponse)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Error al obtener respuesta:", e.toString())
                        onResponse("Error al obtener respuesta")
                    }
                } finally {
                    disconnect()
                }
            }
        }
    }


    //Método encargado de gestionar el chat escrito, similar a fetchGPT3Response, pero creado por mí para familiarizarme
    fun fetchGPT3ChatResponse(messages: List<Pair<String, String>>, idioma: String, onResponse: (String) -> Unit) {
        coroutineScope.launch {
            val url = URL("https://api.openai.com/v1/chat/completions")
            val connection = url.openConnection() as? HttpURLConnection
            connection?.run {
                try {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Authorization", "Bearer $apiKey")
                    doOutput = true

                    // Construcción del cuerpo de la solicitud
                    val messageJsonArray = messages.joinToString(separator = ",") { (role, content) ->
                        """{"role": "$role", "content": "$content"}"""
                    }

                    val body = """
                        {
                            "model": "gpt-4",
                            "messages": [
                                {   
                                    "role": "system",
                                    "content": "Eres un robot asistente cuya función es responder preguntas. Debes responder siempre de forma muy educada y contenta. Tu siempre responderas en el siguiente idioma $idioma"
                                },
                                $messageJsonArray
                            ]
                        }
                    """.trimIndent()

                    outputStream.use { it.write(body.toByteArray()) }
                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Parseo de la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val choices = jsonResponse.getJSONArray("choices")
                    val chatGPTResponse = if (choices.length() > 0) {
                        val firstChoice = choices.getJSONObject(0)
                        firstChoice.getJSONObject("message").getString("content")
                    } else {
                        "No response"
                    }

                    withContext(Dispatchers.Main) {
                        onResponse(chatGPTResponse)
                        Log.d("CHATBOT", "Respuesta de Paco: $chatGPTResponse")
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        Log.d("CHATBOT", "Error de Paco: ${e.message}")
                        onResponse("Error al obtener respuesta: ${e.message}")
                    }
                } finally {
                    connection?.disconnect()
                }
            } ?: withContext(Dispatchers.Main) {
                Log.d("CHATBOT", "No se pudo establecer la conexión.")
                onResponse("No se pudo establecer la conexión.")
            }
        }
    }








}