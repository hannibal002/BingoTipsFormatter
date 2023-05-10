import com.google.gson.GsonBuilder
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.UnsupportedFlavorException
import java.util.regex.Pattern

fun main(args: Array<String>) {

    try {
        val inputText = readFromClipboard() ?: return
        val list = inputText.split("\n").toList()
        val startPattern = Pattern.compile("- (?<name>.*): .* \\((?<difficulty>.*)\\)")


        val tips = mutableMapOf<String, BingoJson.BingoTip>()

        var currentTip: BingoJson.BingoTip? = null
        for (line in list) {
            if (line.isEmpty()) continue
            println("Parsing: '$line'")
            val matcher = startPattern.matcher(line)
            if (matcher.matches()) {
                val name = matcher.group("name")
                val difficulty = matcher.group("difficulty").lowercase()
                currentTip = BingoJson.BingoTip()
                currentTip.difficulty = difficulty
                currentTip.note = mutableListOf()
                println("Create new tips element!")
                tips[name] = currentTip
            } else {
                println("Add line!")
                currentTip!!.note!!.add(line)
            }
        }

        println("Trying to parse into json...")

        val bingoJson = BingoJson()
        bingoJson.bingo_tips = tips

        val gsonBuilder = GsonBuilder().setPrettyPrinting()
        val gson = gsonBuilder.create()
        val json = gson.toJson(bingoJson)

        println("Done & Copied!")
        copyToClipboard(json.toString())
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun copyToClipboard(text: String) {
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
}

fun readFromClipboard(): String? {
    val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard ?: return null
    try {
        val data = systemClipboard.getData(DataFlavor.stringFlavor) ?: return null
        return data.toString()
    } catch (e: UnsupportedFlavorException) {
        return null
    }
}