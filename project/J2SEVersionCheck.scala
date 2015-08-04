case class J2SEVersion(majorVersion: Int, minorVersion: Int)

object J2SEVersion {
  val `5` = J2SEVersion(49, 0)
  val `6` = J2SEVersion(50, 0)
  val `7` = J2SEVersion(51, 0)
  val `8` = J2SEVersion(52, 0)
}

object J2SEVersionCheck {
  def apply(bytecode: Array[Byte], versionCheck: J2SEVersion => Boolean) =
    new J2SEVersionCheck(bytecode).checkVersion(versionCheck)
}

private class J2SEVersionCheck(bytecode: Array[Byte])
    extends BytecodePickler(bytecode) {

  def checkVersion(versionCheck: J2SEVersion => Boolean): Unit = {
    reset()
    require(u4() == 0xcafebabe, "Invalid header encountered!")

    val minorVersion = u2()
    val majorVersion = u2()

    val j2seVersion = new J2SEVersion(majorVersion, minorVersion)

    if (!versionCheck(j2seVersion)) {
      sys.error(s"""J2SE version check failed (got ${j2seVersion.majorVersion}.${j2seVersion.minorVersion})""")
    }
  }
}
