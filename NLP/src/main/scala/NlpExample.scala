import java.io.File
import java.net.URL

import epic.models._
import epic.parser.ParserAnnotator
import epic.preprocess
import epic.preprocess.{TreebankTokenizer, MLSentenceSegmenter}
import epic.sequences.{SemiCRF, Segmenter}
import epic.slab.{EntityMention, Token, Sentence}
import epic.trees.{AnnotatedLabel, Tree}
import epic.util.SafeLogging

object NlpExample {
  def main(args: Array[String]): Unit = {
    val sentence="This is a test"

    val tagger=epic.models.PosTageSelector.loadTagger("en").get
    val tags=tagger.bestSequence(sentence)
    println(tags.render)
  }
}
