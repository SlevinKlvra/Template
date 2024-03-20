package com.intec.template.robot

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.actionbean.Pose
import com.ainirobot.coreservice.client.listener.ActionListener
import com.ainirobot.coreservice.client.listener.CommandListener
import com.ainirobot.coreservice.client.listener.Person
import com.ainirobot.coreservice.client.listener.TextListener
import com.ainirobot.coreservice.client.person.PersonApi
import com.ainirobot.coreservice.client.person.PersonListener
import com.intec.template.robot.data.Place
import com.intec.template.robot.listeners.NavigationListener
import com.intec.template.robot.listeners.SpeechRecognitionListener
import com.intec.template.ui.viewmodels.RobotViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RobotManager @Inject constructor(
    private val robotConnectionService: RobotConnectionService
){

    private var speechRecognitionListener: SpeechRecognitionListener? = null

    private val navigationListeners = mutableListOf<NavigationListener>()

    private lateinit var commandListener: CommandListener

    private lateinit var personListener: PersonListener

    private lateinit var actionListener: ActionListener

    private lateinit var placesListener: CommandListener

    private lateinit var headListener: CommandListener

    private lateinit var textListener: TextListener

    val placesList: MutableList<Place> = mutableListOf()

    private val _destinationsList = MutableLiveData(listOf<String>())

    var onPersonDetected: ((List<Person>?) -> Unit)? = null

    private val personApi = PersonApi.getInstance()

    init {
        // Registrar el actionListener con la API del robot
        setupActionListener()
        setupCommandListener()
        setupPersonListener()
        setupPlacesListener()
        setupHeadListener()
        setupTextListener()
        robotConnectionService.onRobotApiConnected = {
            getPlaceList()
        }
        robotConnectionService.connectToRobotApi()
    }

    fun addNavigationListener(listener: NavigationListener) {
        navigationListeners.add(listener)
    }

    fun removeNavigationListener(listener: NavigationListener) {
        navigationListeners.remove(listener)
    }

    fun unregisterPersonListener() {
        Log.d("RobotMan PersonListener", "Unregistering Person")
        personApi.unregisterPersonListener(personListener)
    }

    fun registerPersonListener() {
        Log.d("RobotMan PersonListener", "Registering Person")
        personApi.registerPersonListener(personListener)
    }

    fun detectPerson(faceId: Int): List<Person>? {
        startFocusFollow(faceId)
        return personApi?.allPersons
    }

    fun stopDetection() {
        stopFocusFollow()
        unregisterPersonListener()
    }

    private fun setupActionListener(){
        actionListener = object : ActionListener() {
            @Deprecated("Deprecated in Java")
            override fun onStatusUpdate(status: Int, data: String) {
                when (status) {
                    Definition.STATUS_NAVI_AVOID -> navigationListeners.forEach { it.onRouteBlocked() }
                    Definition.STATUS_NAVI_AVOID_END -> navigationListeners.forEach { it.onObstacleDisappeared() }
                    Definition.STATUS_START_NAVIGATION -> navigationListeners.forEach { it.onNavigationStarted() }
                    // Notificar otros estados a los listeners según sea necesario
                    Definition.STATUS_TRACK_TARGET_SUCCEED -> {Log.d("RobotManager", "Target tracking succeeded")}
                    Definition.STATUS_GUEST_APPEAR -> {Log.d("RobotManager", "Guest appeared")}
                    Definition.STATUS_GUEST_LOST -> {Log.d("RobotManager", "Guest lost")}
                    Definition.STATUS_GUEST_FARAWAY -> {Log.d("RobotManager", "Guest faraway")}
                    Definition.STATUS_TRACK_TARGET_SUCCEED -> {Log.d("RobotManager", "Target tracking succeeded")}
                }
            }
            @Deprecated("Deprecated in Java")
            override fun onError(errorCode: Int, errorString: String?) {
                // Maneja los errores aquí
                Log.e("RobotManager", "Error en el seguimiento: $errorString")
            }

            @Deprecated("Deprecated in Java")
            override fun onResult(status: Int, responseString: String?) {
                // Maneja el resultado aquíPerson
                Log.d("RobotManager", "Respuesta del seguimiento: $responseString")
            }
        }
    }

    private fun setupCommandListener(){
        commandListener = object : CommandListener() {
            override fun onResult(result: Int, message: String, extraData: String?){
                if ("succeed" == message) {
                    //success
                    Log.d("RobotManager", "Command succeeded")
                } else {
                    //failed
                    Log.d("RobotManager", "Command failed")
                }
            }

            override fun onStatusUpdate(status: Int, data: String?, extraData: String?) {
                super.onStatusUpdate(status, data, extraData)
                Log.d("RobotManager", "Command status update: $status, $data, $extraData")
            }
        }
    }

    private fun setupHeadListener(){
        headListener = object : CommandListener()
        {
            override fun onResult(result: Int, message: String) {
                try {
                    val json = JSONObject(message)
                    val status = json.getString("status")
                    if (Definition.CMD_STATUS_OK == status) {
                        //success
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: java.lang.NullPointerException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setupPlacesListener() {
        placesListener = object : CommandListener() {
            @Deprecated("Deprecated in Java")
            override fun onResult(result: Int, message: String) {
                try {
                    val jsonArray = JSONArray(message)
                    val newPlaces = mutableListOf<Place>()
                    val newDestinations = mutableListOf<String>()
                    val newPoses = mutableListOf<Pose>()

                    for (i in 0 until jsonArray.length()) {
                        val json = jsonArray.getJSONObject(i)
                        val x = json.getDouble("x")
                        val y = json.getDouble("y")
                        val theta = json.getDouble("theta")
                        val name = json.getString("name")
                        val ignoreDistance = false
                        val safedistance = 1
                        val pose = Pose(
                            x.toFloat(),
                            y.toFloat(),
                            theta.toFloat(),
                            name,
                            ignoreDistance,
                            safedistance
                        )
                        val place = Place(x, y, theta, name)
                        //Log.d("POSE", pose.toString())
                        //Log.d("PLACE", place.toString())

                        newPlaces.add(place)
                        newDestinations.add(name)
                        newPoses.add(pose)
                        Log.d("RobotManager PLACES", newPlaces.toString())
                    }

                    placesList.addAll(newPlaces)
                    _destinationsList.value = placesList.map { it.name }
                    Log.d("RobotManager PLACES", placesList.toString())
                } catch (e: JSONException) {
                    Log.e("ERROR", "Error parsing JSON", e)
                } catch (e: NullPointerException) {
                    Log.e("ERROR", "Null pointer exception", e)
                }
            }
        }
    }

    private fun setupPersonListener(){
        personListener = object : PersonListener() {
            override fun personChanged() {
                val personList = PersonApi.getInstance().allPersons
                onPersonDetected?.invoke(personList)
                Log.d("RobotMan PersonListener", "Person changed: $personList")
            }
        }
    }

    private fun setupTextListener(){
        textListener = object : TextListener() {
            override fun onStart() {
                // Manejar inicio de TTS
            }

            override fun onStop() {
                // Manejar fin de TTS
            }

            override fun onError() {
                // Manejar error de TTS
            }

            override fun onComplete() {
                // Manejar completitud de TTS
            }
        }
    }

    fun getPlaceList() : MutableLiveData<List<String>> {
        Log.d("RobotManager", "Getting place list")
        RobotApi.getInstance().getPlaceList(1, placesListener)
        return _destinationsList
    }

    fun startFocusFollow(faceId : Int){
        registerPersonListener()
        RobotApi.getInstance().startFocusFollow(0, faceId,10.toLong(), 100.toFloat(), actionListener)
    }

    fun stopFocusFollow() {
        // Código para detener el enfoque
        Log.d("stopFocusFollow", "Deteniendo seguimiento")
        unregisterPersonListener()
        RobotApi.getInstance().stopFocusFollow(0)
    }

    fun moveForward() {
        // Lógica para mover hacia adelante
        RobotApi.getInstance().goForward(0, 0.3f,1f,true, commandListener)
    }

    fun moveBackward() {
        // Lógica para mover hacia adelante
        RobotApi.getInstance().goBackward(0, 0.3f, commandListener)
    }

    fun moveLeft(){
        RobotApi.getInstance().turnLeft(0, 0.3f, commandListener)
    }

    fun moveRight(){
        RobotApi.getInstance().turnRight(0, 0.3f, commandListener)
    }

    fun moveHeadUp(){
        RobotApi.getInstance().moveHead(0, "absolute", "absolute", 50, 80, headListener)
    }

    fun moveHeadDown(){
        RobotApi.getInstance().moveHead(0, "absolute", "absolute", 50, 10, headListener)
    }

    fun resetHead(){
        RobotApi.getInstance().resetHead(0, headListener)
    }

    fun stopForward() {
        // Lógica para detener el movimiento hacia adelante
        RobotApi.getInstance().stopMove(1, commandListener)
    }

    fun setSpeechRecognitionListener(listener: SpeechRecognitionListener) {
        this.speechRecognitionListener = listener
    }

    // Llama a estos métodos cuando recibas resultados del reconocimiento de voz
    private fun notifySpeechPartialResult(result: String) {
        speechRecognitionListener?.onSpeechPartialResult(result)
    }

    private fun notifySpeechFinalResult(result: String) {
        speechRecognitionListener?.onSpeechFinalResult(result)
    }

    fun speak(text: String) {
        // Lógica para ejecutar TTS
        // Por ejemplo, usando una API de TTS proporcionada por el SDK del robot
        robotConnectionService.skillApi.playText(text, textListener)
    }

    fun goTo(destinyGoal: String){
        RobotApi.getInstance().startNavigation(0, destinyGoal,0.12345, 100000, actionListener)
    }
}