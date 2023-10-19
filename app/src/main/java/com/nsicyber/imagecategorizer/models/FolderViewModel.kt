package com.nsicyber.imagecategorizer.models


data class FolderViewModel(var title:String?=null,var imageList:List<String?>?=null,var isSelected:Boolean?=true)
data class ImageViewModel(var uri: String?=null,var isSelected:Boolean=false)
