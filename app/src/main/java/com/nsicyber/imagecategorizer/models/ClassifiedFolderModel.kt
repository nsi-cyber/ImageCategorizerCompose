package com.nsicyber.imagecategorizer.models

data class ClassifiedFolderModel (var title:String?=null,var images:List<String>? = listOf(),var isSelected:Boolean=false)
data class ClassifiedFolderList(var list:List<ClassifiedFolderModel?>?)