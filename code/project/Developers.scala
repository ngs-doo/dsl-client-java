object Developers {
  lazy val members = Map(
      "melezov" -> "Marko Elezović"
    , "rinmalavi" -> "Marin Vila"
    , "zapov" -> "Rikard Pavelić"
  )

  def toXml =
    <developers>
      {members map { m =>
        <developer>
          <id>{m._1}</id>
          <name>{m._2}</name>
          <url>http://github.com/{m._1}</url>
        </developer>
      }}
    </developers>
}
