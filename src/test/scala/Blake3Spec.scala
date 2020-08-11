import java.nio.ByteBuffer

import scala.jdk.CollectionConverters._

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class Blake3Spec extends AnyFlatSpecLike with Matchers {
  import Blake3Spec._

  it should "correctly bind the `blake3` lib" in {

    def test(init: (Blake3Jni, Long) => Unit, expected: TestCase => String)(testCase: TestCase) = {

      val blake3: Blake3Jni = new Blake3Jni()
      val hasher = blake3.allocate_hasher()

      init(blake3, hasher)

      val testInput = makeTestInput(testCase.input_len)

      blake3.blake3_hasher_update(hasher, testInput, testInput.length)

      val outputLength = expected(testCase).length / 2
      var output: Array[Byte] = Array.ofDim[Byte](outputLength)

      val seek = scala.util.Random.between(0, outputLength)
      var seekOutput: Array[Byte] = Array.ofDim[Byte](outputLength - seek)


      blake3.blake3_hasher_finalize(hasher, output, outputLength)
      blake3.blake3_hasher_finalize_seek(hasher, seek , seekOutput, outputLength - seek)

      bytesToHex(output) shouldBe expected(testCase)
      bytesToHex(seekOutput) shouldBe expected(testCase).drop(seek * 2)

      blake3.delete_hasher(hasher)
    }

    testVectors.cases.foreach(
      test((blake3, hasher) =>
          blake3.blake3_hasher_init(hasher),
          _.hash
          )
        )

    testVectors.cases.foreach(
      test((blake3, hasher) =>
          blake3.blake3_hasher_init_keyed(hasher, testVectors.key.getBytes),
          _.keyed_hash
          )
        )

    testVectors.cases.foreach(
      test((blake3, hasher) =>
          blake3.blake3_hasher_init_derive_key(hasher, testVectors.context_string),
          _.derive_key
          )
        )
  }
}

object Blake3Spec {
  import io.circe.Decoder
  import io.circe.parser.parse
  import io.circe.generic.semiauto.deriveDecoder

  System.loadLibrary("blake3")

  case class TestCase(input_len: Int, hash: String, keyed_hash: String, derive_key: String)
  implicit val testCaseDecoder: Decoder[TestCase] = deriveDecoder[TestCase]

  case class TestVectors(_comment:String, key:String,context_string:String, cases:Seq[TestCase])
  implicit val testVectorsDecoder: Decoder[TestVectors] = deriveDecoder[TestVectors]

  private val testVectorsJsonFile:String = scala.io.Source.fromFile("BLAKE3/test_vectors/test_vectors.json").getLines.mkString

  val testVectors = parse(testVectorsJsonFile).toOption.get.as[TestVectors].toOption.get

  def makeTestInput(size:Int): Array[Byte] =
    Array.ofDim[Byte](size).zipWithIndex.map { case (_, i) =>
      (i % 251).toByte
    }

  def bytesToHex( byteArray: Array[Byte]): String = {
    val stringBuilder: StringBuilder = new StringBuilder(byteArray.length * 2)
    byteArray.foreach { b =>
      stringBuilder.append(String.format("%02x", b))
    }
    stringBuilder.toString()
  }
}
