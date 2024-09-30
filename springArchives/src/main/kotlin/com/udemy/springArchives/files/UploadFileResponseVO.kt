package com.udemy.springArchives.files

import java.io.Serializable

data class UploadFileResponseVO (

    var fileName: String,
    var fileDownloadUri: String,
    var fileType: String,
    var size: Long,

)  : Serializable