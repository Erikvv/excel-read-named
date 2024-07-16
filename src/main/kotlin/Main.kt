package com.zenmo

import org.apache.logging.log4j.LogManager
import org.apache.poi.ss.util.AreaReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.FormulaEvaluator

fun main() {
//    val logger = LogManager.getLogger();
//    logger.info("Hello, world!")

    val classloader = Thread.currentThread().contextClassLoader
    val inputStream = classloader.getResourceAsStream("Dealnr.bedrijfsnaam.data.xlsx")

    val workbook = XSSFWorkbook(inputStream)

    workbook.allNames.forEach {
        println(it.nameName)
    }

    val companyNameName = workbook.getName("companyName")

    val areaReference = AreaReference(companyNameName.refersToFormula, workbook.spreadsheetVersion)

    val cellReference = areaReference.firstCell
    val sheet = workbook.getSheet(cellReference.sheetName)
    val row = sheet.getRow(cellReference.row)
    val cell = row.getCell(cellReference.col.toInt())

    val cellValue: String = when (cell.cellType) {
        CellType.STRING -> cell.stringCellValue
        CellType.NUMERIC -> cell.numericCellValue.toString()
        CellType.BOOLEAN -> cell.booleanCellValue.toString()
        CellType.FORMULA -> {
            val evaluator: FormulaEvaluator = workbook.creationHelper.createFormulaEvaluator()
            val cellValue = evaluator.evaluate(cell)
            cellValue.formatAsString()
        }
        else -> ""
    }

    println("companyName: $cellValue")

    val quarterHourlyValuesName = workbook.getName("electricityConsumptionQuarterHourlyValues")
    val ref = AreaReference(quarterHourlyValuesName.refersToFormula, workbook.spreadsheetVersion)
    val numCols = ref.lastCell.col - ref.firstCell.col + 1
    if (numCols != 2) {
        throw IllegalArgumentException("Number of columns in electricityConsumptionQuarterHourlyValues should be 2")
    }

    val numRows = ref.lastCell.row - ref.firstCell.row + 1
    println("numRows: $numRows")


    println("first cell: ${ref.firstCell}")
    val firstCell = workbook.getSheet(ref.firstCell.sheetName).getRow(ref.firstCell.row).getCell(ref.firstCell.col.toInt())
    println("first cell value: ${firstCell.dateCellValue}")
    val secondCell = workbook.getSheet(ref.firstCell.sheetName).getRow(ref.firstCell.row).getCell(ref.firstCell.col + 1)
    println("second cell value: ${secondCell.numericCellValue}")

    val summerCell = workbook.getSheet(ref.firstCell.sheetName).getRow(13_000).getCell(ref.firstCell.col.toInt())
    println("summer cell value: ${summerCell.dateCellValue}")

    for (i in 0 until 20) {
        val cell = workbook.getSheet(ref.firstCell.sheetName).getRow(8660 - (24 * 6 * 4) + i).getCell(ref.firstCell.col.toInt())
        println("cell value: ${cell.dateCellValue}")
    }

    // print columns of table vehicles
    workbook.getTable("vehicles").columns.forEach {
        println(it.name)
    }

}
