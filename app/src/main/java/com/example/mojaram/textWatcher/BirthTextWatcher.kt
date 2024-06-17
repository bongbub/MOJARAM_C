import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class BirthTextWatcher(private val editText: EditText) : TextWatcher {
    private var isFormatting = false
    private var deleteLength = 0

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (s != null) {
            deleteLength = count
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No need to implement
    }

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting) return

        isFormatting = true

        s?.let {
            val cleanString = it.toString().replace(".", "")
            val cursorPosition = editText.selectionStart
            var formattedString = ""

            when {
                cleanString.length >= 8 -> {
                    formattedString = "${cleanString.substring(0, 4)}.${cleanString.substring(4, 6)}.${cleanString.substring(6, 8)}"
                }
                cleanString.length >= 6 -> {
                    formattedString = "${cleanString.substring(0, 4)}.${cleanString.substring(4, 6)}.${cleanString.substring(6)}"
                }
                cleanString.length >= 4 -> {
                    formattedString = "${cleanString.substring(0, 4)}.${cleanString.substring(4)}"
                }
                else -> {
                    formattedString = cleanString
                }
            }

            val newCursorPosition = calculateCursorPosition(cursorPosition, formattedString, deleteLength)
            it.replace(0, it.length, formattedString)

            // Ensure cursor position is within the valid range
            if (newCursorPosition > it.length) {
                editText.setSelection(it.length)
            } else {
                editText.setSelection(newCursorPosition)
            }
        }

        isFormatting = false
    }

    private fun calculateCursorPosition(cursorPosition: Int, formattedString: String, deleteLength: Int): Int {
        var newCursorPosition = cursorPosition

        // Adjust cursor position based on the formatting
        if (deleteLength > 0) {
            // Deletion case
            if (newCursorPosition > 0 && formattedString.substring(newCursorPosition - 1, newCursorPosition) == ".") {
                newCursorPosition--
            }
        } else {
            // Addition case
            if (newCursorPosition > 4 && formattedString.substring(newCursorPosition - 1, newCursorPosition) == ".") {
                newCursorPosition++
            } else if (newCursorPosition > 6 && formattedString.substring(newCursorPosition - 1, newCursorPosition) == ".") {
                newCursorPosition++
            }
        }

        return newCursorPosition
    }
}