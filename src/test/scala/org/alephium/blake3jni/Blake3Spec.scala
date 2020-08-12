package org.alephium.blake3jni

import scala.io.Source

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class Blake3Spec extends AnyFlatSpecLike with Matchers {
  import Blake3Spec._

  it should "correctly bind the `blake3` lib" in {

    def test(init: Long => Unit, expected: TestCase => String)(testCase: TestCase) = {

      val hasher = Blake3Jni.allocate_hasher()

      init(hasher)

      val testInput = makeTestInput(testCase.input_len)

      Blake3Jni.blake3_hasher_update(hasher, testInput, testInput.length)

      val outputLength = expected(testCase).length / 2
      val output: Array[Byte] = Array.ofDim[Byte](outputLength)

      val seek = scala.util.Random.between(0, outputLength)
      val seekOutput: Array[Byte] = Array.ofDim[Byte](outputLength - seek)


      Blake3Jni.blake3_hasher_finalize(hasher, output, outputLength)
      Blake3Jni.blake3_hasher_finalize_seek(hasher, seek.toLong , seekOutput, outputLength - seek)

      bytesToHex(output) shouldBe expected(testCase)
      bytesToHex(seekOutput) shouldBe expected(testCase).drop(seek * 2)

      Blake3Jni.delete_hasher(hasher)
    }

    testVectors.cases.foreach(
      test(hasher =>
          Blake3Jni.blake3_hasher_init(hasher),
          _.hash
          )
        )

    testVectors.cases.foreach(
      test(hasher =>
          Blake3Jni.blake3_hasher_init_keyed(hasher, testVectors.key.getBytes),
          _.keyed_hash
          )
        )

    testVectors.cases.foreach(
      test(hasher =>
          Blake3Jni.blake3_hasher_init_derive_key(hasher, testVectors.context_string),
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

  private val testVectorsJsonFile:String = Source.fromFile("BLAKE3/test_vectors/test_vectors.json").getLines().mkString("")

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
