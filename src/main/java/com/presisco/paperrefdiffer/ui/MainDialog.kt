package com.presisco.paperrefdiffer.ui

import javafx.scene.Parent
import javafx.scene.control.TextArea
import tornadofx.*
import java.util.regex.Pattern

class MainDialog : View() {
    private lateinit var refAText: TextArea
    private lateinit var refBText: TextArea
    private lateinit var diffText: TextArea
    private lateinit var identicalText: TextArea

    private val refPattern = Pattern.compile("(.+?)\n")

    override val root: Parent = borderpane {
        center = hbox {
            vbox {
                label { text = "paper A reference text(end with line break)" }
                refAText = textarea {
                    id = "ref_a"
                    text = "paste ref text, separate with line break"
                    isWrapText = false
                }
            }
            vbox {
                label { text = "paper B reference text(end with line break)" }
                refBText = textarea {
                    id = "ref_b"
                    text = "paste ref text, separate with line break"
                    isWrapText = false
                }
            }
            vbox {
                label { text = "different reference" }
                diffText = textarea {
                    id = "diff"
                    text = "difference"
                    isWrapText = false
                }
            }
            vbox {
                label { text = "identical reference" }
                identicalText = textarea {
                    id = "identical"
                    text = "identical"
                    isWrapText = false
                }
            }
        }
        bottom =
            hbox {
                button {
                    id = "clear"
                    text = "clear"
                    action { onClear() }
                }
                button {
                    id = "diff"
                    text = "compare"
                    action { onDiff() }
                }
        }
        minWidth = 800.0
        maxWidth = 1200.0
        minHeight = 800.0
    }

    private val onClear = {
        refAText.clear()
        refBText.clear()
        diffText.clear()
        identicalText.clear()
    }

    private fun String.text2set(): Set<String>{
        val set = HashSet<String>()
        val countMatcher = refPattern.matcher(this)
        while (countMatcher.find()) {
            set.add(countMatcher.group(1))
        }
        return set
    }

    private fun Set<*>.toText(): String{
        val sb = StringBuilder()
        this.forEach { sb.append(it.toString()).append("\n") }
        return sb.toString()
    }

    private val onDiff = {
        val refASet = refAText.text.text2set()
        val refBSet = refBText.text.text2set()

        val diffSet = HashSet<String>()
        diffSet.addAll(refASet.subtract(refBSet))
        diffSet.addAll(refBSet.subtract(refASet))

        val identicalSet = refASet.intersect(refBSet)

        diffText.text = diffSet.toText()
        identicalText.text = identicalSet.toText()
    }

}