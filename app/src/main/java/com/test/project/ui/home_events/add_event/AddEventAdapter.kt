package com.test.project.ui.home_events.add_event

class AddEventAdapter {
    lateinit var listener: OnButtonClickListener

    interface OnButtonClickListener {
        fun onButtonClick()
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.listener = listener
    }
}