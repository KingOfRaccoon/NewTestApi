package com.example.newtestapi

import android.util.Log
import com.company.didi.model.DiDiDriverWithPhoto
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object ManageDataGoogleSheets {
    private const val id = "1wEyGVpV67bRa2LX1-_0RVzFWgY6Iemd2uqP7xnM4KdQ"
    fun createSpreadsheet(service: Sheets) {
        var spreadsheet = Spreadsheet()
                .setProperties(
                        SpreadsheetProperties()
                                .setTitle("CreateNewSpreadsheet")
                )
        MainScope().launch(Dispatchers.IO) {
            spreadsheet = service.spreadsheets().create(spreadsheet).execute()
        }
    }

    fun writeData(service: Sheets, message: DiDiDriverWithPhoto){
        MainScope().launch(Dispatchers.IO) {
            val coloums = listOf("ID", "Телефон", "Город", "Имя")
            val data = listOf(message.requestNumber.toString(), message.phone, message.city, message.name)
            val value : ValueRange = if (isSheetEmpty(service, id, "Лист1"))
                ValueRange().setValues(mutableListOf(coloums, data) as List<MutableList<Any>>?)
            else
                ValueRange().setValues(mutableListOf(data) as List<MutableList<Any>>?)
            service.spreadsheets().values().append(id, "Лист1!A1:Z1", value)
                    .setValueInputOption("RAW").execute()
        }
    }
    fun readData(service: Sheets){
        MainScope().launch(Dispatchers.IO) {
            val data = service.spreadsheets().values().get(id, "Лист1!A1:B2").execute()
            Log.e("data", data.toString())
        }
    }

    fun addData(service: Sheets, message: Message){
        MainScope().launch(Dispatchers.IO) {
            val size = service.spreadsheets().values().batchGet(id).size

        }
    }

    fun convertFromDataClass(any: Any, isHaveData: Boolean = true): ValueRange {
        val mes = any.toString()
//    val message = "Message(sender=Кто, text=Я, recipient=Кто я)"
//    val mes = "Trainer(second_name=Звездаков, date=23-10-2003, sex=Мужской, groupID=CuuQJ30TctLCxENOv0hs, type=trainer, img=https://firebasestorage.googleapis.com/v0/b/balamut-4af92.appspot.com/o/images%2F86?alt=media&token=1e599eba-6d8e-47f8-9073-90fa41e3d3f7, first_name=Александрдрдрдр)"

        var str = ""
        var first = 0
        for (it in mes.indices) {
            if (mes[it] == '(') {
                first = it
                break
            }
        }
        for (m in first+1 until mes.lastIndex) {
            str += mes[m]
        }
        val list = str.split(',')
        val fixedList = mutableListOf<String>()
        val listName = mutableListOf<String>()
        list.forEach {
            val array = it.split('=')
            listName.add(array[0])
            fixedList.add(array[1])
        }
        println(listName)
        println(fixedList)
        if (isHaveData)
            return ValueRange().setValues(mutableListOf(fixedList) as List<MutableList<Any>>?)
        else
            return ValueRange().setValues(mutableListOf(listName, fixedList) as List<MutableList<Any>>?)
    }

    fun convertFromJson(string: String): ValueRange? {
//    val mes ="{second_name=Звездаков, date=23-10-2003, sex=Мужской, groupID=CuuQJ30TctLCxENOv0hs, type=trainer, img=https://firebasestorage.googleapis.com/v0/b/balamut-4af92.appspot.com/o/images%2F86?alt=media&token=1e599eba-6d8e-47f8-9073-90fa41e3d3f7, first_name=Александрдрдрдр}"
        val mes = string
        var str = ""
        var first = 0
        for (it in mes.indices) {
            if (mes[it] == '{') {
                first = it
                break
            }
        }
        for (m in first+1 until mes.lastIndex) {
            str += mes[m]
        }
        val list = str.split(',')
        val listName = mutableListOf<String>()
        val fixedList = mutableListOf<String>()
        list.forEach {
            val array = it.split('=')
            listName.add(array[0])
            fixedList.add(array[1])
        }
        println(listName)
        println(fixedList)
        return ValueRange().setValues(mutableListOf(listName, fixedList) as List<MutableList<Any>>?)
    }

    private fun isSheetEmpty(service: Sheets, spreadsheetId: String, sheetName: String): Boolean {
        val result = service.spreadsheets().values().get(spreadsheetId, "'$sheetName'").execute()
        return result.getValues() == null || result.getValues().size == 0
    }
}