package com.intec.template.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ainirobot.coreservice.client.actionbean.Pose
import com.ainirobot.coreservice.client.listener.Person
import com.intec.template.robot.RobotManager
import com.intec.template.robot.SkillApiService
import com.intec.template.robot.data.Place
import com.intec.template.robot.listeners.SpeechRecognitionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RobotViewModel @Inject constructor(
    private val robotManager : RobotManager,
    private val skillApiService: SkillApiService
) : AndroidViewModel(application = Application()), SpeechRecognitionListener {
    // ViewModel implementation

    //testestestestts

    //Variable para habilitar o deshabilitar el reconocimiento de voz
    private val _isListening = MutableLiveData(false)
    val isListening: LiveData<Boolean> = _isListening

    // Usando LiveData para recoger el speech recognition
    private val _recognizedText = MutableLiveData<String>()
    val recognizedText: LiveData<String> = _recognizedText

    private var hasHandledPersonDetection = false

    // Exponer los datos de MutableLiveData del RobotManager directamente o usar un LiveData que observe los cambios
    val destinationsList: LiveData<List<String>> = robotManager.getPlaceList()

    private val _speechText = MutableStateFlow("")
    val speechText = _speechText.asStateFlow()

    init{
        Log.d("RobotViewModel", "RobotViewModel Init")
        robotManager.setSpeechRecognitionListener(this)
        configurePersonDetection()

        skillApiService.partialSpeechResult.observeForever { speechResult ->
            viewModelScope.launch {
                _speechText.value = speechResult
            }
        }
    }

    private fun configurePersonDetection() {
        Log.d("RobotViewModel", "Configurar detección de personas")
        robotManager.onPersonDetected = { personList ->
            Log.d("RobotViewModel", "Person List: $personList")
            if (personList != null && !hasHandledPersonDetection) {
                handlePersonDetection(personList)
            }else {
                Log.d("RobotViewModel", "No person detected")
                startPersonDetection()
            }
        }
    }

    private fun startPersonDetection() {
        Log.d("RobotViewModel", "Iniciar detección de personas")
        val detectedPerson: List<Person>? = robotManager.detectPerson(0)
        Log.d("RobotViewModel", "Detected person: $detectedPerson")
    }

    private fun handlePersonDetection(personList: List<Any>) {
        if (personList.isNotEmpty() && !hasHandledPersonDetection) {
            Log.d("RobotViewModel", "Person detected: $personList")
            hasHandledPersonDetection = true
        }
    }

    fun comenzarSeguimiento(){
        Log.d("RobotViewModel", "Comenzar seguimiento")
        robotManager.registerPersonListener()
    }

    fun pararSeguimiento(){
        Log.d("RobotViewModel", "Comenzar seguimiento")
        robotManager.stopDetection()
    }

    fun irAdelante(){
        robotManager.moveForward()
    }

    fun parar(){
        robotManager.stopForward()
    }

    fun irAtras(){
        robotManager.moveBackward()
    }

    fun mirarArriba(){
        robotManager.moveHeadUp()
    }

    fun mirarAbajo(){
        robotManager.moveHeadDown()
    }

    fun irA(destino: String){
        robotManager.goTo(destino)
    }

    fun toggleListening() {
        val currentState = _isListening.value ?: false
        _isListening.value = !currentState
        if (_isListening.value == true) {
            // Iniciar escucha
        } else {
            // Detener escucha
            _recognizedText.value = "" // Opcional: limpiar el texto al detener
        }
    }

    // Método para actualizar el texto reconocido
    fun updateRecognizedText(text: String) {
        _recognizedText.postValue(text)
    }

    override fun onSpeechPartialResult(result: String) {
        _recognizedText.postValue(result)    }

    override fun onSpeechFinalResult(result: String) {
        _recognizedText.postValue(result)
    }

    fun speak(text: String) {
        robotManager.speak(text)
    }
}