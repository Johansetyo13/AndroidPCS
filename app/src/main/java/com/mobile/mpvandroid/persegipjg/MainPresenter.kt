package com.mobile.mpvandroid.persegipjg

class MainPresenter(private val mainView: MainView) {

    //persegi panjang
    fun hitungLuasPersegiPanjang(panjang: Float, lebar: Float) {

        if (panjang == 0F) {
            mainView.showError("tidak boleh 0")
            return
        }
        if (lebar == 0F) {
            mainView.showError("tidak boleh 0")
            return
        }
        val luas = panjang + lebar
        mainView.updateLuas(luas)
    }

}