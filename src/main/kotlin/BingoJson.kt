class BingoJson {
    var bingo_tips: MutableMap<String, BingoTip>? = null

    class BingoTip {
        var difficulty: String? = null
        var note: MutableList<String>? = null
    }
}