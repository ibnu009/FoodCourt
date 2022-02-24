package com.ibnu.foodcourt.ui.profile.add

import android.content.Context
import androidx.lifecycle.*
import com.ibnu.foodcourt.data.model.Category
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.repository.ProductRepository
import com.ibnu.foodcourt.utils.ConstVal
import com.ibnu.foodcourt.utils.PostStateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    var postState: PostStateHandler? = null

    fun getCategories(token: String): LiveData<ApiResponse<List<Category>>> {
        val result = MutableLiveData<ApiResponse<List<Category>>>()
        viewModelScope.launch {
            productRepository.getCategories(token).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun validateUploadProduct(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        standId: String,
        categoryId: String,
        productName: String,
        price: String,
        path: String,
        token: String
    ) {
        when {
            standId.isEmpty() || categoryId.isEmpty() -> {
                postState?.onFailure("Terjadi kegagalan")
            }
            productName.isEmpty() -> {
                postState?.onFailure("Nama produk tidak boleh kosong")
            }
            price.isEmpty() -> {
                postState?.onFailure("Harga tidak boleh kosong")
            }
            path.isEmpty() -> {
                postState?.onFailure("Pilih gambar produk terlebih dahulu")
            }
            else -> {
                uploadProduct(
                    context,
                    lifecycleOwner,
                    standId,
                    categoryId,
                    productName,
                    price,
                    path,
                    token
                )
            }
        }

    }

    private fun uploadProduct(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        standId: String,
        categoryId: String,
        productName: String,
        price: String,
        path: String,
        token: String
    ) {
        MultipartUploadRequest(context, ConstVal.URL_UPLOAD_PRODUCT)
            .setMethod("POST")
            .addHeader("Authorization", token)
            .addParameter("stand_id", standId)
            .addParameter("category_id", categoryId)
            .addParameter("product_name", productName)
            .addParameter("description", "WADAW")
            .addParameter("price", price.replace(",",""))
            .setMaxRetries(2)
            .addFileToUpload(path, "thumbnail", contentType = "image/*")
            .subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object :
                RequestObserverDelegate {
                override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                }

                override fun onCompletedWhileNotObserving() {
                }

                override fun onError(
                    context: Context,
                    uploadInfo: UploadInfo,
                    exception: Throwable
                ) {
                    exception.stackTrace
                    postState?.onFailure("Gagal upload product!")
                }

                override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                    Timber.d("product lagi proses")
                    postState?.onInitiating()
                }

                override fun onSuccess(
                    context: Context,
                    uploadInfo: UploadInfo,
                    serverResponse: ServerResponse
                ) {
                    Timber.d("product done terupload $uploadInfo,\n ${serverResponse.bodyString}")
                    postState?.onSuccess("Berhasil mengupload Product!")
                }
            })
    }

}