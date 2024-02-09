package com.intec.template.robot

import android.content.Context
import com.ainirobot.coreservice.client.ApiListener
import com.ainirobot.coreservice.client.speech.SkillApi
import com.ainirobot.coreservice.client.speech.SkillCallback
import javax.inject.Inject

class SkillApiService @Inject constructor(
    private val context: Context
) {
    private var skillApi: SkillApi = SkillApi()

    private val apiListener = object : ApiListener {
        override fun handleApiDisabled() {
            // Manejar API deshabilitada
        }

        override fun handleApiConnected() {
            // Conexión exitosa, registrar callback
            skillApi.registerCallBack(mSkillCallback)
        }

        override fun handleApiDisconnected() {
            // Manejar desconexión
        }
    }

    init {
        connectApi()
    }

    private fun connectApi() {
        skillApi.connectApi(context, apiListener)
    }

    private val mSkillCallback = object : SkillCallback() {

        override fun onSpeechParResult(text: String?) {
            //TO-DO("Not yet implemented")
        }

        override fun onStart() {
            //TO-DO("Not yet implemented")
        }

        override fun onStop() {
            //TO-DO("Not yet implemented")
        }

        override fun onVolumeChange(volume: Int) {
            //TO-DO("Not yet implemented")
        }

        override fun onQueryEnded(queryEndStatus: Int) {
            //TO-DO("Not yet implemented")
        }
    }

    // Métodos adicionales según sea necesario...
}