import edu.stanford.nlp.coref.data.CorefChain
import edu.stanford.nlp.ling._
import edu.stanford.nlp.ie.util._
import edu.stanford.nlp.pipeline._
import edu.stanford.nlp.semgraph._
import edu.stanford.nlp.trees._

import java.util._


object StanfordNlpTriples {
  def main(args: Array[String]): Unit = {
    val text = "Joe Smith was born in California. " + "In 2017, he went to Paris, France in the summer. " + "His flight left at 3:00pm on July 10th, 2017. " + "After eating some escargot for the first time, Joe said, \"That was delicious!\" " + "He sent a postcard to his sister Jane Smith. "

    val props = new Properties()

    import edu.stanford.nlp.ling.CoreLabel
    import edu.stanford.nlp.pipeline.CoreDocument
    import edu.stanford.nlp.pipeline.StanfordCoreNLP

    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote")
    // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
    props.setProperty("coref.algorithm", "neural")
    // build pipeline
    val pipeline = new StanfordCoreNLP(props)
    // create a document object
    val document = new CoreDocument(text)
    // annnotate the document
    pipeline.annotate(document)
    // examples

    // 10th token of the document
    val token: CoreLabel = document.tokens.get(10)

    println(token)
  }
}
