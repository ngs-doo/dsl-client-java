module simple
{
  root Self { Self? *self;  }
  snowflake<Self> Selfy { ancestors<self> fat; ancestors<selfID> slim; }
  mixin M;
  mixin X;
  value V {
    has mixin M;
    has mixin X;
    url url;
  }
  value I {
    M m;
  }
  value Z {
    has mixin M;
  }
  entity copy(ID) {
    int ID;
  }
  aggregate clone1 { M m; }
  snowflake<clone1> snow1 { m; }
  aggregate clone2(ID) { int ID; }
  report R {
    E e;
    int i;
    float? f;
    SimpleRoot[] roots 'it => true';
    SimpleSnow snowflake 'it => true';
  }

  enum E { A; B; C; }

  root SimpleRoot
  {
    History;

    Int i { default scala '5'; }
    Float f;
    String s;
    E e { default scala 'E.B'; }

    specification odd 'it => it.i % 2 == 0';
    specification oddWithS 'it => it.i % 2 == 0 && it.s == s' {
      String s;
    }

    specification withS 'it => it.s == s' {
      String s;
    }

    specification withE 'it => it.e == e' {
      E e;
    }

    calculated isOdd from odd;
  }

  report SimpleReport
  {
    SimpleRoot [] all 'sr => true';
    SimpleRoot [] odd 'sr => sr.isOdd';
  }

  snowflake<SimpleRoot> SimpleSnow {
    i;
    f;
    s;
    e;
    isOdd;

    calculated even from 'it => !it.isOdd';

    specification oddWithSInSnow 'it => it.i % 2 == 0 && it.s == s' {
      String s;
    }

    specification withSInSnow 'it => it.s == s' {
      String s;
    }

    specification withEInSnow 'it => it.e == e' {
      E e;
    }
  }

  cube SimpleCube from SimpleRoot {
    dimension s;
    dimension e;

  specification oddInCube 'it => it.i % 2 == 0';

    max i max_i;
    min i min_i;
    min f min_f;
  }

  entity SimpleEntity
  {
    Int vin;
    Float vfl;
    String vstr;
  }

  value Val
  {
    Int vin;
    Float vfl;
    String vstr;
    E e;
  }

  event TrivialEvent{}
/*
  event SimpleRoot.SimpleEvent {
    string s;
    float f;
    int   i;
  }
*/
  value ValDTD
  {
    decimal D;
    date DT;
    timestamp T;
  }

  root RootDTD
  {
    decimal D;
    date DT;
    timestamp T;
  }

  snowflake<RootDTD> snowDTD { D; DT; T; }

  event EveDTD
  {
    decimal D;
    date DT;
    timestamp T;
  }
}

module snapshottest
{
  root SimpleRoot
  {
    Int i;
    Float f;
    Double d;
    String s;
    simple.E e;
  }

  root SimpleRootReferent
  {
    SimpleRoot? *sr { snapshot ; }
    SimpleRoot[] *srs { snapshot ; }
  }
}

module detailtest
{
  root Leaf
  {
    Int i;
    Float f;
    String s;
    simple.E e;

    Node? *node;
  }

  root Node (name)
  {
    String name;
    Node? *parent;

    detail<Leaf.node> leafs;
    detail<Node.parent> others;

    specification withName 'it => it.name == name '{
      String name;
    }
  }
}

module complex
{
  mixin Mixed
  {
    String mixedStr;
  }

  enum Enum { A; B; C; }

  event SimpleEventWithValuesParams {
    Value value;
    Value? optvalue;
    List<Value> valuelist;
    Array<Value> valuearr;
    Set<Value> valueset;
    List<Value>? valuelistopt;
    Array<Value>? valuearropt;
    Set<Value>? valuesetopt;
  }

  event SimpleEventWithStringParams {
    String string;
    //String? stringopt;
    List<String> stringlist;
    Array<String> stringarr;
    Set<String> stringset;
    List<String>? stringlistopt;
    Array<String>? stringarropt;
    Set<String>? stringsetopt;
    List<String?> stringoptlist;
    Array<String?> stringoptarr;
    Set<String?> stringoptset;
    List<String?>? bstringoptlistopt;
    Array<String?>? stringoptarropt;
    Set<String?>? stringoptsetopt;
  }

  event SimpleEventWithIntParams {
    List<Int> intlist;
    Array<Int> intarr;
    Set<Int> intset;
    List<Int>? bintlistopt;
    Array<Int>? intarropt;
    Set<Int>? intsetopt;
  }

  event SimpleEventWithEnumParams {
    Enum e;
    Enum? opte;
    Set<Enum> setEnum;
    Set<Enum>? optsetEnum;
    List<Enum>? optlistEnum;
    List<Enum> listEnum;
    Array<Enum> arrEnum;
    Array<Enum>? optarrEnum;
    Empty empty;
    Empty? emptyOpt;
  }

  event SimpleEventWithRootParams {

    EmptyRoot? optRoot;
    EmptyRoot root;
    List<EmptyRoot> emptyRootlist;
    Array<EmptyRoot> emptyRootarr;
    Set<EmptyRoot> emptyRootset;
    List<EmptyRoot>? emptyRootlistopt;
    Array<EmptyRoot>? emptyRootarropt;
    Set<EmptyRoot>? emptyRootsetopt;
  }

  root BaseRoot
  {
    has mixin Mixed;

    Ent ent;
    Int age;

    String name;
    Empty empty;

    Value value;
    Value? optvalue;
    List<Value> valuelist;
    Array<Value> valuearr;
    Set<Value> valueset;
    List<Value>? valuelistopt;
    List<Value?>? valuesoptlistopt;
    Array<Value>? valuearropt;
    Set<Value>? valuesetopt;
    Set<Value?>? valueoptsetopt;

    // simple references
    List<Int> intlist;
    Array<Int> intarr;
    Set<Int> intset;
    List<Int>? bintlistopt;
    Array<Int>? intarropt;
    Set<Int>? intsetopt;

    // root references
    EmptyRoot *root;
    EmptyRoot? *optRoot;

    List<EmptyRoot> *emptyRootList;
    Array<EmptyRoot> *emptyRootArr;
    Set<EmptyRoot> *emptyRootSet;

    Enum e;
    Enum? opte;
    Set<Enum> setEnum;
    Set<Enum>? optsetEnum;
    List<Enum>? optlistEnum;
    List<Enum> listEnum;
    Array<Enum> arrEnum;
    Array<Enum>? optarrEnum;

    calculated int ageSquared from 'it => it.age * it.age';
    calculated underAge from 'it => it.age < 18';

    specification findByAge 'it => age.Contains(it.age)'
    {
      int[] age;
    }

    specification acceptingRoots ' it => true'
    {
      EmptyRoot root;
      EmptyRoot? optRoot;
      List<EmptyRoot> emptyRootList;
      Array<EmptyRoot> emptyRootArr;
      Set<EmptyRoot> emptyRootSet;
      List<EmptyRoot>? emptyRootListopt;
      Array<EmptyRoot>? emptyRootArropt;
      Set<EmptyRoot>? emptyRootSetopt;
    }

    specification acceptingPrimitives 'it => true'
    {
      Int?      optInt;
      Int     rint;
      List<Int> intlist;
      Array<Int> intarr;
      Set<Int> intset;
      List<Int>? bintlistopt;
      Array<Int>? intarropt;
      Set<Int>? intsetopt;
    }

    specification acceptingValues 'it => true' {
      Value value;
      Value? optvalue;
      List<Value> valuelist;
      Array<Value> valuearr;
      Set<Value> valueset;
      List<Value>? valuelistopt;
      List<Value?>? valuesoptlistopt;
      Array<Value>? valuearropt;
      Set<Value>? valuesetopt;
      Set<Value?>? valueoptsetopt;
    }

    specification acceptingEnums 'it => true' {
      Enum e;
      Enum? opte;
      Set<Enum> setEnum;
      Set<Enum>? optsetEnum;
      List<Enum>? optlistEnum;
      List<Enum> listEnum;
      Array<Enum> arrEnum;
      Array<Enum>? optarrEnum;
    }

    History;
  }

  entity Ent
  {
    String ne;
    int[]? numers;
    BaseRoot? *parent;

    Detail[] detail;
    connected.TestRoot1? *testRoot;
  }

  snowflake EntGrid from Ent
  {
    ne;
    numers;
    parent.name as ParentName;
    detail;
    ent.name as OriginalName;
    ent.root;
    parent.ageSquared;
    parent.underAge isUnderAge;
    calculated int ageSquared2 from 'it => 3';
    calculated underAge2 from 'it => false';
  }

  entity Detail;

  value Value
  {
    int[] ints;
    List<float?> floats;
    Complex c1;
    Complex? c2;
    Complex[] c3;
    Complex[]? c4;
    List<Complex?>? c5;
  }

  value Complex
  {
    int i;
    string? j;
    calculated int squared from 'it => it.i * it.i';
    calculated underFive from 'it => it.i < 5';
  }

  value Empty;

  root EmptyRoot;

  entity Guards
  {
    decimal(5) with;
    string(13) BigDecimal;
    List<money> for;
    List<decimal(6)?>? match;
    Set<string(1)?> scala;
    Array<Int?>? math;
  }
}

module connected
{
  entity TestEnt;
  root   TestRoot1
  {
    TestEnt testEnt;
    complex.BaseRoot *foreignRoot;
    simple.Val       foreignVal;
  }

  root   TestRoot2
  {
    simple.SimpleRoot *foreignRoot;
    simple.Val       foreignVal;
  }
}

module concept
{
  mixin Concept;

  value emptyConcept {
    has mixin Concept;
  }

  value fieldedConcept {
    has mixin Concept;
    String s1;
    String s2;
  }

  root rootWithConcept {
    Concept? c;
    int i;
  }
}