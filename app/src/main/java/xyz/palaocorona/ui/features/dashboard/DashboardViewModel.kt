package xyz.palaocorona.ui.features.dashboard

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.palaocorona.base.ui.BaseViewModel
import xyz.palaocorona.data.dashboard.DashboardRepository
import xyz.palaocorona.util.SingleLiveEvent
import javax.inject.Inject

class DashboardViewModel @Inject constructor(val repository: DashboardRepository): BaseViewModel() {
    
    var sliderImages = MutableLiveData<MutableList<String>>()
    var isInternetAvailable = SingleLiveEvent<Boolean>()
    
    fun isUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }
    
    fun getSliderImages() {
        if(sliderImages.value == null) {
            val disposable = repository.getSliderImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.d(it)
                    sliderImages.value = it
                }, {
                    it.printStackTrace()
                })
            compositeDisposable.add(disposable)
        }
    }
    
    fun checkInternet(func: () -> Unit) {
        this.isInternetAvailable.value = repository.isInternetAvailable()
        if(isInternetAvailable.value == true) {
            func()
        }
    }
}