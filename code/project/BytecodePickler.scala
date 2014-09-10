
class BytecodePickler(bytecode: Array[Byte]) {
  private var index = -1

  protected def reset(): Unit = {
    index = 0
  }

  protected def u1() = {
    val res = bytecode(index) & 0xff
    index = index + 1
    res
  }

  protected def u2() = {
    (u1() << 8) | u1()
  }

  protected def u4() = {
    (u2() << 16) | u2()
  }

  protected def u8() = {
    (u4().toLong << 16) | u4()
  }

  /** Returns number of tag indexes to skip */
  protected def skipTag() =
    u1 match {
      case 1 => index += u2() + 2; 1

      case 3
         | 4
         | 9
         | 10
         | 11
         | 12 => index += 4; 1

      case 5
         | 6 => index += 8; 2

      case 7
         | 8 => index += 2; 1

      case tag =>
        sys.error("Encountered unknown tag: " + tag)
    }

  protected def overwrite(value: Short): Unit = {
    bytecode(index -2) = (value >> 8).toByte
    bytecode(index -1) = value.toByte
  }
}
